package id.co.arya.dansassignment.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import id.co.arya.dansassignment.data.response.JobListResponse;
import id.co.arya.dansassignment.databinding.FragmentHomeBinding;
import id.co.arya.dansassignment.ui.DetailActivity;
import okhttp3.ResponseBody;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private int page = 1;
    private String description = "";
    private String location = "";
    private Boolean isFulltime = false;
    private Context mContext;
    private LinearLayoutManager layoutManager;
    private JobListAdapter adapter;
    private Boolean isLoadMore = false;
    private Boolean isFilter = true;
    private Boolean isFulltimeChecked = true;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.init();

        fetchJobList();

        setupToRecyclerView();
    }

    private void setupToRecyclerView() {
        adapter = new JobListAdapter();
        adapter.clearData();
        adapter.notifyDataSetChanged();
        binding.rvJobList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        binding.rvJobList.setLayoutManager(layoutManager);
        binding.rvJobList.setAdapter(adapter);

        listener();
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
    }

    private void listener() {

        binding.imageFilter.setOnClickListener(view -> {
            if (isFilter) {
                binding.filterSection.setVisibility(View.VISIBLE);
                isFilter = false;
            } else {
                binding.filterSection.setVisibility(View.GONE);
                isFilter = true;
            }
        });

        Objects.requireNonNull(binding.searchBar.getEditText()).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    description = binding.searchBar.getEditText().getText().toString();
                    adapter.clearData();
                    adapter.notifyDataSetChanged();
                    fetchJobListSearch();
                    return false;
                }
                return false;
            }
        });

        binding.isFulltime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isFulltimeChecked) {
                    binding.isFulltime.setChecked(true);
                    isFulltime = true;
                    isFulltimeChecked = false;
                } else {
                    binding.isFulltime.setChecked(false);
                    isFulltime = false;
                    isFulltimeChecked = true;
                }
            }
        });

        binding.applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location = Objects.requireNonNull(binding.location.getEditText()).getText().toString();
                adapter.clearData();
                adapter.notifyDataSetChanged();
                fetchJobListSearch();
                binding.filterSection.setVisibility(View.GONE);
            }
        });

        adapter.setOnSelectedJob(new JobListAdapter.JobSelector() {
            @Override
            public void selectedJob(JobListResponse job, int position) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("DATA_JOB", job);
                startActivity(intent);
            }
        });
    }

    private void fetchJobList() {
        binding.progressBar.setVisibility(View.VISIBLE);
        homeViewModel.getJobListData(page)
                .observe(getViewLifecycleOwner(), jobListResponse -> {
                    if (jobListResponse != null) {
                        setToAdapter(jobListResponse);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });

        binding.rvJobList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItem = layoutManager.getChildCount();
                int pastVisibleItem = layoutManager.findFirstVisibleItemPosition();
                int totalItems = adapter.getItemCount();
                if (!isLoadMore && page < totalItems) {
                    if (visibleItem + pastVisibleItem >= totalItems) {
                        page++;
                        if (page <= 2) {
                            fetchJobList();
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void fetchJobListSearch() {
        binding.progressBar.setVisibility(View.VISIBLE);
        homeViewModel.getJobListSearch(description, location, isFulltime)
                .observe(getViewLifecycleOwner(), jobListResponse -> {
                    if (jobListResponse != null) {
                        setToAdapter(jobListResponse);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setToAdapter(ArrayList<JobListResponse> jobListResponse) {
        adapter.setData(jobListResponse);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
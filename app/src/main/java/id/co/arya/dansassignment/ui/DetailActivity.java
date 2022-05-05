package id.co.arya.dansassignment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import id.co.arya.dansassignment.R;
import id.co.arya.dansassignment.data.response.JobListResponse;
import id.co.arya.dansassignment.databinding.ActivityDetailBinding;
import id.co.arya.dansassignment.ui.home.HomeViewModel;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private JobListResponse jobListResponse;
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        jobListResponse = (JobListResponse) getIntent().getSerializableExtra("DATA_JOB");

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.init();

        binding.progressBar.setVisibility(View.VISIBLE);
        homeViewModel.getJobDetail(jobListResponse.getId())
                .observe(this, jobListResponse -> {
                    if (jobListResponse != null) {
                        setupView(jobListResponse);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });

        binding.backIcon.setOnClickListener(view -> {
            finish();
        });
    }

    private void setupView(JobListResponse jobListResponse) {

        if (jobListResponse != null) {
            binding.titleJobDetail.setText(jobListResponse.getTitle());
            binding.companyJobDetail.setText(jobListResponse.getCompany());
            binding.locationJobDetail.setText(jobListResponse.getLocation());
            Picasso.get().load(jobListResponse.getCompanyUrl()).into(binding.imageJobDetail);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.jobSpecification.setText(Html.fromHtml(jobListResponse.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                binding.jobSpecification.setText(Html.fromHtml(jobListResponse.getDescription()));
            }
        }


    }

}
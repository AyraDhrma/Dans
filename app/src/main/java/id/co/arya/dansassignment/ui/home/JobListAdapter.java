package id.co.arya.dansassignment.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import id.co.arya.dansassignment.R;
import id.co.arya.dansassignment.data.response.JobListResponse;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder> {

    ArrayList<JobListResponse> list = new ArrayList<>();
    private JobSelector jobSelector;

    public void setOnSelectedJob(JobSelector jobSelector) {
        this.jobSelector = jobSelector;
    }

    public void setData(ArrayList<JobListResponse> response) {
        list.addAll(response);
    }

    public void clearData() {
        list.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView title = holder.itemView.findViewById(R.id.title_job);
        TextView company = holder.itemView.findViewById(R.id.company_job);
        TextView location = holder.itemView.findViewById(R.id.location_job);
        ImageView image = holder.itemView.findViewById(R.id.image_job);

        if (list.get(position) != null) {
            title.setText(list.get(position).getTitle());
            company.setText(list.get(position).getCompany());
            location.setText(list.get(position).getLocation());
            Picasso.get().load(list.get(position).getCompanyUrl()).into(image);
        }

        holder.itemView.setOnClickListener(view -> {
            jobSelector.selectedJob(list.get(position), position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    interface JobSelector {
        void selectedJob(JobListResponse job, int position);
    }

}

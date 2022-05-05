package id.co.arya.dansassignment.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import id.co.arya.dansassignment.data.response.JobListResponse;
import id.co.arya.dansassignment.repository.MainRepository;
import okhttp3.ResponseBody;

public class HomeViewModel extends ViewModel {

    private MainRepository mainRepository;
    private LiveData<ArrayList<JobListResponse>> jobListData;
    private LiveData<JobListResponse> jobDetailData;

    public void init() {
        jobListData = new MutableLiveData<>();
        jobDetailData = new MutableLiveData<>();
        mainRepository = new MainRepository();
    }

    public LiveData<ArrayList<JobListResponse>> getJobListData(int page) {
        mainRepository.getJobList(page);
        jobListData = mainRepository.showJobList();
        return jobListData;
    }

    public LiveData<JobListResponse> getJobDetail(String id) {
        mainRepository.getJobDetail(id);
        jobDetailData = mainRepository.jobDetailData();
        return jobDetailData;
    }

    public LiveData<ArrayList<JobListResponse>> getJobListSearch(String description, String location, Boolean isFulltime) {
        mainRepository.getJobListSearch(description, location, isFulltime);
        jobListData = mainRepository.showJobList();
        return jobListData;
    }
}
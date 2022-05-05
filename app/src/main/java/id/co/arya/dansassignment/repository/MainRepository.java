package id.co.arya.dansassignment.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;

import id.co.arya.dansassignment.data.response.JobListResponse;
import id.co.arya.dansassignment.network.ApiService;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainRepository {

    public static final String BASE_URL = "http://dev3.dansmultipro.co.id/api/";

    private ApiService apiService;
    private MutableLiveData<ArrayList<JobListResponse>> jobListData;
    private MutableLiveData<JobListResponse> jobDetailData;

    public MainRepository() {
        jobListData = new MutableLiveData<>();
        jobDetailData = new MutableLiveData<>();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(logging);

        apiService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }

    public void getJobList(int page) {
        apiService.getJobList(page)
                .enqueue(new Callback<ArrayList<JobListResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<JobListResponse>> call, Response<ArrayList<JobListResponse>> response) {
                        if (response.body() != null) {
                            jobListData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<JobListResponse>> call, Throwable t) {
                        jobListData.postValue(null);
                    }
                });
    }

    public void getJobListSearch(String description, String location, Boolean isFulltime) {
        apiService.getJobListSearch(description, location, isFulltime)
                .enqueue(new Callback<ArrayList<JobListResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<JobListResponse>> call, Response<ArrayList<JobListResponse>> response) {
                        if (response.body() != null) {
                            jobListData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<JobListResponse>> call, Throwable t) {
                        jobListData.postValue(null);
                    }
                });
    }

    public void getJobDetail(String id) {
        apiService.getJobDetail(id)
                .enqueue(new Callback<JobListResponse>() {
                    @Override
                    public void onResponse(Call<JobListResponse> call, Response<JobListResponse> response) {
                        if (response.body() != null) {
                            jobDetailData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<JobListResponse> call, Throwable t) {
                        jobDetailData.postValue(null);
                    }
                });
    }

    public LiveData<ArrayList<JobListResponse>> showJobList() {
        return jobListData;
    }

    public LiveData<JobListResponse> jobDetailData() {
        return jobDetailData;
    }

}

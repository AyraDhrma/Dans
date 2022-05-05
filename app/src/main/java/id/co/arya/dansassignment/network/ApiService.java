package id.co.arya.dansassignment.network;

import java.util.ArrayList;

import id.co.arya.dansassignment.data.response.JobListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("recruitment/positions.json")
    Call<ArrayList<JobListResponse>> getJobList(
            @Query("page") int page
    );

    @GET("recruitment/positions.json")
    Call<ArrayList<JobListResponse>> getJobListSearch(
            @Query("description") String description,
            @Query("location") String location,
            @Query("full_time") Boolean isFulltime
    );

    @GET("recruitment/positions/{id}")
    Call<JobListResponse> getJobDetail(
            @Path("id") String id
    );

}

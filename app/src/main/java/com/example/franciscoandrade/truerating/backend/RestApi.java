package com.example.franciscoandrade.truerating.backend;

import com.example.franciscoandrade.truerating.model.InspectionResultsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by franciscoandrade on 3/3/18.
 */

public interface RestApi {
    @GET("9w7m-hzhe.json")
    Call<InspectionResultsModel> getZipcodeDiscover(
            @Query("zipcode") String zipcode
    );

    @GET("9w7m-hzhe.json")
    Call<InspectionResultsModel> getDBADiscover(
            @Query("dba") String dba
    );

}

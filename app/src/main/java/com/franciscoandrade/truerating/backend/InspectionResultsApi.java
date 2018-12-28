package com.franciscoandrade.truerating.backend;

import com.franciscoandrade.truerating.model.InspectionResultsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by franciscoandrade on 3/3/18.
 */

public interface InspectionResultsApi {
    @GET("9w7m-hzhe.json")
    Call<List<InspectionResultsModel>> getZipcodeDiscover(
            @Query("zipcode") String zipcode
    );

    @GET("9w7m-hzhe.json")
    Call<List<InspectionResultsModel>> getDBADiscover(
            @Query("dba") String dba
    );
}

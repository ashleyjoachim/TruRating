package com.example.franciscoandrade.truerating.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.franciscoandrade.truerating.R;
import com.example.franciscoandrade.truerating.backend.RestApi;
import com.example.franciscoandrade.truerating.model.InspectionResultsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofitGrading();
        networkCallGrading("11220");
    }

    private void retrofitGrading() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://data.cityofnewyork.us/resource/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void networkCallGrading(String zipcode){
        RestApi service = retrofit.create(RestApi.class);
        Call<List<InspectionResultsModel>> response =service.getInspectionDiscover(zipcode);
        response.enqueue(new Callback<List<InspectionResultsModel>>() {
            @Override
            public void onResponse(Call<List<InspectionResultsModel>> call, Response<List<InspectionResultsModel>> response) {
                Log.d("RESPONSE", "onResponse: "+response);
                Log.d("RESPONSE", "onResponse: "+response.body().get(0).getBoro());
            }

            @Override
            public void onFailure(Call<List<InspectionResultsModel>> call, Throwable t) {

                Log.d("RESPONSE", "onFailure: "+t.getMessage());
            }
        });

    }

}

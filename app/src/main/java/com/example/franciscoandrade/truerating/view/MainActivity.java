package com.example.franciscoandrade.truerating.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.franciscoandrade.truerating.R;
import com.example.franciscoandrade.truerating.backend.RestApi;
import com.example.franciscoandrade.truerating.model.InspectionResultsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        retrofitGrading();
        networkCallGrading();
    }

    private void retrofitGrading() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://data.cityofnewyork.us/resource/9w7m-hzhe.json")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void networkCallGrading(){
        RestApi service = retrofit.create(RestApi.class);
        //Call<InspectionResultsModel> response =
    }

}

package com.example.franciscoandrade.truerating.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.franciscoandrade.truerating.R;
import com.example.franciscoandrade.truerating.backend.RestApi;
import com.example.franciscoandrade.truerating.controller.GradingAdapter;
import com.example.franciscoandrade.truerating.model.InspectionResultsModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RecyclerView main_recycler_view;
    private GradingAdapter gradingAdapter;
    private List<InspectionResultsModel> inspectionResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText searchEditText = findViewById(R.id.main_search_bar);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String zipcode = searchEditText.getText().toString();

                    networkCallGrading(zipcode);
                    return true;
                }
                return false;
            }

        });
        main_recycler_view= findViewById(R.id.main_recycler_view);
        retrofitGrading();
        gradingAdapter= new GradingAdapter(this);
        main_recycler_view.setAdapter(gradingAdapter);
        main_recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        main_recycler_view.setLayoutManager(linearLayoutManager);
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
        Call<List<InspectionResultsModel>> response =service.getZipcodeDiscover(zipcode);
        response.enqueue(new Callback<List<InspectionResultsModel>>() {
            @Override
            public void onResponse(Call<List<InspectionResultsModel>> call, Response<List<InspectionResultsModel>> response) {
               if(response.isSuccessful()){
                   inspectionResultsList= new ArrayList<>();
                   inspectionResultsList.addAll(response.body());

                   gradingAdapter.adGrades(inspectionResultsList);
               }
            }

            @Override
            public void onFailure(Call<List<InspectionResultsModel>> call, Throwable t) {

                Log.d("RESPONSE", "onFailure: "+t.getMessage());
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

}

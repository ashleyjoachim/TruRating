package com.example.franciscoandrade.truerating.view;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
                    networkZipcodeSearch(zipcode);
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


        networkZipcodeSearch("10001");


        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String input = searchEditText.getText().toString().toUpperCase().trim();
                    main_recycler_view.scrollToPosition(0);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    if (input.length() == 5) {
                        networkZipcodeSearch(input);
                        return true;
                    } else {
                        networkNameSearch(input);
                        return true;
                    }
                }
                return false;
            }
        });


    }

    private void retrofitGrading() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://data.cityofnewyork.us/resource/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void networkZipcodeSearch(String zipcode) {
        RestApi service = retrofit.create(RestApi.class);
        Call<List<InspectionResultsModel>> response = service.getZipcodeDiscover(zipcode);
        response.enqueue(new Callback<List<InspectionResultsModel>>() {
            @Override
            public void onResponse(Call<List<InspectionResultsModel>> call, Response<List<InspectionResultsModel>> response) {
                if (response.isSuccessful()) {
                    inspectionResultsList = new ArrayList<>();
                    inspectionResultsList.addAll(response.body());
                    gradingAdapter.adGrades(inspectionResultsList);
                }
            }

            @Override
            public void onFailure(Call<List<InspectionResultsModel>> call, Throwable t) {

                Log.d("RESPONSE", "onFailure: " + t.getMessage());
            }
        });

    }

    public void networkNameSearch(String name) {
        RestApi service = retrofit.create(RestApi.class);
        Call<List<InspectionResultsModel>> response = service.getDBADiscover(name);
        response.enqueue(new Callback<List<InspectionResultsModel>>() {
            @Override
            public void onResponse(Call<List<InspectionResultsModel>> call, Response<List<InspectionResultsModel>> response) {
                if (response.isSuccessful()) {
                    inspectionResultsList = new ArrayList<>();
                    inspectionResultsList.addAll(response.body());
                    gradingAdapter.adGrades(inspectionResultsList);
                    Log.d("MainActivity", "onResponse: " + response.body().size());
                }
            }

            @Override
            public void onFailure(Call<List<InspectionResultsModel>> call, Throwable t) {

                Log.d("MainActivity", "onFailure: " + t.getMessage());
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


        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}

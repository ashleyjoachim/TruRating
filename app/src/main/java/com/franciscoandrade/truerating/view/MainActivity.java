package com.franciscoandrade.truerating.view;

import android.content.Context;
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
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.franciscoandrade.truerating.R;
import com.franciscoandrade.truerating.backend.InspectionResultsApi;
import com.franciscoandrade.truerating.backend.SearchDatabase;
import com.franciscoandrade.truerating.controller.GradingAdapter;
import com.franciscoandrade.truerating.model.InspectionResultsModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    final private static String preferenceKey = "truRating_prefs";

    private Retrofit retrofit;
    private RecyclerView main_recycler_view;
    private GradingAdapter gradingAdapter;
    private List<InspectionResultsModel> inspectionResultsList;
    private SharedPreferences preferences;
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText searchEditText = findViewById(R.id.main_search_bar);

        preferences = getSharedPreferences(preferenceKey, MODE_PRIVATE);
        gson = new Gson();



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
        main_recycler_view = findViewById(R.id.main_recycler_view);
        retrofitGrading();
        gradingAdapter = new GradingAdapter(this);


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

    public ArrayList<InspectionResultsModel> getList(String zipcode){
        Type listType = new TypeToken<List<InspectionResultsModel>>(){}.getType();
        return gson.fromJson(preferences.getString(zipcode,"empty"),listType);
    }

    public void storeList(String zipcode, List<InspectionResultsModel> list){
        if(!preferences.contains(zipcode)) {
            preferences.edit().putString(zipcode, gson.toJson(list)).apply();
        }
    }

    public void networkZipcodeSearch(final String zipcode) {
        if(preferences.contains(zipcode)){
            gradingAdapter.adGrades(getList(zipcode));
            return;
        }
        InspectionResultsApi service = retrofit.create(InspectionResultsApi.class);
        Call<List<InspectionResultsModel>> response = service.getZipcodeDiscover(zipcode);
        response.enqueue(new Callback<List<InspectionResultsModel>>() {
            @Override
            public void onResponse(Call<List<InspectionResultsModel>> call, Response<List<InspectionResultsModel>> response) {
                if (response.isSuccessful()) {
                    inspectionResultsList = new ArrayList<>();
                    inspectionResultsList.addAll(response.body());

                    storeList(zipcode, inspectionResultsList);

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
        InspectionResultsApi service = retrofit.create(InspectionResultsApi.class);
        Call<List<InspectionResultsModel>> response = service.getDBADiscover(name);
        response.enqueue(new Callback<List<InspectionResultsModel>>() {
            @Override
            public void onResponse(Call<List<InspectionResultsModel>> call, Response<List<InspectionResultsModel>> response) {
                if (response.isSuccessful()) {
                    inspectionResultsList = new ArrayList<>();
                    inspectionResultsList.addAll(response.body());

                    Gson gson = new Gson();
                    String toJson = gson.toJson(inspectionResultsList);

                    gradingAdapter.adGrades(inspectionResultsList);
                    SearchDatabase searchDatabase = new SearchDatabase(getApplicationContext());

                    for (int i = 0; i < response.body().size(); i++) {
                        String searchTerms = response.body().get(i).getDba();
                        searchDatabase.addSearchTerm(new InspectionResultsModel(searchTerms));
                    }

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

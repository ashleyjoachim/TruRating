package com.franciscoandrade.truerating.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.franciscoandrade.truerating.R;
import com.franciscoandrade.truerating.backend.InspectionResultsApi;
import com.franciscoandrade.truerating.backend.SearchDatabase;
import com.franciscoandrade.truerating.controller.GradingAdapter;
import com.franciscoandrade.truerating.model.InspectionResultsModel;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    FusedLocationProviderClient mFusedLocationClient;
    double a, b;
    LatLng newCoor;
    private GoogleApiClient googleApiClient;
    private double longitude;
    private double latitude;
    private Location mLastLocation;
    private SearchDatabase searchDatabase;

    private Retrofit retrofit;
    private RecyclerView main_recycler_view;
    private GradingAdapter gradingAdapter;
    private List<InspectionResultsModel> inspectionResultsList;
    private BottomSheetBehavior mBottomSheetBehavior;
    private LinearLayout btnA_Rating, btnB_Rating, btnOther_Rating;
    private TextView btnA_RatingBar, btnB_RatingBar, btnOther_RatingBar;
    private List<InspectionResultsModel> listAresult;
    private List<InspectionResultsModel> listBresult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchDatabase = new SearchDatabase(getApplicationContext());
        setContentView(R.layout.activity_maps);
        btnA_RatingBar = findViewById(R.id.btnA_RatingBar);
        btnB_RatingBar = findViewById(R.id.btnB_RatingBar);
        btnOther_RatingBar = findViewById(R.id.btnOther_RatingBar);
        btnA_Rating = findViewById(R.id.btnA_Rating);
        btnB_Rating = findViewById(R.id.btnB_Rating);
        btnOther_Rating = findViewById(R.id.btnOther_Rating);

        btnA_Rating.setOnClickListener(this);
        btnB_Rating.setOnClickListener(this);

        btnOther_Rating.setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        main_recycler_view = findViewById(R.id.main_recycler_view);
        retrofitGrading();
        gradingAdapter = new GradingAdapter(this);
        main_recycler_view.setAdapter(gradingAdapter);
        main_recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        main_recycler_view.setLayoutManager(linearLayoutManager);
        networkZipcodeSearch("10001");

        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(300);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetBehavior.setPeekHeight(70);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

        final EditText searchEditText = findViewById(R.id.maps_search_bar);

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    String input = searchEditText.getText().toString().toUpperCase().trim();
                    main_recycler_view.scrollToPosition(0);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    if (input.equals("")) {
                        searchEditText.setError("Enter Text");
                        return false;

                    }
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
        InspectionResultsApi service = retrofit.create(InspectionResultsApi.class);
        Call<List<InspectionResultsModel>> response = service.getZipcodeDiscover(zipcode);
        response.enqueue(new Callback<List<InspectionResultsModel>>() {
            @Override
            public void onResponse(Call<List<InspectionResultsModel>> call, Response<List<InspectionResultsModel>> response) {
                if (response.isSuccessful()) {
                    listAresult = new ArrayList<>();
                    listBresult = new ArrayList<>();
                    inspectionResultsList = new ArrayList<>();
                    inspectionResultsList.addAll(response.body());
                    gradingAdapter.adGrades(inspectionResultsList);

                    new LoadFilteredData().execute(inspectionResultsList);

                    for (int i = 0; i < response.body().size() - 1; i++) {
                        String searchTerms = response.body().get(i).getDba();
                        searchDatabase.addSearchTerm(new InspectionResultsModel(searchTerms));
                    }
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
                inspectionResultsList = new ArrayList<>();
                if (response.isSuccessful()) {

                    inspectionResultsList.addAll(response.body());
                    gradingAdapter.adGrades(inspectionResultsList);

                    for (int i = 0; i < response.body().size(); i++) {
                        searchDatabase.addSearchTerm(new InspectionResultsModel(response.body().get(i).getDba()));
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        LatLng nyc = new LatLng(40.7429437, -73.9418781);

        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(nyc, 12);
        mMap.animateCamera(cu);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1020);
        } else {

            mMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(false);
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setTiltGesturesEnabled(false);

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    mMap.clear();
                    a = latLng.latitude;
                    b = latLng.longitude;
                    newCoor = new LatLng(a, b);
                    Geocoder coder = new Geocoder(getApplicationContext());
                    List<Address> address;
                    String zip = "";
                    try {
                        address = coder.getFromLocation(a, b, 2);
                        if (address != null) {
                            mMap.addMarker(new MarkerOptions().position(newCoor).title(address.get(0).getAddressLine(0)));
                            zip = address.get(0).getPostalCode();
                            new LoadData().execute(zip);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            });
        }
    }

    private void moveToCurrentLocation(LatLng currentLocation) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnA_Rating:
                Log.d("BTNClickA", "onClick: ");
                offBars();
                btnA_RatingBar.setBackgroundColor(getResources().getColor(R.color.blue));
                gradingAdapter.notifyDataSetChanged();
                gradingAdapter.adGrades(listAresult);
                gradingAdapter.notifyDataSetChanged();
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.btnB_Rating:
                Log.d("BTNClickB", "onClick: ");
                offBars();
                btnB_RatingBar.setBackgroundColor(getResources().getColor(R.color.green));
                gradingAdapter.notifyDataSetChanged();
                gradingAdapter.adGrades(listBresult);
                gradingAdapter.notifyDataSetChanged();
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.btnOther_Rating:
                Log.d("BTNClickOther", "onClick: ");
                offBars();
                btnOther_RatingBar.setBackgroundColor(getResources().getColor(R.color.black));
                gradingAdapter.adGrades(inspectionResultsList);
                gradingAdapter.notifyDataSetChanged();
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }
    }

    private void offBars() {
        btnA_RatingBar.setBackgroundColor(getResources().getColor(R.color.dark_gray));
        btnB_RatingBar.setBackgroundColor(getResources().getColor(R.color.dark_gray));
        btnOther_RatingBar.setBackgroundColor(getResources().getColor(R.color.dark_gray));
    }

    public class LoadData extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... voids) {
            networkZipcodeSearch(voids[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            gradingAdapter.notifyDataSetChanged();
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private class LoadFilteredData extends AsyncTask<List<InspectionResultsModel>, Void, Void> {
        @Override
        protected Void doInBackground(List<InspectionResultsModel>[] lists) {
            Log.d("DATA", "doInBackground: " + lists[0].get(1).getGrade());
            for (InspectionResultsModel data : lists[0]) {
                if (data.getGrade() != null && data.getGrade().equals("A")) {
                    listAresult.add(data);
                } else if (data.getGrade() != null && data.getGrade().equals("B")) {
                    listBresult.add(data);
                }
            }
            return null;
        }
    }
}

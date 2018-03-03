package com.example.franciscoandrade.truerating.view;

import android.annotation.SuppressLint;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.franciscoandrade.truerating.R;
import com.example.franciscoandrade.truerating.backend.LocationHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

/**
 * Created by c4q on 3/3/18.
 */

public class TestActivty extends AppCompatActivity {

    TextView locationView;
    String zipcode;
    FusedLocationProviderClient fusedLocationProviderClient;
    Geocoder geocoder;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        locationView = findViewById(R.id.location_text);

//        LocationHelper locationHelper = new LocationHelper(this, this);
//        String loc = locationHelper.getLocation();
        fusedLocationProviderClient = new FusedLocationProviderClient(getApplicationContext());
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        try {
                            geocoder = new Geocoder(TestActivty.this, Locale.ENGLISH);

                            zipcode = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getPostalCode();
                            locationView.setText(zipcode);

                        } catch (java.io.IOException e) {
                            Log.e("TAG", e.getMessage());
                        }
                    }
                });

    }
}

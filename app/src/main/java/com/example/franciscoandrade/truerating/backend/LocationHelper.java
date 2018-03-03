package com.example.franciscoandrade.truerating.backend;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

/**
 * Created by c4q on 3/3/18.
 */

public class LocationHelper {
    private Context context;
    private Activity activity;
    private Geocoder geocoder;
    private String zipcode;
    private FusedLocationProviderClient fusedLocationProviderClient;


    public LocationHelper(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
        fusedLocationProviderClient = new FusedLocationProviderClient(context);
        getPermission();
    }


    public boolean permitted() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void getPermission() {
        if (!permitted()) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    public void setZipcode(String zipcode){
        this.zipcode = zipcode;
    }

//    @SuppressLint("MissingPermission")
//    public String getLocation() {
//
//        fusedLocationProviderClient.getLastLocation()
//                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        try {
//                            geocoder = new Geocoder(context, Locale.ENGLISH);
//                            setZipcode(geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getPostalCode());
//                        } catch (java.io.IOException e) {
//                            Log.e("TAG", e.getMessage());
//                        }
//                    }
//                });
//        return zipcode;
//    }
}
package com.franciscoandrade.truerating.backend;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

/**
 * Created by c4q on 3/3/18.
 */

public class LocationHelper {
    private Context context;
    private Activity activity;

    public LocationHelper(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(context);
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
}
package com.example.franciscoandrade.truerating.backend;

import android.Manifest;
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
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by c4q on 3/3/18.
 */

public class LocationHelper implements LocationListener {
    private Context context;
    private Activity activity;
    private Geocoder geocoder;
    private String zipcode;
    private Address returnAddress;
    private LocationManager locationManager;



    public LocationHelper(Activity activity, Context context){
        this.context = context;
        this.activity = activity;
        getPermission();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }


    public boolean permitted(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            return false;
        }
        return true;
    }

    public void getPermission(){
        if(!permitted()){
            ActivityCompat.requestPermissions(activity, new String []{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
    }

    public String getZipcode(){
        return zipcode;
    }

    @Override
    public void onLocationChanged(Location location) {

        geocoder = new Geocoder(context, Locale.ENGLISH);

        try{
            returnAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1).get(0);
        } catch(java.io.IOException e){

        }
        zipcode = returnAddress.getPostalCode();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(context,"Location updated!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

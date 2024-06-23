package com.dating.flirtify.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationHelper {
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationResultListener locationResultListener;

    public LocationHelper(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (context instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        } else {
            getCurrentLocation();
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null && locationResultListener != null) {
                            String district = getDistrictFromLocation(location);
                            locationResultListener.onLocationReceived(location, district);
                        }
                    }
                });
    }

    private String getDistrictFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return address.getSubAdminArea(); // This gets the district (quận/huyện)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setLocationResultListener(LocationResultListener listener) {
        this.locationResultListener = listener;
    }

    public List<String> findAdjacentDistricts(Location location, double radiusInKm) {
        List<String> adjacentDistricts = new ArrayList<>();

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String currentDistrict = address.getSubAdminArea();

                // Find nearby addresses within the specified radius
                List<Address> nearbyAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                for (Address nearbyAddress : nearbyAddresses) {
                    String nearbyDistrict = nearbyAddress.getSubAdminArea();
                    if (nearbyDistrict != null && !nearbyDistrict.equals(currentDistrict)) {
                        // Calculate distance between current location and nearby location
                        float[] results = new float[1];
                        Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                                nearbyAddress.getLatitude(), nearbyAddress.getLongitude(), results);
                        double distanceInKm = results[0]; // Convert to kilometers
                        if (distanceInKm <= radiusInKm) {
                            adjacentDistricts.add(nearbyDistrict);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return adjacentDistricts;
    }


    public interface LocationResultListener {
        void onLocationReceived(Location location, String district);
    }
}

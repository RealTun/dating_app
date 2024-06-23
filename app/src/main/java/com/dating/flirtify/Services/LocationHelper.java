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
    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;
    private LocationResultListener locationResultListener;

    public LocationHelper(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // Yêu cầu quyền truy cập vị trí từ người dùng
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
    // Lấy vị trí hiện tại của người dùng
    public void getCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null && locationResultListener != null) {
                        String addressLine = getAddressLineFromLocation(location);
                        locationResultListener.onLocationReceived(location, addressLine);
                    }
                });
    }

    // Lấy địa chỉ cụ thể từ vị trí được cung cấp
    public String getAddressLineFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thiết lập listener để nhận kết quả vị trí
    public void setLocationResultListener(LocationResultListener listener) {
        this.locationResultListener = listener;
    }

    // Listener để nhận kết quả vị trí
    public interface LocationResultListener {
        void onLocationReceived(Location location, String addressLine);
    }
}

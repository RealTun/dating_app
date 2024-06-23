package com.dating.flirtify.Services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DistanceCalculator {

    // Tính khoảng cách giữa hai vị trí
    public static float calculateDistanceForLocation(Location location1, Location location2) {
        if (location1 != null && location2 != null) {
            return location1.distanceTo(location2);
        } else {
            return -1; // Xử lý trường hợp không tìm thấy vị trí
        }
    }

    // Tính khoảng cách giữa hai địa chỉ
    public static double calculateDistanceForAddress(Context context, String address1, String address2) {
        Location location1 = getLocationFromAddress(context, address1);
        Location location2 = getLocationFromAddress(context, address2);

        if (location1 != null && location2 != null) {
            return calculateDistanceForLocation(location1, location2);
        } else {
            return -1; // Xử lý trường hợp không tìm thấy vị trí
        }
    }

    // Lấy vị trí từ địa chỉ
    private static Location getLocationFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                Location resultLocation = new Location("locationProvider");
                resultLocation.setLatitude(location.getLatitude());
                resultLocation.setLongitude(location.getLongitude());
                return resultLocation;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

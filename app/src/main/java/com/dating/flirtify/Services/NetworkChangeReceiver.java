package com.dating.flirtify.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private boolean isConnected = true; // Ban đầu coi như đã kết nối

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (!isNetworkAvailable(context)) {
                if (isConnected) {
                    Toast.makeText(context, "Đã mất kết nối Internet", Toast.LENGTH_SHORT).show();
                    isConnected = false; // Cập nhật trạng thái mất kết nối
                }
            } else {
                if (!isConnected) {
                    Toast.makeText(context, "Đã khôi phục kết nối Internet", Toast.LENGTH_SHORT).show();
                    isConnected = true; // Cập nhật trạng thái đã kết nối lại
                }
            }
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}

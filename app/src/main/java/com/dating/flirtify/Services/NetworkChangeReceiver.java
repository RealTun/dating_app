package com.dating.flirtify.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (!isNetworkAvailable(context)) {
                // Xử lý khi mất kết nối Internet
                Toast.makeText(context, "Đã mất kết nối Internet", Toast.LENGTH_SHORT).show();
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

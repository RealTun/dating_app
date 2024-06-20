package com.dating.flirtify.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.dating.flirtify.Models.Responses.MatcherResponse;
import com.dating.flirtify.R;

import java.util.Date;

public class NotificationHelper {
    public static void showNotification(Context context, MatcherResponse matcher) {
        String channelId = "flirtify_channel_id";
        String channelName = "Flirtify Notifications";

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the notification channel if the API level is 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Flirtify")
                .setContentText(matcher.getFullname() + " sent you a new message.")
                .setAutoCancel(true);

        // Use the appropriate notification builder for API level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(channelId);
        }

        Notification notification = builder.build();

        if (manager != null) {
            manager.notify(getNotificationId(), notification);
            Log.d("Notification", "not null");
        }
    }

    private static int getNotificationId() {
        return (int) System.currentTimeMillis();
    }
}

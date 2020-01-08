package com.aliindustries.islamiccalendar;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Calendar;

import androidx.core.app.NotificationCompat;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static android.content.Context.MODE_PRIVATE;
import static androidx.core.util.Preconditions.checkArgument;

public class AlarmReceiver extends BroadcastReceiver{
    private static final String CHANNEL_ID = "com.aliindustries.islamiccalendar.channelId";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, Main2Activity.class);

        SharedPreferences prefs = context.getSharedPreferences("UserData", MODE_PRIVATE);
        String islam_month_txt = prefs.getString("islamicmonth","");
        String islam_day_txt = prefs.getString("islamicday","");
        String islam_yr_txt = prefs.getString("islamicyr","");


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Main2Activity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        Calendar calendar = Calendar.getInstance();

        Notification notification = builder.setContentTitle("Today is " + getDayOfMonthSuffix(Integer.parseInt(islam_day_txt)) + " " + islam_month_txt + " " + islam_yr_txt)
                .setContentText("Gregorian date is " + calendar.getTime().toString())
                .setTicker("New Notification!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX) // or NotificationCompat.PRIORITY_MAX
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationDemo",
                    NotificationManager.IMPORTANCE_HIGH
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(100, notification);
        }
    }
    @SuppressLint("RestrictedApi")
    String getDayOfMonthSuffix(final int n) {
        checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return n+"th";
        }
        switch (n % 10) {
            case 1:  return n+"st";
            case 2:  return n+"nd";
            case 3:  return n+"rd";
            default: return n+"th";
        }
    }

}
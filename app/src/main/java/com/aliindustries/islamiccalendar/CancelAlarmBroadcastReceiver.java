package com.aliindustries.islamiccalendar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class CancelAlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Notification Dialog Closed",
                Toast.LENGTH_LONG).show();
        Log.d("Notification:","Notification Dialog Closed");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,  0, new Intent(), 0);
        resultPendingIntent.cancel();
        NotificationCompat.Builder mb = new NotificationCompat.Builder(context);
        mb.setContentIntent(resultPendingIntent);
        if (manager != null) {
            manager.cancel(0);
        }
    }
}
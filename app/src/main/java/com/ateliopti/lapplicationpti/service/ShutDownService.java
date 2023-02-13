package com.ateliopti.lapplicationpti.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.receiver.ShutDownReceiver;

public class ShutDownService extends Service {

    NotificationManager notificationManager;

    NotificationChannel channel;

    private ShutDownReceiver shutDownReceiver;


    public ShutDownService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();

        final Intent launchNotificationIntent = new Intent(this, MainActivity.class);


        launchNotificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, launchNotificationIntent,
                PendingIntent.FLAG_IMMUTABLE);

        channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(getString(R.string.channel_id), getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_MIN);

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(getApplicationContext(), getString(R.string.channel_id))
                    .setOngoing(true) // impossible d'enlever
                    .setContentIntent(pendingIntent)
                    .setContentTitle(getString(R.string.notification_titre))
                    .setContentText(getString(R.string.notification_texte))
                    .setSmallIcon(R.drawable.pti_notification)
                    .build();


            startForeground(1, notification);

            if (shutDownReceiver == null) {
                shutDownReceiver = new ShutDownReceiver();
                registerReceiver(shutDownReceiver, new IntentFilter(Intent.ACTION_SHUTDOWN));
            }


        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }

        super.onDestroy();


        try {
            unregisterReceiver(shutDownReceiver);
        } catch (Exception ignored) {

        }


    }


}

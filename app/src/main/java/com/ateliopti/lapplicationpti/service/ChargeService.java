package com.ateliopti.lapplicationpti.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;


import androidx.annotation.Nullable;

import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.receiver.ChargerReceiver;

public class ChargeService extends Service {
    // Service lancé uniquement si "Chargement automatique" est décoché

    NotificationManager notificationManager;
    NotificationChannel channel;

    private ChargerReceiver chargerReceiver;

    public void onCreate() {
        super.onCreate();

        channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(getString(R.string.channel_eye_id), getString(R.string.channel_eye_name),
                    NotificationManager.IMPORTANCE_MIN);

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(getApplicationContext(), getString(R.string.channel_eye_id))

                    .setSmallIcon(R.drawable.pti_notification)
                    .build();


            startForeground(1, notification);

            System.out.println("@atelio " + " ONCREATE ChargeService");


        }


        if (chargerReceiver == null) {
            chargerReceiver = new ChargerReceiver();
            IntentFilter chargerFilter = new IntentFilter();
            chargerFilter.addAction(Intent.ACTION_POWER_CONNECTED);
            chargerFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
            registerReceiver(chargerReceiver, chargerFilter);
        }


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("@atelio " + " ONSTARTCOMMAND ALWAYSONSERVICE");

        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (notificationManager != null) { //If this is the only notification on your channel
                notificationManager.deleteNotificationChannel(channel.getId());
            }
            stopForeground(true);
        }

        super.onDestroy();

        try {
            unregisterReceiver(chargerReceiver);
        } catch (Exception ignored) {

        }


    }
}
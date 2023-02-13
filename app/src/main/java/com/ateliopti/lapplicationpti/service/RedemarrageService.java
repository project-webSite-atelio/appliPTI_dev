package com.ateliopti.lapplicationpti.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.R;


public class RedemarrageService extends Service {


    NotificationManager notificationManager;
    NotificationChannel channel;

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



        }


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            stopForeground(true);

            try{
                if (notificationManager != null && channel != null) { //If this is the only notification on your channel
                    notificationManager.deleteNotificationChannel(channel.getId()); // crash?
                }
            }catch (Exception ignored){

            }

        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {


        Intent dialogIntent = new Intent(this, MainActivity.class);
        dialogIntent.putExtra("user_action", "kill");
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(dialogIntent);

    }

}
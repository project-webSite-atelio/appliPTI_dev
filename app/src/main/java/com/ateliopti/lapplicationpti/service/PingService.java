package com.ateliopti.lapplicationpti.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ateliopti.lapplicationpti.AsyncPing;
import com.ateliopti.lapplicationpti.Fonctions;
import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.manager.EtatManager;

public class PingService extends Service {
    private Context context;
    private static CountDownTimer timerHommeMort;

    AsyncPing asyncPing;

    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    Intent intent = new Intent("homme_mort");

    NotificationManager notificationManager;
    NotificationChannel channel;

    EtatManager etatManager;

    Fonctions fonctions;

    int compteur = 0;
    int nombreMax = 3;
    int temps = 10;


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


            System.out.println("@atelio " + " ONCREATE PingService");


        }


        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "s:TempWakeLock");
    }

    @Override
    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }

        try {
            timerHommeMort.cancel();
            timerHommeMort = null;
        } catch (Exception ignored) {

        }

        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }

        //   LocalBroadcastManager.getInstance(this).unregisterReceiver(intent);


        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        context = getBaseContext();

        etatManager = new EtatManager(context);
        fonctions = new Fonctions(context);

        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }

        asyncPing = new AsyncPing(context);

        compteurHommeMort();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Compteur
    public void compteurHommeMort() {
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        executeCommand();

        timerHommeMort = new CountDownTimer(temps * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                System.out.println("demarrerService(: " + millisUntilFinished / 1000 + " | compteur " + compteur);
            }

            @Override
            public void onFinish() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        sendHommeMort();

                    }
                });
            }
        };
        timerHommeMort.start();
    }


    private void executeCommand() {

        asyncPing.run();

        if (asyncPing.getResult().equals("Succes")) {
            compteur = 0;
        } else {
            compteur++;
        }

    }

    // Envoi du signal
    private void sendHommeMort() {
        if (compteur >= nombreMax) {
            Intent intent = new Intent("ping");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            etatManager.open();
            etatManager.updateDate(fonctions.demandeDate());

            System.out.println("@atelio pti off par ping");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopForeground(true);
            } else {
                stopSelf();
            }
        } else {

            compteurHommeMort();
        }
    }
}
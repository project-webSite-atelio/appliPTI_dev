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

import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.manager.AlarmeIZSRManager;

import java.util.concurrent.TimeUnit;

public class IZSRService extends Service {

    private AlarmeIZSRManager alarmeIZSRManager;

    private static CountDownTimer timerIZSR;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    NotificationManager notificationManager;
    NotificationChannel channel;

    private Context context;


    private int dureeIZSR;


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


            System.out.println("@atelio " + " ONCREATE IZSRService");


        }


        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "s:TempWakeLock");

        System.out.println("IZSR");

    }

    public void compteurIZSR() {
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }

        System.out.println("Le temps défini est : " + dureeIZSR * 60);

        timerIZSR = new CountDownTimer(dureeIZSR * 1000 * 60, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                String izsrTexte = "ALARME ZONE SANS RESEAU DANS : %d MIN, %d SEC\nAppuyer longuement sur le Logo pour\n annuler l'alarme zone sans réseau";
                String temps = String.format(izsrTexte,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                );


                System.out.println("Compteur IZSR : " + millisUntilFinished / 1000);
                Intent intentActualiserBouton = new Intent("actualiserIzsrBouton");
                intentActualiserBouton.putExtra("nouveauTemps", temps);


                LocalBroadcastManager.getInstance(context).sendBroadcast(intentActualiserBouton);


            }

            @Override
            public void onFinish() {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        sendPopupIZSR();

                    }
                });

            }
        };
        timerIZSR.start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        context = getBaseContext();
        alarmeIZSRManager = new AlarmeIZSRManager(context);

        alarmeIZSRManager.open();

        dureeIZSR = intent.getIntExtra("temps", 5);


        compteurIZSR();


        return Service.START_STICKY;

    }


    public IZSRService() {


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

        try {
            timerIZSR.cancel();
            timerIZSR = null;
        } catch (Exception ignored) {

        }

        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }

        super.onDestroy();
    }

    private void sendPopupIZSR() {
        try {
            timerIZSR.cancel();
            timerIZSR = null;
        } catch (Exception ignored) {

        }

        Intent intent = new Intent("finCompteurIZSR");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}

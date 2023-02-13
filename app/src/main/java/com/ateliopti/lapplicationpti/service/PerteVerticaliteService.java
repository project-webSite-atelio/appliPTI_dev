package com.ateliopti.lapplicationpti.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;

import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.manager.AlarmePVManager;

public class PerteVerticaliteService extends Service implements SensorEventListener {

    private Context context;

    private static CountDownTimer timerVerticale; // Timer
    private static CountDownTimer verif5Secondes; // Timer

    private int tempsPerteVerticalite; // XML
    private int angle; // XML

    private boolean perteVerticalite; // XML
    private final boolean finPerte = false;
    private boolean avantPrealarmeVerif = false;

    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    AlarmePVManager alarmePVManager;

    private boolean verifReleve = false; // Vérification si le téléphone est relevé

    // BDD bdd;

    boolean uneAnnulation = false;

    // Utilisation de l'accélérométre
    float x, y, z;
    Display mDisplay;
    SensorManager sensorManager = null;
    Sensor accelerometre;
    NotificationManager notificationManager;
    NotificationChannel channel;

    private float[] mGravity;
    private float[] mMagnetic;
    double tan ;
    private float yy;
    private float tan2;

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


            System.out.println("@atelio " + " ONCREATE PerteVerticaliteService");


        }

        // TODO : à supprimer
        System.out.println("Service actif : perte de verticalité");


        powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "s:TempWakeLock");

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mDisplay = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        // Nécessaire pour que le sensor soit utilisé dans le service
        sensorManager.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    public void onDestroy() {


        sensorManager.unregisterListener(this, accelerometre);
        sensorManager = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }

        System.out.println("Service inactif : pV");
        uneAnnulation = false;
        try {
            timerVerticale.cancel();
            timerVerticale = null;
        } catch (Exception ignored) {

        }

        if (wakeLock != null) {
            if (wakeLock.isHeld()) {
                wakeLock.release();
                wakeLock = null;
            }
        }

        super.onDestroy();


    }


    @SuppressLint("WakelockTimeout")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        context = getBaseContext();

        uneAnnulation = false;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mDisplay = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        // Récupération des données du XML
        perteVerticalite = true;
        //  bdd.open();

        alarmePVManager = new AlarmePVManager(context);

        alarmePVManager.open();

        tempsPerteVerticalite = alarmePVManager.getAlarmePV().getPvDureeDetection();
        angle = 90 - alarmePVManager.getAlarmePV().getPvAngleDetection();

        System.out.println("pv tempsPerteVerticalite = " + tempsPerteVerticalite);


        if (perteVerticalite) {
            System.out.println("Perte verticale activée");

            if (wakeLock != null) {
                if (!wakeLock.isHeld()) {
                    wakeLock.acquire();
                }
            }
            compteurVerticale();


        } else {
            System.out.println(perteVerticalite + " fonction désactivée");
        }
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Rien à faire
    }

    @Override
    public void onSensorChanged(SensorEvent event) {




        if (timerVerticale != null) {
            if (perteVerticalite) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    switch (mDisplay.getRotation()) {
                        case Surface.ROTATION_0:
                            x = event.values[0];
                            y = event.values[1];
                            break;
                        case Surface.ROTATION_90:
                            x = -event.values[1];
                            y = event.values[0];
                            break;
                        case Surface.ROTATION_180:
                            x = -event.values[0];
                            y = -event.values[1];
                            break;
                        case Surface.ROTATION_270:
                            x = event.values[1];
                            y = -event.values[0];
                            break;
                    }

                    switch (mDisplay.getRotation()) {
                        case Surface.ROTATION_0:
                            yy = event.values[1];
                            z = event.values[2];
                            break;
                        case Surface.ROTATION_90:
                            yy = -event.values[2];
                            z = event.values[1];
                            break;
                        case Surface.ROTATION_180:
                            yy = -event.values[1];
                            z = -event.values[2];
                            break;
                        case Surface.ROTATION_270:
                            yy = event.values[2];
                            z = -event.values[1];
                            break;
                    }

                    double newAngle1 = 0;
                    double newAngle2 = 0;
                    tan = y/x;
                    tan2= yy/z;
                    newAngle1 = Math.toDegrees((double)Math.atan(tan));
                    newAngle2 = Math.toDegrees((double)Math.atan(tan2));


/*
                    float accelerometerMaxRange = 10; // This is NOT right, but it's a good value to work with

                    if (z > 9) {
                        // Phone is horizontally flat, can't point towards gravity, really. Do whatever you think is right
                    } else {
                        newAngle  = (float)(x * 90 / accelerometerMaxRange);
                        if (y < 0) {
                            newAngle = 180 - newAngle;
                        }
                    }*/

                    System.out.println("@ATELIO ANGLE TYPE_MAGNETIC_FIELD = " + newAngle2);
                  //  System.out.println("@ATELIO ANGLE x = " + x + " / y = " + y + " / z = "+ z);

                    z = event.values[2]; // la valeur "z"

                    // Calcul de l'angle
                    if (Math.abs(newAngle1) <= angle || Math.abs(newAngle2) <= angle ) { // Si l'appareil n'est pas redressé

                        System.out.println("@atelio pv Y " + y + "  vs " + angle);

                        if (verif5Secondes != null) {
                            verif5Secondes.cancel();
                        }

                        if (avantPrealarmeVerif) {
                            if (verif5Secondes != null) {
                                verif5Secondes.cancel();
                                verif5Secondes.start();
                            }

                        }
                        avantPrealarmeVerif = false;
                    } else {
                        if (!avantPrealarmeVerif) {
                            verif5Secondes();
                            avantPrealarmeVerif = true;
                        }
                    }
                }
            }
        }
    }

    // Compteur
    public void compteurVerticale() {


        if (wakeLock != null) {
            if (!wakeLock.isHeld()) {
                wakeLock.acquire(10 * 60 * 1000L);
            }
        }


        System.out.println("pv compteur tempsPerteVerticalite = " + tempsPerteVerticalite);

        timerVerticale = new CountDownTimer(tempsPerteVerticalite * 1000, 1000) {
            @Override

            public void onTick(long millisUntilFinished) {
                //   System.out.println("Vertically loss : " + millisUntilFinished / 1000);

                System.out.println("Compteur Perte de verticalité (" + millisUntilFinished / 1000 + ")");


            }

            @Override
            public void onFinish() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> sendMessageVerticalite());
            }
        };

        timerVerticale.start();
    }

    public void verif5Secondes() {
        verif5Secondes = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (finPerte) {
                    System.out.println("Verif : " + millisUntilFinished / 1000);
                } else {
                    System.out.println("Verif2 : " + millisUntilFinished / 1000);
                }
            }

            @Override
            public void onFinish() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {

                    if (!uneAnnulation && verifReleve && !finPerte) {
                        uneAnnulation = true;

                        if (verif5Secondes != null) {
                            verif5Secondes.cancel();
                        }
                        try {
                            timerVerticale.cancel();
                            timerVerticale = null;
                        } catch (Exception ignored) {

                        }
                        verifReleve = false;
                    }

                    if (avantPrealarmeVerif) {
                        try {
                            if (timerVerticale != null) {
                                timerVerticale.cancel();
                                timerVerticale.start();
                            }

                        } catch (Exception ignored) {

                        }
                        avantPrealarmeVerif = false;
                    }
                });
            }
        };
        verif5Secondes.start();
    }

    // Envoi du signal
    private void sendMessageVerticalite() {
        try {
            timerVerticale.cancel();
            timerVerticale = null;
        } catch (Exception ignored) {

        }
        Intent intent = new Intent("alarme_perte_verticalite");

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        } else {
            stopSelf();
        }
    }

}
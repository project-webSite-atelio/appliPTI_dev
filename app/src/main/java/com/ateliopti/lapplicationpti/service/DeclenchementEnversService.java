package com.ateliopti.lapplicationpti.service;

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

public class DeclenchementEnversService extends Service implements SensorEventListener {
    private static CountDownTimer timerDeclenchementEnvers; // Timer
    float x, y, z;
    Display mDisplay;
    SensorManager sensorManager = null;
    Sensor accelerometre;
    NotificationManager notificationManager;
    NotificationChannel channel;
    private Context context;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    public DeclenchementEnversService() {
    }


    public void onCreate() {
        super.onCreate();

        final Intent launchNotificationIntent = new Intent(this, MainActivity.class);


        launchNotificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchNotificationIntent, PendingIntent.FLAG_IMMUTABLE);
        // TODO bug ?

        channel = null;

        try {

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
        } catch (Exception ignored) {

        }

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "s:TempWakeLock");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mDisplay = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        // Nécessaire pour que le sensor soit utilisé dans le service
        sensorManager.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        context = getApplicationContext();
        if (wakeLock != null) {
            if (!wakeLock.isHeld()) {
                wakeLock.acquire();
            }
        }
        compteurDeclenchementEnvers();

        return Service.START_STICKY;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Rien à faire
    }

    public void compteurDeclenchementEnvers() {

        if (wakeLock != null) {
            if (!wakeLock.isHeld()) {
                wakeLock.acquire();
            }
        }


        //  System.out.println("pv compteur tempsPerteVerticalite = " + tempsPerteVerticalite);

        timerDeclenchementEnvers = new CountDownTimer(5 * 1000, 1000) {
            @Override

            public void onTick(long millisUntilFinished) {
                //   System.out.println("Vertically loss : " + millisUntilFinished / 1000);

                System.out.println("@atelio declenchement envers (" + millisUntilFinished / 1000 + ")");
            }

            @Override
            public void onFinish() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        sendDeclenchementEnvers();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            stopForeground(true);
                        } else {
                            stopSelf();
                        }
                    }
                });
            }
        };
        timerDeclenchementEnvers.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (timerDeclenchementEnvers != null) {
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
                z = event.values[2]; // la valeur "z"


                // int inclination = (int) Math.round(Math.toDegrees(Math.acos(nZ)));

                // int angle = 90;

                if (y < -8) { // Appareil tête à l'envers
                    System.out.println("@atelio declenchement envers");
                } else {
                    if (timerDeclenchementEnvers != null) {
                        timerDeclenchementEnvers.cancel();
                        timerDeclenchementEnvers.start();
                    }
                }
                //          System.out.println("x = " + x + " / y = " + y + " /  z = " + z);
            }
        }
    }

    @Override
    public void onDestroy() {


        super.onDestroy();

        sensorManager.unregisterListener(this, accelerometre);
        sensorManager = null;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) { //If this is the only notification on your channel
                     //       notificationManager.deleteNotificationChannel(channel.getId());
                }
                stopForeground(true);


            }
        } catch (Exception ignored) {

        }


        try {
            timerDeclenchementEnvers.cancel();
            timerDeclenchementEnvers = null;
        } catch (Exception ignored) {

        }

    }

    private void sendDeclenchementEnvers() {
        try {
            timerDeclenchementEnvers.cancel();
            timerDeclenchementEnvers = null;
        } catch (Exception ignored) {

        }

        Intent intent = new Intent("declenchement_envers");
        //intent.putExtra("message", "alarmemouvement");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        if (wakeLock != null) {
            if (wakeLock.isHeld()) {
                wakeLock.release();
                wakeLock = null;
            }
        }

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

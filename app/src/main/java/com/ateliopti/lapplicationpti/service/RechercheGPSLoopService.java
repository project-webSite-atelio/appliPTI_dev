package com.ateliopti.lapplicationpti.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ateliopti.lapplicationpti.Fonctions;
import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.manager.TempLocalisationManager;
import com.ateliopti.lapplicationpti.manager.TrajetManager;
import com.ateliopti.lapplicationpti.model.TempLocalisation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RechercheGPSLoopService extends Service {
    Context context;
    Fonctions fonctions;
    boolean find = false; // position trouvé
    boolean findPrealarme = false; // position trouvé lors d'une préalarme

    boolean prealarmeTrigger = false; // pour empêcher une seconde exécution
    int dureeRecherchePrealarme; // durée de la recherche de la localisation lors d'une préalarme

    // Timers
    private static CountDownTimer timerGps; // Timer
    private static CountDownTimer timerGpsOff;
    private static CountDownTimer timerPrealarme;

    TrajetManager trajetManager;
    TempLocalisationManager tempLocalisationManager;
    PowerManager powerManager;

    PowerManager.WakeLock wakeLock;

    // Notification
    NotificationManager notificationManager;
    NotificationChannel channel;

    // Coordonnées
    private double currentLatitude;
    private double currentLongitude;
    private float currentAccuracy;

    // Google services
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;

    public void onCreate() {
        super.onCreate();

        // Filtres
        final IntentFilter filter = new IntentFilter();
        filter.addAction("prealarmeGPS");
        filter.addAction("annulation_prealarmeGPS"); // annuler la recherche du gps pendant la préalarme

        this.registerReceiver(gpsBroadcastReceiver, filter);


        // Notification
        final Intent launchNotificationIntent = new Intent(this, MainActivity.class);
        launchNotificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        @SuppressLint("UnspecifiedImmutableFlag") final PendingIntent pendingIntent = PendingIntent.getActivity(this,
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


            System.out.println("@atelio " + " ONCREATE RechercheGPSLoopService");
        }


        trajetManager = new TrajetManager(context);
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        // Permet de garder le service allumé
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "s:TempWakeLock");

    }

    private final BroadcastReceiver gpsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals("prealarmeGPS")) {

                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        for (String key : bundle.keySet()) {
                            Log.e("@atelio", key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                        }
                    }

                    if (intent.hasExtra("dureeRecherchePrealarme")) { // durée de recherche prealarme : " + dureeRecherchePrealarme)) {
                        System.out.println("@atelio INTENT RECEIVED");
                        dureeRecherchePrealarme = intent.getExtras().getInt("dureeRecherchePrealarme");
                    }

                    recherchePrealarme();
                } else if (intent.getAction().equals("annulation_prealarmeGPS")) {
                    annulerRecherchePrealarme();
                }
            }
        }


    };

    private void annulerRecherchePrealarme() {
        // Destruction du timer, et permission de la recherche préalarme
        System.out.println("GPSLOOP " + " Arret Recherche PREALARME");

        System.out.println("@atelio extinction recherche gps préalarme");
        if (timerPrealarme != null) {
            timerPrealarme.cancel();
            timerPrealarme = null;
        }
        prealarmeTrigger = false;
    }

    // Recherche de localisation lors d'une préalarme
    public void recherchePrealarme() {
        System.out.println("@atelio recherche prealarme");

        // Empeche une seconde execution
        if (!prealarmeTrigger) {
            prealarmeTrigger = true;
            requeteGps();
            timerPrealarme();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }


        try {
            unregisterReceiver(gpsBroadcastReceiver);
        } catch (Exception ignored) {

        }

        try {
            timerGps.cancel();
            timerGps = null;
        } catch (Exception ignored) {

        }

        try {
            timerGpsOff.cancel();
            timerGpsOff = null;
        } catch (Exception ignored) {

        }

        pause();

        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }

    }

    @SuppressLint("WakelockTimeout")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();

        trajetManager = new TrajetManager(context);
        tempLocalisationManager = new TempLocalisationManager(context);
        context = getApplicationContext();

        fonctions = new Fonctions(context);

        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10 * 1000);

        timerGpsOn();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Compteur
    public void timerGpsOn() {

        System.out.println("GPS ON : départ recherche");

        trajetManager.open();
        int temps = trajetManager.getTrajet().getDureeRechercheGps();

        requeteGps();

        if (timerGpsOff != null) {
            timerGpsOff.cancel();
        }

        final int precision = trajetManager.getTrajet().getPrecisionGps();

        timerGps = new CountDownTimer(temps * 1000L, 1000) {
            @SuppressLint("WakelockTimeout")
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("GPS ON : " + millisUntilFinished / 1000 + " " + currentLatitude + "/" +
                        currentLongitude + "    --- " + currentAccuracy + " (loop)");
                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "s:TempWakeLock");

                if (wakeLock != null && !wakeLock.isHeld()) {
                    wakeLock.acquire();
                }

                if (((int) currentAccuracy <= precision) && !find && currentLatitude != 0 && currentLongitude != 0) {
                    find = true;

                    String latitude = String.valueOf(currentLatitude);
                    String longitude = String.valueOf(currentLongitude);
                    String date = fonctions.demandeDate();

                    TempLocalisation tempLocalisation = new TempLocalisation(date, latitude, longitude, "");
                    tempLocalisationManager.open();
                    tempLocalisationManager.updateTempLocalisation(tempLocalisation);

                    System.out.println("ATELIO-ESSAI " + tempLocalisation.toString());
                    pause();
                    positionGPSSucces();

                    timerGpsOff();
                }
            }

            @Override
            public void onFinish() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> timerGpsOff());
            }
        };
        timerGps.start();
    }

    // Compteur
    @SuppressLint("WakelockTimeout")
    public void timerGpsOff() {

        System.out.println("GPS : arrêt recherche");

        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }

        trajetManager.open();

        int temps = trajetManager.getTrajet().getDureePauseGps();

        pause();

        System.out.println("Le temps défini est : " + temps);

        if (timerGps != null) {
            timerGps.cancel();
        }

        if (timerGpsOff != null) {
            timerGpsOff.cancel();
        }

        timerGpsOff = new CountDownTimer(temps * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("GPS OFF : " + millisUntilFinished / 1000 + " (loop)");
            }

            @Override
            public void onFinish() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {

                    find = false; // Permettre la modification de la localisation
                    timerGpsOn();
                });
            }
        };
        timerGpsOff.start();
    }

    public void timerPrealarme() {
        System.out.println("GPS Recherche PREALARME");

        findPrealarme = false;
        // possibilité de prealarme durée


        int temps = dureeRecherchePrealarme;

        requeteGps();

        if (timerPrealarme != null) {
            timerPrealarme.cancel();
        }
        trajetManager.open();

        final int precision = trajetManager.getTrajet().getPrecisionGps();

        timerPrealarme = new CountDownTimer(temps * 1000L, 1000) {
            @SuppressLint("WakelockTimeout")
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("GPS OPREALARME : " + millisUntilFinished / 1000 + " " + currentLatitude + "/" +
                        currentLongitude + "    --- " + currentAccuracy + " (" + temps + ")");
                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "s:TempWakeLock");

                if ((wakeLock != null) &&
                        (!wakeLock.isHeld())) {
                    wakeLock.acquire();
                }

                //
                if (((int) currentAccuracy <= precision) && !findPrealarme && currentLatitude != 0 && currentLongitude != 0) {
                    findPrealarme = true;

                    String latitude = String.valueOf(currentLatitude);
                    String longitude = String.valueOf(currentLongitude);
                    String date = fonctions.demandeDate();

                    TempLocalisation tempLocalisation = new TempLocalisation(date, latitude, longitude, "");
                    tempLocalisationManager.open();
                    tempLocalisationManager.updateTempLocalisation(tempLocalisation);

                    System.out.println("GPS OPREALARME succes " + tempLocalisation.toString());
                    positionGPSSucces();
                    timerPrealarme.cancel();
                    prealarmeTrigger = false;

                }
            }

            @Override
            public void onFinish() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {

                    timerPrealarme.cancel();
                    prealarmeTrigger = false;


                });
            }
        };
        timerPrealarme.start();
    }

    // Envoi du signal
    private void positionGPSSucces() {

        System.out.println("GPS Position actualisé");


        Intent intent = new Intent("position_find");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void requeteGps() {

        // Réinitialisation
        currentLatitude = 0;
        currentLongitude = 0;
        currentAccuracy = 1000f;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.i("MapsActivity", "Location: trouvé");
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);

                trajetManager.open();

                final int precision = trajetManager.getTrajet().getPrecisionGps();

                trajetManager.close();

                if (location.getLatitude() != 0 && location.getLongitude() != 0 && location.getAccuracy() < precision) {

                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
                    currentAccuracy = location.getAccuracy();

                    Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());

                    String serviceValue = "Location: (date " + demandeDate() + ") " + location.getLatitude() + "/" + location.getLongitude();
                    System.out.println(serviceValue);


                    Intent intent = new Intent("service_receive");

                    intent.putExtra("value", serviceValue);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


                    mLastLocation = location;
                    if (mCurrLocationMarker != null) {
                        mCurrLocationMarker.remove();
                    }

                }
            }
        }
    };

    public String demandeDate() {
        Date tempsActuel = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(tempsActuel);
    }

    public void pause() {
        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

}

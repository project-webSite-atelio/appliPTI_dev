package com.ateliopti.lapplicationpti;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.Settings;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.manager.AlarmeAMManager;
import com.ateliopti.lapplicationpti.manager.AlarmeAgressionManager;
import com.ateliopti.lapplicationpti.manager.AlarmeIZSRManager;
import com.ateliopti.lapplicationpti.manager.AlarmePVManager;
import com.ateliopti.lapplicationpti.manager.AlarmeSOSManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.ateliopti.lapplicationpti.manager.TempLocalisationManager;
import com.ateliopti.lapplicationpti.model.AlarmeAM;
import com.ateliopti.lapplicationpti.model.AlarmeAgression;
import com.ateliopti.lapplicationpti.model.AlarmeIZSR;
import com.ateliopti.lapplicationpti.model.AlarmePV;
import com.ateliopti.lapplicationpti.model.Options;
import com.ateliopti.lapplicationpti.model.TempLocalisation;
import com.ateliopti.lapplicationpti.receiver.NotationOuiReceiver;
import com.ateliopti.lapplicationpti.receiver.NotationNonReceiver;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


import static com.ateliopti.lapplicationpti.R.style.ThemeAstre;
import static com.ateliopti.lapplicationpti.R.style.ThemeAtelio;
import static com.ateliopti.lapplicationpti.R.style.ThemeCofely;
import static com.ateliopti.lapplicationpti.R.style.ThemeMyAngel;
import static com.ateliopti.lapplicationpti.R.style.ThemeScoreMb;
import static com.ateliopti.lapplicationpti.R.style.ThemeNexecur;
import static com.ateliopti.lapplicationpti.R.style.ThemeOfficeEasy;
import static com.ateliopti.lapplicationpti.R.style.ThemeOneDirect;
import static com.ateliopti.lapplicationpti.R.style.ThemeOnet;
import static com.ateliopti.lapplicationpti.R.style.ThemePepperlFuchs;
import static com.ateliopti.lapplicationpti.R.style.ThemeProsegur;
import static com.ateliopti.lapplicationpti.R.style.ThemeSaasse;
import static com.ateliopti.lapplicationpti.R.style.ThemeScoreMb;
import static com.ateliopti.lapplicationpti.R.style.ThemeSeris;
import static com.ateliopti.lapplicationpti.R.style.ThemeSerisLive;

import static com.ateliopti.lapplicationpti.helper.NotificationHelper.NOTIFICATION_CHANNEL_ID;

public class Fonctions {

    private static MediaPlayer mp; //new MediaPlayer();

    private static final Handler handler = new Handler();
    private static Vibrator vibrator = null; // Vibreur
    private static Runnable runnable = null;

    private final Context context;
    private final TelephonyManager telephonyManager;
    private final OptionsManager optionsManager;

    private final TempLocalisationManager tempLocalisationManager;

    // Alarmes Manager
    private final AlarmeSOSManager alarmeSOSManager;
    private final AlarmeAgressionManager alarmeAgressionManager;
    private final AlarmePVManager alarmePVManager;
    private final AlarmeAMManager alarmeAMManager;
    private final AlarmeIZSRManager alarmeIZSRManager;

    public Fonctions(Context context) {
        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        optionsManager = new OptionsManager(context);

        alarmeSOSManager = new AlarmeSOSManager(context);
        alarmeAgressionManager = new AlarmeAgressionManager(context);
        alarmePVManager = new AlarmePVManager(context);
        alarmeAMManager = new AlarmeAMManager(context);
        alarmeIZSRManager = new AlarmeIZSRManager(context);

        tempLocalisationManager = new TempLocalisationManager(context);

        mp = MediaPlayerFonctions.getInstance();

    }

    // Vibreur 
    private Vibrator getVibrator() {
        if (vibrator == null) {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        return vibrator;
    }

    // Récupérer le numéro IMEI de l'appareil
    public String getIMEI() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);


        } else {

            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions

                    return "";
                }
                String imei = telephonyManager.getDeviceId();
                Log.e("imei", "=" + imei);
                if (imei != null && !imei.isEmpty()) {
                    return imei;
                } else {
                    return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }


    }

    // Séparer l'IMEI en 5 parties de 3 numéros
    String separateurIMEI(String imei) {
        String str = "";

        System.out.println("@atelio imei " + imei);

        try {
            if (imei == null || imei.equals("Erreur")) {
                return "En attente de la première activation PTI";
            } else {
                str = imei;
                List<String> parts = new ArrayList<String>();
                int index = 0;

                int totalChars = str.length();


                if (totalChars == 15) { // IMEI
                    while (index < totalChars) {
                        parts.add(str.substring(index, Math.min(index + 3, totalChars)));
                        index += 3;
                    }

                    return parts.get(0) + " " + parts.get(1) + " " + parts.get(2) + " " + parts.get(3) + " " + parts.get(4);

                } else { // Android ID
                    while (index < totalChars) {
                        parts.add(str.substring(index, Math.min(index + 4, totalChars)));
                        index += 4;
                    }

                    return parts.get(0) + " " + parts.get(1) + " " + parts.get(2) + " " + parts.get(3);
                }


            }
        } catch (Exception ignored) {
            return "";
        }
    }

    public String[] getConfigSituations() {
        optionsManager.open();

        List<String> situationsListe = new ArrayList<>();

        if (optionsManager.getOptions().isaTrajet()) {
            situationsListe.add("En extérieur");
        }

        if (optionsManager.getOptions().isaBatiment()) {
            situationsListe.add("En intérieur");
        }

        if (optionsManager.getOptions().isaIzsr()) {
            situationsListe.add("En zone sans réseau");
        }

        String[] stringSituationsListe = new String[situationsListe.size()];
        stringSituationsListe = situationsListe.toArray(stringSituationsListe);

        return stringSituationsListe;
    }

    public String[] getTypesAlarme() {
        alarmeAgressionManager.open();
        alarmeAMManager.open();
        alarmePVManager.open();

        List<String> typesAlarmeListe = new ArrayList<>();

        AlarmePV alarmePV = alarmePVManager.getAlarmePV();
        AlarmeAM alarmeAM = alarmeAMManager.getAlarmeAM();
        AlarmeAgression alarmeAgression = alarmeAgressionManager.getAlarmeAgression();


        if (alarmePV.isaAlarmePv()) {
            int dureeDetection = alarmePV.getPvDureeDetection();
            typesAlarmeListe.add("Volontaire" + " +\n" + "position allongée" + " " + dureeDetection + "sec");
        }

        if (alarmeAM.isaAlarmeAm()) {
            int dureeDetection = alarmeAM.getAmDureeDetection();
            typesAlarmeListe.add("Volontaire" + " +\n" + "position immobile" + " " + dureeDetection + "sec");
        }

        if (alarmeAgression.isaAlarmeAgression()) {
            int dureeDetection = alarmeAgression.getAgressionDureeDetection();
            typesAlarmeListe.add("Volontaire" + " +\n" + "homme mort" + " " + dureeDetection + "min");
        }

        String[] stringTypesAlarmeListe = new String[typesAlarmeListe.size()];
        stringTypesAlarmeListe = typesAlarmeListe.toArray(stringTypesAlarmeListe);

        return stringTypesAlarmeListe;


    }

    public String demandeDate() {
        Date tempsActuel = Calendar.getInstance().getTime();
        String pattern = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(tempsActuel);
    }

    public String getDateRecord() {
        tempLocalisationManager.open();

        //   System.out.println("DATE RECORD" + tempLocalisationManager.getTempLocalisation().getDate());

        if (!tempLocalisationManager.getTempLocalisation().getDate().isEmpty()) {

            return tempLocalisationManager.getTempLocalisation().getDate().replace('/', '-');

        } else {
            Date tempsActuel = Calendar.getInstance().getTime();
            String pattern = "dd-MM/-yyyy HH:mm:ss"; // avec tiret sinon TLG ne valide pas le message
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            return sdf.format(tempsActuel);
        }

    }


    public boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    void viderTempLocalisation() {
        tempLocalisationManager.open();

        TempLocalisation tempLocalisation = new TempLocalisation(1, "", "", "", "");
        tempLocalisationManager.updateTempLocalisation(tempLocalisation);


    }

    /*
     * jouerSon
     * @params: String
     * joue le son donné en param
     */
    public void jouerSon(String son) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int soundId = 0;
        int volume = 100;

        Resources res = context.getResources();
        am.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);

        soundId = res.getIdentifier(son, "raw", context.getPackageName());
        AssetFileDescriptor afd = res.openRawResourceFd(soundId);

        mp.reset();
        mp.setAudioStreamType(AudioManager.STREAM_ALARM); // hauts-parleurs


        try {
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
    }

    public void vibrer(boolean vibre) {
        if (vibre) {
            try {
                runnable = new Runnable() {
                    public void run() {
                        long[] pattern = {0, 1000};
                        getVibrator().vibrate(pattern, 0);
                    }
                };
                handler.postDelayed(runnable, 500);
            } catch (Exception ignored) {

            }
        } else {
            try {
                handler.removeCallbacks(runnable);
                getVibrator().cancel();
            } catch (Exception ignored) {

            }
        }
    }


    void jouerBuzzerPing(boolean joue) {

        if (joue) {

            optionsManager.open();
            int volume = 100;


            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            am.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);


            //    am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);


            Resources res = context.getResources();
            AssetFileDescriptor afd = res.openRawResourceFd(R.raw.warning);

            mp.reset();


            mp.setAudioStreamType(AudioManager.STREAM_ALARM); // hauts-parleurs

            if (!mp.isLooping()) {
                mp.setLooping(true);
            }

            try {
                mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();

        } else {
            try {
                mp.stop();
            } catch (Exception ignored) {

            }
        }

    }


    public void jouerSonAlarme(boolean joue) {

        if (joue) {

            optionsManager.open();


            int volume = optionsManager.getOptions().getPuissanceSonore();

            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

            float percent = (float) volume / 10;

            int nouveauVolume = (int) (maxVolume * percent);
            am.setStreamVolume(AudioManager.STREAM_MUSIC, nouveauVolume / 10, 0);


            Resources res = context.getResources();
            AssetFileDescriptor afd = res.openRawResourceFd(R.raw.alarme);

            mp.reset();


            mp.setAudioStreamType(AudioManager.STREAM_MUSIC); // hauts-parleurs

            if (!mp.isLooping()) {
                mp.setLooping(true);
            }


            try {
                mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();

        } else {
            try {
                mp.stop();
            } catch (Exception ignored) {

            }
        }

    }

    // Returns filename, main hexadecimal color and id theme of logo
    public String[] chargerTheme() {
        optionsManager.open();

        String[] res = new String[3];


        try {
            String logo = optionsManager.getOptions().getLogo();


            switch (logo) {
                case "ATELIO":
                    res[0] = "#80387D";
                    res[1] = "logo_dark";
                    res[2] = String.valueOf(ThemeAtelio);
                    break;

                case "OFFICE EASY":
                    res[0] = "#186A8F";
                    res[1] = "logo_office_easy";
                    res[2] = String.valueOf(ThemeOfficeEasy);
                    break;

                case "ASTRE":
                    res[0] = "#235F9B";
                    res[1] = "logo_astre";
                    res[2] = String.valueOf(ThemeAstre);
                    break;

                case "SAASSE":
                    res[0] = "#020202";
                    res[1] = "logo_saasse";
                    res[2] = String.valueOf(ThemeSaasse);
                    break;

                case "SERIS":
                    res[0] = "#09458D";
                    res[1] = "logo_seris";
                    res[2] = String.valueOf(ThemeSeris);
                    break;

                case "SERIS LIVE":
                    res[0] = "#004A94";
                    res[1] = "logo_seris_live";
                    res[2] = String.valueOf(ThemeSerisLive);
                    break;

                case "PEPPERL+FUCHS":
                    res[0] = "#00A486";
                    res[1] = "logo_pepperl_fuchs";
                    res[2] = String.valueOf(ThemePepperlFuchs);
                    break;

                case "ONET":
                    res[0] = "#03366E";
                    res[1] = "logo_onet";
                    res[2] = String.valueOf(ThemeOnet);
                    break;

                case "PROSEGUR":
                    res[0] = "#F8D219";
                    res[1] = "logo_prosegur";
                    res[2] = String.valueOf(ThemeProsegur);
                    break;

                case "NEXECUR":
                    res[0] = "#F7A701";
                    res[1] = "logo_nexecur";
                    res[2] = String.valueOf(ThemeNexecur);
                    break;

                case "COFELY":
                    res[0] = "#00B0F4";
                    res[1] = "logo_cofely";
                    res[2] = String.valueOf(ThemeCofely);
                    break;

                case "ONEDIRECT":
                    res[0] = "#231F20";
                    res[1] = "logo_onedirect";
                    res[2] = String.valueOf(ThemeOneDirect);
                    break;

                case "MY ANGEL":
                    res[0] = "#007FC7";
                    res[1] = "logo_my_angel";
                    res[2] = String.valueOf(ThemeMyAngel);
                    break;

                case "MY PTI": // Ancien nom
                case "SCORE MB":
                    res[0] = "#384A59";
                    res[1] = "logo_score_mb";
                    res[2] = String.valueOf(ThemeScoreMb);
                    break;

                default: // Atelio
                    res[0] = "#384A59";
                    res[1] = "logo_atelio";
                    res[2] = String.valueOf(ThemeAtelio);
                    break;
            }
        } catch (Exception e) {

            res[0] = "#80387D";
            res[1] = "logo_dark";
            res[2] = String.valueOf(R.drawable.logo_dark);
        }


        return res;


    }

    static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public boolean verificationPing() {
        try {

            String hostname = "";

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
            int timeout = 1500;
            Socket sock = new Socket();

            if (context.getResources().getString(R.string.app_type).equals("prod")) {
                hostname = context.getResources().getString(R.string.server_url);
            } else {
                hostname = context.getResources().getString(R.string.server_url_dev);
            }


            SocketAddress socketAddress = new InetSocketAddress(hostname, 22);

            sock.connect(socketAddress, timeout);
            sock.close();

            return true;

        } catch (IOException e) {
            return false;
        }


    }


    // Renvoie vrai si c'est une adresse e-mail
    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +  //part before @
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }


    public void createNotification(Intent intent, String value) {

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final Intent launchNotificationIntent = new Intent(context, MainActivity.class);

        launchNotificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0, launchNotificationIntent,
                PendingIntent.FLAG_IMMUTABLE);


        int drawableId = 0;
        if (android.os.Build.VERSION.SDK_INT > 23) {
            drawableId = R.drawable.surveillance_old;
        } else {
            drawableId = R.drawable.surveillance_old;
        }


        if (android.os.Build.VERSION.SDK_INT >= 23) {


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
            mBuilder.setSmallIcon(drawableId);
            mBuilder.setContentTitle("Rappel : Utilisez l'application PTI")
                    .setContentText("Dès que vous êtes en situation de travail isolé …")
                    .setAutoCancel(false)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)

                    .setContentIntent(pendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                assert mNotificationManager != null;
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert mNotificationManager != null;
            mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
        } else {

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getService(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Resources res = context.getResources();
            Notification.Builder builder = new Notification.Builder(context);

            builder.setContentIntent(contentIntent)
                    .setSmallIcon(drawableId)
                    // .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.surveillance))
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(false)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentTitle("Rappel : Utilisez l'application PTI")
                    .setContentText("Dès que vous êtes en situation de travail isolé …");

            Notification n = builder.build();

            nm.notify(9, n);


        }


    }

    public void createNotificationNotation() {

        int layoutNotation;
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            layoutNotation = R.layout.notation_compat;
        } else {
            layoutNotation = R.layout.notation;
        }

        Intent nonIntent = new Intent(context, NotationNonReceiver.class);
        PendingIntent nonPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                nonIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        Intent ouiIntent = new Intent(context, NotationOuiReceiver.class);
        PendingIntent ouiPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                ouiIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        RemoteViews contentView = new RemoteViews(context.getPackageName(), layoutNotation);
        String color = chargerTheme()[0];

        contentView.setTextViewText(R.id.content_button1, "Non");
        contentView.setTextColor(R.id.content_button1, Color.parseColor(color));
        contentView.setOnClickPendingIntent(R.id.content_button1, nonPendingIntent);

        contentView.setTextViewText(R.id.content_button2, "Oui");
        contentView.setTextColor(R.id.content_button2, Color.parseColor(color));
        contentView.setOnClickPendingIntent(R.id.content_button2, ouiPendingIntent);

        int drawableId = 0;
        if (android.os.Build.VERSION.SDK_INT > 23) {
            drawableId = R.drawable.surveillance_old;
        } else {
            drawableId = R.drawable.surveillance_old;
        }


        if (android.os.Build.VERSION.SDK_INT >= 23) {


            String notificationChannelId = "11113";
            String notficationChannelName = "AppliPTINotation";


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, notificationChannelId);

            mBuilder.setSmallIcon(drawableId); // CRASH APP

            mBuilder.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(contentView)
                    .setAutoCancel(true)

                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, notficationChannelName, NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                assert mNotificationManager != null;
                mBuilder.setChannelId(notificationChannelId);
                //     mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                mNotificationManager.createNotificationChannel(notificationChannel);

            }

            assert mNotificationManager != null;

            mNotificationManager.notify("NOTATION", 55, mBuilder.build());

        } else {

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getService(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Resources res = context.getResources();
            Notification.Builder builder = new Notification.Builder(context);

            builder.setContentIntent(contentIntent)
                    .setSmallIcon(drawableId)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.surveillance_old))

                    .setAutoCancel(true)

                    .setContent(contentView);

            Notification n = builder.build();

            nm.notify(007, n);

        }

    }


    public String[] getTypesAlarmeInterfaceAlternative() {

        alarmeAgressionManager.open();
        alarmeAMManager.open();
        alarmePVManager.open();
        optionsManager.open();

        List<String> typesAlarmeListe = new ArrayList<>();

        AlarmePV alarmePV = alarmePVManager.getAlarmePV();
        AlarmeAM alarmeAM = alarmeAMManager.getAlarmeAM();
        AlarmeAgression alarmeAgression = alarmeAgressionManager.getAlarmeAgression();
        Options options = optionsManager.getOptions();

        if (alarmeAM.isaAlarmeAm()) {
            int dureeDetection = alarmeAM.getAmDureeDetection();
            typesAlarmeListe.add("Position immobile" + " " + dureeDetection + "sec");
        }

        if (alarmePV.isaAlarmePv()) {
            int dureeDetection = alarmePV.getPvDureeDetection();
            typesAlarmeListe.add("Position allongée" + " " + dureeDetection + "sec");
        }

        if (alarmeAgression.isaAlarmeAgression()) {
            int dureeDetection = alarmeAgression.getAgressionDureeDetection();
            typesAlarmeListe.add("Homme mort" + " " + dureeDetection + "min");
        }

        if (options.isaIzsr()) {
            typesAlarmeListe.add("En zone sans réseau");
        }

        String[] stringTypesAlarmeListe = new String[typesAlarmeListe.size()];
        stringTypesAlarmeListe = typesAlarmeListe.toArray(stringTypesAlarmeListe);


        return stringTypesAlarmeListe;
    }


    // Récupérer la durée de la préalarme selon son type
    public int getDureePrealarme(String typeAlarme) {
        int dureeNotification = 0;

        switch (typeAlarme) {
            case "SOS": // volontaire
            case "SOS+ENVERS": // mise tête à l'envers
                alarmeSOSManager.open();
                dureeNotification = alarmeSOSManager.getAlarmeSOS().getSosDureeNotification();
                break;

            case "PV": // perte de verticalité
                alarmePVManager.open();
                dureeNotification = alarmePVManager.getAlarmePV().getPvDureeNotification();
                break;

            case "AM": // absence de mouvement
                alarmeAMManager.open();
                dureeNotification = alarmeAMManager.getAlarmeAM().getAmDureeNotification();
                break;

            case "IZSR": // intervention en zone sans réseau
                alarmeIZSRManager.open();
                dureeNotification = alarmeIZSRManager.getAlarmeIZSR().getIzsrDureeNotification();
                break;

            case "Agression": // homme mort
                alarmeAgressionManager.open();
                dureeNotification = alarmeAgressionManager.getAlarmeAgression().getAgressionDureeNotification();

                break;
        }

        return dureeNotification;



    }


    public String addZero(String heure) {

        String[] heureSeparation = heure.split(":");


        String heureFormat, minuteFormat = "";

        if (heureSeparation[0].length() == 1) {
            heureFormat = "0" + heureSeparation[0];
        } else {
            heureFormat = heureSeparation[0];
        }

        if (heureSeparation[1].length() == 1) {
            minuteFormat = "0" + heureSeparation[1];
        } else {
            minuteFormat = heureSeparation[1];
        }

        return heureFormat + ":" + minuteFormat;

    }

    public static String getCurrentHour() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
        return sdfHour.format(cal.getTime());
    }

    /**

     * @return true    true if the given hour is between
     */

    public static boolean isHourInInterval(String heureAppareil, String heureDebut, String heureFin) {


        LocalTime hAppareil = LocalTime.parse(heureAppareil);
        LocalTime hDebut = LocalTime.parse(heureDebut);
        LocalTime hFin = LocalTime.parse(heureFin);

        boolean mode;


        if(hDebut.isAfter(hFin)){

            mode = (hAppareil.isAfter(hDebut) || hAppareil.equals(hDebut)) || (hAppareil.isBefore(hFin) || hAppareil.equals(hFin));
            // Heure de fin est supérieur à Heure de début
        }else{
            mode = (hAppareil.isAfter(hDebut) || hAppareil.equals(hDebut)) && (hAppareil.isBefore(hFin) || hAppareil.equals(hFin));

        }

        return mode;


    }


}
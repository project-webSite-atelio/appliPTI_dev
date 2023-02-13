package com.ateliopti.lapplicationpti;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ateliopti.lapplicationpti.manager.EtatManager;
import com.ateliopti.lapplicationpti.manager.LicenceManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.ateliopti.lapplicationpti.manager.VersionManager;
import com.ateliopti.lapplicationpti.model.Configuration;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.protocol.HTTP;

public class AsyncConfiguration extends AsyncHttpClient {

    private static final String TEXTE_LICENCE_FLOTTANTE_MAX = "Activation de la surveillance PTI refusée car le nombre de surveillances PTI autorisées à s'activer simultanément est atteint";
    private static final String TEXTE_ECHEC_LICENCE = "Echec Licence";
    private static final String TEXTE_ECHEC_INTERNET = "Echec Internet";
    private static final String TEXTE_LICENCE_ACTIVE = "Licence activée";

    private static final MediaPlayer mp = new MediaPlayer();

    private final Context context;

    private LicenceManager licenceManager;
    private EtatManager etatManager;
    private OptionsManager optionsManager;
    private PostMethod pm;

    AsyncConfiguration(Context context) {
        this.context = context;
    }

    public void run() {

        licenceManager = new LicenceManager(context);
        etatManager = new EtatManager(context);
        optionsManager = new OptionsManager(context);

        final int timeoutSeconds = 20;

        System.out.println("ETAPE 3");
        final Fonctions fonctions = new Fonctions(context);
        String imei = fonctions.getIMEI();

        String adresse;
        if (context.getResources().getString(R.string.app_type).equals("prod")) {
            System.out.println("ETAPE 4");
            adresse = context.getResources().getString(R.string.prod_full_url);
        } else {
            System.out.println("ETAPE 5");
            adresse = context.getResources().getString(R.string.dev_full_url);
        }

        String url = adresse + "api/config.php?id=" + imei;

        System.out.println("ATELIO CONFIGRECUP " + url);


        AsyncHttpClient clientTLG = new AsyncHttpClient(true,80,443);
        clientTLG.addHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

        clientTLG.setTimeout(timeoutSeconds * 1000);
        clientTLG.setMaxRetriesAndTimeout(4, 1000);
        clientTLG.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response2) {
                String responseJson;
                try {
                    responseJson = response2 == null ? "" : new String(response2, getCharset());
                    System.out.println("responseJson " + responseJson);

                    // Echec : pas de licence
                    if (responseJson.equals("[null]")) {
                        Toast.makeText(context, TEXTE_ECHEC_LICENCE, Toast.LENGTH_SHORT).show();

                        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                        am.setStreamVolume(AudioManager.STREAM_ALARM, 100, 0);

                        Resources res = context.getResources();
                        AssetFileDescriptor afd = res.openRawResourceFd(R.raw.echec_licence);

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


                        mp.setOnCompletionListener(mp -> {
                            ((MainActivity) context).desactivationSurveillancePTI();
                            ((MainActivity) context).reactivationPtiSwitch();
                        });


                    } else {

                        Gson gson = new Gson();

                        licenceManager.open();

                        Configuration[] arr = gson.fromJson(responseJson, Configuration[].class);
                        Configuration recuperation = arr[0];
                        System.out.println(recuperation.toString());
                        CopieConfiguration cc = new CopieConfiguration(context);//, recuperation);

                        boolean autorisation = recuperation.isAutorisation();
                        optionsManager.open();
                        System.out.println("RECUPERATION DE CONFIG PHP (Autorisation) : " + autorisation);

                        try {
                            etatManager.open();
                            if (!etatManager.getEtat().getDerniereDesactivation().equals("")) {
                                if (optionsManager.getOptions().isHistoriqueSupervision()) {
                                    String adresse;
                                    if (context.getResources().getString(R.string.app_type).equals("prod")) {
                                        adresse = context.getResources().getString(R.string.prod_full_url);
                                    } else {
                                        adresse = context.getResources().getString(R.string.dev_full_url);
                                    }

                                    pm = new PostMethod("ping", "appli_pti", etatManager.getEtat().getDerniereDesactivation() + ";PTI Désactivation Ping", fonctions.getIMEI(), adresse + "getsms.php", context);
                                    pm.execute();
                                }
                                etatManager.updateDate("");
                            }
                        } catch (Exception ignored) {

                        }

                        ((MainActivity) context).checkRappelUtilisation();

                        if (autorisation) {
                            // Vérification si une licence existe
                            if (licenceManager.exists()) {
                                System.out.println("@ATELIO licence existante");
                                etatManager.open();

                                if (!etatManager.getEtat().isFinCharge()) {
                                    cc.execute(recuperation, 1);


                                }
                            } else {
                                System.out.println("@ATELIO licence activé");
                                cc.execute(recuperation, 0);

                                System.out.println(recuperation.toString());
                                // Configuration mis à jour
                                // Licence activé
                                ((MainActivity) context).theme();


                                Toast.makeText(context, TEXTE_LICENCE_ACTIVE, Toast.LENGTH_SHORT).show();

                                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                                am.setStreamVolume(AudioManager.STREAM_ALARM, 100, 0);

                                Resources res = context.getResources();
                                AssetFileDescriptor afd = res.openRawResourceFd(R.raw.licence_active);

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

                                mp.setOnCompletionListener(mp -> {
                                    ((MainActivity) context).licenceTitre();

                                    ((MainActivity) context).demarrerServiceAlways("AFFICHER");

                                     ((MainActivity) context).choisirSituation();

                                });
                            }
                        } else {

                            Toast.makeText(context, TEXTE_LICENCE_FLOTTANTE_MAX, Toast.LENGTH_LONG).show();
                            String adresse;

                            if (context.getResources().getString(R.string.app_type).equals("prod")) {
                                adresse = context.getResources().getString(R.string.prod_full_url);
                            } else {
                                adresse = context.getResources().getString(R.string.dev_full_url);
                            }

                            pm = new PostMethod("", "xpti_doubt", "Limite licence", fonctions.getIMEI(), adresse + "getsms.php", context);
                            pm.execute();

                            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                            am.setStreamVolume(AudioManager.STREAM_ALARM, 100, 0);

                            Resources res = context.getResources();
                            AssetFileDescriptor afd = res.openRawResourceFd(R.raw.nombre_surveillance_atteint);

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

                            // Désactivation de la surveillance
                            mp.setOnCompletionListener(mp -> {
                                ((MainActivity) context).desactivationSurveillancePTI();
                                ((MainActivity) context).reactivationPtiSwitch();
                            });


                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("Echec internet" + statusCode + "  " + error);
                echecInternet();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private void echecInternet() {

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Toast.makeText(context, TEXTE_ECHEC_INTERNET, Toast.LENGTH_SHORT).show();
        am.setStreamVolume(AudioManager.STREAM_ALARM, 100, 0);
        Resources res = context.getResources();
        int soundId;

        licenceManager.open();
        if (licenceManager.exists()) {
            soundId = res.getIdentifier("surveillance_pti_necessite_internet", "raw", context.getPackageName());
        } else {
            soundId = res.getIdentifier("echec_internet", "raw", context.getPackageName());
        }

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


        mp.setOnCompletionListener(mp -> {
            ((MainActivity) context).desactivationSurveillancePTI();
            ((MainActivity) context).reactivationPtiSwitch();
        });



    }

    //
    void defineVersion(Context context) {
        int appVersion;
        appVersion = Integer.parseInt(context.getResources().getString(R.string.app_version));
        VersionManager versionManager = new VersionManager(context);
        versionManager.open();
        versionManager.setVersion(appVersion);
    }

}

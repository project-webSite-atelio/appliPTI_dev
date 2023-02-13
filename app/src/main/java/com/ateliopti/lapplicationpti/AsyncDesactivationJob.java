package com.ateliopti.lapplicationpti;

import android.content.Context;

import com.ateliopti.lapplicationpti.manager.EtatManager;
import com.ateliopti.lapplicationpti.manager.LicenceManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.ateliopti.lapplicationpti.model.Configuration;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class AsyncDesactivationJob extends AsyncHttpClient {

    Fonctions fonctions;
    Context context;

    private LicenceManager licenceManager;
    private EtatManager etatManager;
    private OptionsManager optionsManager;
    PostMethod pm;

    public AsyncDesactivationJob(Context context) {
        this.context = context;
    }


    public void run() {



        licenceManager = new LicenceManager(context);

        etatManager = new EtatManager(context);
        optionsManager = new OptionsManager(context);
        fonctions = new Fonctions(context);

        int timeoutSeconds = 5;

        AsyncHttpClient client = new AsyncHttpClient(true,80,443);
        client.setTimeout(timeoutSeconds * 1000);


        client.get("https://www.google.fr", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                try {


                    final Fonctions fonctions = new Fonctions(context);
                    String imei = fonctions.getIMEI();

                    String adresse;
                    if (context.getResources().getString(R.string.app_type).equals("prod")) {
                        adresse = context.getResources().getString(R.string.prod_full_url);
                    } else {
                        adresse = context.getResources().getString(R.string.dev_full_url);
                    }

                    String url = adresse + "api/config.php?id=" + imei;

                    System.out.println("ATELIO CONFIGRECUP " + url);


                    AsyncHttpClient clientTLG = new AsyncHttpClient(true,80,443);
                    clientTLG.get(url, new AsyncHttpResponseHandler() {

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] response2) {
                            String responseJson;
                            try {
                                responseJson = response2 == null ? "" : new String(response2, getCharset());

                                if (!responseJson.equals("[null]"))  {

                                    Gson gson = new Gson();

                                    licenceManager.open();

                                    Configuration[] arr = gson.fromJson(responseJson, Configuration[].class);
                                    Configuration recuperation = arr[0];
                                    System.out.println(recuperation.toString());

                                    boolean autorisation = recuperation.isAutorisation();

                                    optionsManager.open();
                                    System.out.println("RECUPERATION DE CONFIG PHP (Autorisation) : " + autorisation);


                                    try {
                                        etatManager.open();
                                        String derniereDesactivation = etatManager.getEtat().getDerniereDesactivation();
                                        if (!derniereDesactivation.equals("")) {


                                            if (optionsManager.getOptions().isHistoriqueSupervision()) {

                                                String adresse;

                                                if (context.getResources().getString(R.string.app_type).equals("prod")) {
                                                    adresse = context.getResources().getString(R.string.prod_full_url);
                                                } else {
                                                    adresse = context.getResources().getString(R.string.dev_full_url);
                                                }

                                                if(derniereDesactivation.contains(",")){
                                                    String[] elements = derniereDesactivation.split(",");

                                                    String dateDerniereDesactivation = elements[0];
                                                    String motifExtinction = elements[1];

                                                    switch (motifExtinction){
                                                        case "shutdown":
                                                            pm = new PostMethod("ping",
                                                                    "appli_pti",
                                                                    dateDerniereDesactivation + ";PTI Désactivation Shutdown", fonctions.getIMEI(),
                                                                    adresse + "getsms.php",
                                                                    context);
                                                            break;

                                                    }


                                                }else{
                                                    pm = new PostMethod("ping",
                                                            "appli_pti",
                                                            etatManager.getEtat().getDerniereDesactivation() + ";PTI Désactivation Ping", fonctions.getIMEI(),
                                                            adresse + "getsms.php",
                                                            context);

                                                }


                                               pm.execute();
                                            }


                                            etatManager.updateDate("");
                                        }
                                    } catch (Exception ignored) {

                                    }


                                    // Vérification si une licence existe
                                    if (licenceManager.exists()) {
                                        System.out.println("@ATELIO licence existante");
                                        etatManager.open();

                                    } else {
                                        System.out.println("@ATELIO licence activé");



                                        System.out.println(recuperation.toString());

                                    }

                                }

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                            System.out.println("Echec internet" + statusCode);
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

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                System.out.println("Echec internet" + statusCode);
                System.out.println("Echec internet" + e);


            }


            @Override
            public void onFinish() {
                super.onFinish();


            }

            @Override
            public void onRetry(int retryNo) {

            }
        });


    }


}

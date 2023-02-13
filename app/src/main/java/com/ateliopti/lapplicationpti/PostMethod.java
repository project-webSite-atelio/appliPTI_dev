package com.ateliopti.lapplicationpti;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.widget.Toast;

import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.ateliopti.lapplicationpti.manager.TempLocalisationManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;

public class PostMethod extends AsyncHttpClient {

    final private Context context;

    private String special;
    private String messageId; // xpti_doubt (pas visible sur l'historique)
    private String message;
    private String imei;
    private String site;

    private PostMethod pm;

    PostMethod(Context context) {
        this.context = context;
    }

    PostMethod(String special, String messageId, String message, String imei, String site, Context context) {
        this.special = special;
        this.messageId = messageId;
        this.message = message;
        this.imei = imei;
        this.site = site;
        this.context = context;

    }

    void execute() {


        final int timeoutSeconds = 20;

        System.out.println("ETAPE 3");
        final Fonctions fonctions = new Fonctions(context);


        String adresse;
        if (context.getResources().getString(R.string.app_type).equals("prod")) {
            System.out.println("ETAPE 4");
            adresse = context.getResources().getString(R.string.prod_full_url);
        } else {
            System.out.println("ETAPE 5");
            adresse = context.getResources().getString(R.string.dev_full_url);
        }

        String imei;
        String url;

        switch (special) {
            case "google_essai":
            case "bug":
            case "licence":
                url = adresse + "signaler_bug.php";
                imei = getImei();
                break;

            default:
                url = adresse + "getsms.php";
                imei = fonctions.getIMEI();
                break;
        }


        System.out.println("ATELIO CONFIGRECUP " + url);


        AsyncHttpClient clientTLG = new AsyncHttpClient(true,80,443);
        clientTLG.addHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");


        if (special.equals("bug") || special.equals("licence")) {
            clientTLG.setTimeout(10 * 1000);
            clientTLG.setMaxRetriesAndTimeout(1, 1000);
        } else {
            clientTLG.setTimeout(timeoutSeconds * 1000);
            clientTLG.setMaxRetriesAndTimeout(4, 1000);
        }

        final String xml;
        if (special.equals("ping")) { // Sans la date


            xml = String.format("<InboundMessage>" +
                            "<MessageId>%s</MessageId>" +
                            "<MessageText>%s;</MessageText>" +
                            "<From>%s</From>" +
                            "</InboundMessage>",
                    messageId, message, imei);


        } else { // Date générée
            // Date alarme
            String date;
            String modeJourNuit = "";

            if(special.equals("alarme")){
                TempLocalisationManager tempLocalisationManager = new TempLocalisationManager(context);
                tempLocalisationManager.open();

                OptionsManager optionsManager = new OptionsManager(context);

                optionsManager.open();

                if(optionsManager.getOptions().isaScenarioJourNuit()){
                    message += ";" + "Mode Jour Nuit";
                }


                if(!tempLocalisationManager.getTempLocalisation().getDate().isEmpty()){
                    date = tempLocalisationManager.getTempLocalisation().getDate();
                }else{
                    date = getDate();
                }
            }else{
                date = getDate();
            }

            xml = "<InboundMessage>" +
                    "  <MessageId>" + messageId + "</MessageId>" +
                    "  <MessageText>" + date + ";" + message + ";" + "</MessageText>" +
                    "  <From>" + imei + "</From>" +
                    "</InboundMessage>";
        }


        System.out.println("ETAPE 2" + xml);

        RequestParams params = new RequestParams();

        StringEntity entity = null;
        entity = new StringEntity(xml, HTTP.UTF_8);



        clientTLG.post(context, url, entity, "application/xml",
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String res = responseBody.toString();
                        //System.out.println("TESTA = " + res);
                        if (special.equals("bug") || special.equals("licence")) {
                            String messageToast = "Merci, nous revenons vers vous le plus rapidement possible …";
                            Toast.makeText(context, messageToast, Toast.LENGTH_LONG).show();
                        }
                        Log.e("response ", res);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        //  Probleme réseau ? Message non envoyé
                        if (special.equals("bug") || special.equals("licence")) {
                            String messageToast = "Merci, mais la déclaration n'a pas été réceptionnée, vérifier la connexion Internet …";
                            Toast.makeText(context, messageToast, Toast.LENGTH_LONG).show();

                        }

                    }
                });



    }

    public String getImei() {
        return imei;
    }

    // Date actuelle avec formattage
    @SuppressLint("SimpleDateFormat")
    public String getDate() {
        String date;
        date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        return date;
    }

}



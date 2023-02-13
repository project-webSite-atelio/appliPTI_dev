package com.ateliopti.lapplicationpti;


import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.protocol.HTTP;

public class AsyncPing extends AsyncHttpClient {

    Context context;
    private boolean success = false;
    private String result = "";

    public AsyncPing(Context context) {
        this.context = context;
    }


    public void run() {

        try {
            success = false;

            int timeoutSeconds = 5;

            AsyncHttpClient client = new AsyncHttpClient(true,80,443);


            client.addHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");


            client.setTimeout(timeoutSeconds - 2 * 1000);
            client.setResponseTimeout(2 * 1000);
            client.setMaxRetriesAndTimeout(1, 5000);

            Fonctions fonctions = new Fonctions(context);
            String imei = fonctions.getIMEI();

            // ((MainActivity) context).getResources().getString(R.string.server_url) + ((MainActivity) context).getResources().getString(R.string.server_port) +


            String adresse;

            if (context.getResources().getString(R.string.app_type).equals("prod")) {
                adresse = context.getResources().getString(R.string.prod_full_url);
            } else {
                adresse = context.getResources().getString(R.string.dev_full_url);
            }

            String url = adresse + "api/config.php?id=" + imei;

            client.get(url, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                    success = true;

                    setResult("Succes");


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                    success = false;

                    setResult("Echec");
                }


                @Override
                public void onFinish() {
                    super.onFinish();

                    if (!success) {


                        setResult("Echec");
                    }

                    System.out.println("PINGJSON" + getResult());
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });
        }catch (Exception ignored){

        }


    }

    public String getResult() {
        return result;
    }

    private void setResult(String result) {
        this.result = result;
    }
}

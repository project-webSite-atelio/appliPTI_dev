package com.ateliopti.lapplicationpti;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;

import com.ateliopti.lapplicationpti.manager.OptionsManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

// Upload d'un fichier

public class AsyncUpload extends AsyncTask<String, String, String> {
    private final Context context;
    private String typeAlarme;
    private boolean estIZSR;

    private String destinataireScenarioExceptionnel;
    private String detailScenarioExceptionnel;

    private int dureeIZSR;
    private int dureeIZSRPrealarme;

    private OptionsManager optionsManager;


    private boolean estScenarioExceptionnel = false;


    private AudioManager am;
    private MediaPlayer mMediaPlayer;
    private int serverResponseCode;
    private String upLoadServerUri;

    // Récuperation du String
    public AsyncUpload(Context context, String typeAlarme, boolean estIZSR) {
        this.context = context;
        this.typeAlarme = typeAlarme;
        this.estIZSR = estIZSR;
        estScenarioExceptionnel = false;

    }

    // Scénario exceptionnel
    public AsyncUpload(Context context, String typeAlarme, String destinataireScenarioExceptionnel, String detailScenarioExceptionnel, int dureeIZSR, int dureeIZSRPrealarme, boolean estIZSR) {
        this.context = context;
        this.typeAlarme = typeAlarme;
        this.destinataireScenarioExceptionnel = destinataireScenarioExceptionnel;
        this.detailScenarioExceptionnel = detailScenarioExceptionnel;
        this.dureeIZSR = dureeIZSR;
        this.dureeIZSRPrealarme = dureeIZSRPrealarme;
        this.estIZSR = estIZSR;
        estScenarioExceptionnel = true;
    }

    // Scénario exceptionnel
    public AsyncUpload(Context context, String typeAlarme, String destinataireScenarioExceptionnel, String detailScenarioExceptionnel, boolean estIZSR) {
        this.context = context;
        this.typeAlarme = typeAlarme;
        this.destinataireScenarioExceptionnel = destinataireScenarioExceptionnel;
        this.detailScenarioExceptionnel = detailScenarioExceptionnel;
        this.estIZSR = estIZSR;
        estScenarioExceptionnel = true;
    }

    public AsyncUpload(Context context, String typeAlarme, int dureeIZSR, int dureeIZSRPrealarme, boolean estIZSR) {
        this.context = context;
        this.typeAlarme = typeAlarme;
        this.dureeIZSR = dureeIZSR;
        this.dureeIZSRPrealarme = dureeIZSRPrealarme;
        this.estIZSR = estIZSR;
        estScenarioExceptionnel = false;
    }


    @Override
    protected void onPreExecute() {
        // write show progress Dialog code here
        super.onPreExecute();

        /************* Php script path ****************/

//        String adresse = ((MainActivity) context).getResources().getString(R.string.server_url) + ((MainActivity) context).getResources().getString(R.string.server_port);

        String adresse = "";

        if (context.getResources().getString(R.string.app_type).equals("prod")) {
            adresse = context.getResources().getString(R.string.prod_full_url);
        } else {
            adresse = context.getResources().getString(R.string.dev_full_url);
        }


        if (isEstIZSR()) {
            System.out.println("upLoadServerUri status IZSR = " + isEstIZSR());
            upLoadServerUri = adresse + "upload_rec_izsr.php?type=mp4";
        } else {
            System.out.println("upLoadServerUri status NONE = " + isEstIZSR());
            upLoadServerUri = adresse + "upload_rec.php?type=mp4";
        }

        // System.out.println("SITE = " + upLoadServerUri);
    }


    @Override
    protected String doInBackground(String... params) {
        // File root = android.os.Environment.getExternalStorageDirectory();


        File file = null;
        String sourceFileUri = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            File root = new File(context.getExternalFilesDir(null), "rec");
            if (!root.exists()) {
                root.mkdirs();
            }


            file = new File(root, "rec.mp4");
            file.setReadable(true);
            file.setWritable(true);


        } else {


            File root = android.os.Environment.getExternalStorageDirectory();
             sourceFileUri = root.getAbsolutePath() + "/AppliPTI/rec.mp4";

        }


        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;

        String urlServer = upLoadServerUri;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead = 0, bytesAvailable = 0, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;

        FileInputStream fileInputStream = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                fileInputStream = new FileInputStream(file);
            } else {
                fileInputStream = new FileInputStream(new File(sourceFileUri));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        URL url = null;
        try {
            url = new URL(urlServer);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type",
                "multipart/form-data;boundary=" + boundary);

        try {
            outputStream = new DataOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        optionsManager = new OptionsManager(context);

        try {

            Fonctions fonctions = new Fonctions(context);
            if (estScenarioExceptionnel) {

                String output = "Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fonctions.getIMEI()
                        + "|" + getTypeAlarme();



                output += "|" + fonctions.getDateRecord();

                if (isEstIZSR()) {
                    output += "%" + this.dureeIZSR;
                    output += "%" + this.dureeIZSRPrealarme;
                }




                output += "|" + this.destinataireScenarioExceptionnel;
                output += "%" + this.detailScenarioExceptionnel;


                output += "|\"" + lineEnd;


                outputStream.writeBytes(output);

                System.out.println(output);

            } else {

                String output = "Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fonctions.getIMEI()
                        + "|" + getTypeAlarme();

                optionsManager.open();

                // Vérification si le scénario jour/nuit est activé

                if(optionsManager.getOptions().isaScenarioJourNuit() && !getTypeAlarme().equals("Debut IZSR")){
                    output += "%JourNuit";

                }

                output += "|" + fonctions.getDateRecord();
                if (isEstIZSR()) {
                    output += "%" + this.dureeIZSR;
                    output += "%" + this.dureeIZSRPrealarme;
                }


                output += "\"" + lineEnd;

                outputStream.writeBytes(output);
                System.out.println(output);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.writeBytes(lineEnd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // crash montre
            bytesAvailable = fileInputStream.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        try {
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                    + lineEnd);

            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();


        } catch (Exception ignored) {

        }

        return "Executed";
    }


    @Override
    protected void onPostExecute(String result) {

    }

    public String getTypeAlarme() {
        return typeAlarme;
    }

    public void setTypeAlarme(String typeAlarme) {
        this.typeAlarme = typeAlarme;
    }

    public boolean isEstIZSR() {
        return estIZSR;
    }

    public void setEstIZSR(boolean estIZSR) {
        this.estIZSR = estIZSR;
    }

    public int getDureeIZSR() {
        return dureeIZSR;
    }

    public void setDureeIZSR(int dureeIZSR) {
        this.dureeIZSR = dureeIZSR;
    }

    public int getDureeIZSRPrealarme() {
        return dureeIZSRPrealarme;
    }

    public void setDureeIZSRPrealarme(int dureeIZSRPrealarme) {
        this.dureeIZSRPrealarme = dureeIZSRPrealarme;
    }

}
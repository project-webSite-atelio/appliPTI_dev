package com.ateliopti.lapplicationpti.popup;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

public class AutorisationPopUp extends AlertDialog {


    Context context;

    public AutorisationPopUp(Context context) {
        super(context);
        this.context = context;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Cette application peut collecter des données de localisation même lorsque l'application " +
                    "est fermée ou non utilisée, dans le but de communiquer votre position précise à un tiers en cas de danger.")
                    .setCancelable(false)
                    .setTitle("Vie privée …")
                    .setPositiveButton("Ok, j'ai compris", (dialog, id) ->
                            ActivityCompat.requestPermissions((Activity) context, new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            22));

            AlertDialog alert = builder.create();
            alert.show();

        } else {
            this.autrePermissions();

        }
    }


    public void autrePermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    (Activity) context, new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    23);
        }

    }

}

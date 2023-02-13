package com.ateliopti.lapplicationpti.popup;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.view.View;
import android.widget.Button;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ateliopti.lapplicationpti.Fonctions;
import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.manager.OptionsManager;

public class LocalisationSonorePopUp {

    private Context context;

    private boolean estSonore;

    private String typeAlarme;

    private AlertDialog builderLocalisationSonore;

    private final OptionsManager optionsManager;

    private Intent annulationParMouvementService;

    private final Fonctions fonctions;

    public LocalisationSonorePopUp(Context context, boolean estSonore) {
        this.context = context;
        this.estSonore = estSonore;
        fonctions = new Fonctions(context);

        optionsManager = new OptionsManager(context);

        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ping") || intent.getAction().equals("fin_pti")) {
                    dismiss();
                }
            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("ping"));
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("fin_pti"));
    }


    public void show() {

        ((MainActivity) context).stopGpsService();

        ((MainActivity) context).retourAuPremierPlan();


        try {
            builderLocalisationSonore.dismiss();
        } catch (Exception ignored) {

        }


        //  ((MainActivity) context).stopGpsService();

        fonctions.vibrer(false);
        String messageBuilder;
        String buttonBuilder;

        messageBuilder = "L'alarme vient d'être communiquée.";


        //  (si IZSR)

        // Si aucun type d'alarme par défaut : message = "redémarrer pti"
        if (getTypeAlarme() == null) {
            this.setTypeAlarme("");
        }

        if (getTypeAlarme().equals("En zone sans réseau")) {
            buttonBuilder = "Arrêter surveillance PTI";
        } else {
            buttonBuilder = "OK, j'ai compris";
        }

        optionsManager.open();
        if (optionsManager.getOptions().isLocalisationSonore()) {

            if (isEstSonore()) {
                messageBuilder += "\nLocalisation sonore toutes les 30 secondes …";

                buttonBuilder += "\nInterrompre localisation sonore";
            }
        }

        builderLocalisationSonore = new AlertDialog.Builder(context)
                .setMessage(messageBuilder)
                .setCancelable(false)
                .setPositiveButton(buttonBuilder, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                        ((MainActivity) context).serviceLocalisationSonore(false);


                        if (getTypeAlarme().equals("En zone sans réseau")) {

                            ((MainActivity) context).finIzsr();
                        }

                    }
                })
                .create();


        builderLocalisationSonore.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {

                final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                defaultButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        try {
                            builderLocalisationSonore.dismiss();
                        } catch (Exception ignored) {

                        }

                        ((MainActivity) context).serviceLocalisationSonore(false);

                        if (getTypeAlarme() != null) { // TODO : ttempt to invoke virtual method 'boolean java.lang.String.equals(java.lang.Object)' on a null object reference

                            ((MainActivity) context).finIzsr();
                        } else {

                            ((MainActivity) context).startPingService();
                            ((MainActivity) context).redemarrageServices(false);
                        }

                    }

                });
            }
        });
        try {
            builderLocalisationSonore.show();

        } catch (Exception ignored) {

        }
    }

    private void dismiss() {

        ((MainActivity) context).serviceLocalisationSonore(false);

        try {
            builderLocalisationSonore.dismiss();
        } catch (Exception ignored) {

        }

        try {
            context.stopService(annulationParMouvementService);

            fonctions.jouerSonAlarme(false);
            fonctions.vibrer(false);


        } catch (Exception ignored) {

        }
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private String getTypeAlarme() {
        return typeAlarme;
    }

    public void setTypeAlarme(String typeAlarme) {
        this.typeAlarme = typeAlarme;
    }

    public boolean isEstSonore() {
        return estSonore;
    }

    public void setEstSonore(boolean estSonore) {
        this.estSonore = estSonore;
    }
}

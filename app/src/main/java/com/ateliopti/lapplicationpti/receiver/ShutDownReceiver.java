package com.ateliopti.lapplicationpti.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ateliopti.lapplicationpti.Fonctions;
import com.ateliopti.lapplicationpti.manager.EtatManager;

public class ShutDownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        EtatManager etatManager;
        Fonctions fonctions;

        if (Intent.ACTION_SHUTDOWN.equalsIgnoreCase(intent.getAction())) {

            fonctions = new Fonctions(context);
            etatManager = new EtatManager(context);
            etatManager.open();
            if (etatManager.getEtat().isActivation()) {
                etatManager.open();
                etatManager.updateEtat(false); // Désactivation PTI
                etatManager.updateDate(fonctions.demandeDate() + "," + "shutdown"); // Date d'extinction, avec séparateur "," et type de fermeture
            }

        }

        if (Intent.ACTION_BOOT_COMPLETED.equalsIgnoreCase(intent.getAction())) {
            // some operation
        }

    }
}
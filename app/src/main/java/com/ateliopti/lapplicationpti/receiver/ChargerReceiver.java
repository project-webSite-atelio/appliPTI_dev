package com.ateliopti.lapplicationpti.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.helper.AppHelper;
import com.ateliopti.lapplicationpti.manager.EtatManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;

public class ChargerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        String action = intent.getAction();

        // Si l'appareil est branché
        if (action != null) {
            if (action.matches(Intent.ACTION_POWER_CONNECTED)) {


                try {

                    EtatManager etatManager = new EtatManager(context);
                    etatManager.open();

                    if (!AppHelper.isAppRunning(context, "com.ateliopti.lapplicationpti.MainActivity") && etatManager.getEtat().isActivation()) {

                        Intent i = new Intent(context, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("methodName", "myMethode");
                        context.startActivity(i);
                    }
                }catch (Exception ignored){

                }

                // Activation Automatique PTI

                // Si l'appareil est débranché
            } else if (action.matches(Intent.ACTION_POWER_DISCONNECTED)) {


                try {

                    OptionsManager optionsManager = new OptionsManager(context);
                    optionsManager.open();

                    boolean activationAutomatique = false;

                    try {
                        System.out.println("@atelio " + optionsManager.getOptions().isActivationPTIAutomatique());
                        activationAutomatique = optionsManager.getOptions().isActivationPTIAutomatique();
                    } catch (Exception ignored) {
                        activationAutomatique = false;
                    }

                    if (activationAutomatique) {
                        //    ((MainActivity) context).actionDisconnected();


                        if (AppHelper.isAppRunning(context, "com.ateliopti.lapplicationpti.MainActivity")) {

                            Intent i = new Intent(context, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("methodName", "myMethod");

                            context.startActivity(i);

                        } else {
                            // App is not running

                            Intent i = new Intent(context, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("methodName", "myMethod");

                            context.startActivity(i);


                        }

                    }
                }catch (Exception ignored){

                }


            }
        }

    }

}

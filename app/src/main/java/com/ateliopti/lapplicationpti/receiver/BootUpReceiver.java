package com.ateliopti.lapplicationpti.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.helper.AppHelper;
import com.ateliopti.lapplicationpti.manager.EtatManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.ateliopti.lapplicationpti.service.AlwaysOnService;

public class BootUpReceiver extends BroadcastReceiver {
    OptionsManager optionsManager;
    EtatManager etatManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(final Context context, Intent intent) {


        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            optionsManager = new OptionsManager(context);
            etatManager = new EtatManager(context);
            optionsManager.open();
            etatManager.open();

            boolean activationAutomatique = false;
            boolean sollicitation = false;

            try {
                // System.out.println("@atelio " + optionsManager.getOptions().isActivationPTIAutomatique());
                activationAutomatique = optionsManager.getOptions().isActivationPTIAutomatique();
            } catch (Exception ignored) {

            }

            try {
                // System.out.println("@atelio " + optionsManager.getOptions().isSollicitation());
                sollicitation = optionsManager.getOptions().isSollicitation();
            } catch (Exception ignored) {
            }

            try {

                if (activationAutomatique || sollicitation || !etatManager.getEtat().getDerniereDesactivation().equals("")) {


                    if (!etatManager.getEtat().getDerniereDesactivation().equals("")) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                Intent intentAlways = new Intent(context, AlwaysOnService.class);


                                intentAlways.setAction("AFFICHER");

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                                    ContextCompat.startForegroundService(context, intentAlways);


                                    // startForegroundService(intentAlways);
                                } else {
                                    context.startService(intentAlways);

                                }


                            }
                        }, 1100);

                    }


                    if (activationAutomatique || sollicitation) {


                        Handler handler = new Handler();
                        final boolean finalActivationAutomatique = activationAutomatique;
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                if (AppHelper.isAppRunning(context, "com.ateliopti.lapplicationpti.MainActivity")) {
                                    Intent i = new Intent(context, MainActivity.class);

                                    if (finalActivationAutomatique) {
                                        i.putExtra("methodName", "myMethod"); // permet de simuler un appui d'interrupteur
                                    }
                                    context.startActivity(i);
                                } else {
                                    // App is not running
                                    Intent i = new Intent(context, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    if (finalActivationAutomatique) {
                                        i.putExtra("methodName", "myMethod");// permet de simuler un appui d'interrupteur
                                    }
                                    context.startActivity(i);

                                }
                            }
                        }, 1000);
                    }
                }


            } catch (Exception ignored) {

            }
        }
    }

}

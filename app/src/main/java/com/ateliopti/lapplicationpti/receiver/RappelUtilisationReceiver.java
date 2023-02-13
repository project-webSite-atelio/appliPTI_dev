package com.ateliopti.lapplicationpti.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ateliopti.lapplicationpti.Fonctions;
import com.ateliopti.lapplicationpti.manager.EtatManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;

import java.util.Calendar;

import static android.content.Intent.FLAG_INCLUDE_STOPPED_PACKAGES;

public class RappelUtilisationReceiver extends BroadcastReceiver {
    private final String REMINDER_BUNDLE = "MyReminderBundle";


    public static final String INTENT_RAPPEL_UTILISATION = "android.intent.action.RAPPEL_UTILISATION_RECEIVER";
    private static final String INTENT_RAPPEL_NOTIFICATION = "android.intent.action.RAPPEL_NOTIFICATION";

    // this constructor is called by the alarm manager.
    public RappelUtilisationReceiver() {
    }

    // you can use this constructor to create the alarm.
    //  Just pass in the main activity as the context,
    //  any extras you'd like to get later when triggered
    //  and the timeout
    public RappelUtilisationReceiver(Context context, Bundle extras, int timeoutInSeconds) {
        AlarmManager alarmMgr =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RappelUtilisationReceiver.class);
        intent.putExtra(REMINDER_BUNDLE, extras);
        intent.addFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent,
                        PendingIntent.FLAG_IMMUTABLE);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.SECOND, timeoutInSeconds);

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(INTENT_RAPPEL_UTILISATION)) {
                //   System.out.println("RAPPEL_UTILISATION_RECEIVER covered");

                AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent myIntent = new Intent(context, RappelUtilisationReceiver.class);
                myIntent.setAction("android.intent.action.RAPPEL_NOTIFICATION");

                myIntent.addFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
                Calendar time = Calendar.getInstance();
                time.setTimeInMillis(System.currentTimeMillis());
                //   time.add(Calendar.SECOND, 5);
                OptionsManager optionsManager = new OptionsManager(context);
                optionsManager.open();

                try {
                    int heure = optionsManager.getOptions().getDureeRappelUtilisation();

                    ///     System.out.println("RAPPEL_UTILISATION_RECEIVER " + heure + " heures");


                    if (heure > 0 && optionsManager.getOptions().isaRappelUtilisation()) {
                        if (alarmMgr != null) {
                            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis() + 1000 * heure * 60 * 60,
                                    1000 * heure * 60 * 60, pendingIntent);
                        }
                    }
                } catch (Exception ignored) {

                }


            } else if (intent.getAction().equals(INTENT_RAPPEL_NOTIFICATION)) {

                Fonctions fonctions = new Fonctions(context);

                String test = "";
                try {
                    EtatManager etatManager = new EtatManager(context);

                    etatManager.open();

                    test = etatManager.getEtat().isActivation() ? "allumé" : "éteint";
                } catch (Exception ignored) {
                    test = "éteint";

                }

                if (test.equals("éteint")) {
                    fonctions.createNotification(intent, test);
                }


            }
        }

    }
}
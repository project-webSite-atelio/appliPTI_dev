package com.ateliopti.lapplicationpti.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateUtils;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ateliopti.lapplicationpti.Fonctions;
import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.manager.OptionsManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class TimeChangedReceiver extends BroadcastReceiver {

    OptionsManager optionsManager;

    Fonctions fonctions;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        optionsManager = new OptionsManager(context);

        fonctions = new Fonctions(context);

        optionsManager.open();

        if (optionsManager.exists()) {

            optionsManager.open();


            if(optionsManager.getOptions().getHeureDebutScenarioJourNuit() == null)
                return;

            if(optionsManager.getOptions().getHeureFinScenarioJourNuit() == null)
                return;


            String heureDebut = fonctions.addZero(optionsManager.getOptions().getHeureDebutScenarioJourNuit());
            String heureFin = fonctions.addZero(optionsManager.getOptions().getHeureFinScenarioJourNuit());


            // Heure actuelle
            String heureNow = fonctions.addZero(fonctions.getCurrentHour());



            // Mode jour selon l'heure de l'appareil
            if (Fonctions.isHourInInterval(fonctions.addZero(heureNow), heureDebut, heureFin)) {
                Intent intentSend = new Intent("MODE_JOUR");
                LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(context);
                mgr.sendBroadcast(intentSend);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intentSend);

            } else {
                // Mode nuit selon l'heure de l'appareil
                Intent intentSend = new Intent("MODE_NUIT");
                LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(context);
                mgr.sendBroadcast(intentSend);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intentSend);
            }




        }


    }


}
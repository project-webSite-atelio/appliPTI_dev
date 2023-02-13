package com.ateliopti.lapplicationpti.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import com.ateliopti.lapplicationpti.manager.EcranManager;
import com.ateliopti.lapplicationpti.model.Ecran;

/**
 * Created by Pascal.Nguyen on 06/11/2017.
 */

public class PowerReceiver extends BroadcastReceiver {
    private static final int countPowerOff = 0;

    private static Context cntx;

    private final int milliseconds = 0;

    private static long a;
    private static long seconds_screenon;
    private static long seconds_screenoff;

    private static long actual_diff;

    private static long diffrence;
    private static long OLD_TIME;
    private static boolean OFF_SCREEN;
    private static boolean ON_SCREEN;

    private static boolean sent_msg;

    private boolean etape1 = false;
    private boolean etape2 = false;
    private EcranManager ecranManager;

    private Handler handlerAnnule;

    private boolean actif = true;


    @Override
    public void onReceive(final Context context, final Intent intent) {


        String action = intent.getAction();

        if (action != null) {

            ecranManager = new EcranManager(context);
            cntx = context;


            // Si l'écran est éteint
            if (action.matches(Intent.ACTION_SCREEN_OFF)) {

                System.out.println("@atelio écran éteint");


                ecranManager.open();

                //        System.out.println(ecranManager.getEcran().toString());


                if (etape1) {
                    etape2 = true;
                }


                // Temps actuel
                a = System.currentTimeMillis();

                // écran éteint : temps actuel
                seconds_screenoff = a;
                // ancien temps = temps actuel
                OLD_TIME = seconds_screenoff;

                // écran éteint est vrai
                OFF_SCREEN = true;

                System.out.println("@atelio différence = " + actual_diff);


                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {

                        //    System.out.println("ON_SCREEN" + actual_diff);
                        if (ON_SCREEN) {
                            if (seconds_screenon != 0 && seconds_screenoff != 0) {

                                actual_diff = cal_diff(seconds_screenon, seconds_screenoff);


                                if (actual_diff <= 1000) {
                                    sent_msg = true;

                                    if (sent_msg) {

                                        seconds_screenon = 0L;
                                        seconds_screenoff = 0L;
                                        sent_msg = false;


                                    }
                                } else {


                                    seconds_screenon = 0L;
                                    seconds_screenoff = 0L;

                                }
                            }
                        }
                    }

                    public void onFinish() {

                        ecranManager.open();
                        if (!ecranManager.exists()) {

                            ecranManager.open();
                            ecranManager.inserer(true);
                        }

                        ecranManager.open();
                        if (!ecranManager.getEcran().isEstActif()) {
                            ecranManager.open();
                            Ecran ok = new Ecran(0, true);
                            ecranManager.updateEcran(ok);
                        }


                        seconds_screenoff = 0L;
                    }
                }.start();

            } else if (action.matches(Intent.ACTION_SCREEN_ON)) {

/*
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                }
            };
            */

                try {
                    if (handlerAnnule != null) {
                        handlerAnnule.removeCallbacksAndMessages(null);
                    }
                    //       System.out.println("@atelio runnable canceled");
                } catch (Exception ignored) {

                }


                handlerAnnule = new Handler();
                Runnable runnable = new Runnable() {
                    public void run() {
                        System.out.println("@atelio runnable reset");
                        if (etape1) {

                            handlerAnnule = null;
                            etape1 = false;
                            etape2 = false;
                        }

                        System.out.println("APPPPPPPPPELLLLL");
                        //afficher();
                    }
                };

                handlerAnnule.postDelayed(runnable, 3500);


                System.out.println("@atelio écran allumé");


                System.out.println("@atelio différence = " + actual_diff);


                ecranManager.open();


//            System.out.println(ecranManager.getEcran().toString());


                etape1 = true;

                a = System.currentTimeMillis();
                seconds_screenon = a;
                OLD_TIME = seconds_screenoff;

                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //      System.out.println("OFF_SCREEN" + actual_diff);
                        if (OFF_SCREEN) {
                            if (seconds_screenon != 0 && seconds_screenoff != 0) {
                                actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                                if (actual_diff <= 1000) {
                                    sent_msg = true;
                                    if (sent_msg) {

                                        seconds_screenon = 0L;
                                        seconds_screenoff = 0L;
                                        sent_msg = false;
                                    }
                                } else {
                                    seconds_screenon = 0L;
                                    seconds_screenoff = 0L;
                                }
                            }
                        }
                    }

                    public void onFinish() {
                        seconds_screenon = 0L;
                    }
                }.start();
            }


            ecranManager.open();


            // Vérification si une config existe, sinon => plantage


            try {

                if (etape1 && etape2 && ecranManager.getEcran().isEstActif() && actif) {
                    actif = false;

                    Intent intentPower = new Intent("POWER_ALERT");
               //    intentPower.setClass(context, MainActivity.class);
                    context.sendBroadcast(intentPower.setPackage("com.ateliopti.lapplicationpti"));

                    etape1 = etape2 = false;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            actif = true;
                        }
                    }, 7000);
                }
            } catch (Exception ignored) {
                System.out.println("Exception configuration not existing");
            }

        }


    }


    private long cal_diff(long seconds_screenon2, long seconds_screenoff2) {
        if (seconds_screenon2 >= seconds_screenoff2) {
            diffrence = (seconds_screenon2) - (seconds_screenoff2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        } else {
            diffrence = (seconds_screenoff2) - (seconds_screenon2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        }
        return diffrence;
    }
}
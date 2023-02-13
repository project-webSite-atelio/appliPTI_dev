package com.ateliopti.lapplicationpti.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.app.KeyguardManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.MyPhoneStateListener;
import com.ateliopti.lapplicationpti.helper.AppHelper;
import com.ateliopti.lapplicationpti.manager.OptionsManager;

public class ScreenReceiver extends BroadcastReceiver {

    static boolean ecran = false;

    @Override
    public void onReceive(final Context context, Intent intent) {





        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            ecran = true;
            System.out.println("ECRAN ALLUME etat");
        }

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            ecran = false;
            System.out.println("ECRAN ETEINT etat");
        }


        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) || intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {


            //        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            //        if (keyguardManager.inKeyguardRestrictedInputMode()) {
            //           System.out.println("~~~SCREEN LOCKED~~~ etat");
            //       } else {
            //           System.out.println("~~~SCREEN NOT LOCKED~~~ etat");
            //       }


        }


        MyPhoneStateListener phoneListener = new MyPhoneStateListener();
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        System.out.println(telephony.getCallState());


        // Idle State
        if (telephony.getCallState() == TelephonyManager.CALL_STATE_IDLE) {

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

                System.out.println("@atelio screen on");


                OptionsManager optionsManager = new OptionsManager(context);
                optionsManager.open();

                String valeurInterface = String.valueOf(optionsManager.getOptions().isSollicitation());

                if (valeurInterface.equals("true")) {


                    // context.sendBroadcast(new Intent("FOREGROUND"));
                    if (AppHelper.isAppRunning(context, "com.ateliopti.lapplicationpti.MainActivity")) {
                        Intent i = new Intent(context, MainActivity.class);
                        context.startActivity(i);
                    } else {
                        // App is not running
                        Intent i = new Intent(context, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);

                    }


                }


            }
        }




    }

}

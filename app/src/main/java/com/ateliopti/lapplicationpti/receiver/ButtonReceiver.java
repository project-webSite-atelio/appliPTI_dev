package com.ateliopti.lapplicationpti.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ateliopti.lapplicationpti.MainActivity;

public class ButtonReceiver extends BroadcastReceiver {

    public static final String INTENT_ACTION_SOS_BUTTON_KEY_UP = "android.intent.action.SOS_BUTTON";
    public static final String INTENT_ACTION_SOS_BUTTON_KEY_UP_BIS = "com.kodiak.intent.action.KEYCODE_SOS";
    public static final String INTENT_ACTION_SOS_BUTTON_KEY_UP_TER = "android.intent.action.SOS.down";


    private Context mCtx = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        mCtx = context;
        String action = intent.getAction();
        Log.v("REDKEYAPP", "Key action received  :" + action);

        if (action != null) {

            if (action.equals(INTENT_ACTION_SOS_BUTTON_KEY_UP) || action.equals(INTENT_ACTION_SOS_BUTTON_KEY_UP_BIS)
                    || action.equals(INTENT_ACTION_SOS_BUTTON_KEY_UP_TER)) {
                System.out.println("bouton sos pressed");

                Intent intentPower = new Intent("APP_ALERT");
                context.sendBroadcast(intentPower.setPackage("com.ateliopti.lapplicationpti"));


            }
        }

    }

}

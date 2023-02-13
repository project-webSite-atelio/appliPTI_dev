package com.ateliopti.lapplicationpti.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 */

public class PTIOffReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {

        if(intent.getAction() !=null){
            System.out.println("RECEIVER INTENT is " + intent.getAction());
        }

        context.sendBroadcast(new Intent("PTI_OFF_CHECK"));

    }
}
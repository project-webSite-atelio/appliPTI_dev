package com.ateliopti.lapplicationpti.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class ForegroundReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_USER_BACKGROUND)) {
            System.out.println("atelio serv background");
        }else if  (intent.getAction().equals(Intent.ACTION_USER_FOREGROUND)) {
            System.out.println("atelio serv foreground");
        }
            //     System.out.println("atelio serv context" + context.getPackageName());

        if (isAppForground(context)) {
            // App is in Foreground

            context.sendBroadcast(new Intent("APP_FOREGROUND"));

        } else {

            context.sendBroadcast(new Intent("APP_BACKGROUND"));
            // App is in Background
        }



    }

    public boolean isAppForground(Context mContext) {

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            return topActivity.getPackageName().equals(mContext.getPackageName());
        }

        return true;
    }

}
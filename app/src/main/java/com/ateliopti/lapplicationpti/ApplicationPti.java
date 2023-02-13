package com.ateliopti.lapplicationpti;

import android.app.Application;
import android.content.Context;

/**
 * ApplicationPti
 */
public class ApplicationPti extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        ApplicationPti.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ApplicationPti.context;
    }
}
package com.ateliopti.lapplicationpti.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.manager.VersionManager;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotationNonReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            VersionManager versionManager = new VersionManager(context);
            versionManager.open();

            versionManager.updateRappelNotation(false);


            if (android.os.Build.VERSION.SDK_INT >= 23) {
                NotificationManagerCompat nm = NotificationManagerCompat.from(context.getApplicationContext());
                nm.cancel("NOTATION", 55);
            } else {
                NotificationManager mNotification = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                mNotification.cancel(007);
            }





        }
    }
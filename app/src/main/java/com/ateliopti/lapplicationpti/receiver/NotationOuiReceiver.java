package com.ateliopti.lapplicationpti.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.NotificationManagerCompat;

import com.ateliopti.lapplicationpti.manager.VersionManager;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotationOuiReceiver extends BroadcastReceiver {
    final String STORE_LINK = "market://details?id=com.ateliopti.lapplicationpti";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            NotificationManagerCompat nm = NotificationManagerCompat.from(context.getApplicationContext());
            nm.cancel("NOTATION", 55);
        } else {
            NotificationManager mNotification = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            mNotification.cancel(007);
        }

        VersionManager versionManager = new VersionManager(context);
        versionManager.open();
        versionManager.updateRappelNotation(false);

        Intent notationActivity = new Intent(Intent.ACTION_VIEW);
        notationActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        notationActivity.setData(Uri.parse(STORE_LINK));
        context.startActivity(notationActivity);

    }
}
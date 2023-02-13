package com.ateliopti.lapplicationpti.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.ateliopti.lapplicationpti.GPSInterface;
import com.ateliopti.lapplicationpti.MainActivity;

public class GpsListener extends BroadcastReceiver {

    private GPSInterface gpsInterface = null;
    private Context context;

    public GpsListener() {
    }

    public GpsListener(Context ctx, GPSInterface gpsInterface) {
        this.gpsInterface = gpsInterface;
        this.context = ctx;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

       // Log.d("ATELIO-DEBUG", ((MainActivity) context).getCallingActivity().getPackageName());

      //      if(((MainActivity) context).getCallingActivity().getPackageName() != null){
           //     if (((MainActivity) context).getCallingActivity().getPackageName().equals("com.ateliopti.lapplicationpti")) {


        String action = intent.getAction();

//        Log.d("ATELIO-DEBUG", "GPS package = " + ((MainActivity) context).getCallingActivity().getPackageName());

/*
        if (((MainActivity) context).getCallingActivity().getPackageName().equals(“known”)) {
            Intent intent2 = getIntent();
            // extraire l'intent imbriqué
            Intent forward = (Intent) intent.getParcelableExtra(“key”);
            // rediriger l'intent imbriqué
            startActivity(forward);
        }
*/


        if(action != null){


            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            Log.d("ATELIO-DEBUG", "GPS appareil = " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));

            gpsInterface.onGpsStatusChanged(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));


        }




   // }
    //     }


     }
}
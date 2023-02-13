package com.ateliopti.lapplicationpti.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.manager.AlarmeSOSManager;

public class DeclenchementBluetoothService extends Service {
    private Context context;

    private static CountDownTimer timerBluetooth;

    private AlarmeSOSManager alarmeSOSManager;

    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    NotificationManager notificationManager;
    NotificationChannel channel;

    BluetoothAdapter mBluetoothAdapter;

    private final boolean telecommandeTrouve = false;

    private String bluetoothName;


    public void onCreate() {
        super.onCreate();

        final Intent launchNotificationIntent = new Intent(this, MainActivity.class);


        launchNotificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, launchNotificationIntent,
                PendingIntent.FLAG_IMMUTABLE);


        channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(getString(R.string.channel_id), getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_MIN);

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(getApplicationContext(), getString(R.string.channel_id))
                    .setOngoing(true) // impossible d'enlever
                    .setContentIntent(pendingIntent)
                    .setContentTitle(getString(R.string.notification_titre))
                    .setContentText(getString(R.string.notification_texte))
                    .setSmallIcon(R.drawable.pti_notification)
                    .build();


            startForeground(1, notification);


            System.out.println("@atelio " + " ONCREATE DeclenchementBluetoothService");


        }

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "s:TempWakeLock");
        wakeLock.acquire();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        compteurBluetooth();

    }


    public DeclenchementBluetoothService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        context = getBaseContext();

        alarmeSOSManager = new AlarmeSOSManager(context);

        alarmeSOSManager.open();
        bluetoothName = alarmeSOSManager.getAlarmeSOS().getSosDeclenchementBluetooth();

        return Service.START_STICKY;
    }


    // Compteur Bluetooth

    // Compteur
    public void compteurBluetooth() {

        int tempsRecherche = 5;

        if (wakeLock != null) {
            if (!wakeLock.isHeld()) {
                wakeLock.acquire();
            }
        }


        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();


            } else {
                if (!mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.startDiscovery();
                }


            }
        }


        timerBluetooth = new CountDownTimer(tempsRecherche * 1000, 1000) {
            @Override

            public void onTick(long millisUntilFinished) {
                //   System.out.println("Vertically loss : " + millisUntilFinished / 1000);

                System.out.println("Compteur Bluetooth (" + millisUntilFinished / 1000 + ")");
            }

            @Override
            public void onFinish() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (telecommandeTrouve) {
                            sendPrealarmeVolontaire();
                        } else {
                            try {
                                timerBluetooth.cancel();
                                timerBluetooth = null;
                            } catch (Exception ignored) {

                            }
                            compteurBluetooth();
                        }
                    }
                });
            }
        };
        timerBluetooth.start();
    }


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();

                if (deviceName != null) {
                    if (deviceName.equals(bluetoothName)) {
                        sendPrealarmeVolontaire();
                    }

                }

                System.out.println("@atelio " + deviceName);
                // String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

    // Si destruction

    @Override
    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }

        unregisterReceiver(mReceiver);

        try {
            timerBluetooth.cancel();
            timerBluetooth = null;
        } catch (Exception ignored) {

        }

        if (wakeLock != null) {
            if (wakeLock.isHeld()) {
                wakeLock.release();
                wakeLock = null;
            }
        }

        super.onDestroy();
    }

    // Envoi du signal
    private void sendPrealarmeVolontaire() {
        try {
            timerBluetooth.cancel();
            timerBluetooth = null;
        } catch (Exception ignored) {

        }
        System.out.println("@atelio ALARME BLUETOOTH DECLENCHE");
        Intent intent = new Intent("alarme_bluetooth");

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        } else {
            stopSelf();
        }
    }


}

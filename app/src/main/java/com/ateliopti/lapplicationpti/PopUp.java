package com.ateliopti.lapplicationpti;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;

import android.widget.Button;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ateliopti.lapplicationpti.manager.EtatManager;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PopUp {

    private final int INTERVALLE_DECOMPTE = 100;

    private String titre;
    private String message;
    private Context context;

    private AlertDialog builderPopUp;

    private CountDownTimer alertDialogCountdownPopUp;
    private final Fonctions fonctions;

    private final EtatManager etatManager;


    PopUp(String titre, String message, Context context) {
        this.titre = titre;
        this.message = message;
        this.context = context;

        fonctions = new Fonctions(context);

        etatManager = new EtatManager(context);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ping");
        intentFilter.addAction("fin_pti");

        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ping")) {
                    dismiss();
                }

                if (intent.getAction().equals("fin_pti")) {
                    dismiss();
                }

            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter(intentFilter));


        //builderPopUp = new AlertDialog(getContext());
    }

    private String getTitre() {
        return titre;
    }

    void resetTitre() {
        this.titre = "";
    }

    void setTitre(String titre) {
        this.titre = titre;
    }

    private String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    void afficherFenetre(String type) {

        try {
            if (builderPopUp != null && builderPopUp.isShowing()) {
                builderPopUp.dismiss();
            }
        } catch (Exception ignored) {

        }


        try {


            switch (type) {


                // Affiche l'IMEI de l'utilisateur
                case "imei":

                    String imei = fonctions.separateurIMEI(fonctions.getIMEI());

                    builderPopUp = new AlertDialog.Builder(context)
                            .setTitle("Votre ID …")
                            .setMessage(imei)

                            .setPositiveButton("OK, j'ai compris", (dialog, whichButton) -> {
                                if (builderPopUp != null && builderPopUp.isShowing()) {
                                    builderPopUp.dismiss();
                                }

                            })
                            .create();


                    break;


                case "autorisation":


                    builderPopUp = new AlertDialog.Builder(context)
                            .setTitle(getTitre())
                            .setMessage("Activez toutes les autorisations demandées par l'application pour pouvoir activer la surveillance PTI\n\n" +
                                    "> Paramètres > Applications > L'application PTI > Autorisations")
                            .setPositiveButton("OK, j'ai compris", (dialog, whichButton) -> {

                                if (builderPopUp != null && builderPopUp.isShowing()) {
                                    builderPopUp.dismiss();
                                }



                                ((MainActivity) context).reactivationPtiSwitch();
                            })
                            .create();

                    break;



                case "gps_not_always":


                    builderPopUp = new AlertDialog.Builder(context)
                            .setTitle(getTitre())
                            .setMessage("Activez l'autorisation \"Toujours\" pour pouvoir activer la surveillance PTI\n\n" +
                                    "> Paramètres > Applications > L'application PTI > Autorisations > Localisation > Toujours")
                            .setPositiveButton("OK, j'ai compris", (dialog, whichButton) -> {

                                if (builderPopUp != null && builderPopUp.isShowing()) {
                                    builderPopUp.dismiss();
                                }

                                ((MainActivity) context).reactivationPtiSwitch();
                            })
                            .create();

                    break;



                // Renvoie vers les paramètres GPS
                case "gps":


                    builderPopUp = new AlertDialog.Builder(context)
                            .setTitle(getTitre())
                            .setMessage(getMessage())
                            .setPositiveButton("OK, j'ai compris", (dialog, whichButton) -> {

                                if (builderPopUp != null && builderPopUp.isShowing()) {
                                    builderPopUp.dismiss();
                                }

                                final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(intent);

                                ((MainActivity) context).reactivationPtiSwitch();
                            })
                            .create();

                    break;

                    // Indication google play pour la géolocalisation
                case "confirm":


                    builderPopUp = new AlertDialog.Builder(context)
                            .setTitle("Vie privée …")
                            .setMessage(context.getResources().getString(R.string.gps_alert))

                            .setNegativeButton("En savoir plus", (dialog, whichButton) -> {

                                if (builderPopUp != null && builderPopUp.isShowing()) {
                                    builderPopUp.dismiss();
                                }

                                // Popup "En savoir plus"
                                resetTitre();

                                afficherFenetre("en_savoir_plus");


                            })

                            .setPositiveButton("OK, j'ai compris", (dialog, whichButton) -> {

                                if (builderPopUp != null && builderPopUp.isShowing()) {
                                    builderPopUp.dismiss();
                                }


                            })
                            .create();


                    break;


                case "en_savoir_plus":


                    builderPopUp = new AlertDialog.Builder(context)
                            .setTitle("En savoir plus …")
                            .setMessage(context.getResources().getString(R.string.gps_alert_en_savoir_plus))
                            .setPositiveButton("OK, j'ai compris", (dialog, whichButton) -> {

                                if (builderPopUp != null && builderPopUp.isShowing()) {
                                    builderPopUp.dismiss();
                                }


                            })
                            .create();


                    break;


                case "izsr":


                    builderPopUp = new AlertDialog.Builder(context)
                            .setTitle(getTitre())
                            .setMessage(getMessage())
                            .setCancelable(false)
                            .setMessage(getMessage())

                            .setPositiveButton("OK, j'ai compris", null)
                            .create();


                    builderPopUp.setOnShowListener(dialog -> {

                        final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        final CharSequence positiveButtonText = defaultButton.getText();

                        defaultButton.setOnClickListener(view -> {
                        });

                        alertDialogCountdownPopUp = new CountDownTimer(5 * 1000, INTERVALLE_DECOMPTE) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                // TODO : à supprimer
                                defaultButton.setEnabled(true);
                                defaultButton.setText(String.format(
                                        Locale.getDefault(), "%s (%d)",
                                        positiveButtonText,
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                                ));


                            }

                            @Override
                            public void onFinish() {

                                try {
                                    ((MainActivity) context).boutonVert();

                                    ((MainActivity) context).reactivationPtiSwitch();
                                    if (builderPopUp != null && builderPopUp.isShowing()) {
                                        builderPopUp.dismiss();
                                    }
                                    alertDialogCountdownPopUp.cancel();
                                } catch (Exception ignored) {

                                }

                            }
                        }.start();
                    });


                    break;
                case "lock":
                case "lock+patience":


                    String buttonTexte;
                    if (type.equals("lock")) {
                        buttonTexte = "OK, j'ai compris";
                    } else {
                        buttonTexte = "Patience";
                    }

                    builderPopUp = new AlertDialog.Builder(context)
                            .setMessage(getMessage())
                            .setCancelable(false)
                            .setPositiveButton(buttonTexte, null)

                            .create();


                    final int finalDureeNotification = 5;


                    builderPopUp.setOnShowListener(dialog -> {

                        final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        final CharSequence positiveButtonText = defaultButton.getText();

                        defaultButton.setOnClickListener(view -> {
                        });

                        alertDialogCountdownPopUp = new CountDownTimer(finalDureeNotification * 1000, INTERVALLE_DECOMPTE) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                                defaultButton.setEnabled(true);
                                defaultButton.setText(String.format(
                                        Locale.getDefault(), "%s (%d)",
                                        positiveButtonText,
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                                ));


                            }

                            @Override
                            public void onFinish() {

                                try {
                                    if (builderPopUp != null && builderPopUp.isShowing()) {
                                        builderPopUp.dismiss();
                                    }
                                } catch (Exception ignored) {

                                }

                                alertDialogCountdownPopUp.cancel();

                                new Handler().postDelayed(() -> {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ((MainActivity) context).finishAndRemoveTask();
                                    } else {
                                        ((MainActivity) context).finishAffinity();
                                    }


                                }, 1000);


                            }
                        }.start();
                    });


                    break;

                case "gps_prealarme":

                    String buttonTexteGPS = "OK, j'ai compris";


                    builderPopUp = new AlertDialog.Builder(context)
                            .setMessage(getMessage())
                            .setCancelable(false)
                            .setPositiveButton(buttonTexteGPS, null)

                            .create();


                    final int finalDureeNotificationGPS = 5;


                    builderPopUp.setOnShowListener(dialog -> {

                        final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        final CharSequence positiveButtonText = defaultButton.getText();

                        defaultButton.setOnClickListener(view -> {
                        });

                        alertDialogCountdownPopUp = new CountDownTimer(finalDureeNotificationGPS * 1000, INTERVALLE_DECOMPTE) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                // TODO : à supprimer
                                defaultButton.setEnabled(true);
                                defaultButton.setText(String.format(
                                        Locale.getDefault(), "%s (%d)",
                                        positiveButtonText,
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                                ));


                            }

                            @Override
                            public void onFinish() {

                                alertDialogCountdownPopUp.cancel();
                                new Handler().postDelayed(() -> {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ((MainActivity) context).finishAndRemoveTask();
                                    } else {
                                        ((MainActivity) context).finishAffinity();
                                    }


                                }, 1000);

                            }
                        }.start();
                    });


                    break;


                case "Delai_5secondes":
                    try {


                        etatManager.open();
                        etatManager.updateEtat(false);

                        fonctions.jouerBuzzerPing(true);

                        final String buttonTexte5 = "OK, j'ai compris";

                        builderPopUp = new AlertDialog.Builder(context)
                                .setMessage(getMessage())
                                .setCancelable(false)
                                .setPositiveButton(buttonTexte5, null)

                                .create();


                        final int finalDureeNotification5 = 5;


                        builderPopUp.setOnShowListener(dialog -> {

                            final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                            final CharSequence positiveButtonText = defaultButton.getText();

                            defaultButton.setOnClickListener(view -> {
                            });

                            alertDialogCountdownPopUp = new CountDownTimer(finalDureeNotification5 * 1000, INTERVALLE_DECOMPTE) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    // TODO : à supprimer
                                    defaultButton.setEnabled(true);
                                    defaultButton.setText(String.format(
                                            Locale.getDefault(), "%s (%d)",
                                            positiveButtonText,
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                                    ));


                                }

                                @Override
                                public void onFinish() {
                                    if (alertDialogCountdownPopUp != null) {
                                        alertDialogCountdownPopUp.cancel();
                                        alertDialogCountdownPopUp = null;
                                    }

                                    defaultButton.setText(buttonTexte5);
                                    defaultButton.setOnClickListener(view -> {


                                        try {
                                            fonctions.jouerSonAlarme(false);

                                            ((MainActivity) context).servicePingSonore(false);
                                            ((MainActivity) context).checkAlways();


                                            new Handler().postDelayed(() -> {


                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    ((MainActivity) context).finishAndRemoveTask();
                                                } else {
                                                    ((MainActivity) context).finishAffinity();
                                                }


                                            }, 1000);


                                        } catch (Exception ignored) {

                                        }


                                    });
                                }
                            }.start();
                        });
                    } catch (Exception ignored) {

                    }


                    break;


                case "Charger+Ping":

                    etatManager.open();
                    etatManager.updateEtat(false);

                    fonctions.jouerBuzzerPing(true);

                    String buttonTexte2 = "OK, j'ai compris";

                    builderPopUp = new AlertDialog.Builder(context)
                            .setMessage(getMessage())
                            .setCancelable(false)
                            .setPositiveButton(buttonTexte2, null)

                            .create();


                    final int finalDureeNotification2 = 5;


                    builderPopUp.setOnShowListener(dialog -> {

                        final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        final CharSequence positiveButtonText = defaultButton.getText();

                        defaultButton.setOnClickListener(view -> {
                        });

                        alertDialogCountdownPopUp = new CountDownTimer(finalDureeNotification2 * 1000, INTERVALLE_DECOMPTE) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                // TODO : à supprimer
                                defaultButton.setEnabled(true);
                                defaultButton.setText(String.format(
                                        Locale.getDefault(), "%s (%d)",
                                        positiveButtonText,
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                                ));


                            }

                            @Override
                            public void onFinish() {

                                alertDialogCountdownPopUp.cancel();

                                new Handler().postDelayed(() -> {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ((MainActivity) context).finishAndRemoveTask();
                                    } else {
                                        ((MainActivity) context).finishAffinity();
                                    }


                                }, 1000);

                            }
                        }.start();
                    });


                    break;


                case "Charger+Ping2":


                    etatManager.open();
                    etatManager.updateEtat(false);

                    fonctions.jouerBuzzerPing(true);

                    builderPopUp = new AlertDialog.Builder(context)
                            .setTitle(getTitre())
                            .setMessage(getMessage())

                            .setPositiveButton("OK, j'ai compris", (dialog, whichButton) -> {


                                if (builderPopUp != null && builderPopUp.isShowing()) {
                                    builderPopUp.dismiss();
                                }

                                try {
                                    fonctions.jouerBuzzerPing(false);
                                } catch (Exception ignored) {

                                }

                                ((MainActivity) context).servicePingSonore(false);

                                new Handler().postDelayed(() -> {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ((MainActivity) context).finishAndRemoveTask();
                                    } else {
                                        ((MainActivity) context).finishAffinity();
                                    }


                                }, 1000);


                            })


                            .setOnCancelListener(dialog -> {

                                if (builderPopUp != null && builderPopUp.isShowing()) {
                                    builderPopUp.dismiss();
                                }

                                try {
                                    fonctions.jouerBuzzerPing(false);
                                } catch (Exception ignored) {

                                }

                                ((MainActivity) context).servicePingSonore(false);

                                new Handler().postDelayed(() -> {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ((MainActivity) context).finishAndRemoveTask();
                                    } else {
                                        ((MainActivity) context).finishAffinity();
                                    }

                                }, 1000);


                            })

                            .create();


                    break;


                default:


                    builderPopUp = new AlertDialog.Builder(context)
                            .setTitle(getTitre())
                            .setMessage(getMessage())
                            .setPositiveButton("OK, j'ai compris", (dialog, whichButton) -> {

                                if (builderPopUp != null && builderPopUp.isShowing()) {
                                    builderPopUp.dismiss();
                                }


                                ((MainActivity) context).servicePingSonore(false);

                                try {
                                    fonctions.jouerBuzzerPing(false);
                                } catch (Exception ignored) {

                                }
                                System.out.println("@FIN appli : PopUp 596");
                                new Handler().postDelayed(() -> {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ((MainActivity) context).finishAndRemoveTask();
                                    } else {
                                        ((MainActivity) context).finishAffinity();
                                    }


                                }, 1000);


                            })

                            .setOnCancelListener(dialog -> {
                                if (builderPopUp != null && builderPopUp.isShowing()) {
                                    builderPopUp.dismiss();
                                }


                                ((MainActivity) context).servicePingSonore(false);


                                try {
                                    fonctions.jouerBuzzerPing(false);
                                } catch (Exception ignored) {

                                }
                                System.out.println("@FIN appli : PopUp 625");
                                new Handler().postDelayed(() -> {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ((MainActivity) context).finishAndRemoveTask();
                                    } else {
                                        ((MainActivity) context).finishAffinity();
                                    }


                                }, 1000);


                            }).create();
                    break;
            }

        } catch (Exception ignored) {

        }

        try {
            if (builderPopUp != null) {
                builderPopUp.show();
            }
        } catch (Exception ignored) {

        }


    }


    void dismiss() {

        try {
            //    builderPopUp.cancel();

            builderPopUp.dismiss();
        } catch (Exception ignored) {

        }
    }

    boolean isShowing() {
        if (builderPopUp == null) {
            return false;
        } else {
            return builderPopUp.isShowing();
        }

    }


}

package com.ateliopti.lapplicationpti.popup;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;

import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ateliopti.lapplicationpti.AsyncUpload;
import com.ateliopti.lapplicationpti.Fonctions;
import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.manager.AlarmeAMManager;
import com.ateliopti.lapplicationpti.manager.AlarmeAgressionManager;
import com.ateliopti.lapplicationpti.manager.AlarmeIZSRManager;
import com.ateliopti.lapplicationpti.manager.AlarmePVManager;
import com.ateliopti.lapplicationpti.manager.AlarmeSOSManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.ateliopti.lapplicationpti.manager.TempLocalisationManager;
import com.ateliopti.lapplicationpti.model.TempLocalisation;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PrealarmePopUp {

    private Context context;
    private final String situation;
    private final String typeAlarme;

    private AlertDialog builderPrealarme; // Dialogk
    private CountDownTimer alertDialogCountdownPrealarme; // Minuteur

    // Gestion base de données
    final private AlarmeSOSManager alarmeSOSManager;
    final private AlarmePVManager alarmePVManager;
    final private AlarmeAMManager alarmeAMManager;
    final private AlarmeIZSRManager alarmeIZSRManager;
    final private AlarmeAgressionManager alarmeAgressionManager;
    final private TempLocalisationManager tempLocalisationManager;
    final private OptionsManager optionsManager;

    final private Fonctions fonctions;
    private String pin = "";
    private EditText input;
    private boolean codePin;
    private ScrollView scrollView;

    //
    public PrealarmePopUp(final Context context, String situation, String typeAlarme) {
        this.context = context;
        this.situation = situation;
        this.typeAlarme = typeAlarme;

        alarmeSOSManager = new AlarmeSOSManager(context);
        alarmePVManager = new AlarmePVManager(context);
        alarmeAMManager = new AlarmeAMManager(context);
        alarmeIZSRManager = new AlarmeIZSRManager(context);
        alarmeAgressionManager = new AlarmeAgressionManager(context);
        optionsManager = new OptionsManager(context);
        tempLocalisationManager = new TempLocalisationManager(context);

        fonctions = new Fonctions(context);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ping");
        intentFilter.addAction("fin_pti");

        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                if (intent.getAction().equals("ping")) {
                    fonctions.vibrer(false);
                    dismiss();
                }

                if (intent.getAction().equals("fin_pti")) {

                    if (alertDialogCountdownPrealarme != null) {
                        System.out.println("@atelio ligne stop alarme");
                        alertDialogCountdownPrealarme.cancel();
                    }

                    fonctions.vibrer(false);
                    dismiss();
                }


            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter(intentFilter));


    }

    public void show() {


        try {
            if (builderPrealarme != null) {
                builderPrealarme.dismiss();
            }
        } catch (Exception ignored) {

        }


        final String typeAlarme = getTypeAlarme();

     //   int dureeNotification = 0;


        boolean sonne = false;
        boolean vibre = true;
        boolean annulationParMouvement = false;

        String typeAlarmeMessage = "";

        codePin = false;

        switch (typeAlarme) {
            case "SOS": // volontaire
            case "SOS+ENVERS": // mise tête à l'envers

                typeAlarmeMessage = "Alarme action volontaire …";

                alarmeSOSManager.open();
            //    dureeNotification = alarmeSOSManager.getAlarmeSOS().getSosDureeNotification();
                if (alarmeSOSManager.getAlarmeSOS().getSosTypeNotif().equals("Sonore")) {
                    sonne = true;
                }

                if (!alarmeSOSManager.getAlarmeSOS().getSosCodeAnnulation().equals("")) {
                    codePin = true;
                    pin = alarmeSOSManager.getAlarmeSOS().getSosCodeAnnulation();
                }

                break;

            case "PV": // perte de verticalité

                typeAlarmeMessage = "Alarme position allongée …";

                alarmePVManager.open();
            //    dureeNotification = alarmePVManager.getAlarmePV().getPvDureeNotification();
                if (alarmePVManager.getAlarmePV().getPvTypeNotif().equals("Sonore")) {
                    sonne = true;
                }

                if (alarmePVManager.getAlarmePV().isPvAnnulation()) {
                    annulationParMouvement = true;
                    vibre = false;

                }

                break;

            case "AM": // absence de mouvement

                typeAlarmeMessage = "Alarme position immobile …";
                alarmeAMManager.open();
           //     dureeNotification = alarmeAMManager.getAlarmeAM().getAmDureeNotification();
                if (alarmeAMManager.getAlarmeAM().getAmTypeNotif().equals("Sonore")) {
                    sonne = true;
                }
                if (alarmeAMManager.getAlarmeAM().isAmAnnulation()) {

                    annulationParMouvement = true;
                    vibre = false;

                }
                break;

            case "IZSR": // intervention en zone sans réseau

                typeAlarmeMessage = "Alarme délai dépassé en zone sans réseau …";

                alarmeIZSRManager.open();
           //     dureeNotification = alarmeIZSRManager.getAlarmeIZSR().getIzsrDureeNotification();
                if (alarmeIZSRManager.getAlarmeIZSR().getIzsrTypeNotif().equals("Sonore")) {
                    sonne = true;
                }

                break;

            case "Agression": // homme mort

                //   provenanceAlarme += "Alarme Agression";

                typeAlarmeMessage = "Alarme homme mort …";

                alarmeAgressionManager.open();
            //    dureeNotification = alarmeAgressionManager.getAlarmeAgression().getAgressionDureeNotification();
                if (alarmeAgressionManager.getAlarmeAgression().getAgressionTypeNotif().equals("Sonore")) {
                    sonne = true;
                }

                if (!alarmeAgressionManager.getAlarmeAgression().getAgressionCodeAnnulation().equals("")) {
                    codePin = true;
                    pin = alarmeAgressionManager.getAlarmeAgression().getAgressionCodeAnnulation();
                }

                break;
        }

        if (sonne)
            fonctions.jouerSonAlarme(true);

        if (vibre)
            fonctions.vibrer(true);


        if (codePin) {
            LayoutInflater inflater = ((MainActivity) context).getLayoutInflater();

            android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
            dialogBuilder.setTitle(typeAlarmeMessage);
            dialogBuilder.setCancelable(false);
            View dialogView = inflater.inflate(R.layout.alarme_code_pin, null);
            dialogBuilder.setView(dialogView);

            String info = "L'alarme sera communiquée si le compte à rebours arrive à 0" +
                    " (pour annuler l'alarme, tapez le code PIN PTI puis appuyez sur annuler l'alarme) ";


            scrollView = dialogView.findViewById(R.id.scrollView);
            TextView tv1 = dialogView.findViewById(R.id.messagePin);
            tv1.setText(info);

            input = dialogView.findViewById(R.id.code_pin);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setHint("Tapez ici le code PIN PTI");

            LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tv1Params.bottomMargin = 5;
            tv1Params.leftMargin = 5;
            tv1Params.rightMargin = 5;

            if(((MainActivity) context).getTypeEcran()){// écran rond
                tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
                input.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
            }


            dialogBuilder.setPositiveButton("Valider", (dialog, whichButton) -> {
                Toast.makeText(context, "Patience …", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            })
                    .setOnCancelListener(dialog -> {

                    });
            builderPrealarme = dialogBuilder.create();

        } else {

            String messageBuilder = "L'alarme sera communiquée si le compte à rebours arrive à 0";// + provenanceAlarme;
            if (annulationParMouvement) {
                messageBuilder += " (bouger pour annuler)";
            }

            builderPrealarme = new AlertDialog.Builder(context)
                    .setTitle(typeAlarmeMessage)
                    .setMessage(messageBuilder)
                    .setCancelable(false)
                    .setPositiveButton("Annuler l'alarme", null)
                    .create();
        }




       // final int finalDureeNotification = dureeNotification;
        final int finalDureeNotification = fonctions.getDureePrealarme(typeAlarme);

        final boolean finalSonne = sonne;
        builderPrealarme.setOnShowListener(dialog -> {

            final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            final CharSequence positiveButtonText = defaultButton.getText();

            defaultButton.setOnClickListener(view -> {
                ((MainActivity) context).annulerServiceAnnulationMouvement();

                if (situation.equals("En zone sans réseau")) {

                    if (((MainActivity) context).fonctions.verificationPing()) {
                        dismiss();

                        // VERIF 040520
                        ((MainActivity) context).desactiverIzsr(false);
                        //     ((MainActivity) context).finIzsr();  VERIF 040520
                    } else {
                        Toast.makeText(context, "Désactivation de la surveillance PTI impossible car absence de réseau", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if (codePin) {

                        // pour les montres, scroller vers le bas, peut etre à faire condition pour telephone ?
                        int pageHeight = scrollView.getHeight();
                        int scrollY = scrollView.getScrollY();
                        scrollView.smoothScrollTo(0, scrollY + pageHeight);// scroll one page height


                        if (pin.equals(input.getText().toString())) {
                            dismiss();
                            ((MainActivity) context).redemarrageServices(true);

                        }
                    } else {
                        dismiss();
                        ((MainActivity) context).redemarrageServices(true);

                    }
                }
            });

            if (alertDialogCountdownPrealarme != null) {
                alertDialogCountdownPrealarme.cancel();
                alertDialogCountdownPrealarme = null;
            }


            ////

            alertDialogCountdownPrealarme = new CountDownTimer(finalDureeNotification * 1000, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO : à supprimer
                    defaultButton.setEnabled(true);
                    //      defaultButton.setOnClickListener(null);
                    System.out.println("Compteur : prealarme " + (millisUntilFinished / 1000));
                    defaultButton.setText(String.format(
                            Locale.getDefault(), "%s (%d)",
                            positiveButtonText,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                    ));
                }

                @Override
                public void onFinish() {
                    ((MainActivity) context).annulerServicePrealarme();
                    try {
                        alertDialogCountdownPrealarme.cancel();

                    } catch (Exception ignored) {

                    }

                    fonctions.jouerSonAlarme(false);

                    try {
                        //    dialog.cancel();
                        dialog.dismiss();
                    } catch (Exception ignored) {

                    }

                    LocalisationSonorePopUp localisationSonorePopUp = new LocalisationSonorePopUp(context, finalSonne);


                    if (situation.equals("En zone sans réseau")) {
                        localisationSonorePopUp.setTypeAlarme("En zone sans réseau");
                    }

                    // Tuer la boucle GPS
                    // Localisation sonore
                    optionsManager.open();
                    if (optionsManager.getOptions().isLocalisationSonore() && finalSonne) {
                        ((MainActivity) context).serviceLocalisationSonore(true);
                    }

                    // Alarme lancement
                    // Pause infini
                    ((MainActivity) context).annulerServiceAnnulationMouvement();

                    if (!situation.equals("En zone sans réseau")) {
                        tempLocalisationManager.open();

                        String message = "";

                        // Test manuel

                        TempLocalisation temp = tempLocalisationManager.getTempLocalisation();

                        switch (situation) {
                            case "En extérieur":
                                if (temp.getDate().equals("")) {
                                    message = getTypeAlarme() + ";" + "Aucune localisation enregistrée (T)";
                                } else {
                                    message = getTypeAlarme() + ";" + "https://maps.google.com/?q=" + temp.getLatitude() + "," + temp.getLongitude();
                                }
                                break;

                            case "En intérieur":
                                if (temp.getDate().equals("")) {
                                    message = getTypeAlarme() + ";" + "Aucune localisation enregistrée (T)";
                                } else {
                                    message = getTypeAlarme() + ";" + temp.getSite() + " (T)";
                                }
                                break;
                        }

                        System.out.println("@atelio" + message);

                        // Stoppe les services GPS
                        ((MainActivity) context).stopGpsService();
                        ((MainActivity) context).stopPingService();

                        tempLocalisationManager.open();
                        String type = tempLocalisationManager.getTempLocalisation().getSite();

                        // Si fichier audio localisation, envoi fichier audio avec nom = détail alarme
                        if (type.equals("Localisation vocale")) {
                            if (!((MainActivity) context).getDestinataireScenarioExceptionnel().equals("")) {
                                String destinataire = ((MainActivity) context).getDestinataireScenarioExceptionnel();
                                String detail = ((MainActivity) context).getDetailScenarioExceptionnel();

                                AsyncUpload asyncUpload;
                                asyncUpload = new AsyncUpload(context, getTypeAlarme(), destinataire, detail, false);
                                asyncUpload.execute();

                            } else {
                                AsyncUpload asyncUpload;
                                asyncUpload = new AsyncUpload(context, getTypeAlarme(), false);
                                asyncUpload.execute();
                            }

                            // Etat normal, envoi simple xml
                        } else {
                            // Scénario exceptionnel
                            // Il y a un scénario exceptionnel

                            if (!((MainActivity) context).getDestinataireScenarioExceptionnel().equals("")) {
                                message += ";" + ((MainActivity) context).getDestinataireScenarioExceptionnel() + "|" + ((MainActivity) context).getDetailScenarioExceptionnel();
                                ((MainActivity) context).messageTLG(message, true);

                            } else { // Scénario normal
                                ((MainActivity) context).messageTLG(message, true);
                            }
                        }


                    }

                    // Envoi d'un broadcast Instavox
                    if(optionsManager.getOptions().isaBroadcastInstavox() && !situation.equals("En zone sans réseau")){
                        Intent intentSend = new Intent("unipro.hotkey.sos.down");
                        context.sendBroadcast(intentSend);
                    }

                    localisationSonorePopUp.setTypeAlarme(typeAlarme);
                    localisationSonorePopUp.show();

                }
            }.start();

        });
        try {

            if (builderPrealarme != null && builderPrealarme.isShowing()) {
                builderPrealarme.dismiss();
            }

            assert builderPrealarme != null;
            builderPrealarme.show();

        } catch (Exception ignored) {

        }
    }


    public void dismiss() {
        ((MainActivity) context).annulerServicePrealarme();
        System.out.println("DISMISS prealarme popup");


        if (builderPrealarme != null && builderPrealarme.isShowing()) {
            try {
                builderPrealarme.dismiss(); //  at com.ateliopti.lapplicationpti.PrealarmePopUp.dismiss(PrealarmePopUp.java:475)
            } catch (Exception ignored) {

            }
        }

        try {
            ((MainActivity) context).fonctions.jouerSonAlarme(false);
        } catch (Exception ignored) {

        }

        if (alertDialogCountdownPrealarme != null) {
            System.out.println("@atelio ligne stop alarme");
            alertDialogCountdownPrealarme.cancel();
            alertDialogCountdownPrealarme = null;
        }

        try {
            ((MainActivity) context).fonctions.vibrer(false);
        } catch (Exception ignored) {

        }

        try {
            ((MainActivity) context).annulerServiceAnnulationMouvement();
        } catch (Exception ignored) {

        }


    }


    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    private String getTypeAlarme() {
        return typeAlarme;
    }

    public boolean isShowing() {
        return builderPrealarme.isShowing();
    }


}

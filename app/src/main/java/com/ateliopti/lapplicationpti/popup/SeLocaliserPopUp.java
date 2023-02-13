package com.ateliopti.lapplicationpti.popup;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;

import android.location.Address;
import android.location.Geocoder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;

import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ateliopti.lapplicationpti.Fonctions;
import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.ScenarioExceptionnelAppareilPopUp;
import com.ateliopti.lapplicationpti.ScenarioExceptionnelChoixScenarioPopUp;
import com.ateliopti.lapplicationpti.TemplateRondeListView;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.ateliopti.lapplicationpti.manager.TempLocalisationManager;

import com.ateliopti.lapplicationpti.manager.TrajetManager;
import com.ateliopti.lapplicationpti.model.TempLocalisation;
import com.ateliopti.lapplicationpti.model.Trajet;
import com.ateliopti.lapplicationpti.service.RecherchePositionService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class SeLocaliserPopUp {

    final static String MESSAGE_RECHERCHE = "Localisation en cours d'acquisition …";
    final static String MESSAGE_ECHEC = "Echec";

    // Choix
    final static String LOCALISATION_APPROUVE = "C'est assez précis";
    final static String LOCALISATION_NON_APPROUVE = "Ce n'est pas assez précis";
    boolean gpsFind;
    private CountDownTimer alertDialogCountdownPopUp;

    Context context;

    TextView tv2;

    // Bouton valider
    Button boutonValider;

     ListView finalLvItems1;

    public SeLocaliserPopUp(Context context) {

        gpsFind = false;

        this.context = context;
        final IntentFilter filter = new IntentFilter();
        filter.addAction("seLocaliserGpsFind");


        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);


        final RelativeLayout layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(5, 2, 5, 2);
        //builderSingle.setTitle("Localisation …");


        String texte = "<b>Localisation</b>";


        TextView tv1 = new TextView(context);
        tv1.setTextColor(Color.BLACK);
        tv1.setTextSize(22f);
        tv1.setPadding(25,25,25,25);

        tv1.setText(Html.fromHtml(texte));

        RelativeLayout.LayoutParams tv1Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.bottomMargin = 5;
        tv1Params.leftMargin = 5;
        tv1Params.rightMargin = 5;

        tv1.setId(View.generateViewId());
        layout.addView(tv1);//, tv1Params);

        tv2 = new TextView(context);
        tv2.setText(MESSAGE_RECHERCHE);

        RelativeLayout.LayoutParams tv2Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv2Params.bottomMargin = 5;
        tv2Params.leftMargin = 5;
        tv2Params.rightMargin = 5;
        tv2Params.topMargin = 28;


        tv2Params.addRule(RelativeLayout.BELOW, tv1.getId());

        tv2.setId(View.generateViewId());
        layout.addView(tv2, tv2Params);

        AbsListView lvItems = null;

        if (((MainActivity) context).templateRonde) {
            lvItems = new TemplateRondeListView(context);
        } else {
            lvItems = new ListView(context);
        }
        String[] choix = {LOCALISATION_APPROUVE, LOCALISATION_NON_APPROUVE};

        ArrayAdapter<String> modeAdapter = null;

        if (((MainActivity) context).getTypeEcran()) {// écran rond
            modeAdapter = new ArrayAdapter<>(context, R.layout.eblaze_single_choice, choix);
        } else {
            modeAdapter = new ArrayAdapter<>(context, R.layout.list_multiple_lines, android.R.id.text1, choix);
        }

        if (((MainActivity) context).templateRonde) {
            lvItems.setChoiceMode(TemplateRondeListView.CHOICE_MODE_SINGLE);

        } else {
            lvItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        }
        lvItems.setAdapter(modeAdapter);


        RelativeLayout.LayoutParams listParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        listParams.bottomMargin = 5;
        listParams.leftMargin = 5;
        listParams.rightMargin = 5;
        listParams.addRule(RelativeLayout.BELOW, tv2.getId());

        layout.addView(lvItems, listParams);

        builderSingle.setView(layout);
        finalLvItems1 = (ListView) lvItems;

        // On grise l'option (précis) car il n'y a pas encore de GPS
        builderSingle.setPositiveButton("Valider", (dialog, whichButton) -> {
            dialog.dismiss();

            int selectedPosition = finalLvItems1.getCheckedItemPosition(); // Position -1 de la liste

            System.out.println("devtest [" + choix[selectedPosition] + "]");
         //   if (selectedPosition > 0) {


                switch (choix[selectedPosition]) {

                    case LOCALISATION_APPROUVE:

                        System.out.println("devtest " + "selection choix localisation précise");

                        // TODO : activation de la surveillance
                        String valueIZSR = "";

                        if(((MainActivity) context).tempAlarmeMode.equals("En zone sans réseau")) {

                            ((MainActivity) context).tempSituation = "En zone sans réseau";

                    //        valueIZSR = "En zone sans réseau " + ((MainActivity) context).dureeIZSR + "min";


                        }else{
                            ((MainActivity) context).tempSituation = "En extérieur";
                        }



                        ((MainActivity) context).optionsManager.open();
                        switch (((MainActivity) context).optionsManager.getOptions().getScenarioExceptionnel()) {
                            case 0: // Normal
                                ((MainActivity) context).routing();
                                break;

                            case 1: // Choix des scénarios
                                ScenarioExceptionnelChoixScenarioPopUp scenarioExceptionnelChoixScenarioPopUp =
                                        new ScenarioExceptionnelChoixScenarioPopUp(context, valueIZSR, ((MainActivity) context).tempSituation);
                                scenarioExceptionnelChoixScenarioPopUp.show();
                                break;

                            case 2: // Scénario exceptionnel toujours
                                ((MainActivity) context).scenarioExceptionnel = true;
                                ScenarioExceptionnelAppareilPopUp exceptionnelAppareilPopUp =
                                        new ScenarioExceptionnelAppareilPopUp(context, valueIZSR, ((MainActivity) context).tempSituation);
                                exceptionnelAppareilPopUp.show();
                                break;
                        }


                        if( ((MainActivity) context).tempSituation.equals("En extérieur")){
                            ((MainActivity) context).modeTrajet();
                        }



                        break;

                    case LOCALISATION_NON_APPROUVE:

                        // Reset de localisation en mémoire
                        //tempLocalisationManager.open();
                     //   tempLocalisationManager.vider(); // cause probleme

                        if(((MainActivity) context).tempAlarmeMode.equals("En zone sans réseau")){


                            ((MainActivity) context).tempSituation = "En zone sans réseau";








                        }else{
                            ((MainActivity) context).tempSituation = "En intérieur";
                        }


                        ChoixSitePopUp choixSitePopUp = new ChoixSitePopUp(context, "");


                        break;

                    default:

                        break;

                }

        });



        // Simuler appui "Valider"


        finalLvItems1 = (ListView) lvItems;


        lvItems.setOnItemClickListener((parent, view, position, id) -> {


            //when you need to act on itemClick


            if (position == -1) {
                finalLvItems1.setItemChecked(-1, true);
                boutonValider.setEnabled(false);
            } else if (position == 0) {
                if (gpsFind) {
                    boutonValider.setEnabled(true);


                } else {
                  //  finalLvItems1.setItemChecked(-1, true);

                    finalLvItems1.setItemChecked(1, true);
                    boutonValider.setEnabled(true);
                   // boutonValider.setEnabled(false);
                }
            } else if (position == 1) {
                boutonValider.setEnabled(true);
            }


        });

        builderSingle.setOnCancelListener(dialog -> {


            try {
                ((MainActivity) context).stopService(((MainActivity) context).recherchePositionService);
            } catch (Exception ignored) {

            }

            ((MainActivity) context).toastAnnulationSurveillancePTI();
            ((MainActivity) context).recording = false;
            ((MainActivity) context).stopRecording(false);
            ((MainActivity) context).reactivationPtiSwitch();
            ((MainActivity) context).desactivationSurveillancePTI();


        });
        final AlertDialog dialog = builderSingle.create();


        //  dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

        // On désactive le bouton car on commence par la position -1



        dialog.show();

    //    finalLvItems1.getChildAt(1).setEnabled(false);


        boutonValider = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        boutonValider.setEnabled(false);


    //    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);

        // Service de recherche de position
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {


            if (dialog.isShowing()) {
                try {
                    ((MainActivity) context).demarrerService(((MainActivity) context).recherchePositionService);


                    ((MainActivity) context).trajetManager.open();
                    int temps = ((MainActivity) context).trajetManager.getTrajet().getDureeRechercheGps();


                    alertDialogCountdownPopUp = new CountDownTimer(temps * 1000L, 100) {


                        @Override
                        public void onTick(long millisUntilFinished) {

                            tv2.setText(String.format(
                                    Locale.getDefault(), "%s (%d)",
                                    MESSAGE_RECHERCHE,
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                            ));


                        }

                        @Override
                        public void onFinish() {



                     //       dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);

                            finalLvItems1.setItemChecked(1, true);
                            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);

                            ((MainActivity) context).tempLocalisationManager.open();

                            String latitude = ((MainActivity) context).tempLocalisationManager.getTempLocalisation().getLatitude();
                            String longitude = ((MainActivity) context).tempLocalisationManager.getTempLocalisation().getLongitude();

                            if(latitude.equals("") && longitude.equals("")){
                                tv2.setText(MESSAGE_ECHEC);
                            }

                        }
                    }.start();


               //     dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);

                } catch (Exception ignored) {

                }

            }


        }, 1000);


    }

    public void setLocalisation(double latitude, double longitude) {


        //   Toast.makeText(context, "ok = " + latitude, Toast.LENGTH_SHORT).show();


        alertDialogCountdownPopUp.cancel();

        ((MainActivity) context).tempLocalisationManager.open();


        String adresse = "Adresse inconnue.";
        //   try {


        //  Toast.makeText(context, latitude + " / " + longitude, Toast.LENGTH_SHORT).show();

        Geocoder geoCoder = new Geocoder(this.context, Locale.getDefault());

        // Le Geo Coder n'est pas présent sur tous les appareils
        //   if (geoCoder.isPresent()) {
        List<Address> matches = null;
        //  try {
        try {
            try {
                matches = geoCoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception ignored) {

        }
        //  } catch (IOException e) {
        //       e.printStackTrace();
        // }
        Address bestMatch = (matches.isEmpty() ? null : matches.get(0));


        adresse = bestMatch.getAddressLine(0);


        //    } catch (Exception ignored) {

        //  }

        tv2.setText(adresse);

        gpsFind = true;
        // L'option est débloquée
        finalLvItems1.getChildAt(0).setEnabled(true);

        finalLvItems1.setItemChecked(0, true);

        // Réactivation du bouton "Valider"

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boutonValider.setEnabled(true);
            }
        }, 2000);



    }


}
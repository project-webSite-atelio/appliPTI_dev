package com.ateliopti.lapplicationpti.popup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ateliopti.lapplicationpti.ApplicationPti;
import com.ateliopti.lapplicationpti.Fonctions;
import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.ScenarioExceptionnelAppareilPopUp;
import com.ateliopti.lapplicationpti.ScenarioExceptionnelChoixScenarioPopUp;
import com.ateliopti.lapplicationpti.TemplateRondeListView;
import com.ateliopti.lapplicationpti.manager.AlarmeAMManager;
import com.ateliopti.lapplicationpti.manager.AlarmeAgressionManager;
import com.ateliopti.lapplicationpti.manager.AlarmePVManager;
import com.ateliopti.lapplicationpti.manager.EtatManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ChoixTypeAlarmePopUp {

    final static int TRUE = 1;

    final static String EN_INTERIEUR_SITUATION = "En intérieur";



    Context context;


    public ChoixTypeAlarmePopUp(Context context) {


        this.context = context;

       // this.context = ((MainActivity) ApplicationPti.getAppContext());
        ((MainActivity) context).openManager();

        //  Types d'alarmes
        final String[] choixSeul = ((MainActivity) context).fonctions.getTypesAlarme();

        ((MainActivity) context).scenarioExceptionnel = ((MainActivity) context).scenarioExceptionnel;


        // Ancienne interface, compteur des différents modes activés
        int aPerteVerticalite = ((MainActivity) context).alarmePVManager.getAlarmePV().isaAlarmePv() ? 1 : 0;
        int aAbsenceMouvement = ((MainActivity) context).alarmeAMManager.getAlarmeAM().isaAlarmeAm() ? 1 : 0;
        int aHommeMort = ((MainActivity) context).alarmeAgressionManager.getAlarmeAgression().isaAlarmeAgression() ? 1 : 0;


        int compteurTypeAlarme = aPerteVerticalite + aAbsenceMouvement + aHommeMort;


        // Interface alternative : izsr en plus
        if(((MainActivity) context).optionsManager.getOptions().isaInterfaceAlternative()) {

            int aIzsr = ((MainActivity) context).optionsManager.getOptions().isaIzsr() ? 1 : 0;

            compteurTypeAlarme += aIzsr;
        }


        System.out.println("devtest, nombre de choix alarme [" + compteurTypeAlarme + "]");



        String tempMode = "";

        // Récupération du mode unique
        if(compteurTypeAlarme == 1){


            if(aPerteVerticalite == TRUE){
                tempMode = "PV";
            }else if(aAbsenceMouvement == TRUE){
                tempMode = "AM";
            }else if(aHommeMort == TRUE){
                tempMode = "Agression";
            }else{
                tempMode = "IZSR";

            }


            // Attribution du mode
            ((MainActivity) context).tempAlarmeMode = tempMode;

            ((MainActivity) context).optionsManager.open();


            if(((MainActivity) context).optionsManager.getOptions().isaInterfaceAlternative()){ // Interface Alternative

                System.out.println("devtest, isaInterfaceAlternative");


                if (tempMode.equals("IZSR")) {
                    ((MainActivity) context).tempAlarmeMode = "En zone sans réseau";

                    System.out.println("devtest, choix type alarme [" + ((MainActivity) context).tempAlarmeMode + "]");


                    ((MainActivity) context).choisirDureeIzsr();
                    return;

                }

                ((MainActivity) context).seLocaliserPopUp();

                return;
            }





            if (((MainActivity) context).tempSituation.equals(EN_INTERIEUR_SITUATION)) {

                ((MainActivity) context).texteExplication = choixSeul[0];
                ((MainActivity) context).choisirSite("");

            } else {
                ((MainActivity) context).optionsManager.open();

                switch (((MainActivity) context).optionsManager.getOptions().getScenarioExceptionnel()) {
                    case 0: // Normal
                        ((MainActivity) context).routing();
                        break;

                    case 1: // Choix des scénarios
                        ScenarioExceptionnelChoixScenarioPopUp scenarioExceptionnelChoixScenarioPopUp =
                                new ScenarioExceptionnelChoixScenarioPopUp(context, choixSeul[0], ((MainActivity) context).tempSituation);
                        scenarioExceptionnelChoixScenarioPopUp.show();
                        break;

                    case 2: // Scénario exceptionnel toujours
                        ((MainActivity) context).scenarioExceptionnel = true;
                        ScenarioExceptionnelAppareilPopUp exceptionnelAppareilPopUp =
                                new ScenarioExceptionnelAppareilPopUp(context, choixSeul[0], ((MainActivity) context).tempSituation);
                        exceptionnelAppareilPopUp.show();
                        break;

                }
            }



            // Plusieurs types d'alarmes
        } else {

            System.out.println("devtest, choix type alarme [" + "choix"+ "]");


            ((MainActivity) context).ptiSwitchSetClickable(false);

            final String[] choix;

            ((MainActivity) context).optionsManager.open();
            if(((MainActivity) context).optionsManager.getOptions().isaInterfaceAlternative()) {
                choix = ((MainActivity) context).fonctions.getTypesAlarmeInterfaceAlternative();
            }else{
                choix = ((MainActivity) context).fonctions.getTypesAlarme();

            }


            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MaterialAlertDialog_Rounded);


            final LinearLayout layout = new LinearLayout(context);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(parms);

            layout.setGravity(Gravity.CLIP_VERTICAL);
            layout.setPadding(2, 2, 2, 2);


            TextView tv1 = new TextView(context);

            tv1.setTextColor(Color.BLACK);
            tv1.setPadding(25,25,25,25);


            ((MainActivity) context).optionsManager.open();
            if(((MainActivity) context).optionsManager.getOptions().isaInterfaceAlternative()) { // Interface Alternative
                tv1.setText(Html.fromHtml("<b>Type d'alarme souhaité</b>"));

            }else{
                tv1.setText(Html.fromHtml("<b>Choisir les alarmes souhaitées</b>"));
            }



            if (((MainActivity) context).templateRonde) {
                tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f);

            } else {
                tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);

            }


            LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tv1Params.bottomMargin = 5;
            tv1Params.leftMargin = 5;
            tv1Params.rightMargin = 5;

            layout.addView(tv1, tv1Params);
            AbsListView lvItems = null;


            if (((MainActivity) context).templateRonde) {
                tv1.setPadding(50, 0, 50, 0);

                lvItems = new TemplateRondeListView(context);

            } else {
                lvItems = new ListView(context);

            }

            String[] stringArray = choix;

            ArrayAdapter<String> modeAdapter = null;

            if (((MainActivity) context).getTypeEcran()) { // écran rond
                modeAdapter = new ArrayAdapter<String>(context, R.layout.list_multiple_lines_22, android.R.id.text1, stringArray);
            } else {
                modeAdapter = new ArrayAdapter<String>(context, R.layout.list_multiple_lines, android.R.id.text1, stringArray);
            }

            lvItems.setAdapter(modeAdapter);
            if (((MainActivity) context).templateRonde) {
                lvItems.setChoiceMode(TemplateRondeListView.CHOICE_MODE_SINGLE);

            } else {
                lvItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            }
            layout.addView(lvItems);


            // Curseur vers le premier item
            try {
                lvItems.setItemChecked(0, true);
            } catch (Exception ignored) {

            }


            builder.setView(layout);


            final AbsListView finalLvItems = lvItems;
            builder.setPositiveButton("Valider", (dialog, whichButton) -> {


                dialog.dismiss();
                //  switchUnlock(true);
                int selectedPosition = finalLvItems.getCheckedItemPosition();

                if (choix[selectedPosition].contains("allongée")) {
                    ((MainActivity) context).tempAlarmeMode = "PV";
                } else if (choix[selectedPosition].contains("immobile")) {
                    ((MainActivity) context).tempAlarmeMode = "AM";
                } else if (choix[selectedPosition].contains("mort")) {
                    ((MainActivity) context).tempAlarmeMode = "Agression";

                } else if (choix[selectedPosition].contains("zone sans réseau")) {
                    ((MainActivity) context).tempAlarmeMode = "En zone sans réseau";

                    System.out.println("devtest, choix type alarme [" + ((MainActivity) context).tempAlarmeMode + "]");


                    ((MainActivity) context).choisirDureeIzsr();
                    return;

                }

                System.out.println("devtest, choix type alarme [" + ((MainActivity) context).tempAlarmeMode + "]");

                ((MainActivity) context).optionsManager.open();

                if(((MainActivity) context).optionsManager.getOptions().isaInterfaceAlternative()){ // Interface Alternative

                    ((MainActivity) context).seLocaliserPopUp();

                    return;
                }


                if (((MainActivity) context).tempSituation.equals(EN_INTERIEUR_SITUATION)) {
                    ((MainActivity) context).texteExplication = choixSeul[0];
                    ((MainActivity) context).choisirSite("");
                } else {


                    ((MainActivity) context).optionsManager.open();


                    switch (((MainActivity) context).optionsManager.getOptions().getScenarioExceptionnel()) {
                        case 0: // Normal
                            ((MainActivity) context).routing();
                            break;

                        case 1: // Choix des scénarios
                            ScenarioExceptionnelChoixScenarioPopUp scenarioExceptionnelChoixScenarioPopUp =
                                    new ScenarioExceptionnelChoixScenarioPopUp(context, choix[selectedPosition], ((MainActivity) context).tempSituation);
                            scenarioExceptionnelChoixScenarioPopUp.show();
                            break;

                        case 2: // Scénario exceptionnel toujours
                            ((MainActivity) context).scenarioExceptionnel = true;
                            ScenarioExceptionnelAppareilPopUp exceptionnelAppareilPopUp =
                                    new ScenarioExceptionnelAppareilPopUp(context, choix[selectedPosition], ((MainActivity) context).tempSituation);
                            exceptionnelAppareilPopUp.show();

                            break;
                    }


                }


            })
                    // Annulation : choix du type d'alarmes
                    .setOnCancelListener(dialog -> {
                        ((MainActivity) context).toastAnnulationSurveillancePTI();

                        context.stopService(((MainActivity) context).rechercheGPSLoopService);

                        ((MainActivity) context).reactivationPtiSwitch();
                        ((MainActivity) context).desactivationSurveillancePTI();

                    });

            // Affichage de l'AlertDialog
            ((MainActivity) context).choisirTypeAlarme = builder.create();
            ((MainActivity) context).choisirTypeAlarme.show();
        }


    }
}

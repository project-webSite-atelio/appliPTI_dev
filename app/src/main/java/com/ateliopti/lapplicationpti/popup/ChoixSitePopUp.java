package com.ateliopti.lapplicationpti.popup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
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
import com.ateliopti.lapplicationpti.manager.SiteManager;
import com.ateliopti.lapplicationpti.manager.TempLocalisationManager;
import com.ateliopti.lapplicationpti.model.TempLocalisation;

import java.io.File;
import java.io.IOException;

public class ChoixSitePopUp {


    private Button rec;

    Context context;
    /*
    SiteManager siteManager;
    OptionsManager optionsManager;
    TempLocalisationManager tempLocalisationManager;

    int dureeIZSR;

    String tempSituation;
    TextView localisationText;
    TextView localisationText2;

    boolean templateRonde;

    Fonctions fonctions;
    */

    public ChoixSitePopUp(Context context, final String etat) {

        this.context = context;

/*
        // Liste des managers
        siteManager = ((MainActivity) context).siteManager;
        optionsManager = ((MainActivity) context).optionsManager;
        tempLocalisationManager = ((MainActivity) context).tempLocalisationManager;

        dureeIZSR = ((MainActivity) context).dureeIZSR;

        tempSituation = ((MainActivity) context).tempSituation;
        localisationText = ((MainActivity) context).localisationText;
        localisationText2 = ((MainActivity) context).localisationText2;

        fonctions = ((MainActivity) context).fonctions;

        templateRonde = ((MainActivity) context).templateRonde;

*/
        // Open managers
        ((MainActivity) context).siteManager.open();
        ((MainActivity) context).optionsManager.open();
        ((MainActivity) context).tempLocalisationManager.open();


        final String[] choix = ((MainActivity) context).siteManager.getSites();

        // Si l'activation automatique est activée


        boolean activationAutomatique = false;

        try {
            activationAutomatique = ((MainActivity) context).optionsManager.getOptions().isActivationPTIAutomatique();
        } catch (Exception ignored) {

        }


        // Activation automatique
        if (activationAutomatique) {

            String date = ((MainActivity) context).fonctions.demandeDate();


            String[] dateChangement = date.split(" ");

            String ligne1 = "";
            String ligneSuperieur = "";

            ((MainActivity) context).siteManager.open();
            ((MainActivity) context).optionsManager.open();


            ((MainActivity) context).localisationText.setText(Html.fromHtml(ligne1));

            String nomSite = "";
            if (((MainActivity) context).siteManager.count() == 0) {
                nomSite = "Aucune localisation enregistrée";
            } else { // 1

                nomSite = choix[0];
            }

            ligneSuperieur = nomSite;


            if (!((MainActivity) context).tempSituation.equals("En zone sans réseau")) {
                if (((MainActivity) context).optionsManager.getOptions().isLocalisationAudio() || ((MainActivity) context).siteManager.count() > 1) {
                    // ligne1 = "Localisation : <b><i> (appuyer ici pour modifier)</i></b>";
                    ligneSuperieur = "<b><i> (appuyez ici pour modifier)</i></b>";
                    ((MainActivity) context).localisationText2.setText(Html.fromHtml(ligneSuperieur));

                } else {
                    // ligne1 = "Localisation : ";
                    ligneSuperieur += "";

                }
            }


            TempLocalisation tempLocalisation = new TempLocalisation(date, "", "", nomSite);
            ((MainActivity) context).tempLocalisationManager.open();
            ((MainActivity) context).tempLocalisationManager.updateTempLocalisation(tempLocalisation);


            ((MainActivity) context).localisationText.setText(Html.fromHtml("Le " + dateChangement[0] + " à " + dateChangement[1] + ", localisation à proximité de : " + ligneSuperieur));
            ((MainActivity) context).routing();

            return;


        }


        // Mode avec une ou aucune localisation
        if ((choix.length == 0 || choix.length == 1) && !((MainActivity) context).optionsManager.getOptions().isLocalisationAudio()) {


            String date = ((MainActivity) context).fonctions.demandeDate();

            TempLocalisation tempLocalisation;

            if (choix.length == 0) {
                tempLocalisation = new TempLocalisation(date, "", "", "Aucune localisation enregistrée");

            } else {
                tempLocalisation = new TempLocalisation(date, "", "", choix[0]);

            }

            ((MainActivity) context).tempLocalisationManager.open();
            ((MainActivity) context).tempLocalisationManager.updateTempLocalisation(tempLocalisation);

            if (etat.equals("IZSR")) {
                String valueIZSR = "En zone sans réseau " + ((MainActivity) context).dureeIZSR;
                valueIZSR += "min";

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

            } else {
                ((MainActivity) context).optionsManager.open();
                switch (((MainActivity) context).optionsManager.getOptions().getScenarioExceptionnel()) {
                    case 0: // Normal
                        ((MainActivity) context).routing();
                        break;

                    case 1: // Choix des scénarios
                        ScenarioExceptionnelChoixScenarioPopUp scenarioExceptionnelChoixScenarioPopUp =
                                new ScenarioExceptionnelChoixScenarioPopUp(context, ((MainActivity) context).texteExplication, ((MainActivity) context).tempSituation);
                        scenarioExceptionnelChoixScenarioPopUp.show();
                        break;

                    case 2: // Scénario exceptionnel toujours
                        ((MainActivity) context).scenarioExceptionnel = true;
                        ScenarioExceptionnelAppareilPopUp exceptionnelAppareilPopUp =
                                new ScenarioExceptionnelAppareilPopUp(context, ((MainActivity) context).texteExplication, ((MainActivity) context).tempSituation);
                        exceptionnelAppareilPopUp.show();
                        break;
                }

            }

            return;

        }
        //

        if (!etat.equals("changement")) {
            ((MainActivity) context).ptiSwitchSetClickable(false);

        }

        // Fenêtre : choix du site et enregistrement de message vocal

        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        final RelativeLayout layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);


        ((MainActivity) context).optionsManager.open();
        AbsListView lvItems = null;
        RelativeLayout.LayoutParams listParams = null;
        if (((MainActivity) context).optionsManager.getOptions().isaInterfaceAlternative()) { // Interface Alternative

            TextView tv1 = new TextView(context);
            tv1.setTextColor(Color.BLACK);

            if (((MainActivity) context).templateRonde) {
                tv1.setPadding(50, 0, 50, 0);
                tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f);
            } else {
                tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            }


            tv1.setText(Html.fromHtml(this.getTitre()));

            RelativeLayout.LayoutParams tv1Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tv1Params.bottomMargin = 22;
            tv1Params.leftMargin = 5;
            tv1Params.rightMargin = 5;
            //tv1Params.addRule(RelativeLayout.BELOW, tv1.getId());

            tv1.setId(View.generateViewId());
            layout.addView(tv1,tv1Params);//, tv1Params);

            String explications = "Exemple :\n" + "Intervention chez xxx au 10 rue des cigognes, 67960 ENTZHEIM, à l’étage 3 dans la salle 39";

            TextView explicationsTv = new TextView(context);

            explicationsTv.setText(Html.fromHtml(explications));

            RelativeLayout.LayoutParams explicationsParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            explicationsParams.bottomMargin = 5;
            explicationsParams.leftMargin = 5;
            explicationsParams.rightMargin = 5;
            explicationsParams.addRule(RelativeLayout.BELOW, tv1.getId());

            explicationsTv.setId(View.generateViewId());

            layout.addView(explicationsTv, explicationsParams);


            RelativeLayout.LayoutParams recParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            recParams.bottomMargin = 5;
            recParams.leftMargin = 5;
            recParams.rightMargin = 5;

            recParams.addRule(RelativeLayout.BELOW, explicationsTv.getId());

             rec = new Button(context);


            rec.setText(Html.fromHtml("APPUYEZ ICI POUR ENREGISTRER<br />LE MESSAGE VOCAL"));
            if (((MainActivity) context).templateRonde) {
                rec.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
            } else {
                rec.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(context.getResources().getString(R.string.alert_dialog_size)));
            }
            rec.setId(View.generateViewId());




            layout.addView(rec, recParams);





        } else {


            ((MainActivity) context).siteManager.open();

            TextView tv1 = new TextView(context);
            tv1.setTextColor(Color.BLACK);

            if (((MainActivity) context).templateRonde) {
                tv1.setPadding(50, 0, 50, 0);
                tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f);
            } else {
                tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            }


            tv1.setText(Html.fromHtml(this.getTitre()));

            RelativeLayout.LayoutParams tv1Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tv1Params.bottomMargin = 22;
            tv1Params.leftMargin = 5;
            tv1Params.rightMargin = 5;

            //tv1Params.addRule(RelativeLayout.BELOW, tv1.getId());

            tv1.setId(View.generateViewId());
            layout.addView(tv1,tv1Params);//, tv1Params);

            //

            rec = new Button(context);


            if (((MainActivity) context).optionsManager.getOptions().isLocalisationAudio()) {

                RelativeLayout.LayoutParams recParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                recParams.bottomMargin = 5;
                recParams.leftMargin = 5;
                recParams.rightMargin = 5;

                recParams.addRule(RelativeLayout.BELOW, tv1.getId());


                rec.setText(Html.fromHtml("APPUYEZ ICI POUR ENREGISTRER<br />LE MESSAGE VOCAL"));
                if (((MainActivity) context).templateRonde) {
                    rec.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
                } else {
                    rec.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(context.getResources().getString(R.string.alert_dialog_size)));
                }
                rec.setId(View.generateViewId());
                layout.addView(rec, recParams);
            }

            String[] sites = ((MainActivity) context).siteManager.getSites();

            lvItems = null;


            if (((MainActivity) context).templateRonde) {

                lvItems = new TemplateRondeListView(context);

            } else {
                lvItems = new ListView(context);

            }


            ArrayAdapter<String> modeAdapter = null;

            if (((MainActivity) context).getTypeEcran()) {// écran rond

                modeAdapter = new ArrayAdapter<>(context, R.layout.eblaze_single_choice, sites);

            } else {

                modeAdapter = new ArrayAdapter<>(context, R.layout.list_multiple_lines, android.R.id.text1, sites);
            }


            if (((MainActivity) context).templateRonde) {
                lvItems.setChoiceMode(TemplateRondeListView.CHOICE_MODE_SINGLE);

            } else {
                lvItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            }
            lvItems.setAdapter(modeAdapter);


            listParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            listParams.bottomMargin = 5;
            listParams.leftMargin = 5;
            listParams.rightMargin = 5;


            if (((MainActivity) context).optionsManager.getOptions().isLocalisationAudio()) {

                listParams.addRule(RelativeLayout.BELOW, rec.getId());

            } else {

                listParams.addRule(RelativeLayout.BELOW, tv1.getId());


            }


            layout.addView(lvItems, listParams);

            try {
                lvItems.setItemChecked(0, true);
            } catch (Exception ignored) {

            }


        }


        builderSingle.setView(layout);


        final ListView finalLvItems = (ListView) lvItems;

        builderSingle.setPositiveButton("Valider", (dialog, which) -> validationChoixSite(choix, etat, finalLvItems));

        // Simuler appui "Valider"


        if (!((MainActivity) context).optionsManager.getOptions().isaInterfaceAlternative()) { // Interface normale

            final ListView finalLvItems1 = (ListView) lvItems;
            lvItems.setOnItemClickListener((parent, view, position, id) -> {
                //when you need to act on itemClick
                if (((MainActivity) context).recording) {
                    finalLvItems1.setItemChecked(-1, true);

                }
            });

        }

        builderSingle.setOnCancelListener(dialog -> {

            if (!etat.equals("changement")) {


                try {
                    ((MainActivity) context).stopService(((MainActivity) context).recherchePositionService);
                } catch (Exception ignored) {

                }
                ((MainActivity) context).toastAnnulationSurveillancePTI();
                ((MainActivity) context).recording = false;
                ((MainActivity) context).stopRecording(false);
                ((MainActivity) context).reactivationPtiSwitch();
                ((MainActivity) context).desactivationSurveillancePTI();

            }

        });
        final AlertDialog dialog = builderSingle.create();
        // Bouton "Record"
        final ListView finalLvItems2 = (ListView) lvItems;
        rec.setOnClickListener(view -> {
            rec.setGravity(Gravity.LEFT | Gravity.CENTER);

            if (((MainActivity) context).recording) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            } else {
                try{
                    finalLvItems2.setItemChecked(-1, true);

                }catch (Exception ignored){

                }

                String[] theme = ((MainActivity) context).fonctions.chargerTheme();
                rec.setBackground(new ColorDrawable(Color.parseColor(theme[0])));
                rec.setTextColor(Color.WHITE);
                rec.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                rec.setText("PARLEZ PUIS VALIDEZ");

                ((MainActivity) context).startRecording();
            }
        });

        dialog.show();

    }

    // Validation du choix de site
    private void validationChoixSite(String[] choix, String etat, ListView lvItems) {




        try{
            // Annulation
            if (choix.length == 0 & !((MainActivity) context).recording) {
                ((MainActivity) context).recording = false;
                ((MainActivity) context).stopRecording(false);
                ((MainActivity) context).choisirSite(etat);
                return;
            }
        }catch (Exception ignored){

        }



        TempLocalisation tempLocalisation;
        ((MainActivity) context).tempLocalisationManager.open();

        ((MainActivity) context).stopRecording(true);



        String date = ((MainActivity) context).fonctions.demandeDate();
        if (((MainActivity) context).recording) {
            tempLocalisation = new TempLocalisation(date, "", "", "Localisation vocale");
            ((MainActivity) context).recording = false;

        } else {
            // Evite crash, out of bounds
            try {
                int selectedPosition = lvItems.getCheckedItemPosition();
                if (choix[selectedPosition] == null) {
                    return;
                }
                tempLocalisation = new TempLocalisation(date, "", "", choix[selectedPosition]);
            } catch (Exception ignored) {
                return;
            }


        }

        ((MainActivity) context).tempLocalisationManager.open();


        ((MainActivity) context).tempLocalisationManager.updateTempLocalisation(tempLocalisation);
        if (!etat.equals("changement")) {
            if (etat.equals("IZSR")) {
                String valueIZSR = "En zone sans réseau " + ((MainActivity) context).dureeIZSR;
                valueIZSR += "min";// + choix[selectedPosition];

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

            } else {
                ((MainActivity) context).optionsManager.open();
                switch (((MainActivity) context).optionsManager.getOptions().getScenarioExceptionnel()) {
                    case 0: // Normal
                        ((MainActivity) context).routing();
                        break;

                    case 1: // Choix des scénarios
                        ScenarioExceptionnelChoixScenarioPopUp scenarioExceptionnelChoixScenarioPopUp =
                                new ScenarioExceptionnelChoixScenarioPopUp(context, ((MainActivity) context).texteExplication, ((MainActivity) context).tempSituation);
                        scenarioExceptionnelChoixScenarioPopUp.show();
                        break;

                    case 2: // Scénario exceptionnel toujours
                        ((MainActivity) context).scenarioExceptionnel = true;
                        ScenarioExceptionnelAppareilPopUp exceptionnelAppareilPopUp =
                                new ScenarioExceptionnelAppareilPopUp(context, ((MainActivity) context).texteExplication, ((MainActivity) context).tempSituation);
                        exceptionnelAppareilPopUp.show();
                        break;
                }

            }
        } else {

            String[] dateChangement = ((MainActivity) context).tempLocalisationManager.getTempLocalisation().getDate().split(" ");
            String ligne1 = "";
            String ligneSuperieur = "";

            ((MainActivity) context).siteManager.open();
            ((MainActivity) context).optionsManager.open();


            if (!((MainActivity) context).tempSituation.equals("En zone sans réseau")) {
                if (((MainActivity) context).optionsManager.getOptions().isLocalisationAudio() || ((MainActivity) context).siteManager.count() > 1) {
                    ligneSuperieur = "<b><i> (appuyez ici pour modifier)</i></b>";
                    ((MainActivity) context).localisationText2.setText(Html.fromHtml(ligneSuperieur));
                } else {
                    ligne1 = "";
                }
            }

            String appuyerEcouter = "";
            String nomSite = ((MainActivity) context).tempLocalisationManager.getTempLocalisation().getSite();
            if (((MainActivity) context).tempLocalisationManager.getTempLocalisation().getSite().equals("Localisation vocale")) {
                appuyerEcouter = " <b><i>(appuyez ici pour écouter)</i></b>";
            }

            ligneSuperieur = nomSite;

            Log.d("ATELIO-DEBUG", "Site = " + nomSite);

            if(nomSite.equals("Localisation vocale")){
                ligneSuperieur = "Message vocal";
            }

            ((MainActivity) context).localisationText.setText(Html.fromHtml("Le " + dateChangement[0] + " à " + dateChangement[1] + ", localisation à proximité de : " + ligneSuperieur + appuyerEcouter));

        }


    }

    public String getTitre() {
        String texte = "";

        ((MainActivity) context).siteManager.open();
        ((MainActivity) context).optionsManager.open();

        if(((MainActivity) context).optionsManager.getOptions().isaInterfaceAlternative()) { // Interface Alternative
            texte = "Message vocal à envoyer en cas d’alarme";

        }else if (((MainActivity) context).siteManager.count() > 0) {

            if (((MainActivity) context).optionsManager.getOptions().isLocalisationAudio()) {
                texte = "Choisir la localisation dans la liste ci-dessous (personnalisable) ou enregistrer le message vocal …";
            } else {
                texte = "Choisir la localisation  dans la liste ci-dessous (personnalisable) …";
            }
        } else {
            texte = "Enregistrer le message vocal";
        }

        return "<b>" + texte + "</b>";
    }



    // Recording



}

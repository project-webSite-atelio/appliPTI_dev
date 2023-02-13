package com.ateliopti.lapplicationpti.popup;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.ateliopti.lapplicationpti.Fonctions;
import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.R;
import com.ateliopti.lapplicationpti.TemplateRondeListView;
import com.ateliopti.lapplicationpti.manager.EtatManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.ateliopti.lapplicationpti.model.Options;

public class ChoixSituationPopUp {

    Context context;
    /*
    EtatManager etatManager;
    OptionsManager optionsManager;
    String tempSituation;
    Fonctions fonctions;
    boolean templateRonde;
    */

    public ChoixSituationPopUp(Context context) {


        this.context = context;



        ((MainActivity) context).optionsManager.open();
        if(((MainActivity) context).optionsManager.getOptions().isaInterfaceAlternative()){ // Interface Alternative


            // Il faut que le background permission gps soit actif pour activer la surveillance
            if(((MainActivity) context).verifierAutorisationExterieur()){
                ((MainActivity) context).choisirTypeAlarme();

            }else{
                ((MainActivity) context).autorisationGpsInvalide();
            }


            return;
        }


        ((MainActivity) context).etatManager.open();
        if (!((MainActivity) context).etatManager.getEtat().isFinCharge()) {

            ((MainActivity) context).optionsManager.open();
            Options currentOptions = ((MainActivity) context).optionsManager.getOptions();
            boolean batiment = currentOptions.isaBatiment();
            boolean trajet = currentOptions.isaTrajet();
            boolean izsr = currentOptions.isaIzsr();

            if (batiment && !trajet && !izsr) { // Bâtiment seul
                ((MainActivity) context).tempSituation = "En intérieur";

                ((MainActivity) context).choisirTypeAlarme();


            } else if (!batiment && trajet && !izsr) { // Trajet seul


                ActivityCompat.requestPermissions(((MainActivity) context),
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        23);


                ((MainActivity) context).tempSituation = "En extérieur";
                ((MainActivity) context).modeTrajet();

            } else if (!batiment && !trajet && izsr) { // IZSR seul
                ((MainActivity) context).tempSituation = "En zone sans réseau";
                ((MainActivity) context).choisirDureeIzsr();

            } else {


                final String exterieurChoix = "En extérieur";
                final String izsrChoix = "En zone sans réseau";

                ((MainActivity) context).ptiSwitchSetClickable(false);

                final String[] choix = ((MainActivity) context).fonctions.getConfigSituations();
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                final LinearLayout layout = new LinearLayout(context);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);

                layout.setLayoutParams(parms);

                layout.setGravity(Gravity.CLIP_VERTICAL);
                layout.setPadding(2, 2, 2, 2);

                TextView tv1 = new TextView(context);
                tv1.setTextColor(Color.BLACK);

                if (((MainActivity) context).templateRonde) {
                    tv1.setPadding(50, 0, 50, 0);
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
                } else {
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                }


                tv1.setText(Html.fromHtml("<b>Choisir la situation de travail isolé</b>"));

                LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tv1Params.bottomMargin = 5;
                tv1Params.leftMargin = 5;
                tv1Params.rightMargin = 5;

                layout.addView(tv1, tv1Params);

                AbsListView lvItems = null;

                if (((MainActivity) context).templateRonde) {

                    lvItems = new TemplateRondeListView(context);

                } else {
                    lvItems = new ListView(context);

                }


                String[] stringArray = choix;

                //   ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, R.layout.list_multiple_lines, android.R.id.text1, stringArray);

                ArrayAdapter<String> modeAdapter = null;

                if (((MainActivity) context).getTypeEcran()) {// écran rond
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

                try {
                    lvItems.setItemChecked(0, true);
                } catch (Exception ignored) {

                }

                builder.setView(layout);


                final AbsListView finalLvItems = lvItems;
                builder.setPositiveButton("Valider", (dialog, whichButton) -> {
                    dialog.dismiss();

                    int selectedPosition = finalLvItems.getCheckedItemPosition(); // Position -1 de la liste

                    ((MainActivity) context).tempSituation = choix[selectedPosition];
                    if (choix[selectedPosition].equals(exterieurChoix)) {

                        ((MainActivity) context).modeTrajet();

                    } else if (choix[selectedPosition].equals(izsrChoix)) {
                        ((MainActivity) context).choisirDureeIzsr();

                    } else { // Mode intérieur
                        ((MainActivity) context).choisirTypeAlarme();

                    }

                })

                        // Annulation : choix de situation de travail
                        .setOnCancelListener(dialog -> {
                            ((MainActivity) context).toastAnnulationSurveillancePTI();

                            ((MainActivity) context).desactivationSurveillancePTI();
                            ((MainActivity) context).reactivationPtiSwitch();

                        });


                builder.setView(layout);

                ((MainActivity) context).choisirSituation = builder.create();

                try {
                    ((MainActivity) context).choisirSituation.show();
                } catch (Exception ignored) {

                }
            }
        }


    }


}

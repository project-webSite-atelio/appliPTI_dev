package com.ateliopti.lapplicationpti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ateliopti.lapplicationpti.manager.AlarmeSOSManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.ateliopti.lapplicationpti.manager.SiteManager;

import java.util.ArrayList;

public class ScenarioExceptionnelChoixScenarioPopUp {

    private static final MediaPlayer mp = new MediaPlayer();
    private final AudioManager am;

    private Context context;

    private final String texte;
    private final String situation;

    private AlertDialog.Builder builderExplications;

    private final AlarmeSOSManager alarmeSOSManager;
    private final SiteManager siteManager;
    private final OptionsManager optionsManager;


    private final Fonctions fonctions;

    private final ArrayList<String> listeExplications = new ArrayList<>();


    public ScenarioExceptionnelChoixScenarioPopUp(Context context, String texte, String situation) {
        this.context = context;
        this.texte = texte;
        this.situation = situation;
        fonctions = new Fonctions(context);

        alarmeSOSManager = new AlarmeSOSManager(context);
        siteManager = new SiteManager(context);
        optionsManager = new OptionsManager(context);
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);


    }

    // Affiche la fenêtre
    public void show() {

        optionsManager.open();

        // Normal : utilisation normale
        // Exceptionnel : utilisation exceptionnelle

        final String[] choix = {"Non", "Oui"};


        builderExplications = new android.app.AlertDialog.Builder(context);


        final LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);


        final ArrayAdapter<String> arrayAdapterItems = new ArrayAdapter<>(
                context, android.R.layout.simple_list_item_single_choice, choix);

        TextView tv1 = new TextView(context);

        tv1.setTextColor(Color.RED);

        if(((MainActivity) context).getTypeEcran()){// écran rond
            tv1.setPadding(50, 0, 50, 0);
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);

        }else{
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);

        }



        tv1.setText(Html.fromHtml("<b>Choisir un contact exceptionnel ?</b>"));

        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.bottomMargin = 5;
        tv1Params.leftMargin = 5;
        tv1Params.rightMargin = 5;

        layout.addView(tv1, tv1Params);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice);


        final ListView lvItems = new ListView(context);

        String[] stringArray = choix;

        ArrayAdapter<String> modeAdapter = null;

        if(((MainActivity) context).getTypeEcran()){// écran rond
            modeAdapter = new ArrayAdapter<String>(context, R.layout.list_multiple_lines_22, android.R.id.text1, stringArray);
        }else{
            modeAdapter = new ArrayAdapter<String>(context, R.layout.list_multiple_lines, android.R.id.text1, stringArray);
        }

        lvItems.setAdapter(modeAdapter);
        lvItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        layout.addView(lvItems);

        try {
            lvItems.setItemChecked(0, true);
        } catch (Exception e) {

        }


        builderExplications.setView(layout);

        builderExplications.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


                dialog.dismiss();
                //  switchUnlock(true);
                int selectedPosition = lvItems.getCheckedItemPosition();

                if (choix[selectedPosition].equals("Non")) {
                    ((MainActivity) context).routing();
                    //     tempAlarmeMode = "PV";
                    // TODO : go to explications
                } else if (choix[selectedPosition].contains("Oui")) {
                    ScenarioExceptionnelAppareilPopUp scenarioExceptionnelAppareilPopUp = new ScenarioExceptionnelAppareilPopUp(context, texte, situation);
                    scenarioExceptionnelAppareilPopUp.show();
                }


            }
        })
                .setOnCancelListener(dialog -> {
                    ((MainActivity) context).toastAnnulationSurveillancePTI();
                    ((MainActivity) context).stopGpsService();
                    ((MainActivity) context).reactivationPtiSwitch();
                    ((MainActivity) context).desactivationSurveillancePTI();

                });

        builderExplications.show();

    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    boolean isShowing() {
        return builderExplications == null;
    }

}

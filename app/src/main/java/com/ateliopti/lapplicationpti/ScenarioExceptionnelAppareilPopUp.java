package com.ateliopti.lapplicationpti;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;

import com.ateliopti.lapplicationpti.manager.AlarmeSOSManager;
import com.ateliopti.lapplicationpti.manager.SiteManager;

public class ScenarioExceptionnelAppareilPopUp {
    private Context context;
    private final String texte;
    private final String situation;

    private AlertDialog.Builder builderExplications;
    private final AlarmeSOSManager alarmeSOSManager;
    private final SiteManager siteManager;
    private final Fonctions fonctions;

    public ScenarioExceptionnelAppareilPopUp(Context context, String texte, String situation) {
        this.context = context;
        this.texte = texte;
        this.situation = situation;
        fonctions = new Fonctions(context);

        alarmeSOSManager = new AlarmeSOSManager(context);
        siteManager = new SiteManager(context);

    }

    public void show() {

        final String[] choix = {"Téléphone fixe", "Téléphone GSM"};

        builderExplications = new android.app.AlertDialog.Builder(context);


        final LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        TextView tv1 = new TextView(context);
        tv1.setTextColor(Color.BLACK);

        tv1.setText(Html.fromHtml("<b>Le contact exceptionnel est équipé d'un …</b>"));

        if(((MainActivity) context).getTypeEcran()){// écran rond
            tv1.setPadding(50, 0, 50, 0);
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f);

        }else{
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);

        }

        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.bottomMargin = 5;
        tv1Params.leftMargin = 5;
        tv1Params.rightMargin = 5;

        layout.addView(tv1, tv1Params);

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
        } catch (Exception ignored) {

        }

        builderExplications.setView(layout);

        builderExplications.setPositiveButton("Valider", (dialog, whichButton) -> {
            dialog.dismiss();

            int selectedPosition = lvItems.getCheckedItemPosition();

            ScenarioExceptionnelDestinationPopUp scenarioExceptionnelDestinationPopUp = new ScenarioExceptionnelDestinationPopUp(context, texte, situation);
            scenarioExceptionnelDestinationPopUp.show(choix[selectedPosition]);

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

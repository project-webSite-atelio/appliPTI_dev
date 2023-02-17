package com.ateliopti.lapplicationpti;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ateliopti.lapplicationpti.manager.AlarmeSOSManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.ateliopti.lapplicationpti.manager.SiteManager;
import com.ateliopti.lapplicationpti.hbb20.CountryCodePicker;

public class ScenarioExceptionnelDestinationPopUp {

    private Context context;

    private final String texte;
    private final String situation;

    private AlertDialog.Builder builderExplications;

    private final AlarmeSOSManager alarmeSOSManager;
    private final SiteManager siteManager;
    private final OptionsManager optionsManager;


    private final Fonctions fonctions;

    private EditText detailEdit;

    ScenarioExceptionnelDestinationPopUp(Context context, String texte, String situation) {
        this.context = context;
        this.texte = texte;
        this.situation = situation;
        fonctions = new Fonctions(context);

        alarmeSOSManager = new AlarmeSOSManager(context);
        siteManager = new SiteManager(context);
        optionsManager = new OptionsManager(context);


    }

    @SuppressLint("InflateParams")
    void show(final String choix) {

        optionsManager.open();

        builderExplications = new android.app.AlertDialog.Builder(context);

        builderExplications = new AlertDialog.Builder(context);

        final RelativeLayout layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        siteManager.open();
        final String texte = "<b>Renseigner ci-dessous …</b>";

        TextView tv1 = new TextView(context);
        tv1.setTextColor(Color.BLACK);

        if(((MainActivity) context).getTypeEcran()){ // template ronde
            tv1.setPadding(50, 0, 50, 0);
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f);
        }else{
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
        }


       // tv1.setText(Html.fromHtml(texte));
        tv1.setText(Html.fromHtml(texte));

        RelativeLayout.LayoutParams tv1Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.bottomMargin = 5;
        tv1Params.leftMargin = 5;
        tv1Params.rightMargin = 5;

        //tv1Params.addRule(RelativeLayout.BELOW, tv1.getId());

        tv1.setId(View.generateViewId());
        layout.addView(tv1);//, tv1Params);


        View content;
        if (choix.equals("Téléphone GSM")) {

            if(((MainActivity) context).getTypeEcran()){ // template ronde

                content = ((MainActivity) context).getLayoutInflater().inflate(R.layout.scenario_exceptionnel_tel_gsm_22, null);

            }else{

                content = ((MainActivity) context).getLayoutInflater().inflate(R.layout.scenario_exceptionnel_tel_gsm, null);

            }


        } else {

            if(((MainActivity) context).getTypeEcran()){ // template ronde
                content = ((MainActivity) context).getLayoutInflater().inflate(R.layout.scenario_exceptionnel_tel_fixe_22 ,null);

            }else{
                content = ((MainActivity) context).getLayoutInflater().inflate(R.layout.scenario_exceptionnel_tel_fixe, null);

            }


            detailEdit = content.findViewById(R.id.detailEdit);

        }

        RelativeLayout.LayoutParams recParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        recParams.bottomMargin = 5;
        recParams.leftMargin = 5;
        recParams.rightMargin = 5;

        recParams.addRule(RelativeLayout.BELOW, tv1.getId());


        content.setId(View.generateViewId());
        layout.addView(content, recParams);
        builderExplications.setView(layout);



        final CountryCodePicker ccp1;
        ccp1 = content.findViewById(R.id.contact1);
        final EditText destinataireEdit = content.findViewById(R.id.contact_numero1);
        ccp1.registerCarrierNumberEditText(destinataireEdit);


        builderExplications.setPositiveButton("Valider", (dialog, whichButton) -> {

            if (choix.equals("Téléphone GSM")) {

                if (destinataireEdit.getText().toString().isEmpty()) {
                    show(choix);
                } else {

                    String numero = ccp1.getFullNumber();
                    ((MainActivity) context).definirScenarioExceptionnel(numero, "");
                    ((MainActivity) context).routing();

                }

            } else if (choix.equals("Téléphone fixe")) {

                if (destinataireEdit.getText().toString().equals("") || detailEdit.getText().toString().equals("") || !Fonctions.isValid(detailEdit.getText().toString())) {
                    show(choix);
                } else {
                    String numero = ccp1.getFullNumber();
                    ((MainActivity) context).definirScenarioExceptionnel(numero, detailEdit.getText().toString());
                    ((MainActivity) context).routing();

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

/*
    boolean isShowing() {
        // return builderExplications.isShowing();
        return builderExplications != null;

    }
*/

}

package com.ateliopti.lapplicationpti.popup;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;

import com.ateliopti.lapplicationpti.MainActivity;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ChoixDureeIzsrPopUp {

    OptionsManager optionsManager;

    EditText input;
    Context context;

    public ChoixDureeIzsrPopUp(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        optionsManager = ((MainActivity) context).optionsManager;

        input = new TextInputEditText(context);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setView(input);

        builder.setTitle("Choisir la durée (entre 1 et 60 minutes)")
                .setPositiveButton("Valider", null)
                .setOnCancelListener(dialog -> {

                    ((MainActivity) context).toastAnnulationSurveillancePTI();
                    ((MainActivity) context).reactivationPtiSwitch();
                    ((MainActivity) context).desactivationSurveillancePTI();
                });


        ((MainActivity) context).choisirDureeIzsr = builder.create();


        ((MainActivity) context).choisirDureeIzsr.setOnShowListener(dialog -> {

            final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            defaultButton.setOnClickListener(view -> {


                if (!input.getText().toString().equals("")) {

                    int valeur = Integer.parseInt(input.getText().toString());
                    if (valeur >= 1 && valeur <= 60) {
                        ((MainActivity) context).choisirDureeIzsr.dismiss();
                        ((MainActivity) context).choisirDureeIzsr = null;

                        ((MainActivity) context).dureeIZSR = valeur;

                        this.optionsManager.open();
                        if(this.optionsManager.getOptions().isaInterfaceAlternative()) { // Interface Alternative
                            ((MainActivity) context).seLocaliserPopUp();
                        }else{
                            ((MainActivity) context).choisirSite("IZSR");
                        }




                    } else {

                        try {
                            ((MainActivity) context).choisirDureeIzsr.dismiss();
                        } catch (Exception ignored) {

                        }
                        ((MainActivity) context).choisirDureeIzsr();
                    }
                } else {

                    try {
                        ((MainActivity) context).choisirDureeIzsr.dismiss();
                    } catch (Exception ignored) {

                    }
                    ((MainActivity) context).choisirDureeIzsr();
                }

            });


        });


        try {
            ((MainActivity) context).choisirDureeIzsr.show();
        } catch (Exception ignored) {

        }

    }


}

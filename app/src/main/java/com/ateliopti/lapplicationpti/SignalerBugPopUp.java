package com.ateliopti.lapplicationpti;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignalerBugPopUp extends AppCompatActivity {
    private Context context;

    SignalerBugPopUp(Context context) {
        this.context = context;
    }


    void show() {

        Dialog builderSignalerBug = new Dialog(context);
        builderSignalerBug.setCancelable(false);
        builderSignalerBug.setContentView(R.layout.assistance_technique);
        builderSignalerBug.getWindow().setBackgroundDrawableResource(R.drawable.rounded_window);


        TextView tv1 = builderSignalerBug.findViewById(R.id.tv1);
        tv1.setTextColor(Color.RED);
        tv1.setVisibility(View.GONE);

        Button BtnAnnuler= builderSignalerBug.findViewById(R.id.btn_annuler);
        Button BtnValider= builderSignalerBug.findViewById(R.id.btn_valider);
        EditText numeroContact = builderSignalerBug.findViewById(R.id.numeroContact);
        EditText email = builderSignalerBug.findViewById(R.id.email);
        EditText bugMessage = builderSignalerBug.findViewById(R.id.bugMessage);

        BtnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builderSignalerBug.dismiss();
            }
        });

        BtnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Patience, accusé de réception en cours …", Toast.LENGTH_SHORT).show();
                builderSignalerBug.dismiss();
            }
        });

        builderSignalerBug.show();

        BtnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("TEST-GOOGLE".equals(bugMessage.getText().toString().trim())){

                   Toast.makeText(context, "Activation OK", Toast.LENGTH_SHORT).show();

                    ((MainActivity) context).modifierImeiGoogleEssai();


                    builderSignalerBug.dismiss();

                    return;

                }else if ("".equals(numeroContact.getText().toString().trim()) || "".equals(bugMessage.getText().toString().trim()) || "".equals(email.getText().toString().trim())) {
                    //this will stop your dialog from closing

                    tv1.setVisibility(View.VISIBLE);
                    tv1.setTextColor(Color.RED);
                    tv1.setText("Veuillez remplir les champs");
                    tv1.setError("Veuillez remplir les champs");
                    return;

                } else if (!isEmailValid(email.getText().toString())) {
                    tv1.setVisibility(View.VISIBLE);
                    tv1.setTextColor(Color.RED);
                    tv1.setText("Veuillez saisir une adresse e-mail valide");
                    tv1.setError("Veuillez saisir une adresse e-mail valide");
                    return;


                } else {
                    ((MainActivity) context).signalerBug(numeroContact.getText().toString(), email.getText().toString(), bugMessage.getText().toString());
                    Toast.makeText(context, "Patience, accusé de réception en cours …", Toast.LENGTH_SHORT).show();

                }



                builderSignalerBug.dismiss();

            }
        });
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



    // Renvoie vrai si le format de l'email est valide
    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches(); // renvoie booléen
    }

}
package com.ateliopti.lapplicationpti;

//import androidx.appcompat.app.AlertDialog;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ateliopti.lapplicationpti.model.PageTutoriel;

public class TutoPopUp {
    private Context context;
    private AlertDialog.Builder tutoAlert;
    private final int MAX_PAGES = 3;
    public int compteur = 0;
    public TextView titre;
    public TextView contenu;
    public ImageView image;

    public LinearLayout rectangle;

    TutoPopUp(Context context) {
        this.context = context;
    }

    public PageTutoriel page1, page2, page3;

    void show() {
        // Page 1
        page1 = new PageTutoriel();
        page1.setId(1);
        page1.setResImage(R.drawable.tuto1);
        page1.setTitre("Déclarer votre Licence");
        page1.setContenu("lorem ipsum dolor sit amet, consecteur adispiscing elit. Proin dignissim  porttitor mollis. Suspendisse vitae lacus risu. Nunc cursus id risus ut sollicitudin. Vivamus tempor nibh sit");

        // Page 2
        page2 = new PageTutoriel();
        page2.setId(2);
        page2.setResImage(R.drawable.tuto2);
        page2.setTitre("Page2");
        page2.setContenu("blablafezfzefzegzgzegzbl");

        // Page 3
        page3 = new PageTutoriel();
        page3.setId(3);
        page3.setResImage(R.drawable.tuto3);
        page3.setTitre("Page3");
        page3.setContenu("dgegdfvfdfdbdf\nfsdgsd\ngsgsdg");

        tutoAlert = new AlertDialog.Builder(context);

        tutoAlert.setCancelable(true);
        tutoAlert.setTitle("Tutoriel");
        tutoAlert.setNeutralButton("Ignorer", null);
        tutoAlert.setNegativeButton("Précédent", null);
        tutoAlert.setPositiveButton("Suivant", null);

        final AlertDialog dialog = tutoAlert.create();

        // View
        dialog.setView(LayoutInflater.from(getContext()).inflate(R.layout.textelayout, null, false));
      //  dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        titre = (TextView)dialog.findViewById(R.id.titrePage);
        contenu = (TextView)dialog.findViewById(R.id.contenu);
        image = (ImageView) dialog.findViewById(R.id.image);


        dialog.show();

        rectangle = (LinearLayout) dialog.findViewById(R.id.rectangle);
        rectangle.setBackground(getDrawableWithRadius());

        compteur = 1;
        setPage(compteur, dialog);



        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Double height = displayMetrics.heightPixels * 0.93;
        Double width = displayMetrics.widthPixels * 0.97;

        dialog.getWindow().setLayout(width.intValue(), height.intValue());


        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(compteur > 1){
                    compteur--;
                    setPage(compteur, dialog);

                }

            }
        });

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(compteur < MAX_PAGES){
                    compteur++;
                    setPage(compteur, dialog);

                }
            }
        });
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    boolean isShowing() {
        return tutoAlert == null;
    }

    public void setPage(int id, AlertDialog alertDialog){
        TextView titre = alertDialog.findViewById(R.id.titrePage);
        TextView contenu = alertDialog.findViewById(R.id.contenu);
        ImageView image = alertDialog.findViewById(R.id.image);

        switch(id){
            case 1:
                titre.setText(page1.getTitre());
                contenu.setText(page1.getContenu());
                image.setImageResource(page1.getResImage());
                break;

            case 2:
                titre.setText(page2.getTitre());
                contenu.setText(page2.getContenu());
                image.setImageResource(page2.getResImage());
                break;

            case 3:
                titre.setText(page3.getTitre());
                contenu.setText(page3.getContenu());
                image.setImageResource(page3.getResImage());
                break;

        }
    }


    private Drawable getDrawableWithRadius() {

        GradientDrawable gradientDrawable   =   new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
        gradientDrawable.setColor(Color.DKGRAY);
        return gradientDrawable;
    }

}
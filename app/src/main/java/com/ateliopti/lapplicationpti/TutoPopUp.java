package com.ateliopti.lapplicationpti;

//import androidx.appcompat.app.AlertDialog;
import android.app.AlertDialog;
import android.app.Dialog;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class TutoPopUp {
    private Context context;
    private Dialog tutoAlert;
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

        tutoAlert = new Dialog(context);
        tutoAlert.setCancelable(true);
        tutoAlert.setContentView(R.layout.tuto);
        tutoAlert.getWindow().setBackgroundDrawableResource(R.drawable.rounded_window);


        titre = tutoAlert.findViewById(R.id.titrePage);
        contenu = tutoAlert.findViewById(R.id.contenu);
        image = tutoAlert.findViewById(R.id.image);


        tutoAlert.show();

    }

}
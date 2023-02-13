/*

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ateliopti.lapplicationpti.R;

class Tuto {
    private Context context;
    private AlertDialog.Builder tutoAlert;

    private final int MAX_PAGES = 3;

    public int compteur = 0;

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
        page3.setContenu("dgegdfvfdfdbdf\nfsdgsd\ngsgsdg\nfsfsdf\nfsegfsg\nbdndfdfgdf\nfdsgfdsgs");

        tutoAlert = new AlertDialog.Builder(context);

        tutoAlert.setCancelable(true);

        final LinearLayout layoutMain = new LinearLayout(context);

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);



        layoutMain.setLayoutParams(parms);
        layoutMain.setBackgroundColor(Color.RED);

        layoutMain.setOrientation(LinearLayout.VERTICAL);
        layoutMain.setGravity(Gravity.CLIP_VERTICAL);

        final TextView titreMain = new TextView(context);
        titreMain.setText("Tutoriel");
        titreMain.setTextSize(24);
        titreMain.setTypeface(null, Typeface.BOLD);
        titreMain.setTextColor(Color.BLACK);
        titreMain.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams paramsTitreMain = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsTitreMain.setMargins(0, 25, 0, 35);
        titreMain.setLayoutParams(paramsTitreMain);

        final ScrollView scroll = new ScrollView(context);
        scroll.setPadding(0,0,0,0);

        final LinearLayout layoutMini = new LinearLayout(context);
        LinearLayout.LayoutParams parmsMini = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parmsMini.setMargins(15,0,15,0);
        layoutMini.setOrientation(LinearLayout.VERTICAL);
        layoutMini.setLayoutParams(parmsMini);
        layoutMini.setGravity(Gravity.CLIP_VERTICAL);

        final LinearLayout rectangle = new LinearLayout(context);
        LinearLayout.LayoutParams parmsR = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rectangle.setOrientation(LinearLayout.VERTICAL);
        rectangle.setLayoutParams(parmsR);
        rectangle.setGravity(Gravity.CLIP_VERTICAL);
        rectangle.setPadding(15, 1, 15, 1);
        rectangle.setBackground(getDrawableWithRadius());


        final ImageView image = new ImageView(context);
        image.setImageResource(page1.getResImage());
        image.setAdjustViewBounds(true);
        image.setMaxHeight(550);


        final TextView titrePage = new TextView(context);
        titrePage.setText(page1.getTitre());
        titrePage.setTextSize(24);
        titrePage.setTypeface(null, Typeface.BOLD);
        titrePage.setTextColor(Color.BLACK);
        titrePage.setGravity(Gravity.CENTER);
        //  titrePage.setPadding(0, 15, 0, 15);
        LinearLayout.LayoutParams paramsTitrePage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsTitrePage.setMargins(0, 35, 0, 45);
        titrePage.setLayoutParams(paramsTitrePage);





        final TextView contenu = new TextView(context);
        contenu.setText(page1.getContenu());
        contenu.setGravity(Gravity.CENTER);

        layoutMain.addView(titreMain);
        layoutMain.addView(scroll);
        scroll.addView(layoutMini);
        layoutMini.addView(rectangle);
        rectangle.addView(image);
        layoutMini.addView(titrePage);
        layoutMini.addView(contenu);

        compteur = 1;



        tutoAlert.setView(layoutMain);


        tutoAlert.setNeutralButton("Ignorer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();


            }
        });

        tutoAlert.setNegativeButton("Précédent", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();


            }
        });

        tutoAlert.setPositiveButton("Suivant", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        // Toast.makeText(context, "Patience, accusé de réception en cours …", Toast.LENGTH_SHORT).show();
                        // dialog.dismiss();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });


        final AlertDialog dialog = tutoAlert.create();



        dialog.show();






        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Double height = displayMetrics.heightPixels * 0.93;
        Double width = displayMetrics.widthPixels * 0.97;

        dialog.getWindow().setLayout(width.intValue(), height.intValue());


 //   int[] buttons = new int[] {AlertDialog.BUTTON_POSITIVE, AlertDialog.BUTTON_NEGATIVE,AlertDialog.BUTTON_NEUTRAL};
 //   for (int i : buttons) {
 //       Button b = null;
 //       try {
 //           b = dialog.getButton(i);
 //           b.setPadding(0, 0, 0, -100);
  ///      } catch (Exception e) {
  //          e.printStackTrace();
  //      }
 //   }


        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(compteur > 1){
                    compteur--;
                    setPage(compteur, titrePage, contenu, image);

                }



                //     dialog.dismiss();

            }
        });


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(compteur < MAX_PAGES){
                    compteur++;
                    setPage(compteur, titrePage, contenu, image);

                }



                //     dialog.dismiss();

            }
        });


        //    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBottom(0);

 //   LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); //create a new one

 //   dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setLayoutParams(layoutParams);
 //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(layoutParams);
 //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setLayoutParams(layoutParams);


 //   dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setGravity(Gravity.BOTTOM);
  //  dialog.getButton(AlertDialog.BUTTON_POSITIVE).setGravity(Gravity.BOTTOM);
 //   dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setGravity(Gravity.BOTTOM);

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

    private Drawable getDrawableWithRadius() {

        GradientDrawable gradientDrawable   =   new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
        gradientDrawable.setColor(Color.DKGRAY);
        return gradientDrawable;
    }

    public void setPage(int id, TextView titre, TextView contenu, ImageView image){

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
}


*/
package com.ateliopti.lapplicationpti;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;

import android.os.Bundle;

import android.text.Html;

import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;

import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ateliopti.lapplicationpti.helper.AppHelper;
import com.ateliopti.lapplicationpti.manager.AlarmeAMManager;
import com.ateliopti.lapplicationpti.manager.AlarmeAgressionManager;
import com.ateliopti.lapplicationpti.manager.AlarmeIZSRManager;
import com.ateliopti.lapplicationpti.manager.AlarmePVManager;
import com.ateliopti.lapplicationpti.manager.AlarmeSOSManager;
import com.ateliopti.lapplicationpti.manager.EcranManager;
import com.ateliopti.lapplicationpti.manager.EtatManager;
import com.ateliopti.lapplicationpti.manager.LicenceManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.ateliopti.lapplicationpti.manager.SiteManager;
import com.ateliopti.lapplicationpti.manager.TempLocalisationManager;
import com.ateliopti.lapplicationpti.manager.TrajetManager;
import com.ateliopti.lapplicationpti.manager.VersionManager;
import com.ateliopti.lapplicationpti.model.Ecran;

import com.ateliopti.lapplicationpti.model.TempLocalisation;
import com.ateliopti.lapplicationpti.popup.AutorisationPopUp;
import com.ateliopti.lapplicationpti.popup.ChoixDureeIzsrPopUp;
//import com.ateliopti.lapplicationpti.popup.ChoixScenarioJourNuitPopUp;
import com.ateliopti.lapplicationpti.popup.ChoixSitePopUp;
import com.ateliopti.lapplicationpti.popup.ChoixSituationPopUp;
import com.ateliopti.lapplicationpti.popup.ChoixTypeAlarmePopUp;
import com.ateliopti.lapplicationpti.popup.PrealarmePopUp;
import com.ateliopti.lapplicationpti.popup.SeLocaliserPopUp;
import com.ateliopti.lapplicationpti.receiver.ButtonReceiver;
import com.ateliopti.lapplicationpti.receiver.GpsListener;
import com.ateliopti.lapplicationpti.receiver.PowerReceiver;
import com.ateliopti.lapplicationpti.receiver.RappelUtilisationReceiver;
import com.ateliopti.lapplicationpti.receiver.TimeChangedReceiver;
import com.ateliopti.lapplicationpti.service.AbsenceMouvementService;
import com.ateliopti.lapplicationpti.service.AlwaysOnService;
import com.ateliopti.lapplicationpti.service.AnnulationParMouvementService;
import com.ateliopti.lapplicationpti.service.ChargeService;
import com.ateliopti.lapplicationpti.service.DeclenchementBluetoothService;
import com.ateliopti.lapplicationpti.service.DeclenchementEnversService;
import com.ateliopti.lapplicationpti.service.HommeMortService;
import com.ateliopti.lapplicationpti.service.IZSRService;
import com.ateliopti.lapplicationpti.service.LocalisationSonoreService;
import com.ateliopti.lapplicationpti.service.PerteVerticaliteService;
import com.ateliopti.lapplicationpti.service.PingService;
import com.ateliopti.lapplicationpti.service.PingSonoreService;
import com.ateliopti.lapplicationpti.service.PrealarmeService;
import com.ateliopti.lapplicationpti.service.RechercheGPSLoopService;
import com.ateliopti.lapplicationpti.service.RecherchePositionService;
import com.ateliopti.lapplicationpti.service.RedemarrageService;
import com.ateliopti.lapplicationpti.service.ShutDownService;
import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;




import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static android.content.Intent.FLAG_INCLUDE_STOPPED_PACKAGES;
import static com.ateliopti.lapplicationpti.Fonctions.getCurrentHour;

public class MainActivity extends AppCompatActivity implements GPSInterface {
    private static final String DUREE_NOTIF = "duree_notif";

    private static final String LIEN_GOOGLE_PLAY_STORE = "https://play.google.com/store/apps/details?id=com.ateliopti.lapplicationpti";

    private static final int TAILLE_ECRAN = 240; // Dimensions de la petite montre


    // Gestionnaires de base de données
    public AlarmeAgressionManager alarmeAgressionManager;
    public AlarmeAMManager alarmeAMManager;
    public AlarmeIZSRManager alarmeIZSRManager;
    public AlarmePVManager alarmePVManager;
    public AlarmeSOSManager alarmeSOSManager;
    public SiteManager siteManager;
    public TrajetManager trajetManager;
    public LicenceManager licenceManager;
    public OptionsManager optionsManager;
    public EtatManager etatManager;
    public TempLocalisationManager tempLocalisationManager;
    public VersionManager versionManager;
    public EcranManager ecranManager;

    ScrollView scrollView;

    // mode jour/nuit
    public TextView modeScenarioJourNuit;

    public TextView localisationText;
    public Button localisationText2;


    TextView etatVersion;

    TextView indication;

    TextView buttonSOS;
    com.google.android.material.floatingactionbutton.FloatingActionButton fabMenu;
    com.google.android.material.floatingactionbutton.FloatingActionButton tuto;
    com.google.android.material.floatingactionbutton.FloatingActionButton declarer;
    com.google.android.material.floatingactionbutton.FloatingActionButton news;
    public Fonctions fonctions;
    DebugFonctions debugFonctions;

    AsyncConfiguration asyncConfiguration;
    ToggleButton ptiSwitch;
    ImageView logo;
    ImageView logo2;

    TextView devPar;
    ImageView logoMini;
    Intent intentGoMenu;

    PostMethod pm;

    Intent pingService;

    Intent shutDownService;

    public Intent rechercheGPSLoopService;
    // Nouveau : foreground recherche gps

    Intent perteVerticaliteService;
    Intent absenceMouvementService;
    Intent hommeMortService;
    Intent declenchementEnversService;

    Intent declenchementBluetoothService;

    Intent localisationSonoreService;
    Intent redemarrageService;

    Intent chargeService;

    Intent pingSonoreService;

    Intent prealarmeService;

    public String tempSituation = "";
    public String tempAlarmeMode = "";

    public AlertDialog choisirSituation;
    public AlertDialog choisirDureeIzsr;
    public AlertDialog choisirTypeAlarme;

    AlertDialog choisirSite;

    TextView imeiInfo;

    // Centre tutoriel début
    TextView blocTutoriel;
    TextView blocTutoriel2;

    private boolean preAlarme = false;

    private boolean ptiEtat = false;

    // Désactivation par charge : pour faire un délai
    // private boolean desactivationChargeDelai = false;

    private Intent intentFinPTI;
    private Intent intentAlways;

    // Empecher l'interrupteur de trigger pour izsr
    private boolean noTrigger;

    // Pop-ups
    private PopUp popUp;
    private PrealarmePopUp prealarmePopUp;
    private boolean deconnexionPTI;
    private boolean activationParDechargePing;
    public int dureeIZSR = 1;
    private Intent intentIZSR;
    public String texteExplication;
    private boolean finActivation;

    private Handler handlerIMEI;
    private Runnable runnableIMEI;
    private int delaiIMEI;
    private Intent annulationParMouvementService;

    private PowerReceiver powerReceiver;
    private ButtonReceiver buttonReceiver;

    private TimeChangedReceiver timeChangedReceiver;

    private GpsListener gpsListener;


    // Recording
    public MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    public boolean recording = false;
    private MediaPlayer mp;

    public boolean scenarioExceptionnel;


    // Données pour le scénario exceptionnel
    private String destinataireScenarioExceptionnel = "";
    private String detailScenarioExceptionnel = "";
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 477;


    private AlarmManager alarmMgr;
    private RappelUtilisationReceiver rappelUtilisationReceiver;
    private boolean activationAutomatiquePlay;

    // Montre spéciale eblaze
    public boolean templateRonde = false;
    private AutorisationPopUp autorisations;
    private SeLocaliserPopUp seLocaliserPopUp;

    public Intent recherchePositionService;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("ATELIO-DEBUG", "Etat = onCreate");

        // Retrait de la barre de titre
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();

        // Récupération des dimensions de l'écran
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.d("ATELIO-DEBUG", "Dimensions de la montre : hauteur = " + height + " / longueur = " + width);

        boolean estCarre = width == height; // vrai si dimensions carrés
        boolean petiteMontre = (width <= TAILLE_ECRAN && height <= TAILLE_ECRAN); // Petite montre

        // Définition du template selon appareil
        if (estCarre && !petiteMontre) { // Montre Eblaze forme ronde
            setContentView(R.layout.activity_margin_main);

            templateRonde = true;
        } else {
            setContentView(R.layout.activity_main);

        }


        // Texte de localisation
        imeiInfo = findViewById(R.id.imeiInfo);
        blocTutoriel = findViewById(R.id.blocTutoriel);
        blocTutoriel2 = findViewById(R.id.blocTutoriel2);

        modeScenarioJourNuit = findViewById(R.id.modeScenarioJourNuit);
        localisationText = findViewById(R.id.localisationText);
        localisationText2 = findViewById(R.id.localisationText2);
        indication = findViewById(R.id.indication);
        scrollView = findViewById(R.id.scrollView);

        buttonSOS = findViewById(R.id.buttonSOS); // Bouton alarme volontaire
        ptiSwitch = findViewById(R.id.ptiSwitch); // Interrupteur
        etatVersion = findViewById(R.id.etatVersion);
        devPar = findViewById(R.id.devPar);
        logoMini = findViewById(R.id.logoMini);
        logo2 = findViewById(R.id.logo2);

        news = findViewById(R.id.fab3);
        tuto = findViewById(R.id.fab2);
        declarer = findViewById(R.id.fab1);
        fabMenu = findViewById(R.id.fabMenu);

        final LinearLayout newsLayout = (LinearLayout) findViewById(R.id.newsLayout);
        final LinearLayout tutoLayout = (LinearLayout) findViewById(R.id.tutoLayout);
        final LinearLayout assistanceLayout = (LinearLayout) findViewById(R.id.assistanceLayout);




        String numVersion = getResources().getString(R.string.app_version);
        String nomInterrupteur = "v" + numVersion + " " + "PTI";

        ptiSwitch.setText(nomInterrupteur);
        @SuppressLint("ResourceType") int couleurR = getResources().getInteger(R.color.couleurRouge2);
        ptiSwitch.setTextColor(couleurR);
        Drawable powerR = getResources().getDrawable(R.drawable.ic_power);
        ptiSwitch.setButtonDrawable(powerR);

        // Masquage
        etatVersion.setVisibility(View.INVISIBLE);
        indication.setVisibility(View.INVISIBLE);

        newsLayout.setVisibility(View.GONE);
        tutoLayout.setVisibility(View.GONE);
        assistanceLayout.setVisibility(View.GONE);

        logo = findViewById(R.id.logo);  // Logo

        // Sur ce modèle, il faut réduire la taille du texte car elle prend la moitié de l'écran
        if (petiteMontre) {
            indication.setTextSize(TypedValue.COMPLEX_UNIT_PX, 16f);
        }


        fonctions = new Fonctions(MainActivity.this);
        debugFonctions = new DebugFonctions(MainActivity.this);

        asyncConfiguration = new AsyncConfiguration(MainActivity.this);
        asyncConfiguration.defineVersion(MainActivity.this);

        // Manager des classes
        licenceManager = new LicenceManager(this);
        optionsManager = new OptionsManager(MainActivity.this);
        alarmeAgressionManager = new AlarmeAgressionManager(MainActivity.this);
        alarmeAMManager = new AlarmeAMManager(MainActivity.this);
        alarmeIZSRManager = new AlarmeIZSRManager(MainActivity.this);
        alarmePVManager = new AlarmePVManager(MainActivity.this);
        alarmeSOSManager = new AlarmeSOSManager(MainActivity.this);
        siteManager = new SiteManager(MainActivity.this);
        trajetManager = new TrajetManager(MainActivity.this);
        tempLocalisationManager = new TempLocalisationManager(MainActivity.this);
        etatManager = new EtatManager(MainActivity.this);
        versionManager = new VersionManager(MainActivity.this);

        mp = new MediaPlayer(); // lecteur multimedia

        if (android.os.Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {   //Android M Or Over
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        autorisations = new AutorisationPopUp(MainActivity.this);

        // Actions mMessageReceiver
        IntentFilter intentFilter = new IntentFilter();
        String[] intentActions = getResources().getStringArray(R.array.intent_actions);
        // Récupération des actions sur intent_actions [arrays.xml]
        for (String s : intentActions) {
            intentFilter.addAction(s);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(intentFilter));


        registerReceiver(receiverApplicationSos, new IntentFilter("SOS_APP_ALERT"));


        registerReceiver(receiverAppAlert, new IntentFilter("APP_ALERT")); // RugGear

        registerReceiver(receiverPowerAlert, new IntentFilter("POWER_ALERT"));


        rappelUtilisationReceiver = new RappelUtilisationReceiver();

        IntentFilter rappelUtilisationFilter = new IntentFilter();
        rappelUtilisationFilter.addAction("android.intent.action.RAPPEL_UTILISATION_RECEIVER");
        rappelUtilisationFilter.addAction("android.intent.action.RAPPEL_NOTIFICATION");
        registerReceiver(rappelUtilisationReceiver, new IntentFilter(rappelUtilisationFilter));

        IntentFilter powerFilter = new IntentFilter();

        powerFilter.addAction(Intent.ACTION_USER_PRESENT);
        powerFilter.addAction(Intent.ACTION_SCREEN_ON);
        powerFilter.addAction(Intent.ACTION_SCREEN_OFF);


        powerReceiver = new PowerReceiver();
        registerReceiver(powerReceiver, powerFilter);

        IntentFilter buttonFilter = new IntentFilter();
        buttonFilter.addAction("com.kodiak.intent.action.KEYCODE_SOS");
        buttonFilter.addAction("android.intent.action.SOS_BUTTON");
        buttonFilter.addAction("android.intent.action.SOS.down");

        buttonReceiver = new ButtonReceiver();
        registerReceiver(buttonReceiver, buttonFilter);

        IntentFilter timeChangedFilter = new IntentFilter();
        timeChangedFilter.addAction(Intent.ACTION_TIME_TICK);
        timeChangedFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        timeChangedFilter.addAction(Intent.ACTION_TIME_CHANGED);

        timeChangedReceiver = new TimeChangedReceiver();
        registerReceiver(timeChangedReceiver, timeChangedFilter);

        IntentFilter gpsFilter = new IntentFilter("android.location.PROVIDERS_CHANGED");
        gpsListener = new GpsListener(MainActivity.this, this);
        registerReceiver(gpsListener, gpsFilter);

        openManager();

        // Rappel d'utilsiation
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, RappelUtilisationReceiver.class);
        intent.addFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction("android.intent.action.RAPPEL_NOTIFICATION");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());

        optionsManager.open();
        if (optionsManager.getOptions() != null) {
            if (optionsManager.getOptions().isaRappelUtilisation()) {
                if (optionsManager.getOptions().getDureeRappelUtilisation() > 0) {


                    if (alarmMgr != null) {
                        optionsManager.open();
                        int heure = optionsManager.getOptions().getDureeRappelUtilisation();

                        if (heure > 0 && optionsManager.getOptions().isaRappelUtilisation()) {


                            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis() + 1000L * heure * 60 * 60,
                                    1000L * heure * 60 * 60, pendingIntent);


                        }
                    }

                }

            }

        } else {
            alarmMgr.cancel(pendingIntent);


        }


        try {
            licenceManager.open();
            if (licenceManager.exists()) {
                debugFonctions.getData();
            } else {
                Log.d("ATELIO-DEBUG", "Pas de configuration trouvée");
            }

        } catch (Exception ignored) {

        }



        pingService = new Intent(this, PingService.class);

        shutDownService = new Intent(this, ShutDownService.class);

        rechercheGPSLoopService = new Intent(this, RechercheGPSLoopService.class);
        redemarrageService = new Intent(MainActivity.this, RedemarrageService.class);
        intentGoMenu = new Intent(this, MainActivity.class);

        declenchementEnversService = new Intent(this, DeclenchementEnversService.class);
        absenceMouvementService = new Intent(this, AbsenceMouvementService.class);
        perteVerticaliteService = new Intent(this, PerteVerticaliteService.class);
        hommeMortService = new Intent(this, HommeMortService.class);
        declenchementBluetoothService = new Intent(this, DeclenchementBluetoothService.class);

        intentIZSR = new Intent(this, IZSRService.class);

        localisationSonoreService = new Intent(this, LocalisationSonoreService.class);

        intentAlways = new Intent(this, AlwaysOnService.class);

        chargeService = new Intent(this, ChargeService.class);

        pingSonoreService = new Intent(this, PingSonoreService.class);

        annulationParMouvementService = new Intent(this, AnnulationParMouvementService.class);
        prealarmeService = new Intent(this, PrealarmeService.class);

        recherchePositionService = new Intent(this, RecherchePositionService.class);

        //    messageTLG("Ancienne version");


        demarrerServiceAlways("AFFICHER"); // On vérifie si le service always doit être

        try {
            stopService(localisationSonoreService);
        } catch (Exception ignored) {

        }

        try {
            stopService(pingSonoreService);
            servicePingSonore(false);
            fonctions.jouerBuzzerPing(false);

        } catch (Exception ignored) {

        }

        modeScenarioJourNuit.setVisibility(View.GONE);
        localisationText.setVisibility(View.INVISIBLE);
        localisationText2.setVisibility(View.INVISIBLE);
        indication.setVisibility(View.INVISIBLE);

        logo.setVisibility(View.GONE);
        buttonSOS.setVisibility(View.GONE);

        devPar.setVisibility(View.GONE);
        logoMini.setVisibility(View.GONE);

        declarer.setVisibility(View.VISIBLE);
        tuto.setVisibility(View.VISIBLE);


        popUp = new PopUp("", "", MainActivity.this);
        prealarmePopUp = new PrealarmePopUp(MainActivity.this, "", "");

        // Titre avec le numéro IMEI


        // Vérification des permissions (IMEI)
        handlerIMEI = new Handler();

        delaiIMEI = 1000; //Delay for 15 seconds.  One second = 1000 milliseconds.


        //start handler as activity become visible

        handlerIMEI.postDelayed(runnableIMEI = () -> {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // fonctions.jouerSon("warning");
                System.out.println("pas de permission");

                handlerIMEI.postDelayed(runnableIMEI, delaiIMEI);
            } else {
                System.out.println("permission imei");

                licenceTitre();


                handlerIMEI.removeCallbacksAndMessages(runnableIMEI);

            }

        }, delaiIMEI);


        licenceTitre();


        // Changement de thème
        theme();


        if (!ptiEtat) {
            stopGpsService();
            stopPingService();
        }


        Intent thisIntent = getIntent();


        if (thisIntent.getStringExtra("methodName") != null) {
            if (thisIntent.getStringExtra("methodName").equals("myMethod")) {
                etatManager.open();
                etatManager.updateFinCharge(false);
                actionDisconnected();
            }
        }

        if (thisIntent.getStringExtra("user_action") != null) {
            if (thisIntent.getStringExtra("user_action").equals("kill")) {


                resetRappelUtilisation();

                System.out.println("@FIN appli : ping");


                fonctions.vibrer(false);

                stopServicesDetection();

                etatManager.open();

                // Pas de configuration existante
                if (etatManager.getEtat() == null) {

                    stopService(redemarrageService);
                    stopService(shutDownService);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAndRemoveTask();
                    } else {
                        finishAffinity();
                    }


                } else {


                    if (etatManager.getEtat().isActivation()) {

                        retourAuPremierPlan();

                        intentFinPTI = new Intent("fin_pti");

                        new Handler().postDelayed(() -> {
                            LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intentFinPTI);
                            LocalBroadcastManager.getInstance(popUp.getContext()).sendBroadcast(intentFinPTI);
                            LocalBroadcastManager.getInstance(prealarmePopUp.getContext()).sendBroadcast(intentFinPTI);

                        }, 200);


                        new Handler().postDelayed(() -> {

                            messageTLG("PTI Désactivation");

                            checkAlways();

                            fonctions.jouerSon("warning");

                            etatManager.open();
                            etatManager.updateEtat(false);

                            popUp.resetTitre();
                            popUp.setMessage("Application tuée = désactivation de la surveillance PTI");

                            popUp.afficherFenetre("lock");

                            stopService(redemarrageService);
                            stopService(shutDownService);

                        }, 400);


                    } else { // Pas activé
                        stopService(redemarrageService);
                        stopService(shutDownService);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            this.finishAndRemoveTask();
                        } else {
                            this.finishAffinity();
                        }

                    }
                }
            }
        }

        // Appui long sur le logo, et bouton SOS déclenche une alarme volontaire
        logo.setOnLongClickListener(v -> {
            retourAuPremierPlan();


            if (tempSituation.equals("En zone sans réseau")) {
                desactiverIzsr(false);
            } else {
                prealarme("SOS");
            }

            return true;
        });

        buttonSOS.setOnLongClickListener(v -> {
            retourAuPremierPlan();

            if (tempSituation.equals("En zone sans réseau")) {
                desactiverIzsr(false);
            } else {

                prealarme("SOS");
            }

            return true;
        });


        // "Voir localisation", scrolle tout en bas et met en évidence la localisation
        indication.setOnClickListener(view -> {

            // Scrolle vers le bas
            this.defilementEcran(ScrollView.FOCUS_DOWN);

            // Changement de couleurs
            localisationText.setTextColor(Color.BLACK);
            localisationText.setBackgroundColor(Color.YELLOW);

            // Désactivation du bouton pour éviter plusieurs appels
            indication.setEnabled(false);

            // 8 secondes de  délai
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                localisationText.setTextColor(Color.WHITE);
                localisationText.setBackgroundColor(Color.BLACK);

                // Réactivation du bouton
                indication.setEnabled(true);
            }, 8000);

        });



        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newsLayout.getVisibility()==View.VISIBLE && tutoLayout.getVisibility()==View.VISIBLE && assistanceLayout.getVisibility()==View.VISIBLE){
                    newsLayout.setVisibility(View.GONE);
                    tutoLayout.setVisibility(View.GONE);
                    assistanceLayout.setVisibility(View.GONE);
                }else{
                    newsLayout.setVisibility(View.VISIBLE);
                    tutoLayout.setVisibility(View.VISIBLE);
                    assistanceLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // Bouton déclarer
        declarer.setOnClickListener(view -> {
            licenceManager.open();
            // Si il n'y a pas encore de configuration, l'utilisateur peut déclarer sa licence
            // Dans l'autre cas, il peut signaler les éventuels bugs
            SignalerBugPopUp popUp = new SignalerBugPopUp(MainActivity.this);
            popUp.show();

        });

        // Bouton tuto
        tuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentionTuto = new Intent(MainActivity.this, Tuto.class);
                startActivity(intentionTuto);
            }
        });

        localisationText2.setOnClickListener(v -> {

            if (ptiEtat && tempSituation.equals("En intérieur")) {
                siteManager.open();
                optionsManager.open();

                if (!(siteManager.count() <= 1 && !optionsManager.getOptions().isLocalisationAudio())) {
                    choisirSite("changement");
                }


            } else if (ptiEtat && (tempSituation.equals("En extérieur") || tempSituation.equals("En zone sans réseau"))) {
                tempLocalisationManager.open();

                if (!tempLocalisationManager.getTempLocalisation().getLatitude().equals("") && !tempLocalisationManager.getTempLocalisation().getLongitude().equals("")) {
                    voirPlan();
                }
                if (!(siteManager.count() <= 1 && !optionsManager.getOptions().isLocalisationAudio())) {
                    choisirSite("changement");
                }

            }

        });

        // Titre : imei, affiche l'imei
        imeiInfo.setOnClickListener(view -> {
            try {
                // Le popup s'affiche si la surveillance n'est pas activée
                if (!ptiEtat) {
                    popUp.resetTitre();
                    popUp.afficherFenetre("imei");
                }

            } catch (Exception ignored) {

            }
        });


        etatVersion.setOnClickListener(v -> {
            versionManager.open();
            // Redirection vers le Google Play Store si la version est ancienne
            if (versionManager.getVersion().isObsolete()) {
                Uri lien = Uri.parse(LIEN_GOOGLE_PLAY_STORE);
                Intent intentGooglePlayStore = new Intent(Intent.ACTION_VIEW, lien);
                startActivity(intentGooglePlayStore);
            }
        });


        localisationText.setOnClickListener(v -> {


            tempLocalisationManager.open();
            if (tempLocalisationManager.getTempLocalisation().getSite().equals("Localisation vocale")) {

                Uri myUri1 = null;

                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                am.setStreamVolume(AudioManager.STREAM_ALARM, 100, 0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    File root = new File(getExternalFilesDir(null), "rec");
                    //File root = new File(getExternalFilesDir(null), "rec");
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File file = new File(root, "rec.mp4");
                    file.setReadable(true);
                    file.setWritable(true);

                    mRecorder = new MediaRecorder();

                    mRecorder.setOutputFile(file);


                    myUri1 = Uri.fromFile(file);

                } else {


                    String fileName = "AppliPTI/rec.mp4";
                    String completePath = Environment.getExternalStorageDirectory() + "/" + fileName;

                    System.out.println(completePath);

                    File file = new File(completePath);

                    myUri1 = Uri.fromFile(file);
                }

                mp.reset();

                mp = new MediaPlayer();


                mp.setAudioStreamType(AudioManager.STREAM_ALARM);
                try {
                    mp.setDataSource(MainActivity.this, myUri1);
                } catch (Exception ignored) {

                }

                try {
                    mp.prepare();
                } catch (Exception ignored) {

                }

                try {
                    mp.start();
                } catch (IllegalStateException ignored) {
                    ignored.printStackTrace();
                }

            }


        });


        ptiSwitch.setOnTouchListener((view, motionEvent) -> {
            //      isTouched = true;
            return false;
        });

        ptiSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            boolean permission = true;

            optionsManager.open();
            boolean activationAutomatique = false;
            try {
                activationAutomatique = optionsManager.getOptions().isActivationPTIAutomatique();
            } catch (Exception ignored) {
            }

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("ATELIO-DEBUG", "Permission 'ACCESS_COARSE_LOCATION' refusée");
                permission = false;
            } else if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("ATELIO-DEBUG", "Permission 'ACCESS_FINE_LOCATION' refusée");
                permission = false;
            } else if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("ATELIO-DEBUG", "Permission 'READ_PHONE_STATE' refusée");
                permission = false;
            } else if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                Log.d("ATELIO-DEBUG", "Permission 'RECORD_AUDIO' refusée");
                permission = false;
            } else if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("ATELIO-DEBUG", "Permission 'WRITE_EXTERNAL_STORAGE' refusée");
                permission = false;
            } else if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("ATELIO-DEBUG", "Permission 'READ_EXTERNAL_STORAGE' refusée");
                permission = false;

            }
            if (permission || activationAutomatique) {

                if (!noTrigger) {

                    ptiSwitchSetClickable(false);

                    // Annuler une préalarme


                    if (tempSituation.equals("En zone sans réseau")) {
                        desactiverIzsr(false);
                    } else {
                        intentFinPTI = new Intent("fin_pti");
                        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intentFinPTI);
                        LocalBroadcastManager.getInstance(popUp.getContext()).sendBroadcast(intentFinPTI);
                        LocalBroadcastManager.getInstance(prealarmePopUp.getContext()).sendBroadcast(intentFinPTI);


                        // Si l'interrupteur est vert
                        if (isChecked) {

                            ptiSwitch.setTextOn(nomInterrupteur);
                            @SuppressLint("ResourceType") int couleurV = getResources().getInteger(R.color.couleurVert);
                            ptiSwitch.setTextColor(couleurV);
                            Drawable powerV = getResources().getDrawable(R.drawable.ic_power2);
                            ptiSwitch.setButtonDrawable(powerV);

                            // TODO : disconnect same
                            fonctions.viderTempLocalisation();

                            declarer.setVisibility(View.GONE);
                            tuto.setVisibility(View.GONE);
                            fabMenu.setVisibility(View.GONE);

                            devPar.setVisibility(View.VISIBLE);
                            logoMini.setVisibility(View.VISIBLE);

                            licenceManager.open();
                            if (!licenceManager.exists()) { // Pas de licence, affichage du toast
                                Toast.makeText(MainActivity.this, "Activation Licence PTI", Toast.LENGTH_SHORT).show();
                            }

                            asyncConfiguration.run();

                            // Empeche l'utilisation de l'interrupteur
                            ptiSwitchSetClickable(false); // bon


                            // Désactivation PTI par switch et par l'utilisateur
                        } else {

                            devPar.setVisibility(View.GONE);
                            logoMini.setVisibility(View.GONE);

                            ptiSwitch.setTextOff(nomInterrupteur);
                            ptiSwitch.setTextColor(couleurR);
                            ptiSwitch.setButtonDrawable(powerR);

                            if (ptiEtat) {
                                messageTLG("PTI Désactivation");

                                // Notation
                                optionsManager.open();
                                if (optionsManager.getOptions().isaNotation()) {
                                    versionManager.open();

                                    if (versionManager.getVersion().isRappelNotation()) {
                                        fonctions.createNotificationNotation();
                                    }
                                }

                                new Handler().postDelayed(this::desactivationSurveillancePTI, 50);

                                new Handler().postDelayed(() -> {
                                    popUp.resetTitre();
                                    popUp.setMessage("Désactivation de la surveillance PTI");
                                    popUp.afficherFenetre("lock+patience");
                                }, 100);
                            }
                        }
                    }
                }
            } else {
                ptiSwitchSetChecked(false);

                fonctions.jouerSon("warning");
                popUp.resetTitre();

                popUp.afficherFenetre("autorisation");


            }

        });

    }

    @SuppressLint("WrongConstant")
    public void resetRappelUtilisation() {

        System.out.println("STEP alarm 1");
        optionsManager.open();

        try {

            alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(this, RappelUtilisationReceiver.class);
            intent.setAction("android.intent.action.RAPPEL_NOTIFICATION");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    MainActivity.this, 1, intent,
                    PendingIntent.FLAG_IMMUTABLE);

            alarmMgr.cancel(pendingIntent);
            intent.addFlags(FLAG_INCLUDE_STOPPED_PACKAGES);


            Intent intentReset = new Intent();


            intentReset.addFlags(0x01000000); // Nécessaire, FLAG_ACTIVITY_PREVIOUS_IS_TOP

            System.out.println("RAPPEL_UTILISATION_RECEIVER covered init");

            intentReset.setAction("android.intent.action.RAPPEL_UTILISATION_RECEIVER");

            MainActivity.this.sendBroadcast(intentReset);
        } catch (Exception ignored) {

        }
    }

    // Le service doit être appelé en tant que foreground à partir de la version O d'Android
    public void demarrerService(Intent service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, service);
        } else {
            startService(service);
        }
    }

    // Vérification du ping pour désactiver l'izsr
    public void desactiverIzsr(final boolean miseEnCharge) {

        // Ping réussi
        if (fonctions.verificationPing()) {
            desactivationSurveillancePTI();
            ptiSwitchSetClickable(false); // Empecher le réappui car fin de surveillance

            stopService(intentIZSR);


            final Handler handler0 = new Handler();
            handler0.postDelayed(() -> {
                // Correction AVRIL20
                alarmeIZSRManager.open();
                tempLocalisationManager.open();
                String finInterventionMessage = "Fin IZSR;" + dureeIZSR + "/" + alarmeIZSRManager.getAlarmeIZSR().getIzsrDureeNotification() +
                        ";" + tempLocalisationManager.getTempLocalisation().getSite() + " (T)";
                messageTLG(finInterventionMessage);

            }, 100);

            final Handler handler = new Handler();
            handler.postDelayed(() -> {


                popUp.resetTitre();


                if (miseEnCharge) {
                    fonctions.jouerSon("warning");
                    popUp.setMessage("Mise en charge de l'appareil = désactivation de la surveillance PTI");
                } else {
                    popUp.setMessage("Désactivation de la surveillance PTI");
                }
                popUp.afficherFenetre("lock+patience");
            }, 600);

            // Aucun réseau internet, on affiche le message + pas de fin IZSR possible
        } else {

            absenceDeReseauPopUp(true);


        }

    }

    public void finIzsr() {
        stopService(intentIZSR);
        messageTLG("PTI Désactivation");

        desactivationSurveillancePTI();


        new Handler().postDelayed(() -> demarrerServiceAlways("AFFICHER"), 50);


        final Handler handler = new Handler();
        handler.postDelayed(() -> {


            popUp.resetTitre();
            popUp.setMessage("Désactivation de la surveillance PTI");
            popUp.afficherFenetre("lock+patience");
        }, 100);
    }


    // Receiver MainActivity
    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("@ATELIO INTENT " + intent.getAction() + " CALLED");

            if (intent.getAction().equals("prealarme_annulation_par_mouvement")) {
                System.out.println("@atelio annulation par mouvement préalarme terminé.");
                try {
                    prealarmePopUp.dismiss();
                    reprisePostAnnulationPrealarme();

                } catch (Exception ignored) {

                }

            }


            if (intent.getAction().equals("seLocaliserGpsFind")) {
                System.out.println("@GPS Position actualisé");

                //   double latitude = 0.0;

                double latitude = intent.getDoubleExtra("latitude", 0.0);
                double longitude = intent.getDoubleExtra("longitude", 0.0);

                try {
                    seLocaliserPopUp.setLocalisation(latitude, longitude);
                } catch (Exception ignored) {

                }


            }

            if (intent.getAction().equals("GPS_ALERT")) {
                Log.d("ATELIO-DEBUG", "broadcast");
                System.out.println("@ATELIO receiver " + tempSituation);

                if (tempSituation.equals("En extérieur")) {


                    stopService(rechercheGPSLoopService);
                    retourAuPremierPlan();

                    if (ptiEtat) {
                        messageTLG("PTI Désactivation");
                    }

                    desactivationSurveillancePTI();


                    if (choisirSituation != null) {
                        if (choisirSituation.isShowing()) {
                            System.out.println("@ATELIO choisirSituation ");

                            choisirSituation.dismiss();
                        }
                    }

                    if (choisirTypeAlarme != null) {
                        if (choisirTypeAlarme.isShowing()) {
                            System.out.println("@ATELIO choisirTypeAlarme ");

                            choisirTypeAlarme.dismiss();
                        }
                    }


                    if (choisirDureeIzsr != null) {
                        if (choisirDureeIzsr.isShowing()) {
                            System.out.println("@ATELIO choisirDureeIZSR ");

                            choisirDureeIzsr.dismiss();
                        }
                    }


                    if (!preAlarme) {


                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            stopService(redemarrageService);
                            fonctions.jouerSon("warning");


                            popUp.resetTitre();
                            popUp.setMessage("GPS désactivé = désactivation de la surveillance PTI\nActivez le GPS en mode haute précision pour pouvoir activer la surveillance PTI \"En extérieur\"");
                            popUp.afficherFenetre("gps");
                        }, 200);

                    } else {

                        intentFinPTI = new Intent("fin_pti");
                        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intentFinPTI);
                        LocalBroadcastManager.getInstance(popUp.getContext()).sendBroadcast(intentFinPTI);
                        LocalBroadcastManager.getInstance(prealarmePopUp.getContext()).sendBroadcast(intentFinPTI);


                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            stopService(redemarrageService);
                            fonctions.jouerSon("warning");

                            popUp.resetTitre();
                            popUp.setMessage("GPS désactivé = désactivation de la surveillance PTI\nActivez le GPS en mode haute précision pour pouvoir activer la surveillance PTI \"En extérieur\"");

                            popUp.afficherFenetre("gps_prealarme");
                            //ok je vais le faire grisé pas de kill avec 5 secondes, fenetre de parametre gps

                        }, 500);

                    }
                }
            }


            if (intent.getAction().equals("position_find")) {// && !preAlarme) {
                tempLocalisationManager.open();

                String locationText = "";
                String[] date = tempLocalisationManager.getTempLocalisation().getDate().split(" ");

                try {

                    double latitude;
                    latitude = Double.parseDouble(tempLocalisationManager.getTempLocalisation().getLatitude());
                    double longitude;
                    longitude = Double.parseDouble(tempLocalisationManager.getTempLocalisation().getLongitude());

                    Geocoder geoCoder = new Geocoder(MainActivity.this);

                    // Le Geo Coder n'est pas présent sur tous les appareils
                    //   if (geoCoder.isPresent()) {
                    List<Address> matches = null;
                    try {
                        matches = geoCoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (matches == null) {
                        locationText = "Le " + date[0] + " à " + date[1] + ", localisation à proximité de : Adresse inconnue";

                    } else {
                        Address bestMatch = (matches.isEmpty() ? null : matches.get(0));

                        if (bestMatch == null) {
                            locationText = "Le " + date[0] + " à " + date[1] + ", localisation à proximité de : Adresse inconnue";

                        } else {
                            locationText = "Le " + date[0] + " à " + date[1] + ", localisation à proximité de : " + bestMatch.getAddressLine(0);

                        }


                    }
                    //localisationText.setText(locationText);

                    localisationText.setText(Html.fromHtml(locationText));

                    String ligneSuperieur = "<b>Appuyez ici pour voir sur Google Maps</b>";
                    localisationText2.setText(Html.fromHtml(ligneSuperieur));
                    //    } /*else {
                } catch (Exception ignored) {

                }


            }
            if (intent.getAction().equals("declenchement_envers")) {
                retourAuPremierPlan();
                prealarme("SOS+ENVERS");
            }


            if (intent.getAction().equals("gps_permission")) {


                try {
                    choisirTypeAlarme.dismiss();

                } catch (Exception ignored) {

                }


                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    desactivationSurveillancePTI();
                    ptiSwitchSetChecked(false);
                }, 10);

                Handler handlerOff = new Handler();
                handlerOff.postDelayed(() -> ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        23), 100);


            }


            if (intent.getAction().equals("activation")) {
                actionDisconnected();
            }

            // Intention : détection absence de mouvement
            if (intent.getAction().equals("alarme_absence_mouvement")) {
                retourAuPremierPlan();
                prealarme("AM");

            }

            // Intention : détection perte de verticalité
            if (intent.getAction().equals("alarme_perte_verticalite")) {
                retourAuPremierPlan();
                prealarme("PV");

            }

            // Intention : détection alarme homme mort
            if (intent.getAction().equals("fin_compteur_homme_mort")) {
                retourAuPremierPlan();
                prealarme("Agression");

            }

            // Intention : fin de compteur Intervention en Zone sans Réseau
            if (intent.getAction().equals("finCompteurIZSR")) {
                retourAuPremierPlan();
                prealarme("IZSR");

            }

            // Intention : alarme bluetooth
            if (intent.getAction().equals("alarme_bluetooth")) {
                retourAuPremierPlan();
                prealarme("SOS");

            }


            if (intent.getAction().equals("actualiserIzsrBouton")) {
                String nouveauTemps = intent.getStringExtra("nouveauTemps");
                actualiserBouton(nouveauTemps);
            }

            if (intent.getAction().equals("MODE_JOUR")) {

                modeScenarioJourNuit = findViewById(R.id.modeScenarioJourNuit);
                String txtJour = getResources().getString(R.string.texte_mode_jour);
                modeScenarioJourNuit.setText(txtJour);

            }

            if (intent.getAction().equals("MODE_NUIT")) {
                modeScenarioJourNuit = findViewById(R.id.modeScenarioJourNuit);
                String txtNuit = getResources().getString(R.string.texte_mode_nuit);
                modeScenarioJourNuit.setText(txtNuit);
            }


            // Connexion au site impossible (panne serveur, pas de réseau)
            if (intent.getAction().equals("ping")) {

                System.out.println("@atelio ping receiver");

                final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                System.out.println("@atelio ping joué le : " + timestamp);


                Handler hndler = new Handler();
                hndler.postDelayed(() -> {

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {

                        try {
                            desactivationSurveillancePTI();

                        } catch (Exception ignored) {

                        }


                    }, 120);


                    Handler handler01 = new Handler();
                    handler01.postDelayed(() -> {


                        Handler handlerStop = new Handler();
                        handlerStop.postDelayed(() -> {

                            retourAuPremierPlan();


                            demarrerService(pingSonoreService); // Maintient l'application


                        }, 10); // 210


                        Handler handler04 = new Handler();
                        handler04.postDelayed(() -> {

                            try {

                                intentFinPTI = new Intent("fin_pti");
                                //        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intentFinPTI);
                                if (popUp.isShowing()) {
                                    LocalBroadcastManager.getInstance(popUp.getContext()).sendBroadcast(intentFinPTI);
                                }

                                if (prealarmePopUp.isShowing()) {
                                    LocalBroadcastManager.getInstance(prealarmePopUp.getContext()).sendBroadcast(intentFinPTI);
                                }

                            } catch (Exception ignored) {

                            }

                        }, 100); // 300


                        Handler handler05 = new Handler();
                        handler05.postDelayed(() -> {

                            optionsManager.open();
                            if (optionsManager.getOptions().isaIzsr()) {

                                popUp.setMessage("Absence de réseau = désactivation de la surveillance PTI.\nConseil d’utilisation :\n" +
                                        "Sortez de la zone sans réseau, puis activez la surveillance PTI, puis sélectionnez \"En zone sans réseau\"\n");

                            } else {
                                //  popUp.setMessage("Absence de réseau = désactivation de la surveillance PTI");
                                popUp.setMessage("Absence de réseau = désactivation de la surveillance PTI");
                            }

                            if (activationParDechargePing) {
                                System.out.println("@atelio PING activation par decharge ping true");
                                popUp.afficherFenetre("Charger+Ping");
                            } else {
                                System.out.println("@atelio PING activation par decharge ping false");

                                popUp.afficherFenetre("Delai_5secondes");

                            }
                            System.out.println("@atelio ping receiver C");
                        }, 300); // 500

                        System.out.println("@atelio ping receiver B");
                    }, 200);

                }, 400);

            }
        }
    };


    // PopUp : désactivation de la surveillance PTI impossible car absence de réseau
    public void absenceDeReseauPopUp(final boolean izsr) {

        etatManager.open();
        if (!tempSituation.equals("En zone sans réseau")) {
            etatManager.updateEtat(false);
        }
        Handler handler = new Handler();
        handler.postDelayed(() -> {

            if (!izsr) {
                fonctions.jouerBuzzerPing(true);
            } else {
                fonctions.jouerSon("warning");
            }

            popUp.resetTitre();
            optionsManager.open();


            if (izsr) {
                popUp.setMessage("Désactivation de la surveillance PTI impossible car absence de réseau");
            } else {
                if (optionsManager.getOptions().isaIzsr()) {
                    //   popUp.setTitre("Absence de réseau = désactivation de la surveillance PTI");
                    popUp.setMessage("Absence de réseau = désactivation de la surveillance PTI.\nConseil d’utilisation :\n" +
                            "Sortez de la zone sans réseau, puis activez la surveillance PTI, puis sélectionnez \"En zone sans réseau\"\n");

                } else {
                    //  popUp.setTitre("Absence de réseau = désactivation de la surveillance PTI");
                    popUp.setMessage("Absence de réseau = désactivation de la surveillance PTI");

                }
            }

            if (izsr) {
                popUp.afficherFenetre("izsr"); // Message car pas de réseau et tentative de désactivation du PTI
            } else {
                popUp.afficherFenetre("");
            }

            System.out.println("@atelio ping receiver A");


        }, 100);
    }


    // Utilisé pour la désactivation PTI lors d'un mode IZSR et pas de réseau
    public void boutonVert() {

        noTrigger = true;
        ptiSwitchSetChecked(true);
        noTrigger = false;
        //  ptiSwitchSetClickable(true);

    }

    // Rédemarrage service ping internet
    public void startPingService() {
        stopService(pingService);
        demarrerService(pingService);
    }


    // annulerPrealarme empeche le redémarrage des services
    // Action apres annuler l'alarme
    public void redemarrageServices(boolean annulerPrealarme) {

        try {

            System.out.println("redemarrage des services = MODE " + tempAlarmeMode);


            if (annulerPrealarme) preAlarme = false;

            //   ptiSwitchSetClickable(true);

            reactivationPtiSwitch();


            /*

            tempLocalisationManager.open();

            tempLocalisationManager.getTempLocalisation().setDate("");
            tempLocalisationManager.getTempLocalisation().setLatitude("");
            tempLocalisationManager.getTempLocalisation().setLongitude("");
            tempLocalisationManager.getTempLocalisation().setSite("");

            */


            stopService(localisationSonoreService);

            final String declenchementEnvers = "Mise tête à l'envers";

            /*
            if (!annulerPrealarme) {
                if (preAlarme && tempSituation.equals("En extérieur")) {
                    demarrerService(rechercheGPSLoopService);
                }
            }
            */


            if (annulerPrealarme && tempSituation.equals("En extérieur")) {
                Intent finRechercheGps = new Intent("annulation_prealarmeGPS");
                this.sendBroadcast(finRechercheGps);

            }


            stopServicesDetection();

            openManager();


            if (tempSituation.equals("En zone sans réseau")) {
                alarmeIZSRManager.open();

                stopService(intentIZSR);

                intentIZSR.putExtra("temps", dureeIZSR);

                demarrerService(intentIZSR);


            } else { // intérieur ou extérieur


                if (alarmeSOSManager.getAlarmeSOS().getSosDeclenchementMouvement().equals(declenchementEnvers)) {
                    demarrerService(declenchementEnversService);
                }

                if (!alarmeSOSManager.getAlarmeSOS().getSosDeclenchementBluetooth().equals("")) {
                    demarrerService(declenchementBluetoothService);
                }

                switch (tempAlarmeMode) {
                    case "AM":

                        demarrerService(absenceMouvementService);

                        break;
                    case "PV":

                        demarrerService(perteVerticaliteService);

                        break;
                    case "Agression":
                        demarrerService(hommeMortService);
                        break;


                }
            }

        } catch (Exception ignored) {

        }


    }

    // Arrête tous les services de détection
    public void stopServicesDetection() {


        stopService(intentIZSR);
        stopService(perteVerticaliteService);
        stopService(declenchementEnversService);
        stopService(absenceMouvementService);
        stopService(hommeMortService);
        stopService(declenchementBluetoothService);

    }

    // Arrête les services GPS
    public void stopGpsService() {
        System.out.println("GPS O STOPP");
        stopService(rechercheGPSLoopService);
    }

    // Arrête la vérification Internet
    public void stopPingService() {
        stopService(pingService);
    }

    // Modifie le texte "localisation"
    public void actualiserBouton(String text) {
        buttonSOS.setTextColor(Color.BLACK);
        buttonSOS.setTextSize(13);
        buttonSOS.setText(text);
    }


    // Actions exécutées si l'appareil est connecté
    public void actionConnected() {

        // Si en zone sans réseau
        if (tempSituation.equals("En zone sans réseau")) {
            //     desactivationChargeDelai = false;
            try {
                if ((prealarmePopUp.isShowing() || popUp.isShowing()) && !fonctions.verificationPing()) {
                    absenceDeReseauPopUp(true);
                }
            } catch (Exception ignored) {

            }
        }

        try {


            if (ptiEtat) { // && !desactivationChargeDelai) {

                // En mode IZSR + internet disponible
                if (tempSituation.equals("En zone sans réseau") && fonctions.verificationPing()) { // BUG


                    // On masque les fenêtres
                    new Handler().postDelayed(() -> {

                        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intentFinPTI);
                        LocalBroadcastManager.getInstance(popUp.getContext()).sendBroadcast(intentFinPTI);
                        LocalBroadcastManager.getInstance(prealarmePopUp.getContext()).sendBroadcast(intentFinPTI);

                    }, 20);


                    new Handler().postDelayed(() -> desactiverIzsr(true), 500);


                    etatManager.open();
                    etatManager.updateFinCharge(true);
                    //     asyncConfiguration.cancelRequests(MainActivity.this, true);

                    deconnexionPTI = true;

                    retourAuPremierPlan();
                    //       messageTLG("PTI Désactivation");


                    etatManager.updateEtat(false);


                } else if (tempSituation.equals("En zone sans réseau") && !fonctions.verificationPing()) { // BUG
                    absenceDeReseauPopUp(true);
                } else { // Si mode non IZSR


                    etatManager.open();
                    etatManager.updateFinCharge(true);

                    deconnexionPTI = true;

                    retourAuPremierPlan();
                    messageTLG("PTI Désactivation");


                    etatManager.updateEtat(false);


                    ///


                    finActivation = true;
                    desactivationSurveillancePTI();


                    retourAuPremierPlan();


                    stopService(redemarrageService); // Important, empeche le kill event


                    if (!preAlarme) { // Pas de préalarme actuellement


                        new Handler().postDelayed(() -> fonctions.jouerSon("warning"), 1);


                        new Handler().postDelayed(() -> {


                            optionsManager.open();

                            boolean activationAutomatique;

                            try {
                                System.out.println("@atelio " + optionsManager.getOptions().isActivationPTIAutomatique());
                                activationAutomatique = optionsManager.getOptions().isActivationPTIAutomatique();
                            } catch (Exception ignored) {
                                activationAutomatique = false;
                            }

                            /*
                            if (activationAutomatique) {
                                System.out.println("@atelio desactivation par charge avec délai");
                                desactivationChargeDelai = true;
                            }
*/

                            popUp.resetTitre();
                            popUp.setMessage("Mise en charge de l'appareil = désactivation de la surveillance PTI");
                            popUp.afficherFenetre("lock");


                        }, 2);


                    } else { // Préalarme


                        intentFinPTI = new Intent("fin_pti");

                        // Fin des pop ups
                        new Handler().postDelayed(() -> {

                            LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intentFinPTI);
                            LocalBroadcastManager.getInstance(popUp.getContext()).sendBroadcast(intentFinPTI);
                            LocalBroadcastManager.getInstance(prealarmePopUp.getContext()).sendBroadcast(intentFinPTI);


                        }, 20);


                        new Handler().postDelayed(() -> {
                            System.out.println("@atelio etape B");


                            messageTLG("PTI Désactivation");

                            fonctions.jouerSon("warning");

                            etatManager.open();
                            etatManager.updateEtat(false);

                            popUp.resetTitre();
                            popUp.setMessage("Mise en charge de l'appareil = désactivation de la surveillance PTI");

                            popUp.afficherFenetre("lock+patience");


                            stopService(redemarrageService);
                            stopService(shutDownService);

                        }, 500);


                    }


                }
            }
        } catch (Exception ignored) {

        }
    }

    // Action si l'appareil est débranchée
    public void actionDisconnected() {


        //   boolean tempDisconnected = desactivationChargeDelai;


        // if (tempSituation.equals("En zone sans réseau")) {

        //desactivationChargeDelai = false;
        //   }


        //    Toast.makeText(MainActivity.this, "desactivationVariable = " + tempDisconnected + " /" + desactivationChargeDelai, Toast.LENGTH_SHORT).show();


        //  if (!desactivationChargeDelai) {

        if (AppHelper.isAppRunning(MainActivity.this, "com.ateliopti.lapplicationpti.MainActivity")) {
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            MainActivity.this.startActivity(i);
        }


        if (!ptiEtat && !deconnexionPTI) {


            etatManager.open();
            etatManager.updateFinCharge(false);


            activationParDechargePing = true;

            etatManager.open();
            etatManager.updateEtat(true);

            ptiSwitchSetChecked(true);

            retourAuPremierPlan();

            fonctions.viderTempLocalisation();

            declarer.setVisibility(View.GONE);
            tuto.setVisibility(View.GONE);

            asyncConfiguration.run();


            activationAutomatiquePlay = true;
            ptiSwitchSetClickable(false);

        }
        //    }

    }

    // SmartWatch premier plan
    BroadcastReceiver receiverScreen = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("@ATELIO Premier plan");
            intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.this.startActivity(intentGoMenu);

            MainActivity.this.defilementEcran(ScrollView.FOCUS_UP);

        }
    };


    @SuppressLint("MissingPermission")
    public void prealarme(String type) {

        if (!preAlarme && ptiEtat) { // ajout version 12, empeche le déclenchement si surveillance

            System.out.println("maintien lors préalarme");

            // Affichage de la notification
            new Handler().postDelayed(() -> demarrerService(prealarmeService), 100);

            // Blocage du switch
            ptiSwitchSetClickable(false);

            // Préalarme en cours
            preAlarme = true;

            stopServicesDetection(); // Changement TODO

            if (type.equals("AM")) { // Absence de mouvement
                alarmeAMManager.open();
                if (alarmeAMManager.getAlarmeAM().isAmAnnulation()) {

                    int dureeNotification = alarmeAMManager.getAlarmeAM().getAmDureeNotification();

                    annulationParMouvementService.putExtra(DUREE_NOTIF, dureeNotification);
                    demarrerService(annulationParMouvementService);

                }
            }

            if (type.equals("PV")) { // Perte de verticalité

                alarmePVManager.open();
                if (alarmePVManager.getAlarmePV().isPvAnnulation()) {
                    int dureeNotification = alarmePVManager.getAlarmePV().getPvDureeNotification();

                    annulationParMouvementService.putExtra(DUREE_NOTIF, dureeNotification);
                    demarrerService(annulationParMouvementService);

                }
            }

            if (tempSituation.equals("En extérieur")) {
                trajetManager.open();
                //int dureeRecherche = trajetManager.getTrajet().getDureeRechercheGps();
                int dureeRecherche = fonctions.getDureePrealarme(type);
                Intent atelioIZSR = new Intent("prealarmeGPS");
                atelioIZSR.putExtra("dureeRecherchePrealarme", dureeRecherche - 5); // On retire 5 secondes de recherche
                this.sendBroadcast(atelioIZSR);
            }
        }

        prealarmePopUp = new PrealarmePopUp(MainActivity.this, tempSituation, type);
        prealarmePopUp.show();

    }


    public void annulerServicePrealarme() {
        stopService(prealarmeService);
    }

    public void annulerServiceAnnulationMouvement() {
        stopService(annulationParMouvementService);
    }

    public void ptiSwitchSetClickable(boolean clickable) {
        ptiSwitch.setClickable(clickable);
    }

    public void ptiSwitchSetChecked(boolean checked) {
        ptiSwitch.setChecked(checked);
    }


    public void checkAlways() {

        optionsManager.open();

        boolean activationAutomatique = false;
        boolean sollicitation = false;
        try {
            System.out.println("@atelio " + optionsManager.getOptions().isActivationPTIAutomatique());
            activationAutomatique = optionsManager.getOptions().isActivationPTIAutomatique();
        } catch (Exception ignored) {
        }

        try {
            System.out.println("@atelio " + optionsManager.getOptions().isSollicitation());
            sollicitation = optionsManager.getOptions().isSollicitation();
        } catch (Exception ignored) {
        }

        final boolean finalActivationAutomatique = activationAutomatique;
        final boolean finalSollicitation = sollicitation;
        new Handler().postDelayed(() -> {

            etatManager.open();
            if (finalActivationAutomatique || finalSollicitation || !etatManager.getEtat().getDerniereDesactivation().equals("")) {

                demarrerServiceAlways("AFFICHER");
            }
        }, 1);
    }

    // Switch Off PTI
    public void desactivationSurveillancePTI() {
        try {
            stopService(pingSonoreService);
            stopService(shutDownService);
            stopService(pingService);
            stopService(rechercheGPSLoopService);
            stopService(intentIZSR);

            stopService(chargeService);

            optionsManager.open();
            etatManager.open();

            // Empeche le crash si pas de licence
            licenceManager.open();
            if (licenceManager.exists()) {


                // checkalways() fais la meme chose


                new Handler().postDelayed(() -> {

                    optionsManager.open();

                    boolean activationAutomatique = false;
                    boolean sollicitation = false;
                    try {
                        System.out.println("@atelio " + optionsManager.getOptions().isActivationPTIAutomatique());
                        activationAutomatique = optionsManager.getOptions().isActivationPTIAutomatique();
                    } catch (Exception ignored) {
                    }

                    try {
                        System.out.println("@atelio " + optionsManager.getOptions().isSollicitation());
                        sollicitation = optionsManager.getOptions().isSollicitation();
                    } catch (Exception ignored) {
                    }

                    if (activationAutomatique || sollicitation) { // || !etatManager.getEtat().getDerniereDesactivation().equals("")) {
                        demarrerServiceAlways("AFFICHER");
                    }

                }, 50);
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finishAffinity();
                }
            }


            etatManager.open();
            etatManager.updateEtat(false);


            stopServicesDetection();

            tempSituation = tempAlarmeMode = ""; // Reset

            ptiEtat = false;
            blocTutoriel.setVisibility(View.VISIBLE);
            blocTutoriel2.setVisibility(View.VISIBLE);
            logo2.setVisibility(View.VISIBLE);
            fabMenu.setVisibility(View.VISIBLE);

            //     ptiSwitchSetClickable(true);

            ptiSwitchSetChecked(false);

            asyncConfiguration.cancelAllRequests(true);

            localisationText.setVisibility(View.INVISIBLE);
            localisationText2.setVisibility(View.INVISIBLE);
            indication.setVisibility(View.INVISIBLE);

            etatVersion.setVisibility(View.INVISIBLE);

            modeScenarioJourNuit.setVisibility(View.GONE);

            logo.setVisibility(View.GONE);
            buttonSOS.setVisibility(View.GONE);

            declarer.setVisibility(View.VISIBLE);
            tuto.setVisibility(View.VISIBLE);

        } catch (Exception ignored) {

        }


    }


    // Manipulation de données
    public void openManager() {
        optionsManager.open();
        alarmeAgressionManager.open();
        alarmeAMManager.open();
        alarmeIZSRManager.open();
        alarmePVManager.open();
        alarmeSOSManager.open();
        siteManager.open();
        trajetManager.open();
        etatManager.open();
    }


    // Fenetre de dialogue : choix de la situation 1) Normal
    public void choisirSituation() {
        ChoixSituationPopUp choixSituationPopUp = new ChoixSituationPopUp(MainActivity.this);
    }

    public void seLocaliserPopUp() {
        seLocaliserPopUp = new SeLocaliserPopUp(MainActivity.this);
    }


    public void reactivationPtiSwitch() {
        new Handler().postDelayed(() -> ptiSwitchSetClickable(true), 1);
    }

    public void autorisationGpsInvalide() {
        ptiSwitchSetChecked(false);

        new Handler().postDelayed(() -> fonctions.jouerSon("warning"), 5);
        new Handler().postDelayed(() -> {
            fonctions.jouerSon("warning");
            popUp.resetTitre();
            popUp.afficherFenetre("gps_not_always");
            reactivationPtiSwitch();


        }, 100);
    }


    public void modeTrajet() {

        // On vérifie les autorisations pour le GPS, si ce n'est pas le cas, on refuse la surveillance
        if (!verifierAutorisationExterieur()) {
            autorisationGpsInvalide();

        } else {

            if (!fonctions.isGPSEnabled()) {

                ptiSwitchSetChecked(false);

                new Handler().postDelayed(() -> fonctions.jouerSon("warning"), 5);
                new Handler().postDelayed(() -> {
                    popUp.resetTitre();
                    popUp.setMessage("Activez le GPS en mode haute précision pour pouvoir activer la surveillance PTI \"En extérieur\"");
                    popUp.afficherFenetre("gps");

                    // ptiSwitchSetClickable(false);
                    reactivationPtiSwitch();

                }, 100);

            } else {


                tempSituation = "En extérieur";
                demarrerService(rechercheGPSLoopService);

                optionsManager.open();
                if (!optionsManager.getOptions().isaInterfaceAlternative()) { // Interface Alternative


                    choisirTypeAlarme();

                }


            }

        }


    }

    // Vérifier selon la version Android, les autorisations GPS
    // >= 10 il faut l'autorisation "toujours" sinon l'autorisation GPS seulement suffit (Android 10 = API 29)
    public boolean verifierAutorisationExterieur() {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) { // Si Android 10 ou moins
            return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
    }

    public void choisirDureeIzsr() {
        ChoixDureeIzsrPopUp choixDureeIzsrPopUp = new ChoixDureeIzsrPopUp(MainActivity.this);
    }


    public void choisirSite(final String etat) {
        ChoixSitePopUp choixSitePopUp = new ChoixSitePopUp(MainActivity.this, etat);


    }


    public void choisirTypeAlarme() {
        ChoixTypeAlarmePopUp choixTypeAlarmePopUp = new ChoixTypeAlarmePopUp(MainActivity.this);
    }


    // A renommer
    public void routing() {
        ptiSwitchSetClickable(false);
        optionsManager.open();

        if (!tempSituation.equals("En zone sans réseau")) {

            activationPTI();

            // Mode Zone sans réseau
        } else {
            // Vérification du réseau
            if (fonctions.verificationPing()) {

                activationPTI();

                // Réponse : pas de réseau donc désactivation de la surveillance
            } else {
                desactivationSurveillancePTI();

            }
        }
    }


    // Traitement de signalement de bug
    public void signalerBug(String numeroContact, String email, String bugMessage) {
        String messageSignalerBug = "";
        String adresse = "";
        String pagePhpTlg = "signaler_bug.php"; // Page appellée

        // Appel de la page selon le type de serveur
        adresse = getResources().getString(R.string.app_type).equals("prod") ?
                getResources().getString(R.string.prod_full_url) + pagePhpTlg : getResources().getString(R.string.dev_full_url) + pagePhpTlg;

        // Il faut respecterles différents séparateurs
        messageSignalerBug += "Bug" + ','; // Intitulé
        messageSignalerBug += fonctions.getIMEI() + ';'; // IMEI de l'appareil
        messageSignalerBug += getResources().getString(R.string.app_version) + ';'; // Numéro de version
        messageSignalerBug += email + ';'; // E-mail de l'utilisateur
        messageSignalerBug += bugMessage; // Contenu du bug

        // Envoi par protocole HTTP
        pm = new PostMethod("bug", "appli_pti", messageSignalerBug, numeroContact, adresse, MainActivity.this);
        pm.execute();
    }


    // Par défaut
    public void messageTLG(String msg) {

        boolean postMethodEstAlarme = false;

        Log.d("ATELIER-DEBUG", "Message envoyé = " + msg);
        messageTLG(msg, postMethodEstAlarme);
    }


    public void messageTLG(String message, boolean postMethodEstAlarme) {


        String adresse = "";
        String typeMessage = "";

        if (postMethodEstAlarme) {
            typeMessage = "alarme";
        }


        // Page de réception TLG
        if (getResources().getString(R.string.app_type).equals("prod")) {
            adresse = getResources().getString(R.string.prod_full_url) + "getsms.php";
        } else {
            adresse = getResources().getString(R.string.dev_full_url) + "getsms.php";
        }

        //   Log.d("ATELIO-DEBUG", "Traitement message = " + message);

        //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


        if (message.equals("PTI Activation") || message.equals("PTI Désactivation")) {


            optionsManager.open();
            if (!optionsManager.getOptions().isHistoriqueSupervision()) {

                Log.d("ATELIO-DEBUG", "Historique supervision désactivé");
                return;

            }
        }

        if ("PTI Désactivation".equals(message)) {
            if (!fonctions.verificationPing()) {

                Log.d("ATELIO-DEBUG", "Mise en mémoire date de désactivation car absence de réseau, renvoi ultérieur");

                etatManager.open();
                etatManager.updateDate(fonctions.demandeDate());

            } else {

                pm = new PostMethod("", "appli_pti", message, fonctions.getIMEI(), adresse, MainActivity.this);
                pm.execute();

            }
        } else {// Annonce la version d'utilisation (depuis version 16) pour permettre à TLG de connaître la version de l'application
            if (message.equals("PTI Activation")) {
                message = message + ";" + getResources().getString(R.string.app_version);
            }

            // Envoi de la requête
            pm = new PostMethod(typeMessage, "appli_pti", message, fonctions.getIMEI(), adresse, MainActivity.this);
            pm.execute();
        }


    }


    @SuppressLint("WakelockTimeout")
    public void retourAuPremierPlan() {
        System.out.println("RETOUR AU PREMIER PLAN ATELIO");
        try {
            PowerManager.WakeLock screenLock = null;
            if ((getSystemService(POWER_SERVICE)) != null) {
                screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "s:TempWakeLock");
                if (screenLock != null) {           // we have a WakeLock
                    if (!screenLock.isHeld()) {  // but we don't hold it
                        screenLock.acquire();
                    }
                }

                screenLock.release();
            }


            intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (!Settings.canDrawOverlays(this)) {
                    //   startActivity(intentGoMenu);
                    /*
                    if (android.os.Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {   //Android M Or Over
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 889);
                    }
*/
                } else {
                    //Permission Granted-System will work

                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + this.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intentGoMenu, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);


                }
            } else {

                MainActivity.this.startActivity(intentGoMenu);

            }

            this.defilementEcran(ScrollView.FOCUS_UP);


        } catch (Exception ignored) {

        }
    }

    // Défiler l'écran
    // Haut = ScrollView.FOCUS_UP ; Bas = ScrollView.FOCUS_DOWN
    public void defilementEcran(int typeScroll) {
        try {
            scrollView = findViewById(R.id.scrollView);
            scrollView.fullScroll(typeScroll);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    // Activation de la surveillance
    public void activationPTI() {
        if (!ptiEtat && !finActivation) {

            Log.d("ATELIO-DEBUG", "Activation de la surveillance : Situation = " + tempSituation + " ; Type d'alarme = " + tempAlarmeMode);


            ptiEtat = true;
            blocTutoriel.setVisibility(View.GONE);
            blocTutoriel2.setVisibility(View.GONE);
            logo2.setVisibility(View.GONE);
            fabMenu.setVisibility(View.GONE);

            devPar.setVisibility(View.VISIBLE);
            logoMini.setVisibility(View.VISIBLE);

            etatManager.open();
            alarmeIZSRManager.open();
            tempLocalisationManager.open();

            TempLocalisation tempLocalisation = tempLocalisationManager.getTempLocalisation();

            Log.d("ATELIO-DEBUG", "Localisation = " + tempLocalisation.toString());

            optionsManager.open();


            boolean activationAutomatique = false;
            boolean sollicitation = false;
            try {
                System.out.println("@atelio " + optionsManager.getOptions().isActivationPTIAutomatique());
                activationAutomatique = optionsManager.getOptions().isActivationPTIAutomatique();
            } catch (Exception ignored) {
            }

            try {
                System.out.println("@atelio " + optionsManager.getOptions().isSollicitation());
                sollicitation = optionsManager.getOptions().isSollicitation();
            } catch (Exception ignored) {
            }

            if (activationAutomatique || sollicitation) {
                demarrerServiceAlways("CACHER");

            }


            stopService(redemarrageService);
            demarrerService(redemarrageService);

            stopService(shutDownService);
            demarrerService(shutDownService);


            stopService(pingService);

            versionManager.open();

            etatVersion.setVisibility(View.VISIBLE);

            if (versionManager.getVersion().isObsolete()) {
                etatVersion.setText("MAJ disponible");
                // https://play.google.com/store/apps/details?id=com.ateliopti.lapplicationpti

                System.out.println("ATELIO DEV Ancienne version");

                messageTLG("Ancienne version");


                etatVersion.setTextColor(Color.RED);
            } else {
                etatVersion.setVisibility(View.INVISIBLE);
            }


            // Activation des services : sollicitation et activation pti par charge
            //  demarrerServiceAlways();

            optionsManager.open();


            if (!activationAutomatique) {
                stopService(chargeService);
                demarrerService(chargeService);
            } else {

                if (activationAutomatiquePlay) {
                    activationAutomatiquePlay = false;
                    fonctions.jouerSon("activation_automatique");
                }
            }


            // Pas de ping service en IZSR
            if (!tempSituation.equals("En zone sans réseau")) {
                demarrerService(pingService);
            }


            //      ptiSwitchSetClickable(true);, too early
            openManager();

            redemarrageServices(false);

            messageTLG("PTI Activation");

            // Vérification si l'application est à jour


            if (tempSituation.equals("En zone sans réseau")) {

                new Handler().postDelayed(() -> {


                    // Si IZSR et localisation audio, envoyer données vers l'autre page
                    tempLocalisationManager.open();
                    if (tempLocalisationManager.getTempLocalisation().getSite().equals("Localisation vocale")) {


                        // Scénario exceptionnel

                        if (!(getDestinataireScenarioExceptionnel().equals(""))) {


                            System.out.println("@IZSR durée " + dureeIZSR);
                            System.out.println("@IZSR prealarme " + alarmeIZSRManager.getAlarmeIZSR().getIzsrDureeNotification());

                            AsyncUpload asyncUpload;
                            asyncUpload = new AsyncUpload(
                                    MainActivity.this, "Debut IZSR", getDestinataireScenarioExceptionnel(), getDetailScenarioExceptionnel(),
                                    dureeIZSR, alarmeIZSRManager.getAlarmeIZSR().getIzsrDureeNotification(), true);
                            asyncUpload.execute();

                        } else {
                            AsyncUpload asyncUpload;

                            System.out.println("@IZSR durée " + dureeIZSR);
                            alarmeIZSRManager.open();
                            System.out.println("@IZSR prealarme " + alarmeIZSRManager.getAlarmeIZSR().getIzsrDureeNotification());


                            asyncUpload = new AsyncUpload(
                                    MainActivity.this, "Debut IZSR", dureeIZSR, alarmeIZSRManager.getAlarmeIZSR().getIzsrDureeNotification(), true);
                            asyncUpload.execute();
                        }

                        // Localisation GPS
                    } else if (!tempLocalisationManager.getTempLocalisation().getLatitude().equals("") ||
                            !tempLocalisationManager.getTempLocalisation().getLongitude().equals("")) {

                        // Scénario exceptionnel

                        if (!(getDestinataireScenarioExceptionnel().equals(""))) {
                            alarmeIZSRManager.open();
                            tempLocalisationManager.open();
                            String debutInterventionMessage = "Debut IZSR;" + dureeIZSR + "/" + alarmeIZSRManager.getAlarmeIZSR().getIzsrDureeNotification() +
                                    "/" + getDestinataireScenarioExceptionnel() + "/" + getDetailScenarioExceptionnel() + ";"
                                    + "https://maps.google.com/?q=" + tempLocalisationManager.getTempLocalisation().getLatitude() + ","
                                    + tempLocalisationManager.getTempLocalisation().getLongitude();

                            messageTLG(debutInterventionMessage);

                        } else {
                            alarmeIZSRManager.open();
                            tempLocalisationManager.open();
                            String debutInterventionMessage = "Debut IZSR;" + dureeIZSR + "/" + alarmeIZSRManager.getAlarmeIZSR().getIzsrDureeNotification() + ";" +
                                    "https://maps.google.com/?q=" + tempLocalisationManager.getTempLocalisation().getLatitude() + "," +
                                    tempLocalisationManager.getTempLocalisation().getLongitude();

                            messageTLG(debutInterventionMessage);
                        }


                    } else {


                        // Scénario exceptionnel

                        if (!(getDestinataireScenarioExceptionnel().equals(""))) {
                            String debutInterventionMessage = "Debut IZSR;" + dureeIZSR + "/" + alarmeIZSRManager.getAlarmeIZSR().getIzsrDureeNotification() +
                                    "/" + getDestinataireScenarioExceptionnel() + "/" + getDetailScenarioExceptionnel() + ";" + tempLocalisationManager.getTempLocalisation().getSite() + " (T)";

                            messageTLG(debutInterventionMessage);

                        } else {
                            String debutInterventionMessage = "Debut IZSR;" + dureeIZSR + "/" + alarmeIZSRManager.getAlarmeIZSR().getIzsrDureeNotification() +
                                    ";" + tempLocalisationManager.getTempLocalisation().getSite() + " (T)";

                            messageTLG(debutInterventionMessage);
                        }

                    }
                }, 200);

            }

            etatManager.open();

            etatManager.updateEtat(true);
            optionsManager.open();


            if (optionsManager.exists()) {

                optionsManager.open();


                if(optionsManager.getOptions().isaScenarioJourNuit()){
                  //
                    String heureDebut = fonctions.addZero(optionsManager.getOptions().getHeureDebutScenarioJourNuit());
                    String heureFin = fonctions.addZero(optionsManager.getOptions().getHeureFinScenarioJourNuit());


                    // Heure actuelle
                    String heureNow = fonctions.addZero(fonctions.getCurrentHour());


                    if (fonctions.isHourInInterval(fonctions.addZero(heureNow), heureDebut, heureFin) == true) {

                        modeScenarioJourNuit = findViewById(R.id.modeScenarioJourNuit);
                        String txtJour = getResources().getString(R.string.texte_mode_jour);
                        modeScenarioJourNuit.setText(txtJour);
                    } else {
                        modeScenarioJourNuit = findViewById(R.id.modeScenarioJourNuit);
                        String txtNuit = getResources().getString(R.string.texte_mode_nuit);
                        modeScenarioJourNuit.setText(txtNuit);
                    }
                    modeScenarioJourNuit.setVisibility(View.VISIBLE);
                }



            }


            localisationText.setVisibility(View.VISIBLE);
            localisationText2.setVisibility(View.VISIBLE);
            indication.setVisibility(View.VISIBLE);
            logo.setVisibility(View.VISIBLE);
            buttonSOS.setVisibility(View.VISIBLE);

            tempLocalisationManager.open();
            if (tempLocalisationManager.getTempLocalisation().getDate().equals("")) {
                localisationText.setText("Localisation en cours d'acquisition …");
            } else {
                String[] date = tempLocalisationManager.getTempLocalisation().getDate().split(" ");
                String localisationDate = date[0];
                String localisationHeure = date[1];

                if (tempLocalisationManager.getTempLocalisation().getSite().equals("")) {

                    double latitude = Double.valueOf(tempLocalisationManager.getTempLocalisation().getLatitude());
                    double longitude = Double.valueOf(tempLocalisationManager.getTempLocalisation().getLongitude());

                    Geocoder geoCoder = new Geocoder(MainActivity.this);
                    List<Address> matches = null;

                    try {
                        matches = geoCoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (matches == null) {
                        String localisationString = "Le " + localisationDate + " à " + localisationHeure + ", localisation à proximité de : Adresse inconnue";
                        localisationText.setText(localisationString);

                    } else {
                        Address bestMatch = (matches.isEmpty() ? null : matches.get(0));

                        if (bestMatch == null) {
                            String localisationString = "Le " + localisationDate + " à " + localisationHeure + ", localisation à proximité de : Adresse inconnue";
                            localisationText.setText(localisationString);

                        } else {
                            String localisationString = "Le " + localisationDate + " à " + localisationHeure + ", localisation à proximité de : " +
                                    bestMatch.getAddressLine(0);
                            localisationText.setText(localisationString);


                        }

                    }

                    String ligneSuperieur = "<b>Appuyez ici pour voir sur Google Maps</b>";
                    localisationText2.setText(Html.fromHtml(ligneSuperieur));


                } else {
                    String ligneSuperieur = "";

                    siteManager.open();

                    if (!tempSituation.equals("En zone sans réseau")) {

                        if (optionsManager.getOptions().isLocalisationAudio() || siteManager.count() > 1) {
                            ligneSuperieur = "<b>Appuyez ici pour modifier</b>";
                            localisationText2.setText(Html.fromHtml(ligneSuperieur));
                        }

                        String appuyerEcouter = "";
                        String nomSite = tempLocalisationManager.getTempLocalisation().getSite();
                        if (tempLocalisationManager.getTempLocalisation().getSite().equals("Localisation vocale")) {
                            appuyerEcouter = " <b><i>(appuyez ici pour écouter)</i></b>";
                        }

                        ligneSuperieur = nomSite;

                        if (nomSite.equals("Localisation vocale")) {
                            ligneSuperieur = "Message vocal";
                        }

                        localisationText.setText(Html.fromHtml("Le " + localisationDate + " à " + localisationHeure + ", localisation à proximité de : " + ligneSuperieur + appuyerEcouter));

                    } else {

                        if (optionsManager.getOptions().isLocalisationAudio() || siteManager.count() > 1) {
                            ligneSuperieur = "<b>Appuyez ici pour modifier</b>";
                            localisationText2.setText(Html.fromHtml(ligneSuperieur));
                        }

                        String appuyerEcouter = "";
                        String nomSite = tempLocalisationManager.getTempLocalisation().getSite();
                        if (tempLocalisationManager.getTempLocalisation().getSite().equals("Localisation vocale")) {

                            appuyerEcouter = " <b><i>(appuyez ici pour écouter)</i></b>";
                        }

                        ligneSuperieur = nomSite;

                        if (nomSite.equals("Localisation vocale")) {
                            ligneSuperieur = "Message vocal";
                        }

                        localisationText.setText(Html.fromHtml("Le " + localisationDate + " à " + localisationHeure + ", localisation à proximité de : " + ligneSuperieur + appuyerEcouter));


                    }
                }
            }
        }
    }

    // Ouvre Google Maps avec les coordonnées
    public void voirPlan() {
        tempLocalisationManager.open();

        // Coordonnées de géolocalisation
        String latitude = tempLocalisationManager.getTempLocalisation().getLatitude();
        String longitude = tempLocalisationManager.getTempLocalisation().getLongitude();

        // Lien Google Maps
        String googleMaps = "https://maps.google.com/maps?q=" + latitude + "," + longitude;

        Uri lien = Uri.parse(googleMaps);
        Intent intent = new Intent(Intent.ACTION_VIEW, lien);
        startActivity(intent);
    }


    public void demarrerServiceAlways(String notif) {
        // Activation des services : sollicitation et activation pti par charge

        stopService(intentAlways);

        try {
            optionsManager.open();
            etatManager.open();

            boolean activationAutomatique = false;
            boolean sollicitation = false;
            try {
                System.out.println("@atelio " + optionsManager.getOptions().isActivationPTIAutomatique());
                activationAutomatique = optionsManager.getOptions().isActivationPTIAutomatique();
            } catch (Exception ignored) {

            }

            try {
                System.out.println("@atelio " + optionsManager.getOptions().isSollicitation());
                sollicitation = optionsManager.getOptions().isSollicitation();
            } catch (Exception ignored) {
            }

            if (activationAutomatique || sollicitation || !etatManager.getEtat().getDerniereDesactivation().equals("")) {


                intentAlways.setAction(notif);

                demarrerService(intentAlways);
            }
        } catch (Exception ignored) {

        }
    }


    // Bouton retour
    @Override
    public void onBackPressed() {
        intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);


    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.out.println("@atelio ON DESTROY");

        etatManager.open();
        etatManager.updateFinCharge(false);

        if (popUp != null && popUp.isShowing()) {
            popUp.dismiss();
        }

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);


        try {
            stopService(pingSonoreService);
        } catch (Exception ignored) {

        }
        try {
            unregisterReceiver(gpsListener);
        } catch (Exception ignored) {

        }
        try {
            unregisterReceiver(mMessageReceiver);
        } catch (Exception ignored) {

        }


        try {
            unregisterReceiver(receiverScreen);
        } catch (Exception ignored) {

        }


        try {
            unregisterReceiver(receiverAppAlert);
        } catch (Exception ignored) {

        }

        try {
            unregisterReceiver(receiverApplicationSos);
        } catch (Exception ignored) {

        }

        try {
            unregisterReceiver(powerReceiver);
        } catch (Exception ignored) {

        }

        try {
            unregisterReceiver(timeChangedReceiver);
        } catch (Exception ignored) {

        }

        try {
            unregisterReceiver(buttonReceiver);
        } catch (Exception ignored) {

        }

        try {
            unregisterReceiver(receiverPowerAlert);
        } catch (Exception ignored) {

        }

        try {
            unregisterReceiver(rappelUtilisationReceiver); // A DELETE ?
        } catch (Exception ignored) {

        }

        // MediaPlayer
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }


    }

    public void serviceLocalisationSonore(boolean play) {
        if (play) {
            demarrerService(localisationSonoreService);
        } else {
            stopService(localisationSonoreService);
        }
    }

    public void servicePingSonore(boolean on) {

        if (on) {
            demarrerService(pingSonoreService);
        } else {
            stopService(pingSonoreService);
        }

    }


    // Changement logo, couleur du thème et de la barre d'application
    public void theme() {
        String[] theme = fonctions.chargerTheme();
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor(theme[0])));
        //int resID = Fonctions.getResId(theme[1], R.drawable.class);



        logo = findViewById(R.id.logo);  // Logo
        //logo.setImageResource(resID);

        localisationText2.findViewById(R.id.localisationText2);
        localisationText2.setTextColor(Color.parseColor(theme[0]));


        getTheme().applyStyle(Integer.parseInt(theme[2]), true);
    }


    // Mettre l'ID licence sur le titre
    public void licenceTitre() {
        String getIMEI = fonctions.getIMEI();
        String libelleTitre = "ID : " + fonctions.separateurIMEI(getIMEI);
        imeiInfo.setText(libelleTitre);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Log.e("DEV INTENT", key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
            }
        }

        if (intent.getStringExtra("methodName") != null) {
            if (intent.getStringExtra("methodName").equals("myMethod")) {
                etatManager.open();
                etatManager.updateFinCharge(false);
                actionDisconnected();
            } else if (intent.getStringExtra("methodName").equals("myMethode")) {
                //  Chargeur branché
                actionConnected();

            }

        }


    }

    // Si retour à l'application, on remonte la position à TOP
    protected void onResume() {
        super.onResume();
        this.defilementEcran(ScrollView.FOCUS_UP);
        Log.d("ATELIO-DEBUG", "Etat = onResume");
    }

    protected void onPause() {
        super.onPause();
        Log.d("ATELIO-DEBUG", "Etat = onPause");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d("ATELIO-DEBUG", "Etat = onRestart");
    }


    // Scénario exceptionnel
    public void definirScenarioExceptionnel(String destinataireScenarioExceptionnel, String
            detailScenarioExceptionnel) {
        this.destinataireScenarioExceptionnel = destinataireScenarioExceptionnel;
        this.detailScenarioExceptionnel = detailScenarioExceptionnel;
    }


    public String getDestinataireScenarioExceptionnel() {
        return destinataireScenarioExceptionnel;
    }

    public String getDetailScenarioExceptionnel() {
        return detailScenarioExceptionnel;
    }

    public void reprisePostAnnulationPrealarme() {

        System.out.println("redemarrage des services = MODE " + tempAlarmeMode);


        preAlarme = false;

        //   ptiSwitchSetClickable(true); // bon
        reactivationPtiSwitch(); // déblocage interrupteur

        openManager();


        tempLocalisationManager.open();

        tempLocalisationManager.getTempLocalisation().setDate("");
        tempLocalisationManager.getTempLocalisation().setLatitude("");
        tempLocalisationManager.getTempLocalisation().setLongitude("");
        tempLocalisationManager.getTempLocalisation().setSite("");

        stopService(localisationSonoreService);

        final String declenchementEnvers = "Mise tête à l'envers";

        new Handler().postDelayed(() -> stopServicesDetection(), 50);


        if (tempSituation.equals("En zone sans réseau")) {
            alarmeIZSRManager.open();

            stopService(intentIZSR);

            intentIZSR.putExtra("temps", dureeIZSR);

            demarrerService(intentIZSR);


        } else {
            new Handler().postDelayed(() -> {
                alarmeSOSManager.open(); // fix 070920
                if (alarmeSOSManager.getAlarmeSOS().getSosDeclenchementMouvement().equals(declenchementEnvers)) {
                    stopService(declenchementEnversService);
                    demarrerService(declenchementEnversService);
                }


                if (!alarmeSOSManager.getAlarmeSOS().getSosDeclenchementBluetooth().equals("")) {
                    demarrerService(declenchementBluetoothService);
                }

                switch (tempAlarmeMode) {
                    case "AM": // Absence de mouvement
                        stopService(absenceMouvementService);
                        demarrerService(absenceMouvementService);
                        break;

                    case "PV": // Perte de verticalité
                        stopService(perteVerticaliteService);
                        demarrerService(perteVerticaliteService);
                        break;

                    case "Agression": // Homme-Mort
                        stopService(hommeMortService);
                        demarrerService(hommeMortService);
                        break;

                }
            }, 1000);
        }
    }

    // Alarme par l'application SOS
    BroadcastReceiver receiverAppAlert = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            alarmeSOSManager.open();
            if (!tempSituation.equals("En zone sans réseau")) {

                // RUGGEAR
                intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentGoMenu);

                try {
                    if (ptiEtat && !preAlarme) {
                        retourAuPremierPlan();
                        prealarme("SOS");
                    }
                } catch (Exception ignored) {

                }
            }
        }
    };


    BroadcastReceiver receiverApplicationSos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals("SOS_APP_ALERT")) {
                    alarmeSOSManager.open();

                    if (!tempSituation.equals("En zone sans réseau")) {
                        intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainActivity.this.startActivity(intentGoMenu);

                        try {


                            if (ptiEtat && !preAlarme) {
                                overridePendingTransition(0, 0);

                                Intent intentSend = new Intent("APP_PTI_RECEIVED");
                                intentSend.setPackage("com.ateliopti.lapplicationsos");
                                context.sendBroadcast(intentSend);

                                prealarme("SOS");

                            }
                        } catch (Exception ignored) {

                        }
                    }
                }
            }
        }
    };


    // Alarme bouton POWER
    BroadcastReceiver receiverPowerAlert = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            alarmeSOSManager.open();
            try { // évite plantage car pas de configuration
                if (!tempSituation.equals("En zone sans réseau") && alarmeSOSManager.getAlarmeSOS().getSosDeclenchementMouvement().equals("Verrouillage")) {
                    System.out.println("Condition réussi");

                    fonctions = new Fonctions(context);

                    intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intentGoMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentGoMenu);


                    try {

                        if (ptiEtat && !preAlarme) {

                            retourAuPremierPlan();
                            prealarme("SOS");
                        }

                        //   }
                    } catch (Exception ignored) {

                    }
                }
            } catch (Exception ignored) {
                System.out.println("Exception configuration not existing");

            }

        }
    };

    // Toast : annulation surveillance
    public void toastAnnulationSurveillancePTI() {
        Toast.makeText(this, "Surveillance PTI annulée", Toast.LENGTH_SHORT).show();
    }


    public void stopPlaying() {
        try {
            mPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayer = null;


    }


    public void checkRappelUtilisation() {

        optionsManager.open();


        if (optionsManager.getOptions() != null) {
            boolean condition = optionsManager.getOptions().isaRappelUtilisation() && optionsManager.getOptions().getDureeRappelUtilisation() > 0;

            if (!condition) {

                alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, RappelUtilisationReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        MainActivity.this, 1, intent,
                        PendingIntent.FLAG_IMMUTABLE);

                alarmMgr.cancel(pendingIntent);

            }
        }

    }


    public boolean getTypeEcran() {
        return templateRonde;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == 22) {
            autorisations.autrePermissions();


        }

    }

    // Receiver GPS compatible toutes versions de Android
    @Override
    public void onGpsStatusChanged(boolean gpsStatus) {

        // Pas de GPS ! -> fermer la surveillance
        if (!gpsStatus) {

            Intent intent = new Intent("GPS_ALERT");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                intent.removeFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.removeFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            }

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent.setPackage("com.ateliopti.lapplicationpti"));
            // sendBroadcast(new Intent("gps_alert"));
        }

    }


    // Commencer un enregistrement
    public void startRecording() {
        mRecorder = new MediaRecorder();

        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            File root = new File(this.getExternalFilesDir(null), "rec");
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, "rec.mp4");

            mRecorder.setOutputFile(file);
        } else {

            File root = android.os.Environment.getExternalStorageDirectory();
            File file = new File(root.getAbsolutePath() + "/AppliPTI");

            if (!file.exists()) {
                file.mkdirs();
            }

            String fileName = root.getAbsolutePath() + "/AppliPTI/rec.mp4";
            mRecorder.setOutputFile(fileName);

            // mRecorder.setOutputFile(file.getAbsolutePath());
        }

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stopPlaying();

        recording = true;
    }

    // Stopper l'enregistrement
    public void stopRecording(boolean toast) {


        try {
            mRecorder.stop();
            mRecorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecorder = null;

        if (recording && toast) {
            Toast.makeText(this, "Message vocal enregistrée.", Toast.LENGTH_SHORT).show();
        }

    }


    public void modifierImeiGoogleEssai() {
        String messageSignalerBug = "";
        String adresse = "";
        String pagePhpTlg = "signaler_bug.php"; // Page appellée

        // Appel de la page selon le type de serveur
        adresse = getResources().getString(R.string.app_type).equals("prod") ?
                getResources().getString(R.string.prod_full_url) + pagePhpTlg : getResources().getString(R.string.dev_full_url) + pagePhpTlg;

        // Il faut respecterles différents séparateurs
        messageSignalerBug += "GoogleEssai" + ','; // Intitulé
        messageSignalerBug += fonctions.getIMEI() + ';'; // IMEI de l'appareil
        messageSignalerBug += getResources().getString(R.string.app_version) + ';'; // Numéro de version


        // Envoi par protocole HTTP
        pm = new PostMethod("google_essai", "appli_pti", messageSignalerBug, fonctions.getIMEI(), adresse, MainActivity.this);
        pm.execute();

    }
}

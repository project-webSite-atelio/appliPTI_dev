package com.ateliopti.lapplicationpti;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.ateliopti.lapplicationpti.manager.AlarmeAMManager;
import com.ateliopti.lapplicationpti.manager.AlarmeAgressionManager;
import com.ateliopti.lapplicationpti.manager.AlarmeIZSRManager;
import com.ateliopti.lapplicationpti.manager.AlarmePVManager;
import com.ateliopti.lapplicationpti.manager.AlarmeSOSManager;
import com.ateliopti.lapplicationpti.manager.EtatManager;
import com.ateliopti.lapplicationpti.manager.LicenceManager;
import com.ateliopti.lapplicationpti.manager.OptionsManager;
import com.ateliopti.lapplicationpti.manager.SiteManager;
import com.ateliopti.lapplicationpti.manager.TempLocalisationManager;
import com.ateliopti.lapplicationpti.manager.TrajetManager;
import com.ateliopti.lapplicationpti.manager.VersionManager;
import com.ateliopti.lapplicationpti.model.AlarmeAM;
import com.ateliopti.lapplicationpti.model.AlarmeAgression;
import com.ateliopti.lapplicationpti.model.AlarmeIZSR;
import com.ateliopti.lapplicationpti.model.AlarmePV;
import com.ateliopti.lapplicationpti.model.AlarmeSOS;
import com.ateliopti.lapplicationpti.model.Configuration;
import com.ateliopti.lapplicationpti.model.Etat;
import com.ateliopti.lapplicationpti.model.Licence;
import com.ateliopti.lapplicationpti.model.Options;
import com.ateliopti.lapplicationpti.model.Site;
import com.ateliopti.lapplicationpti.model.TempLocalisation;
import com.ateliopti.lapplicationpti.model.Trajet;
import com.ateliopti.lapplicationpti.model.Version;

import java.io.IOException;
import java.util.Arrays;

class CopieConfiguration {

    private static final MediaPlayer mp = new MediaPlayer();

    private final Context context;

    private final AlarmeAgressionManager alarmeAgressionManager;
    private final AlarmeAMManager alarmeAMManager;
    private final AlarmeIZSRManager alarmeIZSRManager;
    private final AlarmePVManager alarmePVManager;
    private final AlarmeSOSManager alarmeSOSManager;
    private final LicenceManager licenceManager;
    private final OptionsManager optionsManager;
    private final SiteManager siteManager;
    private final TrajetManager trajetManager;
    private final TempLocalisationManager tempLocalisationManager;
    private final EtatManager etatManager;

    private final VersionManager versionManager;

    private final int versionAppli;

    CopieConfiguration(Context context) {
        this.context = context;

        this.alarmeAgressionManager = new AlarmeAgressionManager(context);
        this.alarmeAMManager = new AlarmeAMManager(context);
        this.alarmeIZSRManager = new AlarmeIZSRManager(context);
        this.alarmePVManager = new AlarmePVManager(context);
        this.alarmeSOSManager = new AlarmeSOSManager(context);
        this.licenceManager = new LicenceManager(context);
        this.optionsManager = new OptionsManager(context);
        this.siteManager = new SiteManager(context);
        this.trajetManager = new TrajetManager(context);
        this.tempLocalisationManager = new TempLocalisationManager(context);
        this.etatManager = new EtatManager(context);
        this.versionManager = new VersionManager(context);

        this.versionAppli =  Integer.parseInt(context.getResources().getString(R.string.app_version));
    }

    // status

    void execute(Configuration recuperation, int status) {

        Fonctions fonctions = new Fonctions(context);

        // Alarme Agression
        alarmeAgressionManager.open();
        AlarmeAgression alarmeAgression = new AlarmeAgression(
                1,
                recuperation.isaAlarmeAgression(),
                recuperation.getAgressionDureeDetection(),
                recuperation.getAgressionDureeNotification(),
                recuperation.getAgressionTypeNotif(),
                recuperation.getAgressionCodeAnnulation());

        alarmeAMManager.open();
        AlarmeAM alarmeAM = new AlarmeAM(
                1,
                recuperation.isaAlarmeAm(),
                recuperation.getAmDureeDetection(),
                recuperation.getAmDureeNotification(),
                recuperation.getAmTypeNotif(),
                recuperation.isAmAnnulation()
        );


        alarmeIZSRManager.open();
        AlarmeIZSR alarmeIZSR = new AlarmeIZSR(
                1,
                recuperation.getIzsrDureeNotification(),
                recuperation.getIzsrTypeNotif()
        );

        alarmePVManager.open();
        AlarmePV alarmePV = new AlarmePV(
                1,
                recuperation.isaAlarmePv(),
                recuperation.getPvDureeDetection(),
                recuperation.getPvAngleDetection(),
                recuperation.getPvDureeNotification(),
                recuperation.getPvTypeNotif(),
                recuperation.isPvAnnulation()
        );

        alarmeSOSManager.open();
        AlarmeSOS alarmeSOS = new AlarmeSOS(
                1,
                recuperation.getSosDeclenchementMouvement(),
                recuperation.getSosDeclenchementBluetooth(),
                recuperation.getSosDureeNotification(),
                recuperation.getSosTypeNotif(),
                recuperation.getSosCodeAnnulation()
        );

        optionsManager.open();
        Options options = new Options(
                1,
                recuperation.isaTrajet(),
                recuperation.isaBatiment(),
                recuperation.isaIzsr(),
                recuperation.getPuissanceSonore(),
                recuperation.isLocalisationSonore(),
                recuperation.getLogo(),
                recuperation.isActivationPTIAutomatique(),
                recuperation.isSollicitation(),
                recuperation.isHistoriqueSupervision(),
                recuperation.isaTutoriel(),
                recuperation.getScenarioExceptionnel(),
                recuperation.isLocalisationAudio(),
                recuperation.isaRappelUtilisation(),
                recuperation.getDureeRappelUtilisation(),
                recuperation.isaNotation(),
                recuperation.isaBroadcastInstavox(),
                recuperation.isaInterfaceAlternative(),
                recuperation.isaScenarioJourNuit(),
                recuperation.getHeureDebutScenarioJourNuit(),
                recuperation.getHeureFinScenarioJourNuit()
        );


        trajetManager.open();
        Trajet trajet = new Trajet(
                1,
                recuperation.getDureeRechercheGps(),
                recuperation.getDureePauseGps(),
                recuperation.getPrecisionGps()
        );


        siteManager.open();
        String[] localisations = recuperation.getLocalisations();


        PostMethod pm;
        if (status == 0) { // Première config
            licenceManager.open();

            for (int i = 0; i < localisations.length; i++) {
                System.out.println("@ATELIO " + recuperation.getLocalisations()[i]);
                Site site = new Site(
                        i + 1,
                        localisations[i]
                );
                siteManager.inserer(site);
            }


            alarmeAgressionManager.inserer(alarmeAgression);
            alarmeAMManager.inserer(alarmeAM);
            alarmeIZSRManager.inserer(alarmeIZSR);
            alarmePVManager.inserer(alarmePV);
            alarmeSOSManager.inserer(alarmeSOS);
            optionsManager.inserer(options);
            trajetManager.inserer(trajet);


            tempLocalisationManager.open();

            TempLocalisation tempLocalisation = new TempLocalisation(1, "", "", "", null);
            tempLocalisationManager.inserer(tempLocalisation);


            //  ((MainActivity) context).choisirSituation();

            Licence licence = new Licence(
                    1,
                    true
            );


            licenceManager.insertLicence(licence);



            etatManager.open();
            Etat etat = new Etat(
                    1,
                    false,
                    "",
                    false
            );


            etatManager.inserer(etat);

            versionManager.open();
            Version version = new Version(1, versionAppli, false, true);
            versionManager.inserer(version);

           // String adresse = ((MainActivity) context).getResources().getString(R.string.server_url) + ((MainActivity) context).getResources().getString(R.string.server_port) + "/";


            String adresse = "";

            if(context.getResources().getString(R.string.app_type).equals("prod")){
                adresse = context.getResources().getString(R.string.prod_full_url);
            }else{
                adresse = context.getResources().getString(R.string.dev_full_url);
            }



            pm = new PostMethod("", "xpti_doubt", "Licence activée" + ";" + context.getResources().getString(R.string.app_version), fonctions.getIMEI(), adresse + "getsms.php", context);
            pm.execute();

            // ((MainActivity) context).choisirSituation();

        } else if (status == 1) { // Mise à jour

            boolean changement = false;

            versionManager.open();
            versionManager.updateVersion(recuperation.getDerniereVersion() > versionManager.getVersion().getVersion());



            AlarmeAgression alarmeAgressionBDD = alarmeAgressionManager.getAlarmeAgression();
            if (alarmeAgression.compares(alarmeAgressionBDD)) {
                System.out.println("@ATELIO AlarmeAgression true");
            } else {
                System.out.println("@ATELIO AlarmeAgression false");
                changement = true;
                alarmeAgressionManager.open();
                alarmeAgressionManager.updateAlarmeAgression(alarmeAgression);

            }

            System.out.println("@ATELIO valeurs A : " + alarmeAgression.toString());
            System.out.println("@ATELIO valeurs B : " + alarmeAgressionManager.getAlarmeAgression().toString());




            System.out.println("@ATELIO B1" + alarmeAgressionBDD.isaAlarmeAgression());
            System.out.println("@ATELIO B2" + recuperation.isaAlarmeAgression());




            AlarmeAM alarmeAMBDD = alarmeAMManager.getAlarmeAM();
            if (alarmeAM.compares(alarmeAMBDD)) {
                System.out.println("@ATELIO AlarmeAM true");
            } else {
                System.out.println("@ATELIO AlarmeAM false");
                changement = true;
                alarmeAMManager.open();
                alarmeAMManager.updateAlarmeAM(alarmeAM);
            }

            AlarmeIZSR alarmeIZSRBDD = alarmeIZSRManager.getAlarmeIZSR();
            if (alarmeIZSR.compares(alarmeIZSRBDD)) {
                System.out.println("@ATELIO AlarmeIZSR true");
            } else {
                System.out.println("@ATELIO AlarmeIZSR false");
                changement = true;
                alarmeIZSRManager.open();
                alarmeIZSRManager.updateAlarmeIZSR(alarmeIZSR);
            }

            AlarmePV alarmePVBDD = alarmePVManager.getAlarmePV();
            if (alarmePV.compares(alarmePVBDD)) {
                System.out.println("@ATELIO AlarmePV true");
            } else {
                System.out.println("@ATELIO AlarmePV false");
                changement = true;
                alarmePVManager.open();
                alarmePVManager.updateAlarmePV(alarmePV);
            }

            AlarmeSOS alarmeSOSBDD = alarmeSOSManager.getAlarmeSOS();
            if (alarmeSOS.compares(alarmeSOSBDD)) {
                System.out.println("@ATELIO AlarmeSOS true");
            } else {
                System.out.println("@ATELIO AlarmeSOS false");
                changement = true;
                alarmeSOSManager.open();
                alarmeSOSManager.updateAlarmeSOS(alarmeSOS);
            }

            Options optionsBDD = optionsManager.getOptions();


            System.out.println("@ATELIO Parametrage a " + options);
            System.out.println("@ATELIO Parametrage b " + optionsBDD);


            if (options.compares(optionsBDD)) {
                System.out.println("@ATELIO Options true");
            } else {
                System.out.println("@ATELIO Options false");
                changement = true;
                optionsManager.open();
                optionsManager.updateOptions(options);
            }

            Trajet trajetBDD = trajetManager.getTrajet();

            System.out.println("@ATELIO T1" + trajet.toString());

            System.out.println("@ATELIO T2" + trajetBDD.toString());


            if (BuildConfig.DEBUG) {
                // do something for a debug build
            }

            if (trajet.compares(trajetBDD)) {
                System.out.println("@ATELIO Trajet true");
            } else {
                System.out.println("@ATELIO Trajet false");
                changement = true;
                trajetManager.open();
                trajetManager.updateTrajet(trajet);


            }



            String[] sites = recuperation.getLocalisations(); // JSON
            String[] sitesBDD = siteManager.getSites(); // BDD

            System.out.println("@ATELIO sites" + Arrays.toString(sites));

            System.out.println("@ATELIO sitesBDD" + Arrays.toString(sitesBDD));

            if (Arrays.equals(sites, sitesBDD)) {
                System.out.println("@ATELIO sites identiques");
            } else {
                System.out.println("@ATELIO sites différents");
                changement = true;
                siteManager.vider();


                if (localisations != null) {
                    for (int i = 0; i < localisations.length; i++) {
                        //    System.out.println("@ATELIO " + recuperation.getLocalisations()[i]);
                        Site site = new Site(
                                i + 1,
                                localisations[i]
                        );
                        siteManager.inserer(site);
                    }
                }

            }

            // Autorisation de message vocal car mode "Interface alternative"
            optionsManager.open();
            boolean isInterfaceAlternatif = optionsManager.getOptions().isaInterfaceAlternative();
            boolean aMessageVocal = optionsManager.getOptions().isLocalisationAudio();

            if(isInterfaceAlternatif && !aMessageVocal){
                Options optionsActuels = optionsManager.getOptions();
                optionsActuels.setLocalisationAudio(true);
                optionsManager.updateOptions(optionsActuels);
            }


            // SI la configuration a été changée
            if (changement) {


                // Envoyer configuration mis à jour


                String adresse = "";

                if(context.getResources().getString(R.string.app_type).equals("prod")){
                    adresse = context.getResources().getString(R.string.prod_full_url);
                }else{
                    adresse = context.getResources().getString(R.string.dev_full_url);
                }

                pm = new PostMethod("", "xpti_doubt", "Configuration mise à jour" + ";" + context.getResources().getString(R.string.app_version), fonctions.getIMEI(), adresse + "getsms.php", context);
                pm.execute();

                Toast.makeText(context, "Configuration mise à jour", Toast.LENGTH_SHORT).show();


                ((MainActivity) context).theme();
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                if(BuildConfig.DEBUG){
                    am.setStreamVolume(AudioManager.STREAM_ALARM, 10, 0);
                }else {
                    am.setStreamVolume(AudioManager.STREAM_ALARM, 100, 0);
                }




                Resources res = context.getResources();
                AssetFileDescriptor afd = res.openRawResourceFd(R.raw.configuration_maj_succes);

                mp.reset();
                mp.setAudioStreamType(AudioManager.STREAM_ALARM); // hauts-parleurs

                //       mp.setLooping(true);
                try {
                    mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.start();

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        // TODO : PTI Activé
                        ((MainActivity) context).demarrerServiceAlways("AFFICHER");

                        ((MainActivity) context).choisirSituation();

                    }

                });


            } else {


                ((MainActivity) context).licenceTitre();


                ((MainActivity) context).demarrerServiceAlways("AFFICHER");
                ((MainActivity) context).choisirSituation();
                ((MainActivity) context).theme();


                optionsManager.open();

                etatManager.open();


            }

        }

    }

}

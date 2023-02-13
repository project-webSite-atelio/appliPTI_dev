package com.ateliopti.lapplicationpti;

import android.content.Context;
import android.telephony.TelephonyManager;

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


class DebugFonctions {
    private final LicenceManager licenceManager;
    private final OptionsManager optionsManager;
    private final AlarmeAgressionManager alarmeAgressionManager;
    private final AlarmeAMManager alarmeAMManager;
    private final AlarmeIZSRManager alarmeIZSRManager;
    private final AlarmePVManager alarmePVManager;
    private final AlarmeSOSManager alarmeSOSManager;
    private final SiteManager siteManager;
    private final TrajetManager trajetManager;
    private final TempLocalisationManager tempLocalisationManager;
    private final EtatManager etatManager;
    private final VersionManager versionManager;

    private final Context context;


    public DebugFonctions(Context context) {
        this.context = context;

        licenceManager = new LicenceManager(context);
        optionsManager = new OptionsManager(context);
        alarmeAgressionManager = new AlarmeAgressionManager(context);
        alarmeAMManager = new AlarmeAMManager(context);
        alarmeIZSRManager = new AlarmeIZSRManager(context);
        alarmePVManager = new AlarmePVManager(context);
        alarmeSOSManager = new AlarmeSOSManager(context);
        siteManager = new SiteManager(context);
        trajetManager = new TrajetManager(context);
        tempLocalisationManager = new TempLocalisationManager(context);
        etatManager = new EtatManager(context);
        versionManager = new VersionManager(context);
    }

    public void getData(){

        openManager();
        System.out.println("@ATELIO Configuration actuelle ");

        System.out.println("@ATELIO etat " + etatManager.getEtat().toString());


        System.out.println("HELLO @ATELIO " + optionsManager.getOptions());
        System.out.println("@ATELIO " + alarmeAgressionManager.getAlarmeAgression());
        System.out.println("@ATELIO " + alarmeAMManager.getAlarmeAM());
        System.out.println("@ATELIO " + alarmeIZSRManager.getAlarmeIZSR());
        System.out.println("@ATELIO " + alarmePVManager.getAlarmePV());
        System.out.println("@ATELIO " + alarmeSOSManager.getAlarmeSOS());
        System.out.println("@ATELIO " + trajetManager.getTrajet());
        System.out.println("@ATELIO " + tempLocalisationManager.getTempLocalisation());


        System.out.println("@ATELIO " + siteManager.count() + " site");

        for (int i = 0; i < siteManager.count(); i++) {
            System.out.println("@ATELIO site " + siteManager.getSites()[i]);

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


}
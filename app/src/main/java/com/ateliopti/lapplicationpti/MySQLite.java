package com.ateliopti.lapplicationpti;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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


public final class MySQLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db.sqlite.application.pti";
    // 5 ajout instavox, 6 interface alternative, 7 scenario jour & nuit
    private static final int DATABASE_VERSION = 7;
    private static MySQLite sInstance;

    public static synchronized MySQLite getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MySQLite(context);
        }
        return sInstance;
    }

    private MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Création de la base de données
        // on exécute ici les requêtes de création des tables
        sqLiteDatabase.execSQL(LicenceManager.CREATE_TABLE_LICENCE); // Création Table Licence
        sqLiteDatabase.execSQL(OptionsManager.CREATE_TABLE_OPTIONS); // Création Table Licence
        sqLiteDatabase.execSQL(SiteManager.CREATE_TABLE_SITE); // Création Table Site
        sqLiteDatabase.execSQL(TrajetManager.CREATE_TABLE_TRAJET); // Création Table Trajet
        sqLiteDatabase.execSQL(AlarmeSOSManager.CREATE_TABLE_ALARME_SOS); // Création Table AlarmeSOS
        sqLiteDatabase.execSQL(AlarmeAMManager.CREATE_TABLE_ALARME_AM); // Création Table AlarmeAM
        sqLiteDatabase.execSQL(AlarmePVManager.CREATE_TABLE_ALARME_PV); // Création Table AlarmePV
        sqLiteDatabase.execSQL(AlarmeIZSRManager.CREATE_TABLE_ALARME_IZSR); // Création Table AlarmeIzsr
        sqLiteDatabase.execSQL(AlarmeAgressionManager.CREATE_TABLE_ALARME_AGRESSION); // Création Table AlarmeAgression
        sqLiteDatabase.execSQL(TempLocalisationManager.CREATE_TABLE_TEMP_LOCALISATION); // Création Table TempLocalisation
        sqLiteDatabase.execSQL(EtatManager.CREATE_TABLE_ETAT); // Création Table Etat
        sqLiteDatabase.execSQL(VersionManager.CREATE_TABLE_VERSION); // Création Table Version
        sqLiteDatabase.execSQL(EcranManager.CREATE_TABLE_ETAT); // Création Table Version

    }

    private static final String DATABASE_A_RAPPEL_UTILISATION_V2 = "ALTER TABLE options ADD COLUMN a_rappel_utilisation INTEGER DEFAULT 0;";

    private static final String DATABASE_DUREE_RAPPEL_UTILISATION_V2 = "ALTER TABLE options ADD COLUMN duree_rappel_utilisation INTEGER DEFAULT 0;";

    private static final String DATABASE_A_NOTATION_V2 = "ALTER TABLE options ADD COLUMN a_notation INTEGER DEFAULT 0;";

    private static final String DATABASE_RAPPEL_NOTATION_V2 = "ALTER TABLE version ADD COLUMN rappel_notation INTEGER DEFAULT 1;";

    private static final String DATABASE_A_BROADCAST_INSTAVOX = "ALTER TABLE options ADD COLUMN a_broadcast_instavox INTEGER DEFAULT 0;";

    private static final String DATABASE_A_INTERFACE_ALTERNATIVE = "ALTER TABLE options ADD COLUMN a_interface_alternative INTEGER DEFAULT 0;";

    // Version 38, database version 7
    private static final String DATABASE_A_SCENARIO_JOUR_NUIT = "ALTER TABLE options ADD COLUMN a_scenario_jour_nuit INTEGER DEFAULT 0;";
    private static final String DATABASE_HEURE_DEBUT_SCENARIO_JOUR_NUIT = "ALTER TABLE options ADD COLUMN heure_debut_scenario_jour_nuit VARCHAR(5) DEFAULT NULL;";
    private static final String DATABASE_HEURE_FIN_SCENARIO_JOUR_NUIT = "ALTER TABLE options ADD COLUMN heure_fin_scenario_jour_nuit VARCHAR(5) DEFAULT NULL;";

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onCreate(sqLiteDatabase);
        Log.e("SQLITE", "Updating table from " + oldVersion + " to " + newVersion);

        if (oldVersion < 2) {

            try {
                sqLiteDatabase.execSQL(DATABASE_DUREE_RAPPEL_UTILISATION_V2);
                sqLiteDatabase.execSQL(DATABASE_A_RAPPEL_UTILISATION_V2);
                sqLiteDatabase.execSQL(DATABASE_A_NOTATION_V2);
                sqLiteDatabase.execSQL(DATABASE_RAPPEL_NOTATION_V2);
            } catch (Exception ignored) {

            }
        } else if (oldVersion < 3) {
            sqLiteDatabase.execSQL(EcranManager.CREATE_TABLE_ETAT);
        } else if (oldVersion < 4) {

            // Meme requetes du à un downgrade
            try { // Doublons possibles, évite le crash
                sqLiteDatabase.execSQL(DATABASE_DUREE_RAPPEL_UTILISATION_V2);
                sqLiteDatabase.execSQL(DATABASE_A_RAPPEL_UTILISATION_V2);
                sqLiteDatabase.execSQL(DATABASE_A_NOTATION_V2);
                sqLiteDatabase.execSQL(DATABASE_RAPPEL_NOTATION_V2);
            } catch (Exception ignored) {

            }
        } else if (oldVersion < 5) {
            try {
                sqLiteDatabase.execSQL(DATABASE_A_BROADCAST_INSTAVOX);
            } catch (Exception ignored) {

            }
        } else if (oldVersion < 6) {
            try {
                sqLiteDatabase.execSQL(DATABASE_A_INTERFACE_ALTERNATIVE);
            } catch (Exception ignored) {

            }
        } else if (oldVersion < 7) {
            try {
                sqLiteDatabase.execSQL(DATABASE_A_SCENARIO_JOUR_NUIT);
                sqLiteDatabase.execSQL(DATABASE_HEURE_DEBUT_SCENARIO_JOUR_NUIT);
                sqLiteDatabase.execSQL(DATABASE_HEURE_FIN_SCENARIO_JOUR_NUIT);
            } catch (Exception ignored) {

            }
            // Mise à jour de la base de données
            // méthode appelée sur incrémentation de DATABASE_VERSION
            // on peut faire ce qu'on veut ici, comme recréer la base :

        }
    }

} // class MySQLite
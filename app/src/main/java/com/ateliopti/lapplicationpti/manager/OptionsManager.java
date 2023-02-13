package com.ateliopti.lapplicationpti.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.Options;


public class OptionsManager {

    private static final String TABLE_NAME = "options";
    private static final String KEY_ID_OPTIONS = "id";
    private static final String KEY_A_TRAJET_OPTIONS = "a_trajet";
    private static final String KEY_A_BATIMENT_OPTIONS = "a_batiment";
    private static final String KEY_A_IZSR_OPTIONS = "a_izsr";
    private static final String KEY_PUISSANCE_SONORE_OPTIONS = "puissance_sonore";
    private static final String KEY_LOCALISATION_SONORE_OPTIONS = "localisation_sonore";
    private static final String KEY_LOGO_OPTIONS = "logo";
    private static final String KEY_ACTIVATION_PTI_AUTOMATIQUE = "activation_pti_automatique";
    private static final String KEY_SOLLICITATION = "sollicitation";
    private static final String KEY_HISTORIQUE_SUPERVISION = "historique_supervision";
    private static final String KEY_A_TUTORIEL = "a_tutoriel";
    private static final String KEY_SCENARIO_EXCEPTIONNEL = "scenario_exceptionnel";
    private static final String KEY_LOCALISATION_AUDIO = "localisation_audio";
    private static final String KEY_A_RAPPEL_UTILISATION = "a_rappel_utilisation";
    private static final String KEY_DUREE_RAPPEL_UTILISATION = "duree_rappel_utilisation";
    private static final String KEY_A_NOTATION = "a_notation";
    private static final String KEY_A_BROADCAST_INSTAVOX = "a_broadcast_instavox";
    private static final String KEY_A_INTERFACE_ALTERNATIVE = "a_interface_alternative";
    private static final String KEY_A_SCENARIO_JOUR_NUIT = "a_scenario_jour_nuit";

    private static final String KEY_HEURE_DEBUT_SCENARIO_JOUR_NUIT = "heure_debut_scenario_jour_nuit";
    private static final String KEY_HEURE_FIN_SCENARIO_JOUR_NUIT = "heure_fin_scenario_jour_nuit";

    public static final String CREATE_TABLE_OPTIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID_OPTIONS + " INTEGER primary key," +
            " " + KEY_A_TRAJET_OPTIONS + " INTEGER DEFAULT 0," +
            " " + KEY_A_BATIMENT_OPTIONS + " INTEGER DEFAULT 0," +
            " " + KEY_A_IZSR_OPTIONS + " INTEGER DEFAULT 0," +
            " " + KEY_PUISSANCE_SONORE_OPTIONS + " INTEGER," +
            " " + KEY_LOCALISATION_SONORE_OPTIONS + " INTEGER DEFAULT 0," +
            " " + KEY_LOGO_OPTIONS + " TEXT, " +
            " " + KEY_ACTIVATION_PTI_AUTOMATIQUE + " INTEGER DEFAULT 0," +
            " " + KEY_SOLLICITATION + " INTEGER DEFAULT 0," +
            " " + KEY_HISTORIQUE_SUPERVISION + " INTEGER DEFAULT 0," +
            " " + KEY_A_TUTORIEL + " INTEGER DEFAULT 0," +
            " " + KEY_SCENARIO_EXCEPTIONNEL + " INTEGER DEFAULT 0," +
            " " + KEY_LOCALISATION_AUDIO + " INTEGER DEFAULT 0," +
            " " + KEY_A_RAPPEL_UTILISATION + " INTEGER DEFAULT 0," +
            " " + KEY_DUREE_RAPPEL_UTILISATION + " INTEGER DEFAULT 0," +
            " " + KEY_A_NOTATION + " INTEGER DEFAULT 0, " +
            " " + KEY_A_BROADCAST_INSTAVOX + " INTEGER DEFAULT 0, " +
            " " + KEY_A_INTERFACE_ALTERNATIVE + " INTEGER DEFAULT 0, " +
            " " + KEY_A_SCENARIO_JOUR_NUIT + " INTEGER DEFAULT 0, " +
            " " + KEY_HEURE_DEBUT_SCENARIO_JOUR_NUIT + " VARCHAR(5) DEFAULT NULL, " +
            " " + KEY_HEURE_FIN_SCENARIO_JOUR_NUIT + " VARCHAR(5) DEFAULT NULL " +

            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public OptionsManager(Context context) {
        maBaseSQLite = MySQLite.getInstance(context);
    }

    public void open() {
        //on ouvre la table en lecture/écriture
        db = maBaseSQLite.getWritableDatabase();
    }

    public void close() {
        //on ferme l'accès à la BDD
        db.close();
    }

    // Vérifie si il y a une table configuration créée
    public boolean exists() {
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count > 0;
    }

    public long inserer(Options options) {
        ContentValues values = new ContentValues();

        values.put(KEY_A_TRAJET_OPTIONS, options.isaTrajet());
        values.put(KEY_A_BATIMENT_OPTIONS, options.isaBatiment());
        values.put(KEY_A_IZSR_OPTIONS, options.isaIzsr());
        values.put(KEY_PUISSANCE_SONORE_OPTIONS, options.getPuissanceSonore());
        values.put(KEY_LOCALISATION_SONORE_OPTIONS, options.isLocalisationSonore());
        values.put(KEY_LOGO_OPTIONS, options.getLogo());
        values.put(KEY_ACTIVATION_PTI_AUTOMATIQUE, options.isActivationPTIAutomatique());
        values.put(KEY_SOLLICITATION, options.isSollicitation());
        values.put(KEY_HISTORIQUE_SUPERVISION, options.isHistoriqueSupervision());
        values.put(KEY_A_TUTORIEL, options.isaTutoriel());
        values.put(KEY_SCENARIO_EXCEPTIONNEL, options.getScenarioExceptionnel());
        values.put(KEY_LOCALISATION_AUDIO, options.isLocalisationAudio());
        values.put(KEY_A_RAPPEL_UTILISATION, options.isaRappelUtilisation());
        values.put(KEY_DUREE_RAPPEL_UTILISATION, options.getDureeRappelUtilisation());
        values.put(KEY_A_NOTATION, options.isaNotation());
        values.put(KEY_A_BROADCAST_INSTAVOX, options.isaBroadcastInstavox());
        values.put(KEY_A_INTERFACE_ALTERNATIVE, options.isaInterfaceAlternative());
        values.put(KEY_A_SCENARIO_JOUR_NUIT, options.isaScenarioJourNuit());
        values.put(KEY_HEURE_DEBUT_SCENARIO_JOUR_NUIT, options.getHeureDebutScenarioJourNuit());
        values.put(KEY_HEURE_FIN_SCENARIO_JOUR_NUIT, options.getHeureFinScenarioJourNuit());

        return db.insert(TABLE_NAME, null, values);
    }

    public Options getOptions() {
            Cursor c = db.query(TABLE_NAME,
                new String[]{
                        KEY_ID_OPTIONS,
                        KEY_A_TRAJET_OPTIONS,
                        KEY_A_BATIMENT_OPTIONS,
                        KEY_A_IZSR_OPTIONS,
                        KEY_PUISSANCE_SONORE_OPTIONS,
                        KEY_LOCALISATION_SONORE_OPTIONS,
                        KEY_LOGO_OPTIONS,
                        KEY_ACTIVATION_PTI_AUTOMATIQUE,
                        KEY_SOLLICITATION,
                        KEY_HISTORIQUE_SUPERVISION,
                        KEY_A_TUTORIEL,
                        KEY_SCENARIO_EXCEPTIONNEL,
                        KEY_LOCALISATION_AUDIO,
                        KEY_A_RAPPEL_UTILISATION,
                        KEY_DUREE_RAPPEL_UTILISATION,
                        KEY_A_NOTATION,
                        KEY_A_BROADCAST_INSTAVOX,
                        KEY_A_INTERFACE_ALTERNATIVE,
                        KEY_A_SCENARIO_JOUR_NUIT,
                        KEY_HEURE_DEBUT_SCENARIO_JOUR_NUIT,
                        KEY_HEURE_FIN_SCENARIO_JOUR_NUIT
                },
                "ID = 1", null, null, null, null);
        return cursorToOptions(c);
    }

    public void updateOptions(Options options) {
        ContentValues values = new ContentValues();
        values.put(KEY_A_TRAJET_OPTIONS, options.isaTrajet());
        values.put(KEY_A_BATIMENT_OPTIONS, options.isaBatiment());
        values.put(KEY_A_IZSR_OPTIONS, options.isaIzsr());
        values.put(KEY_PUISSANCE_SONORE_OPTIONS, options.getPuissanceSonore());
        values.put(KEY_LOCALISATION_SONORE_OPTIONS, options.isLocalisationSonore());
        values.put(KEY_LOGO_OPTIONS, options.getLogo());
        values.put(KEY_ACTIVATION_PTI_AUTOMATIQUE, options.isActivationPTIAutomatique());
        values.put(KEY_SOLLICITATION, options.isSollicitation());
        values.put(KEY_HISTORIQUE_SUPERVISION, options.isHistoriqueSupervision());
        values.put(KEY_A_TUTORIEL, options.isaTutoriel());
        values.put(KEY_SCENARIO_EXCEPTIONNEL, options.getScenarioExceptionnel());
        values.put(KEY_LOCALISATION_AUDIO, options.isLocalisationAudio());
        values.put(KEY_A_RAPPEL_UTILISATION, options.isaRappelUtilisation());
        values.put(KEY_DUREE_RAPPEL_UTILISATION, options.getDureeRappelUtilisation());
        values.put(KEY_A_NOTATION, options.isaNotation());
        values.put(KEY_A_BROADCAST_INSTAVOX, options.isaBroadcastInstavox());
        values.put(KEY_A_INTERFACE_ALTERNATIVE, options.isaInterfaceAlternative());
        values.put(KEY_A_SCENARIO_JOUR_NUIT, options.isaScenarioJourNuit());
        values.put(KEY_HEURE_DEBUT_SCENARIO_JOUR_NUIT, options.getHeureDebutScenarioJourNuit());
        values.put(KEY_HEURE_FIN_SCENARIO_JOUR_NUIT, options.getHeureFinScenarioJourNuit());

        db.update(TABLE_NAME, values, "ID = 1", null);
    }


    private Options cursorToOptions(Cursor c) {


        if (c.getCount() == 0) {
            return null;  // Si aucun élément n'a été retourné dans la requête, on renvoie null
        }


        c.moveToFirst(); // Sinon on se place sur le premier élément

        Options options = new Options();

        // Id
        options.setId(c.getInt(0));

        // A Trajet
        boolean setaTrajet = c.getInt(1) > 0;
        options.setaTrajet(setaTrajet);

        // A Batiment
        boolean setaBatiment = c.getInt(2) > 0;
        options.setaBatiment(setaBatiment);

        // A IZSR
        boolean setaIzsr = c.getInt(3) > 0;
        options.setaIzsr(setaIzsr);

        // Puissance sonore
        options.setPuissanceSonore(c.getInt(4));

        // Localisation sonore
        boolean setLocalisationSonore = c.getInt(5) > 0;
        options.setLocalisationSonore(setLocalisationSonore);

        // Logo
        options.setLogo(c.getString(6));

        // Activation PTI Automatique
        boolean setActivationPTIAutomatique = c.getInt(7) > 0;
        options.setActivationPTIAutomatique(setActivationPTIAutomatique);

        // Sollicitation
        boolean setSollicitation = c.getInt(8) > 0;
        options.setSollicitation(setSollicitation);

        // Historique supervision
        boolean setHistoriqueSupervision = c.getInt(9) > 0;
        options.setHistoriqueSupervision(setHistoriqueSupervision);

        // A Tutoriel
        boolean setaTutoriel = c.getInt(10) > 0;
        options.setaTutoriel(setaTutoriel);

        // Scénario exceptionnel
        options.setScenarioExceptionnel(c.getInt(11));

        // Localisation audio
        boolean setLocalisationAudio = c.getInt(12) > 0;
        options.setLocalisationAudio(setLocalisationAudio);


        boolean setaRappelUtilisation = c.getInt(13) > 0;
        options.setaRappelUtilisation(setaRappelUtilisation);

        // Durée rappel utilisation
        options.setDureeRappelUtilisation(c.getInt(14));


        boolean setaNotation = c.getInt(15) > 0;
        options.setaNotation(setaNotation);

        boolean setaBroadcastInstavox = c.getInt(16) > 0;
        options.setaBroadcastInstavox(setaBroadcastInstavox);

        boolean setaInterfaceAlternative = c.getInt(17) > 0;
        options.setaInterfaceAlternative(setaInterfaceAlternative);

        boolean setaScenarioJourNuit = c.getInt(18) > 0;
        options.setaScenarioJourNuit(setaScenarioJourNuit);

        options.setHeureDebutScenarioJourNuit(c.getString(19));

        options.setHeureFinScenarioJourNuit(c.getString(20));

        c.close();

        return options;
    }


}
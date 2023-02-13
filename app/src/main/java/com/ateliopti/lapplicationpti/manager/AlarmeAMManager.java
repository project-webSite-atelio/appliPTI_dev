package com.ateliopti.lapplicationpti.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.AlarmeAM;


public class AlarmeAMManager {

    private static final String TABLE_NAME = "alarme_am";
    private static final String KEY_ID = "id";
    private static final String KEY_A_ALARME_AM = "a_alarme_am";
    private static final String KEY_AM_DUREE_DETECTION = "am_duree_detection";
    private static final String KEY_AM_DUREE_NOTIFICATION = "am_duree_notification";
    private static final String KEY_AM_TYPE_NOTIF = "am_type_notif";
    private static final String KEY_AM_ANNULATION = "am_annulation";


    public static final String CREATE_TABLE_ALARME_AM = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID + " INTEGER primary key," +
            " " + KEY_A_ALARME_AM + " INTEGER DEFAULT 0," +
            " " + KEY_AM_DUREE_DETECTION + " INTEGER," +
            " " + KEY_AM_DUREE_NOTIFICATION + " INTEGER," +
            " " + KEY_AM_TYPE_NOTIF + " TEXT," +
            " " + KEY_AM_ANNULATION + " INTEGER DEFAULT 0" +
            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public AlarmeAMManager(Context context) {
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

    public long inserer(AlarmeAM alarmeAM) {
        ContentValues values = new ContentValues();
        values.put(KEY_A_ALARME_AM, alarmeAM.isaAlarmeAm());
        values.put(KEY_AM_DUREE_DETECTION, alarmeAM.getAmDureeDetection());
        values.put(KEY_AM_DUREE_NOTIFICATION, alarmeAM.getAmDureeNotification());
        values.put(KEY_AM_TYPE_NOTIF, alarmeAM.getAmTypeNotif());
        values.put(KEY_AM_ANNULATION, alarmeAM.isAmAnnulation());
        return db.insert(TABLE_NAME, null, values);
    }

    public AlarmeAM getAlarmeAM() {
        Cursor c = db.query(TABLE_NAME,
                new String[]{
                        KEY_ID,
                        KEY_A_ALARME_AM,
                        KEY_AM_DUREE_DETECTION,
                        KEY_AM_DUREE_NOTIFICATION,
                        KEY_AM_TYPE_NOTIF,
                        KEY_AM_ANNULATION
                },
                "ID = 1", null, null, null, null);
        return cursorToAlarmeAM(c);
    }

    public void updateAlarmeAM(AlarmeAM alarmeAM) {
        ContentValues values = new ContentValues();

        values.put(KEY_A_ALARME_AM, alarmeAM.isaAlarmeAm());
        values.put(KEY_AM_DUREE_DETECTION, alarmeAM.getAmDureeDetection());
        values.put(KEY_AM_DUREE_NOTIFICATION, alarmeAM.getAmDureeNotification());
        values.put(KEY_AM_TYPE_NOTIF, alarmeAM.getAmTypeNotif());
        values.put(KEY_AM_ANNULATION, alarmeAM.isAmAnnulation());

        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    private AlarmeAM cursorToAlarmeAM(Cursor c) {

        if (c.getCount() == 0) {
            return null;  // Si aucun élément n'a été retourné dans la requête, on renvoie null
        }

        c.moveToFirst(); // Sinon on se place sur le premier élément

        AlarmeAM alarmeAM = new AlarmeAM();

        alarmeAM.setId(c.getInt(0));

        boolean setaAlarmeAgression = c.getInt(1) > 0;
        alarmeAM.setaAlarmeAm(setaAlarmeAgression);

        alarmeAM.setAmDureeDetection(c.getInt(2));
        alarmeAM.setAmDureeNotification(c.getInt(3));

        alarmeAM.setAmTypeNotif(c.getString(4));

        boolean setAmAnnulation = c.getInt(5) > 0;
        alarmeAM.setAmAnnulation(setAmAnnulation);

        c.close();

        return alarmeAM;
    }


}
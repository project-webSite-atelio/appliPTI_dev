package com.ateliopti.lapplicationpti.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.AlarmeAgression;

public class AlarmeAgressionManager {

    private static final String TABLE_NAME = "alarme_agression";
    private static final String KEY_ID = "id";
    private static final String KEY_A_ALARME_AGRESSION = "a_alarme_agression";
    private static final String KEY_AGRESSION_DUREE_DETECTION = "agression_duree_detection";
    private static final String KEY_AGRESSION_DUREE_NOTIFICATION = "agression_duree_notification";
    private static final String KEY_AGRESSION_TYPE_NOTIF = "agression_type_notif";
    private static final String KEY_AGRESSION_CODE_ANNULATION = "agression_code_annulation";


    public static final String CREATE_TABLE_ALARME_AGRESSION = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID + " INTEGER primary key," +
            " " + KEY_A_ALARME_AGRESSION + " INTEGER DEFAULT 0," +
            " " + KEY_AGRESSION_DUREE_DETECTION + " INTEGER," +
            " " + KEY_AGRESSION_DUREE_NOTIFICATION + " INTEGER," +
            " " + KEY_AGRESSION_TYPE_NOTIF + " TEXT," +
            " " + KEY_AGRESSION_CODE_ANNULATION + " TEXT" +
            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public AlarmeAgressionManager(Context context) {
        maBaseSQLite = MySQLite.getInstance(context);
    }

    public long inserer(AlarmeAgression alarmeAgression) {
        ContentValues values = new ContentValues();
        values.put(KEY_A_ALARME_AGRESSION, alarmeAgression.isaAlarmeAgression());
        values.put(KEY_AGRESSION_DUREE_DETECTION, alarmeAgression.getAgressionDureeDetection());
        values.put(KEY_AGRESSION_DUREE_NOTIFICATION, alarmeAgression.getAgressionDureeNotification());
        values.put(KEY_AGRESSION_TYPE_NOTIF, alarmeAgression.getAgressionTypeNotif());
        values.put(KEY_AGRESSION_CODE_ANNULATION, alarmeAgression.getAgressionCodeAnnulation());

        return db.insert(TABLE_NAME, null, values);
    }

    public AlarmeAgression getAlarmeAgression() {
        Cursor c = db.query(TABLE_NAME,
                new String[]{
                        KEY_ID,
                        KEY_A_ALARME_AGRESSION,
                        KEY_AGRESSION_DUREE_DETECTION,
                        KEY_AGRESSION_DUREE_NOTIFICATION,
                        KEY_AGRESSION_TYPE_NOTIF,
                        KEY_AGRESSION_CODE_ANNULATION
                },
                "ID = 1", null, null, null, null);
        return cursorToAlarmeAgression(c);
    }

    public void updateAlarmeAgression(AlarmeAgression alarmeAgression) {
        ContentValues values = new ContentValues();
        values.put(KEY_A_ALARME_AGRESSION, alarmeAgression.isaAlarmeAgression());
        values.put(KEY_AGRESSION_DUREE_DETECTION, alarmeAgression.getAgressionDureeDetection());
        values.put(KEY_AGRESSION_DUREE_NOTIFICATION, alarmeAgression.getAgressionDureeNotification());
        values.put(KEY_AGRESSION_TYPE_NOTIF, alarmeAgression.getAgressionTypeNotif());
        values.put(KEY_AGRESSION_CODE_ANNULATION, alarmeAgression.getAgressionCodeAnnulation());
        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    private AlarmeAgression cursorToAlarmeAgression(Cursor c) {

        if (c.getCount() == 0) {
            return null;  // Si aucun élément n'a été retourné dans la requête, on renvoie null
        }

        c.moveToFirst(); // Sinon on se place sur le premier élément

        AlarmeAgression alarmeAgression = new AlarmeAgression();
        alarmeAgression.setId(c.getInt(0));

        boolean setaAlarmeAgression = c.getInt(1) > 0;
        alarmeAgression.setaAlarmeAgression(setaAlarmeAgression);

        alarmeAgression.setAgressionDureeDetection(c.getInt(2));
        alarmeAgression.setAgressionDureeNotification(c.getInt(3));

        alarmeAgression.setAgressionTypeNotif(c.getString(4));
        alarmeAgression.setAgressionCodeAnnulation(c.getString(5));

        c.close();

        return alarmeAgression;
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


}

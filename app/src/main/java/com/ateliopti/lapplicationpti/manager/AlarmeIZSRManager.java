package com.ateliopti.lapplicationpti.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.AlarmeIZSR;


public class AlarmeIZSRManager {

    private static final String TABLE_NAME = "alarme_izsr";
    private static final String KEY_ID = "id";
    private static final String KEY_IZSR_DUREE_NOTIFICATION = "izsr_duree_notification";
    private static final String KEY_IZSR_TYPE_NOTIF = "izsr_type_notif";

    public static final String CREATE_TABLE_ALARME_IZSR = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID + " INTEGER primary key," +
            " " + KEY_IZSR_DUREE_NOTIFICATION + " INTEGER," +
            " " + KEY_IZSR_TYPE_NOTIF + " TEXT" +

            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public AlarmeIZSRManager(Context context) {
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

    public long inserer(AlarmeIZSR alarmeIZSR) {
        ContentValues values = new ContentValues();
        values.put(KEY_IZSR_DUREE_NOTIFICATION, alarmeIZSR.getIzsrDureeNotification());
        values.put(KEY_IZSR_TYPE_NOTIF, alarmeIZSR.getIzsrTypeNotif());

        return db.insert(TABLE_NAME, null, values);
    }


    public AlarmeIZSR getAlarmeIZSR() {
        Cursor c = db.query(TABLE_NAME,
                new String[]{
                        KEY_ID,
                        KEY_IZSR_DUREE_NOTIFICATION,
                        KEY_IZSR_TYPE_NOTIF
                },
                "ID = 1", null, null, null, null);
        return cursorToAlarmeIZSR(c);
    }

    public void updateAlarmeIZSR(AlarmeIZSR alarmeIZSR) {
        ContentValues values = new ContentValues();
        values.put(KEY_IZSR_DUREE_NOTIFICATION, alarmeIZSR.getIzsrDureeNotification());
        values.put(KEY_IZSR_TYPE_NOTIF, alarmeIZSR.getIzsrTypeNotif());

        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    private AlarmeIZSR cursorToAlarmeIZSR(Cursor c) {

        if (c.getCount() == 0) {
            return null;  // Si aucun élément n'a été retourné dans la requête, on renvoie null
        }

        c.moveToFirst(); // Sinon on se place sur le premier élément

        AlarmeIZSR alarmeIZSR = new AlarmeIZSR();
        alarmeIZSR.setId(c.getInt(0));
        alarmeIZSR.setIzsrDureeNotification(c.getInt(1));
        alarmeIZSR.setIzsrTypeNotif(c.getString(2));
        c.close();

        return alarmeIZSR;
    }

}
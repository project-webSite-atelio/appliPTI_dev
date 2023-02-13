package com.ateliopti.lapplicationpti.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.AlarmePV;


public class AlarmePVManager {

    private static final String TABLE_NAME = "alarme_pv";
    private static final String KEY_ID = "id";
    private static final String KEY_A_ALARME_PV = "a_alarme_pv";
    private static final String KEY_PV_DUREE_DETECTION = "pv_duree_detection";
    private static final String KEY_PV_ANGLE_DETECTION = "pv_angle_detection";
    private static final String KEY_PV_DUREE_NOTIFICATION = "pv_duree_notification";
    private static final String KEY_PV_TYPE_NOTIF = "pv_type_notif";
    private static final String KEY_PV_ANNULATION = "pv_annulation";


    public static final String CREATE_TABLE_ALARME_PV = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID + " INTEGER primary key," +
            " " + KEY_A_ALARME_PV + " INTEGER DEFAULT 0," +
            " " + KEY_PV_DUREE_DETECTION + " INTEGER," +
            " " + KEY_PV_ANGLE_DETECTION + " INTEGER," +
            " " + KEY_PV_DUREE_NOTIFICATION + " INTEGER," +
            " " + KEY_PV_TYPE_NOTIF + " TEXT," +
            " " + KEY_PV_ANNULATION + " INTEGER DEFAULT 0" +
            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public AlarmePVManager(Context context) {
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

    public long inserer(AlarmePV alarmePV) {
        ContentValues values = new ContentValues();

        values.put(KEY_A_ALARME_PV, alarmePV.isaAlarmePv());
        values.put(KEY_PV_DUREE_DETECTION, alarmePV.getPvDureeDetection());
        values.put(KEY_PV_ANGLE_DETECTION, alarmePV.getPvAngleDetection());
        values.put(KEY_PV_DUREE_NOTIFICATION, alarmePV.getPvDureeNotification());
        values.put(KEY_PV_TYPE_NOTIF, alarmePV.getPvTypeNotif());
        values.put(KEY_PV_ANNULATION, alarmePV.isPvAnnulation());
        return db.insert(TABLE_NAME, null, values);
    }

    public AlarmePV getAlarmePV() {
        Cursor c = db.query(TABLE_NAME,
                new String[]{
                        KEY_ID,
                        KEY_A_ALARME_PV,
                        KEY_PV_DUREE_DETECTION,
                        KEY_PV_ANGLE_DETECTION,
                        KEY_PV_DUREE_NOTIFICATION,
                        KEY_PV_TYPE_NOTIF,
                        KEY_PV_ANNULATION
                },
                "ID = 1", null, null, null, null);
        return cursorToAlarmePV(c);
    }

    public void updateAlarmePV(AlarmePV alarmePV) {
        ContentValues values = new ContentValues();
        values.put(KEY_A_ALARME_PV, alarmePV.isaAlarmePv());
        values.put(KEY_PV_DUREE_DETECTION, alarmePV.getPvDureeDetection());
        values.put(KEY_PV_ANGLE_DETECTION, alarmePV.getPvAngleDetection());
        values.put(KEY_PV_DUREE_NOTIFICATION, alarmePV.getPvDureeNotification());
        values.put(KEY_PV_TYPE_NOTIF, alarmePV.getPvTypeNotif());
        values.put(KEY_PV_ANNULATION, alarmePV.isPvAnnulation());

        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    private AlarmePV cursorToAlarmePV(Cursor c) {

        if (c.getCount() == 0) {
            return null;  // Si aucun élément n'a été retourné dans la requête, on renvoie null
        }

        c.moveToFirst(); // Sinon on se place sur le premier élément

        AlarmePV alarmePV = new AlarmePV();

        alarmePV.setId(c.getInt(0));

        boolean setaAlarmePv = c.getInt(1) > 0;
        alarmePV.setaAlarmePv(setaAlarmePv);

        alarmePV.setPvDureeDetection(c.getInt(2));
        alarmePV.setPvAngleDetection(c.getInt(3));
        alarmePV.setPvDureeNotification(c.getInt(4));
        alarmePV.setPvTypeNotif(c.getString(5));

        boolean setPvAnnulation = c.getInt(6) > 0;
        alarmePV.setPvAnnulation(setPvAnnulation);

        c.close();

        return alarmePV;
    }


}
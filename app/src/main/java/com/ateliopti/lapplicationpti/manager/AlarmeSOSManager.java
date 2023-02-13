package com.ateliopti.lapplicationpti.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.AlarmeSOS;

public class AlarmeSOSManager {

    private static final String TABLE_NAME = "alarme_sos";
    private static final String KEY_ID = "id";
    private static final String KEY_SOS_DECLENCHEMENT_MOUVEMENT = "sos_declenchement_mouvement";
    private static final String KEY_SOS_DECLENCHEMENT_BLUETOOTH = "sos_declenchement_bluetooth";
    private static final String KEY_SOS_DUREE_NOTIFICATION = "sos_duree_notification";
    private static final String KEY_SOS_TYPE_NOTIF = "sos_type_notif";
    private static final String KEY_SOS_CODE_ANNULATION = "sos_code_annulation";

    public static final String CREATE_TABLE_ALARME_SOS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID + " INTEGER primary key," +
            " " + KEY_SOS_DECLENCHEMENT_MOUVEMENT + " TEXT," +
            " " + KEY_SOS_DECLENCHEMENT_BLUETOOTH + " TEXT," +
            " " + KEY_SOS_DUREE_NOTIFICATION + " INTEGER," +
            " " + KEY_SOS_TYPE_NOTIF + " TEXT," +
            " " + KEY_SOS_CODE_ANNULATION + " TEXT" +
            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public AlarmeSOSManager(Context context) {
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

    public long inserer(AlarmeSOS alarmeSOS) {
        ContentValues values = new ContentValues();

        values.put(KEY_SOS_DECLENCHEMENT_MOUVEMENT, alarmeSOS.getSosDeclenchementMouvement());
        values.put(KEY_SOS_DECLENCHEMENT_BLUETOOTH, alarmeSOS.getSosDeclenchementBluetooth());
        values.put(KEY_SOS_DUREE_NOTIFICATION, alarmeSOS.getSosDureeNotification());
        values.put(KEY_SOS_TYPE_NOTIF, alarmeSOS.getSosTypeNotif());
        values.put(KEY_SOS_CODE_ANNULATION, alarmeSOS.getSosCodeAnnulation());

        return db.insert(TABLE_NAME, null, values);
    }

    public AlarmeSOS getAlarmeSOS() {
        Cursor c = db.query(TABLE_NAME,
                new String[]{
                        KEY_ID,
                        KEY_SOS_DECLENCHEMENT_MOUVEMENT,
                        KEY_SOS_DECLENCHEMENT_BLUETOOTH,
                        KEY_SOS_DUREE_NOTIFICATION,
                        KEY_SOS_TYPE_NOTIF,
                        KEY_SOS_CODE_ANNULATION
                },
                "ID = 1", null, null, null, null);
        return cursorToAlarmeSOS(c);
    }

    public void updateAlarmeSOS(AlarmeSOS alarmeSOS) {
        ContentValues values = new ContentValues();
        values.put(KEY_SOS_DECLENCHEMENT_MOUVEMENT, alarmeSOS.getSosDeclenchementMouvement());
        values.put(KEY_SOS_DECLENCHEMENT_BLUETOOTH, alarmeSOS.getSosDeclenchementBluetooth());
        values.put(KEY_SOS_DUREE_NOTIFICATION, alarmeSOS.getSosDureeNotification());
        values.put(KEY_SOS_TYPE_NOTIF, alarmeSOS.getSosTypeNotif());
        values.put(KEY_SOS_CODE_ANNULATION, alarmeSOS.getSosCodeAnnulation());

        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    private AlarmeSOS cursorToAlarmeSOS(Cursor c) {
        if (c.getCount() == 0) {
            return null;  // Si aucun élément n'a été retourné dans la requête, on renvoie null
        }

        c.moveToFirst(); // Sinon on se place sur le premier élément
        AlarmeSOS alarmeSOS = new AlarmeSOS();
        alarmeSOS.setId(c.getInt(0));
        alarmeSOS.setSosDeclenchementMouvement(c.getString(1));
        alarmeSOS.setSosDeclenchementBluetooth(c.getString(2));
        alarmeSOS.setSosDureeNotification(c.getInt(3));
        alarmeSOS.setSosTypeNotif(c.getString(4));
        alarmeSOS.setSosCodeAnnulation(c.getString(5));

        c.close();

        return alarmeSOS;
    }


}
package com.ateliopti.lapplicationpti.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.Trajet;

public class TrajetManager {

    private static final String TABLE_NAME = "trajet";
    private static final String KEY_ID = "id";
    private static final String KEY_DUREE_RECHERCHE_GPS = "duree_recherche_gps";
    private static final String KEY_DUREE_PAUSE_GPS = "duree_pause_gps";
    private static final String KEY_PRECISION_GPS = "precision_gps";


    public static final String CREATE_TABLE_TRAJET = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID + " INTEGER primary key," +
            " " + KEY_DUREE_RECHERCHE_GPS + " INTEGER," +
            " " + KEY_DUREE_PAUSE_GPS + " INTEGER," +
            " " + KEY_PRECISION_GPS + " INTEGER" +
            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public TrajetManager(Context context) {
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


    public long inserer(Trajet trajet) {
        ContentValues values = new ContentValues();

        values.put(KEY_DUREE_RECHERCHE_GPS, trajet.getDureeRechercheGps());
        values.put(KEY_DUREE_PAUSE_GPS, trajet.getDureePauseGps());
        values.put(KEY_PRECISION_GPS, trajet.getPrecisionGps());

        return db.insert(TABLE_NAME, null, values);
    }

    public Trajet getTrajet() {
        Cursor c = db.query(TABLE_NAME,
                new String[]{
                        KEY_ID,
                        KEY_DUREE_RECHERCHE_GPS,
                        KEY_DUREE_PAUSE_GPS,
                        KEY_PRECISION_GPS
                },
                "ID = 1", null, null, null, null);
        return cursorToTrajet(c);
    }

    public void updateTrajet(Trajet trajet) {
        ContentValues values = new ContentValues();
        values.put(KEY_DUREE_RECHERCHE_GPS, trajet.getDureeRechercheGps());
        values.put(KEY_DUREE_PAUSE_GPS, trajet.getDureePauseGps());
        values.put(KEY_PRECISION_GPS, trajet.getPrecisionGps());

        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    private Trajet cursorToTrajet(Cursor c) {

        if (c.getCount() == 0) {
            return null;  // Si aucun élément n'a été retourné dans la requête, on renvoie null
        }

        c.moveToFirst(); // Sinon on se place sur le premier élément

        Trajet trajet = new Trajet();

        trajet.setId(c.getInt(0));
        trajet.setDureeRechercheGps(c.getInt(1));
        trajet.setDureePauseGps(c.getInt(2));
        trajet.setPrecisionGps(c.getInt(3));


        c.close();

        return trajet;
    }



}
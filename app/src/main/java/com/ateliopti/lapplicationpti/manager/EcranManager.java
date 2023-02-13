package com.ateliopti.lapplicationpti.manager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.Ecran;

public class EcranManager {

    private static final String TABLE_NAME = "ecran";
    private static final String KEY_ID = "id";
    private static final String KEY_EST_ACTIF = "est_actif";



    public static final String CREATE_TABLE_ETAT = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID + " INTEGER primary key," +
            " " + KEY_EST_ACTIF + " INTEGER DEFAULT 0" +
            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public EcranManager(Context context) {
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


    public long count() {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        SQLiteStatement statement = db.compileStatement(sql);
        return statement.simpleQueryForLong();
    }

    public long inserer(boolean ecran) {

        ContentValues values = new ContentValues();
        values.put(KEY_ID, 1);
        values.put(KEY_EST_ACTIF, ecran);

        return db.insert(TABLE_NAME, null, values);

    }


    public void updateEcran(Ecran ecran) {
        ContentValues values = new ContentValues();
        values.put(KEY_EST_ACTIF, ecran.isEstActif());
        System.out.println("@atelio : etat pti = " + ecran);
        db.update(TABLE_NAME, values, "ID = 1", null);
    }

/*
    public long vider() {
        return db.delete(TABLE_NAME, null, null);
    }
    */

    public Ecran getEcran() {
        Cursor c = db.query(TABLE_NAME,
                new String[]{
                        KEY_ID,
                        KEY_EST_ACTIF
                },
                "ID = 1", null, null, null, null);
        return cursorToEcran(c);
    }

    private Ecran cursorToEcran(Cursor c) {
        if (c.getCount() == 0) {
            return null;  // Si aucun élément n'a été retourné dans la requête, on renvoie null
        }
        c.moveToFirst(); // Sinon on se place sur le premier élément

        Ecran ecran = new Ecran();

        ecran.setId(c.getInt(0));
        boolean setEstActif = c.getInt(1) > 0;
        ecran.setEstActif(setEstActif);

        c.close();

        return ecran;
    }


}
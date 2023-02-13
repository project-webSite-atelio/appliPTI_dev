package com.ateliopti.lapplicationpti.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.Etat;

public class EtatManager {

    private static final String TABLE_NAME = "etat";
    private static final String KEY_ID = "id";
    private static final String KEY_ACTIVATION = "activation";
    private static final String KEY_DERNIERE_DESACTIVATION = "derniere_desactivation";
    private static final String KEY_FIN_CHARGE = "fin_charge";


    public static final String CREATE_TABLE_ETAT = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID + " INTEGER primary key," +
            " " + KEY_ACTIVATION + " INTEGER DEFAULT 0," +
            " " + KEY_DERNIERE_DESACTIVATION + " TEXT," +
            " " + KEY_FIN_CHARGE + " INTEGER DEFAULT 0" +
            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public EtatManager(Context context) {
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

    public long inserer(Etat etat) {

        ContentValues values = new ContentValues();
        values.put(KEY_ID, 1);
        values.put(KEY_ACTIVATION, etat.isActivation());
        values.put(KEY_DERNIERE_DESACTIVATION, etat.getDerniereDesactivation());
        values.put(KEY_FIN_CHARGE, etat.isFinCharge());

        return db.insert(TABLE_NAME, null, values);

    }


    public void updateEtat(boolean etat) {
        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVATION, etat);
        System.out.println("@atelio : etat pti = " + etat);
        db.update(TABLE_NAME, values, "ID = 1", null);
    }


    public void updateDate(String date) {
        ContentValues values = new ContentValues();
        values.put(KEY_DERNIERE_DESACTIVATION, date);
        System.out.println("@atelio : date derniere desactivation = " + date);
        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    public void updateFinCharge(boolean etat) {
        ContentValues values = new ContentValues();
        values.put(KEY_FIN_CHARGE, etat);
        db.update(TABLE_NAME, values, "ID = 1", null);
    }

/*
    public long vider() {
        return db.delete(TABLE_NAME, null, null);
    }
    */

    public Etat getEtat() {
        Cursor c = db.query(TABLE_NAME,
                new String[]{
                        KEY_ID,
                        KEY_ACTIVATION,
                        KEY_DERNIERE_DESACTIVATION,
                        KEY_FIN_CHARGE
                },
                "ID = 1", null, null, null, null);
        return cursorToEtat(c);
    }

    private Etat cursorToEtat(Cursor c) {
        if (c.getCount() == 0) {
            return null;  // Si aucun élément n'a été retourné dans la requête, on renvoie null
        }
        c.moveToFirst(); // Sinon on se place sur le premier élément

        Etat etat = new Etat();

        etat.setId(c.getInt(0));
        boolean setActivation = c.getInt(1) > 0;
        etat.setActivation(setActivation);
        etat.setDerniereDesactivation(c.getString(2));

        boolean setFinCharge = c.getInt(3) > 0;
        etat.setFinCharge(setFinCharge);

        c.close();

        return etat;
    }


}
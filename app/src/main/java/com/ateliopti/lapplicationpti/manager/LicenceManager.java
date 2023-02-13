package com.ateliopti.lapplicationpti.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.Licence;

public class LicenceManager {
    private static final String TABLE_NAME = "licence";
    private static final String KEY_ID_LICENCE = "id";
    private static final String KEY_PREMIERE_CONFIGURATION = "premiere_configuration";

    public static final String CREATE_TABLE_LICENCE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID_LICENCE + " INTEGER primary key," +
            " " + KEY_PREMIERE_CONFIGURATION + " INTEGER DEFAULT 0" +
            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public LicenceManager(Context context) {
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

    public void insertLicence(Licence licence) {
        // Ajout d'un enregistrement dans la table
        ContentValues values = new ContentValues();
        values.put(KEY_ID_LICENCE, 0);
        values.put(KEY_PREMIERE_CONFIGURATION, true);
        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        db.insert(TABLE_NAME, null, values);
    }




}
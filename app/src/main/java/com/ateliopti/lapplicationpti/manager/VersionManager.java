package com.ateliopti.lapplicationpti.manager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.Version;

public class VersionManager {

    private static final String TABLE_NAME = "version";
    private static final String KEY_ID = "id";
    private static final String KEY_DERNIERE_VERSION = "derniere_version";
    private static final String KEY_OBSOLETE = "obsolete";
    private static final String KEY_RAPPEL_NOTATION = "rappel_notation";

    public static final String CREATE_TABLE_VERSION = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID + " INTEGER primary key," +
            " " + KEY_DERNIERE_VERSION + " INTEGER DEFAULT 0," +
            " " + KEY_OBSOLETE + " INTEGER DEFAULT 0, " +
            " " + KEY_RAPPEL_NOTATION + " INTEGER DEFAULT 1" +


            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public VersionManager(Context context) {
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

    public long inserer(Version version) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, 1);
        values.put(KEY_DERNIERE_VERSION, version.getVersion());
        values.put(KEY_OBSOLETE, version.isObsolete());
        values.put(KEY_RAPPEL_NOTATION, version.isRappelNotation());


        return db.insert(TABLE_NAME, null, values);
    }


    public void setVersion(int numero) {
        ContentValues values = new ContentValues();
        values.put(KEY_DERNIERE_VERSION, numero);
        db.update(TABLE_NAME, values, "ID = 1", null);
    }


    public void updateVersion(boolean obsolete) {
        ContentValues values = new ContentValues();
        values.put(KEY_OBSOLETE, obsolete);
        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    public void updateFinCharge(boolean etat) {
        ContentValues values = new ContentValues();
        values.put(KEY_DERNIERE_VERSION, etat);
        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    public void updateRappelNotation(boolean rappelNotation) {
        ContentValues values = new ContentValues();
        values.put(KEY_RAPPEL_NOTATION, rappelNotation);
        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    public long vider() {
        return db.delete(TABLE_NAME, null, null);
    }

    public Version getVersion() {
        Cursor c = db.query(TABLE_NAME,
                new String[]{
                        KEY_ID,
                        KEY_DERNIERE_VERSION,
                        KEY_OBSOLETE,
                        KEY_RAPPEL_NOTATION
                },
                "ID = 1", null, null, null, null);
        return cursorToVersion(c);
    }

    private Version cursorToVersion(Cursor c) {
        if (c.getCount() == 0) {
            return null;  // Si aucun élément n'a été retourné dans la requête, on renvoie null
        }
        c.moveToFirst(); // Sinon on se place sur le premier élément
        Version version = new Version();
        version.setId(c.getInt(0));
        version.setVersion(c.getInt(1));

        boolean setObsolete = c.getInt(2) > 0;
        version.setObsolete(setObsolete);

        boolean setRappelNotation = c.getInt(3) > 0;
        version.setRappelNotation(setRappelNotation);


        c.close();

        return version;
    }

}

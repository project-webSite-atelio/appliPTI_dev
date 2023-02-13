package com.ateliopti.lapplicationpti.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.TempLocalisation;

import java.util.ArrayList;

public class TempLocalisationManager {

    private static final String TABLE_NAME = "temp_localisation";
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_SITE = "site";

    public static final String CREATE_TABLE_TEMP_LOCALISATION = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID + " INTEGER primary key," +
            " " + KEY_DATE + " TEXT, " +
            " " + KEY_LATITUDE + " TEXT, " +
            " " + KEY_LONGITUDE + " TEXT," +
            " " + KEY_SITE + " TEXT" +

            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public TempLocalisationManager(Context context) {
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

    public long inserer(TempLocalisation tempLocalisation) {
        ContentValues values = new ContentValues();

        values.put(KEY_DATE, tempLocalisation.getDate());
        values.put(KEY_LATITUDE, tempLocalisation.getLatitude());
        values.put(KEY_LONGITUDE, tempLocalisation.getLongitude());
        values.put(KEY_SITE, tempLocalisation.getSite());

        return db.insert(TABLE_NAME, null, values);
    }


    public void updateTempLocalisation(TempLocalisation tempLocalisation) {
        ContentValues values = new ContentValues();

        values.put(KEY_DATE, tempLocalisation.getDate());
        values.put(KEY_LATITUDE, tempLocalisation.getLatitude());
        values.put(KEY_LONGITUDE, tempLocalisation.getLongitude());
        values.put(KEY_SITE, tempLocalisation.getSite());

        db.update(TABLE_NAME, values, "ID = 1", null);
    }


    public long vider() {
        return db.delete(TABLE_NAME, null, null);
    }

    public TempLocalisation getTempLocalisation() {
        Cursor c = db.query(TABLE_NAME,
                new String[]{
                        KEY_ID,
                        KEY_DATE,
                        KEY_LATITUDE,
                        KEY_LONGITUDE,
                        KEY_SITE
                },
                "ID = 1", null, null, null, null);
        return cursorToTempLocalisation(c);
    }

    private TempLocalisation cursorToTempLocalisation(Cursor c) {
        if (c.getCount() == 0) {
            return null;  // Si aucun élément n'a été retourné dans la requête, on renvoie null
        }
        c.moveToFirst(); // Sinon on se place sur le premier élément

        TempLocalisation tempLocalisation = new TempLocalisation();

        tempLocalisation.setId(c.getInt(0));
        tempLocalisation.setDate(c.getString(1));
        tempLocalisation.setLatitude(c.getString(2));
        tempLocalisation.setLongitude(c.getString(3));
        tempLocalisation.setSite(c.getString(4));

        c.close();

        return tempLocalisation;
    }

    public String[] getSites() {
        Cursor cursor = maBaseSQLite.getReadableDatabase().rawQuery("SELECT nom FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("nom")));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }


}
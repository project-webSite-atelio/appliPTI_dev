package com.ateliopti.lapplicationpti.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.ateliopti.lapplicationpti.MySQLite;
import com.ateliopti.lapplicationpti.model.Site;

import java.util.ArrayList;


public class SiteManager {

    private static final String TABLE_NAME = "site";
    private static final String KEY_ID_SITE = "id";
    private static final String KEY_NOM_SITE = "nom";

    public static final String CREATE_TABLE_SITE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID_SITE + " INTEGER primary key," +
            " " + KEY_NOM_SITE + " TEXT" +
            ");";

    private final MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public SiteManager(Context context) {
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

    public long inserer(Site site) {
        ContentValues values = new ContentValues();

        values.put(KEY_NOM_SITE, site.getNom());
        return db.insert(TABLE_NAME, null, values);
    }

    public long vider(){
        return db.delete(TABLE_NAME, null, null);
    }

    public Site getSite(int id) {
        Cursor c = db.query(TABLE_NAME,
                new String[]{
                        KEY_ID_SITE,
                        KEY_NOM_SITE
                },
                "ID = " + id, null, null, null, null);
        return cursorToSite(c);
    }

    private Site cursorToSite(Cursor c) {

        if (c.getCount() == 0) {
            return null;  // Si aucun élément n'a été retourné dans la requête, on renvoie null
        }

        c.moveToFirst(); // Sinon on se place sur le premier élément

        Site site = new Site();

        site.setId(c.getInt(0));
        site.setNom(c.getString(1));
        c.close();

        return site;
    }

    public String[] getSites(){
        Cursor cursor = maBaseSQLite.getReadableDatabase().rawQuery("SELECT nom FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("nom")));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }


}
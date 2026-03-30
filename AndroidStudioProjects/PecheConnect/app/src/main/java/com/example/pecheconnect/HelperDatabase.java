package com.example.pecheconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "peche_connect.db";
    private static final int DATABASE_VERSION = 1;

    public HelperDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création de la table utilisateur (auth)
        db.execSQL("CREATE TABLE utilisateur (id_user INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password_hash TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS utilisateur");
        onCreate(db);
    }

    // Méthode pour l'enregistrement (MainActivity)
    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password_hash", password);
        long result = db.insert("utilisateur", null, values);
        return result != -1;
    }

    // Méthode pour la connexion (LoginActivity)
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM utilisateur WHERE email=? AND password_hash=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
package com.development.honza.jasichinese.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingsOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String SETTINGS_TABLE_NAME="settings";
    private static final String SETTINGS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS "+SETTINGS_TABLE_NAME+" (id integer primary key autoincrement, path text, speed decimal(5,2))";

    public SettingsOpenHelper(Context context) {
        super(context, SETTINGS_TABLE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SETTINGS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion<4) {
            //db.execSQL(CHARACTERS_ALTER_TABLE_ADD_CATEGORY);
        }
    }

    public void addSettings(Settings settings) {
        ContentValues cv = new ContentValues();
        cv.put("path", settings.getPath());
        cv.put("speed", settings.getSpeed());
        cv.put("id", settings.getId());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(SETTINGS_TABLE_NAME, null, cv);
        db.close();
    }

    public Settings findSettingsById(long id) {
        String query = "select id, speed, path from " + SETTINGS_TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Settings settings = new Settings();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            settings.setId(cursor.getInt(0));
            settings.setSpeed(cursor.getFloat(1));
            settings.setPath(cursor.getString(2));
            cursor.close();
        } else {
            settings = null;
        }
        db.close();
        return settings;
    }

    public void updateSettings (Settings settings) {
        ContentValues cv = new ContentValues();
        cv.put("id", settings.getId());
        cv.put("speed", settings.getSpeed());
        cv.put("path", settings.getPath());

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(SETTINGS_TABLE_NAME, cv, "id = ?", new String[] {settings.getId().toString()});
        db.close();
    }
}

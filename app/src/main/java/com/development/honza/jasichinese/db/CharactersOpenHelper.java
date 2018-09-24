package com.development.honza.jasichinese.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Honza on 7. 3. 2018.
 */

public class CharactersOpenHelper extends SQLiteOpenHelper {
    //private static final int DATABASE_VERSION=1; //verze zmenena na 2 dne 2018-03-18
    private static final int DATABASE_VERSION=1;
    private static final String CHARACTERS_TABLE_NAME="characters";
    private static final String CHARACTERS_TABLE_CREATE=
            "CREATE TABLE IF NOT EXISTS "+CHARACTERS_TABLE_NAME+" (id integer primary key autoincrement, inCzech text not null, inPinyin text not null, inChinese text not null, category text not null)";

    public CharactersOpenHelper(Context context) {
        super(context, CHARACTERS_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CHARACTERS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       if (oldVersion<4) {
            //db.execSQL(CHARACTERS_ALTER_TABLE_ADD_CATEGORY);
        }
    }


    public void addCharacters(Characters characters) {
        ContentValues cv = new ContentValues();
        cv.put("inCzech", characters.getInCzech());
        cv.put("inPinyin", characters.getInPinyin());
        cv.put("inChinese", characters.getInChinese());
        cv.put("category", characters.getCategory());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(CHARACTERS_TABLE_NAME, null, cv);
        db.close();
    }

    public Characters findCharactersById(long id) {
        String query = "select id, inCzech, inPinyin, inChinese, category from " + CHARACTERS_TABLE_NAME + " where id = " + id;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Characters characters = new Characters();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            characters.setId(Integer.parseInt(cursor.getString(0)));
            characters.setInCzech(cursor.getString(1));
            characters.setInPinyin(cursor.getString(2));
            characters.setInChinese(cursor.getString(3));
            characters.setCategory(cursor.getString(4));
            cursor.close();
        } else {
            characters = null;
        }
        db.close();
        return characters;
    }

    public Characters findCharactersByChinese(String chinese) {
        String query = "select id, inCzech, inPinyin, inChinese, category from " + CHARACTERS_TABLE_NAME + " where inChinese = '" + chinese + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Characters characters = new Characters();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            characters.setId(Integer.parseInt(cursor.getString(0)));
            characters.setInCzech(cursor.getString(1));
            characters.setInPinyin(cursor.getString(2));
            characters.setInChinese(cursor.getString(3));
            characters.setCategory(cursor.getString(4));
            cursor.close();
        } else {
            characters = null;
        }
        db.close();
        return characters;
    }

    public boolean deleteRecord(long id) {
        boolean result = false;
        int deletedRows = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        deletedRows = db.delete(CHARACTERS_TABLE_NAME, "id = ?", new String[] {String.valueOf(id)});
        if (deletedRows > 0) {
            result = true;
        }
        db.close();
        return result;
    }

    public boolean deleteAll() {
        boolean result = false;

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CHARACTERS_TABLE_NAME, "id > ?", new String[] {"0"});
        result = true;
        db.close();
        return result;
    }

    public ArrayList<Characters> getAllLCharacters(String category, String poradi, String flashCards) {
        ArrayList<Characters> chars = new ArrayList<Characters>();

        String orderByString;
        String whereString;

        if (category.equals("vše")) {
            whereString = "";
        } else {
            whereString = " where category = '" + category + "'";
        }

        if (poradi.equals("Náhodně")) {
            orderByString = " order by random()";
        } else {
            orderByString = " order by category asc, inCzech asc";
        }
        String selectQuery = "select id, inCzech, inPinyin, inChinese from "+CHARACTERS_TABLE_NAME + whereString + orderByString;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Characters characters = new Characters();
                characters.setId(Integer.parseInt(cursor.getString(0)));

                switch (flashCards) {
                    case "Znaky":
                        characters.setInCzech(cursor.getString(1));
                        characters.setInPinyin(cursor.getString(2));
                        characters.setInChinese(cursor.getString(3));
                        break;
                    case "Pinyin":
                        characters.setInCzech(cursor.getString(1));
                        characters.setInPinyin(cursor.getString(3));
                        characters.setInChinese(cursor.getString(2));
                        break;
                    case "Čeština":
                        characters.setInCzech(cursor.getString(2));
                        characters.setInPinyin(cursor.getString(3));
                        characters.setInChinese(cursor.getString(1));
                        break;
                }
                // Adding Translate to list
                chars.add(characters);
            } while (cursor.moveToNext());
        }

        return chars;
    }

    public ArrayList<Characters> getAllLCharactersByAny(String any) {
        ArrayList<Characters> chars = new ArrayList<Characters>();
        String selectQuery = "select * from "+CHARACTERS_TABLE_NAME + " where category like '%" + any + "%' or inCzech like '%" + any + "%' or inChinese like '%" + any + "%' or inPinyin like '%" + any + "%' COLLATE NOACCENTS";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Characters characters = new Characters();
                characters.setId(Integer.parseInt(cursor.getString(0)));
                characters.setInCzech(cursor.getString(1));
                characters.setInPinyin(cursor.getString(2));
                characters.setInChinese(cursor.getString(3));
                // Adding Translate to list
                chars.add(characters);
            } while (cursor.moveToNext());
        }

        return chars;
    }

    public void updateCharacter(Characters characters) {
        String[] whereArgs = new String[1];
        ContentValues cv = new ContentValues();
        cv.put("inCzech", characters.getInCzech());
        cv.put("inPinyin", characters.getInPinyin());
        cv.put("inChinese", characters.getInChinese());
        cv.put("category", characters.getCategory());

        SQLiteDatabase db = this.getWritableDatabase();

        //whereArgs[0] = String.valueOf(characters.getId());
        db.update(CHARACTERS_TABLE_NAME, cv, "id=" + String.valueOf(characters.getId()), null);
        db.close();
    }

    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        String query = "select distinct category from "+ CHARACTERS_TABLE_NAME +" order by category asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        categories.add(0,"vše");
        return categories;
    }
}

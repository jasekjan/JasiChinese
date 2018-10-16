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

public class WordsOpenHelper extends SQLiteOpenHelper {
    //private static final int DATABASE_VERSION=1; //verze zmenena na 2 dne 2018-03-18
    private static final int DATABASE_VERSION=1;
    private static final String WORDS_TABLE_NAME ="words";
    private static final String WORDS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS "+ WORDS_TABLE_NAME +" (id integer primary key autoincrement, myLang text not null, myReading text not null, myForeign text not null, category text not null)";

    private static final String WORDS_ALTER_TABLE_1 = "ALTER TABLE "+ WORDS_TABLE_NAME + " ADD COLUMN skupina integer default 1, datum_zarazeni date";

    public WordsOpenHelper(Context context) {
        super(context, WORDS_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WORDS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       if (oldVersion<4) {
            //db.execSQL(CHARACTERS_ALTER_TABLE_1);
        }
    }


    public void addCharacters(Words words) {
        ContentValues cv = new ContentValues();
        cv.put("myLang", words.getmyLang());
        cv.put("myReading", words.getmyReading());
        cv.put("myForeign", words.getmyForeign());
        cv.put("category", words.getCategory());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(WORDS_TABLE_NAME, null, cv);
        db.close();
    }

    public Words findCharactersById(long id) {
        String query = "select id, myLang, myReading, myForeign, category from " + WORDS_TABLE_NAME + " where id = " + id;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Words words = new Words();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            words.setId(Integer.parseInt(cursor.getString(0)));
            words.setmyLang(cursor.getString(1));
            words.setmyReading(cursor.getString(2));
            words.setmyForeign(cursor.getString(3));
            words.setCategory(cursor.getString(4));
            cursor.close();
        } else {
            words = null;
        }
        db.close();
        return words;
    }

    public ArrayList<Words> findCharactersByChinese(String chinese) {
        String query = "select id, myLang, myReading, myForeign, category from " + WORDS_TABLE_NAME + " where myForeign = '" + chinese + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Words words = new Words();
        ArrayList<Words> arrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {
                words.setId(Integer.parseInt(cursor.getString(0)));
                words.setmyLang(cursor.getString(1));
                words.setmyReading(cursor.getString(2));
                words.setmyForeign(cursor.getString(3));
                words.setCategory(cursor.getString(4));
                arrayList.add(words);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            words = null;
        }
        db.close();
        return arrayList;
    }

    public ArrayList<Words> findCharactersForInsert(String chinese, String reading) {
        String query = "select id, myLang, myReading, myForeign, category from " + WORDS_TABLE_NAME + " where myForeign = '" + chinese + "' and myReading='" + reading + "'" ;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = { "id", "myLang", "myReading", "myForeign", "category" };
        String selection = "myForeign =? and myReading =?";
        String[] selectionArgs = { chinese, reading }; // matched to "?" in selection
        Cursor cursor = db.query(WORDS_TABLE_NAME, columnsToReturn, selection, selectionArgs, null, null, null);


        //Cursor cursor = db.rawQuery(query, null);

        Words words = new Words();
        ArrayList<Words> arrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {
                words.setId(Integer.parseInt(cursor.getString(0)));
                words.setmyLang(cursor.getString(1));
                words.setmyReading(cursor.getString(2));
                words.setmyForeign(cursor.getString(3));
                words.setCategory(cursor.getString(4));
                arrayList.add(words);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            words = null;
        }
        db.close();
        return arrayList;
    }

    public boolean deleteRecord(long id) {
        boolean result = false;
        int deletedRows = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        deletedRows = db.delete(WORDS_TABLE_NAME, "id = ?", new String[] {String.valueOf(id)});
        if (deletedRows > 0) {
            result = true;
        }
        db.close();
        return result;
    }

    public boolean deleteAll() {
        boolean result = false;

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(WORDS_TABLE_NAME, "id > ?", new String[] {"0"});
        result = true;
        db.close();
        return result;
    }

    public ArrayList<Words> getAllLCharacters(String category, String poradi, String flashCards) {
        ArrayList<Words> wordsArrayList = new ArrayList<Words>();

        String orderByString;
        String whereString;

        if (category.equals("all")) {
            whereString = "";
        } else {
            whereString = " where category = '" + category + "'";
        }

        if (poradi.equals("Náhodně")) {
            orderByString = " order by random()";
        } else {
            orderByString = " order by category asc, myLang asc";
        }
        String selectQuery = "select id, myLang, myReading, myForeign from "+ WORDS_TABLE_NAME + whereString + orderByString;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Words words = new Words();
                words.setId(Integer.parseInt(cursor.getString(0)));

                switch (flashCards) {
                    case "Znaky":
                        words.setmyLang(cursor.getString(1));
                        words.setmyReading(cursor.getString(2));
                        words.setmyForeign(cursor.getString(3));
                        break;
                    case "Pinyin":
                        words.setmyLang(cursor.getString(1));
                        words.setmyReading(cursor.getString(3));
                        words.setmyForeign(cursor.getString(2));
                        break;
                    case "Čeština":
                        words.setmyLang(cursor.getString(2));
                        words.setmyReading(cursor.getString(3));
                        words.setmyForeign(cursor.getString(1));
                        break;
                }
                // Adding Translate to list
                wordsArrayList.add(words);
            } while (cursor.moveToNext());
        }

        return wordsArrayList;
    }

    public ArrayList<Words> getAllLCharactersByAny(String any, String category) {
        ArrayList<Words> wordsArrayList = new ArrayList<Words>();
        String categorySql;
        String whereSql;
        String selectQuery;

        if (category.equals("all")) {
            categorySql = ""; /*" or category like '%" + any + "%'";*/
        } else {
            categorySql = "category = '" + category + "'";
        }

        if (categorySql.equals("")) {
            whereSql = "(myLang like '%" + any + "%' or myForeign like '%" + any + "%' or myReading like '%" + any + "%')";
        } else {
            whereSql = "(myLang like '%" + any + "%' or myForeign like '%" + any + "%' or myReading like '%" + any + "%' ) and " + categorySql;
        }

        selectQuery = "select * from "+ WORDS_TABLE_NAME + " where " + whereSql;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Words words = new Words();
                words.setId(Integer.parseInt(cursor.getString(0)));
                words.setmyLang(cursor.getString(1));
                words.setmyReading(cursor.getString(2));
                words.setmyForeign(cursor.getString(3));
                // Adding Translate to list
                wordsArrayList.add(words);
            } while (cursor.moveToNext());
        }

        return wordsArrayList;
    }

    public void updateCharacter(Words words) {
        String[] whereArgs = new String[1];
        ContentValues cv = new ContentValues();
        cv.put("myLang", words.getmyLang());
        cv.put("myReading", words.getmyReading());
        cv.put("myForeign", words.getmyForeign());
        cv.put("category", words.getCategory());

        SQLiteDatabase db = this.getWritableDatabase();

        //whereArgs[0] = String.valueOf(words.getId());
        db.update(WORDS_TABLE_NAME, cv, "id=" + String.valueOf(words.getId()), null);
        db.close();
    }

    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        String query = "select distinct category from "+ WORDS_TABLE_NAME +" order by category asc";
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

package com.example.wahid.pucrms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "Section_information";
    public static String TABLE_NAME = "data";
    public static String COLUMN_DATA = "Section_data";
    public static String COLUMN_ID    = "id";
    public static int DB_VERSION =    1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "create table "+ TABLE_NAME+" ("+ COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+COLUMN_DATA+" TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(query);
        onCreate(db);

    }

    public void addSection(String section_name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATA,section_name);
        db.insert(TABLE_NAME,null,cv);
    }

    public Cursor getSection()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+COLUMN_DATA+" FROM "+ TABLE_NAME;
        return  db.rawQuery(query,null);
    }
    public  void clearData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE  FROM "+ TABLE_NAME ;
        db.execSQL(query);
    }
}

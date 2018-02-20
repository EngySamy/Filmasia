package com.example.engy.filmasia.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Engy on 2/20/2018.
 */

public class FilmasiaDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME="filmasia.db";
    public static final int VERSION=1;

    public FilmasiaDBHelper(Context context){
        super(context,DB_NAME,null,VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        ///create film table
        final String SQL_CREATE_FILM_TABLE="CREATE TABLE "+
                FilmasiaContract.FilmEntry.TABLE_NAME +" ("+
                FilmasiaContract.FilmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FilmasiaContract.FilmEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                FilmasiaContract.FilmEntry.COLUMN_TYPE + " TEXT , " +
                FilmasiaContract.FilmEntry.COLUMN_RATING + " REAL , " +
                FilmasiaContract.FilmEntry.COLUMN_YEAR + " INTEGER "+ ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FILM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FilmasiaContract.FilmEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}

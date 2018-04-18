package com.example.engy.filmasia.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Engy on 2/20/2018.
 */

public class FilmasiaDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME="filmasia.db";
    public static final int VERSION=4;

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

        // create to watch table
        final String SQL_CREATE_TO_WATCH_TABLE="CREATE TABLE "+
                FilmasiaContract.ToWatchEntry.TABLE_NAME +" ("+
                FilmasiaContract.ToWatchEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FilmasiaContract.ToWatchEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                FilmasiaContract.ToWatchEntry.COLUMN_NOTES + " TEXT NOT NULL, " +
                FilmasiaContract.ToWatchEntry.COLUMN_PRIORITY + " INTEGER NOT NULL " + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TO_WATCH_TABLE);

        // Create a table to hold the places data
        final String SQL_CREATE_PLACES_TABLE = "CREATE TABLE " + FilmasiaContract.PlaceEntry.TABLE_NAME + " (" +
                FilmasiaContract.PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FilmasiaContract.PlaceEntry.COLUMN_PLACE_ID + " TEXT NOT NULL, " +
                "UNIQUE (" + FilmasiaContract.PlaceEntry.COLUMN_PLACE_ID + ") ON CONFLICT REPLACE" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_PLACES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FilmasiaContract.FilmEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FilmasiaContract.ToWatchEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FilmasiaContract.PlaceEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}

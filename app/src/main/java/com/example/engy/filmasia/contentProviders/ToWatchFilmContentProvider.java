package com.example.engy.filmasia.contentProviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.engy.filmasia.sqlite.FilmasiaContract;
import com.example.engy.filmasia.sqlite.FilmasiaDBHelper;

/**
 * Created by Engy on 2/22/2018.
 */

public class ToWatchFilmContentProvider extends ContentProvider {

    FilmasiaDBHelper dbHelper;
    public static final int TO_WATCH=100;
    public static final int TO_WATCH_WITH_ID=101;

    public static final UriMatcher sUriMatcher=buildUriMatcher();

    @Override
    public boolean onCreate() {
        Context context=getContext();
        dbHelper=new FilmasiaDBHelper(context);
        return true;
    }

    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

        //for directory
        uriMatcher.addURI(FilmasiaContract.AUTHORITY,FilmasiaContract.PATH_TOWATCH,TO_WATCH);
        //for single row
        uriMatcher.addURI(FilmasiaContract.AUTHORITY,FilmasiaContract.PATH_TOWATCH + "/#",TO_WATCH_WITH_ID);

        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case TO_WATCH: //this is the only valid case for insert
                long id=db.insert(FilmasiaContract.ToWatchEntry.TABLE_NAME,null,contentValues);
                if(id>0){ //successful insertion
                    returnUri= ContentUris.withAppendedId(FilmasiaContract.ToWatchEntry.CONTENT_URI,id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert new film");
                }
                break;
            default:
                throw new UnsupportedOperationException("Not a valid Uri");
        }
        if(getContext()!=null){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

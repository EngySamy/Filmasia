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
import android.util.Log;

import com.example.engy.filmasia.sqlite.FilmasiaContract;
import com.example.engy.filmasia.sqlite.FilmasiaDBHelper;

/**
 * Created by Engy on 2/22/2018.
 */

public class ToWatchFilmContentProvider extends ContentProvider {

    FilmasiaDBHelper dbHelper;
    //1. specify these constants to use with uri matcher
    public static final int TO_WATCH=100;
    public static final int TO_WATCH_WITH_ID=101;

    public static final UriMatcher sUriMatcher=buildUriMatcher();

    @Override
    public boolean onCreate() {
        Context context=getContext();
        dbHelper=new FilmasiaDBHelper(context);
        return true;
    }

    //2. build uri matcher to express the uris with int
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
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int match=sUriMatcher.match(uri);
        Cursor result;
        switch (match){
            case TO_WATCH: //directory
                result=db.query(FilmasiaContract.ToWatchEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case TO_WATCH_WITH_ID: // one row
                // Uri -> content://Authority/toWatch(0)/#(1) (id)
                String id=uri.getPathSegments().get(1);
                String sel= FilmasiaContract.ToWatchEntry._ID+"=?";
                String[] selArg=new String[]{id};
                result=db.query(FilmasiaContract.ToWatchEntry.TABLE_NAME,
                        projection,sel,selArg,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Not a valid Uri");
        }
        if(getContext()!=null){
            result.setNotificationUri(getContext().getContentResolver(),uri);
        }

        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case TO_WATCH:
                // directory
                return "vnd.android.cursor.dir" + "/" + FilmasiaContract.AUTHORITY + "/" + FilmasiaContract.PATH_TOWATCH;
            case TO_WATCH_WITH_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" + FilmasiaContract.AUTHORITY + "/" + FilmasiaContract.PATH_TOWATCH;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    //3. for insertion: *get writable access for db and *match the uri with the valid uris then *build the new uri
    //and *notify content resolver if uri has changed
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case TO_WATCH: //this is the only valid case for insert
                // the actual insertion in the db
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
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        int result=0;
        switch (match){
            case TO_WATCH_WITH_ID: // one row
                // Uri -> content://Authority/toWatch(0)/#(1) (id)
                String id=uri.getPathSegments().get(1);
                String where= FilmasiaContract.ToWatchEntry._ID+"=?";
                String[] whereArg=new String[]{id};
                result=db.delete(FilmasiaContract.ToWatchEntry.TABLE_NAME,where,whereArg);
                break;
            default:
                Log.e("Content Provider","match = "+match);
                throw new UnsupportedOperationException("Not a valid Uri");
        }
        if(result!=0 && getContext()!=null){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

package com.example.engy.filmasia.sqlite;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by Engy on 2/20/2018.
 */

public class FilmasiaContract {
    public static final String AUTHORITY1="com.example.engy.filmasia.contentProviders_fragments";
    public static final Uri BASE_URI1=Uri.parse("content://"+AUTHORITY1);

    public static final String AUTHORITY2="com.example.engy.filmasia.google_places_and_locations";
    public static final Uri BASE_URI2=Uri.parse("content://"+AUTHORITY2);

    public static final String PATH_TOWATCH="toWatch";
    public static final String PATH_PLACES="places";

    private FilmasiaContract(){}

    public static final class FilmEntry implements BaseColumns{
        public static final String TABLE_NAME="film";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_YEAR="year";
        public static final String COLUMN_TYPE="type";
        public static final String COLUMN_RATING="rating";
    }

    // only this table will use it in content provider as a practice
    public static final class ToWatchEntry implements BaseColumns{

        public static final Uri CONTENT_URI=BASE_URI1.buildUpon().appendPath(PATH_TOWATCH).build();

        public static final String TABLE_NAME="toWatch";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_PRIORITY="priority";
        public static final String COLUMN_NOTES="notes";

    }

    ////add this table for saving cinemas
    public static final class PlaceEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =BASE_URI2.buildUpon().appendPath(PATH_PLACES).build();

        public static final String TABLE_NAME = "places";
        public static final String COLUMN_PLACE_ID = "placeID";
    }
}

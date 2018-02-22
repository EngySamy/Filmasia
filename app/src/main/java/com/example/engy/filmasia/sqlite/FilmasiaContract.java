package com.example.engy.filmasia.sqlite;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by Engy on 2/20/2018.
 */

public class FilmasiaContract {
    public static final String AUTHORITY="com.example.engy.filmasia";
    public static final Uri BASE_URI=Uri.parse("content://"+AUTHORITY);
    public static final String PATH_TOWATCH="toWatch";

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

        public static final Uri CONTENT_URI=BASE_URI.buildUpon().appendPath(PATH_TOWATCH).build();

        public static final String TABLE_NAME="toWatch";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_PRIORITY="priority";

    }
}

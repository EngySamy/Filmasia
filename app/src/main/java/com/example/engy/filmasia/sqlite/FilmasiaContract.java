package com.example.engy.filmasia.sqlite;

import android.provider.BaseColumns;

/**
 * Created by Engy on 2/20/2018.
 */

public class FilmasiaContract {

    private FilmasiaContract(){}

    public static final class FilmEntry implements BaseColumns{
        public static final String TABLE_NAME="film";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_YEAR="year";
        public static final String COLUMN_TYPE="type";
        public static final String COLUMN_RATING="rating";

    }
}

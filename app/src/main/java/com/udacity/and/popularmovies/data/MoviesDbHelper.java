package com.udacity.and.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.udacity.and.popularmovies.data.contracts.MovieContract.MovieEntry;

/**
 * Created on 3/25/2018.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " +
                MovieEntry.TABLE_NAME + "(" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MovieEntry.COLUMN_NAME_SERVER_ID + " INTEGER NOT NULL, " +
                    MovieEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                    MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT NOT NULL, " +
                    MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT NOT NULL, " +
                    MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT NOT NULL, " +
                    MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE + " TEXT NULL, " +
                    MovieEntry.COLUMN_NAME_ORIGINAL_TITLE + " TEXT NULL, " +
                    MovieEntry.COLUMN_NAME_VOTE_COUNT + " INTEGER NOT NULL DEFAULT 0, " +
                    MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " REAL NOT NULL DEFAULT 0, " +
                    MovieEntry.COLUMN_NAME_POPULARITY + " REAL NOT NULL DEFAULT 0, " +
                    MovieEntry.COLUMN_NAME_BACKDROP_PATH + " TEXT NOT NULL, " +
                    MovieEntry.COLUMN_NAME_IS_ADULT + " INTEGER NOT NULL DEFAULT 0, " +
                    MovieEntry.COLUMN_NAME_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                ");";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}

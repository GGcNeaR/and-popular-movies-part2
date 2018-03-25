package com.udacity.and.popularmovies.data.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created on 3/25/2018.
 */

public class MovieContract {
    private MovieContract() {}

    public static final String AUTHORITY = "com.udacity.and.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_SERVER_ID = "server_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
        public static final String COLUMN_NAME_POSTER_LOCAL_PATH = "poster_local_path";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_NAME_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_NAME_GENRE_IDS = "genre_ids";
        public static final String COLUMN_NAME_VOTE_COUNT = "vote_count";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_NAME_POPULARITY = "popularity";
        public static final String COLUMN_NAME_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_NAME_BACKDROP_LOCAL_PATH = "backdrop_local_path";
        public static final String COLUMN_NAME_IS_ADULT = "is_adult";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }
}

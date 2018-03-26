package com.udacity.and.popularmovies.utils;

import android.content.ContentValues;
import android.text.TextUtils;

import com.udacity.and.popularmovies.data.contracts.MovieContract;
import com.udacity.and.popularmovies.models.Movie;

import java.util.Arrays;

/**
 * Created on 3/26/2018.
 */

public class MovieDataUtils {
    public static ContentValues getMovieContentValues(Movie movie,
                                                String posterLocalPath, String backDropLocalPath) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_SERVER_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH, movie.getPosterPath());
        if (posterLocalPath != null) {
            contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_POSTER_LOCAL_PATH, posterLocalPath);
        }
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_GENRE_IDS, TextUtils.join(",", Arrays.asList(movie.getGenreIds())));
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_COUNT, movie.getVoteCount());
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_POPULARITY, movie.getPopularity());
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.getBackdropPath());
        if (backDropLocalPath != null) {
            contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_LOCAL_PATH, backDropLocalPath);
        }
        contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_IS_ADULT, movie.isAdult());

        return contentValues;
    }
}

package com.udacity.and.popularmovies.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

import com.udacity.and.popularmovies.data.contracts.MovieContract;
import com.udacity.and.popularmovies.models.Movie;
import com.udacity.and.popularmovies.utils.MovieDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 3/26/2018.
 */

public class MoviesRepository {
    private Context context;

    public MoviesRepository(Context context) {
        this.context = context;
    }

    public boolean add(Movie movie) {
        boolean success = false;
        ContentValues contentValues = MovieDataUtils.getMovieContentValues(movie, null, null);

        try {
            Uri uri = context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
            if (uri != null) {
                success = true;
            }
        } catch (SQLException ex) {
            Log.e(getClass().getName(), ex.getMessage());
        }

        return success;
    }

    public boolean remove(Movie movie) {
        boolean success = false;

        try {
            int result = context.getContentResolver().delete(
                    ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, movie.getId()),
                    null,
                    null);
            if (result > 0) {
                success = true;
            }
        } catch (SQLException ex) {
            Log.e(getClass().getName(), ex.getMessage());
        }

        return success;
    }

    public boolean exists(Movie movie) {
        boolean success = false;

        try {
            Cursor cursor = context.getContentResolver().query(
                    ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, movie.getId()),
                    null,
                    null,
                    null,
                    null);

            if (cursor.moveToNext()) {
                success = true;
            }
        } catch (SQLException ex) {
            Log.e(getClass().getName(), ex.getMessage());
        }

        return success;
    }

    public List<Movie> get() {
        List<Movie> movies = new ArrayList<>();

        try {
            Cursor cursor = context.getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

            while (cursor.moveToNext()) {
                Movie movie = getMovieFromCursor(cursor);
                if (movie != null) {
                    movies.add(movie);
                }
            }
        } catch (SQLException ex) {
            Log.e(getClass().getName(), ex.getMessage());
        }

        return movies;
    }

    private Movie getMovieFromCursor(Cursor cursor) {
        return null;
    }
}

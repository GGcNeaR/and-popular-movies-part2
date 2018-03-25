package com.udacity.and.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udacity.and.popularmovies.data.contracts.MovieContract;

/**
 * Created on 3/25/2018.
 */

public class MovieContentProvider extends ContentProvider {

    private static final int MOVIES = 100;
    private static final int MOVIES_BY_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MoviesDbHelper moviesDbHelper;

    @Override
    public boolean onCreate() {
        moviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;

        SQLiteDatabase db = moviesDbHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case MOVIES:
                retCursor = db.query(MovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        Uri retUri;

        switch (uriMatcher.match(uri)) {
            case MOVIES:
                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    retUri = Uri.parse(MovieContract.AUTHORITY)
                            .buildUpon()
                            .appendPath(MovieContract.PATH_MOVIES)
                            .appendPath(Long.toString(id))
                            .build();
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriMatcher.match(uri)) {
            case MOVIES:
                // do nothing, no delete all actions is expected o.O
                break;
            case MOVIES_BY_ID:
                String id = uri.getPathSegments().get(1);
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_NAME_SERVER_ID + "=?",
                        new String[] { id });
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Update is currently not supported.");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIES_BY_ID);
        return uriMatcher;
    }
}

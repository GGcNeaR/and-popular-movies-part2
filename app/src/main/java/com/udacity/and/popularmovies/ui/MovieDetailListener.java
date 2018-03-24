package com.udacity.and.popularmovies.ui;

import android.view.View;

import com.udacity.and.popularmovies.models.Movie;

/**
 * Created on 3/24/2018.
 */

public interface MovieDetailListener {
    void onFavouriteClicked(View view, Movie movie);
}

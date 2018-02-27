package com.udacity.and.popularmovies.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MoviesNetworkUtils {
    private static final String POSTER_BASE_URL = "http://image.tmdb.org";
    private static final String POSTER_PATH_T = "t";
    private static final String POSTER_PATH_P = "p";
    // Note to reviewer: I might use some abstraction and return different 'size path' based on device resolution?
    public static final String POSTER_PATH_SIZE = "w185";
    public static final String BACKDROP_PATH_SIZE = "w500";

    public static String getMovieImageUrl(String movieImagePath) {
        return getMovieImageUrl(movieImagePath, POSTER_PATH_SIZE);
    }
    public static String getMovieImageUrl(String movieImagePath, String imgSizePath) {
        String moviePosterPath = movieImagePath.replaceAll("^/+", "");
        URL posterUrl = NetworkUtils.buildUrl(POSTER_BASE_URL,
                new ArrayList<>(Arrays.asList(POSTER_PATH_T, POSTER_PATH_P, imgSizePath, moviePosterPath)),
                null);
        return posterUrl.toString();
    }
}

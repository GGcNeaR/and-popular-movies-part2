package com.udacity.and.popularmovies.ui;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.udacity.and.popularmovies.BR;
import com.udacity.and.popularmovies.BuildConfig;
import com.udacity.and.popularmovies.R;
import com.udacity.and.popularmovies.adapters.MoviesGridAdapter;
import com.udacity.and.popularmovies.adapters.VideosAdapter;
import com.udacity.and.popularmovies.data.MoviesRepository;
import com.udacity.and.popularmovies.data.contracts.MovieContract;
import com.udacity.and.popularmovies.databinding.FragmentMovieDetailBinding;
import com.udacity.and.popularmovies.models.Movie;
import com.udacity.and.popularmovies.models.Video;
import com.udacity.and.popularmovies.utils.JsonUtils;
import com.udacity.and.popularmovies.utils.MovieDataUtils;
import com.udacity.and.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MovieDetailFragment extends Fragment implements MovieDetailListener {

    private Movie movie;
    private MoviesRepository moviesRepository;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Movie.MOVIE_EXTRA, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(Movie.MOVIE_EXTRA);
            moviesRepository = new MoviesRepository(getContext());
            movie.setFavourite(moviesRepository.exists(movie));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMovieDetailBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_movie_detail, container, false);
        View view = binding.getRoot();
        binding.setMovie(movie);
        binding.setPresenter(this);

        return view;
    }

    @Override
    public void onFavouriteClicked(View view, Movie movie) {
        Drawable drawable = !movie.isFavourite() ?
                getResources().getDrawable(R.drawable.ic_favourite_selected) :
                getResources().getDrawable(R.drawable.ic_favourite);
        ((ImageButton) view).setImageDrawable(drawable);

        // add/remove movie
        if (!movie.isFavourite()) {
            favMovie(movie);
        } else {
            unfavMovie(movie);
        }

        movie.setFavourite(!movie.isFavourite());
    }

    private void favMovie(Movie movie) {
        boolean result = moviesRepository.add(movie);
        Toast.makeText(getContext(), "" + result, Toast.LENGTH_SHORT).show();
    }
    private void unfavMovie(Movie movie) {
        boolean result = moviesRepository.remove(movie);
        Toast.makeText(getContext(), "" + result, Toast.LENGTH_SHORT).show();
    }
}

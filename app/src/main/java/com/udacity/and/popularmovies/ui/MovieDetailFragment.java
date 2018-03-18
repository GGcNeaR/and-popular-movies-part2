package com.udacity.and.popularmovies.ui;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udacity.and.popularmovies.BuildConfig;
import com.udacity.and.popularmovies.R;
import com.udacity.and.popularmovies.adapters.MoviesGridAdapter;
import com.udacity.and.popularmovies.adapters.VideosAdapter;
import com.udacity.and.popularmovies.databinding.FragmentMovieDetailBinding;
import com.udacity.and.popularmovies.models.Movie;
import com.udacity.and.popularmovies.models.Video;
import com.udacity.and.popularmovies.utils.JsonUtils;
import com.udacity.and.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MovieDetailFragment extends Fragment {

    private Movie movie;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMovieDetailBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_movie_detail, container, false);
        View view = binding.getRoot();
        binding.setMovie(movie);

        return view;
    }
}

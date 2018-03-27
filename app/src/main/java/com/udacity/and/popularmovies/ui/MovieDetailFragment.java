package com.udacity.and.popularmovies.ui;


import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.udacity.and.popularmovies.R;
import com.udacity.and.popularmovies.data.MoviesRepository;
import com.udacity.and.popularmovies.databinding.FragmentMovieDetailBinding;
import com.udacity.and.popularmovies.models.Movie;


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
        moviesRepository.add(movie);
    }
    private void unfavMovie(Movie movie) {
        moviesRepository.remove(movie);
    }
}

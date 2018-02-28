package com.udacity.and.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.and.popularmovies.R;
import com.udacity.and.popularmovies.models.Movie;
import com.udacity.and.popularmovies.ui.MoviePosterOnClickListener;
import com.udacity.and.popularmovies.utils.MoviesNetworkUtils;
import com.udacity.and.popularmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MoviePosterViewHolder> {

    private Context context;
    private List<Movie> movies;
    private MoviePosterOnClickListener listener;

    public MoviesGridAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    public void setOnMoviePosterOnClickListener(MoviePosterOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_grid_item, parent, false);

        return new MoviePosterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        String url = MoviesNetworkUtils.getMovieImageUrl(movie.getPosterPath());

        Picasso.with(context)
                .load(url)
                .into(holder.moviePosterImageView);

        holder.moviePosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMoviePosterClicked(movie);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MoviePosterViewHolder extends RecyclerView.ViewHolder {
        public ImageView moviePosterImageView;

        public MoviePosterViewHolder(View itemView) {
            super(itemView);

            moviePosterImageView = itemView.findViewById(R.id.movie_poster_iv);
        }
    }
}

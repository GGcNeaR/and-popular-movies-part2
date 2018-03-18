package com.udacity.and.popularmovies.ui;


import android.content.Context;
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
import com.udacity.and.popularmovies.adapters.ReviewsAdapter;
import com.udacity.and.popularmovies.models.Movie;
import com.udacity.and.popularmovies.models.Review;
import com.udacity.and.popularmovies.models.Video;
import com.udacity.and.popularmovies.utils.JsonUtils;
import com.udacity.and.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieReviewFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<AsyncTaskResult<List<Review>>>{

    private static final int REVIEWS_LOADER = 156;
    private Movie movie;

    private RecyclerView movieReviewsRecyclerView;

    public MovieReviewFragment() {
        // Required empty public constructor
    }

    public static MovieReviewFragment newInstance(Movie movie) {
        MovieReviewFragment fragment = new MovieReviewFragment();
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
            initLoader(movie.getId());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_review, container, false);

        movieReviewsRecyclerView = view.findViewById(R.id.movie_reviews_rv);
        movieReviewsRecyclerView.setHasFixedSize(true);
        movieReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public Loader<AsyncTaskResult<List<Review>>> onCreateLoader(int id, Bundle args) {
        return new ReviewsLoader(MovieReviewFragment.this.getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<AsyncTaskResult<List<Review>>> loader, AsyncTaskResult<List<Review>> data) {
        if (data.getException() != null) {
            Toast.makeText(MovieReviewFragment.this.getContext(), getString(R.string.error_loading_videos_from_internet),
                    Toast.LENGTH_SHORT).show();
        } else {
            ReviewsAdapter adapter = new ReviewsAdapter(data.getResult());
            movieReviewsRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<AsyncTaskResult<List<Review>>> loader) {

    }


    private void initLoader(int movieId) {
        Bundle bundle = new Bundle();
        bundle.putInt(MovieReviewFragment.ReviewsLoader.MOVIE_ID_EXTRA, movieId);
        if (NetworkUtils.isNetworkAvailable(this.getContext())) {
            getActivity().getSupportLoaderManager().initLoader(REVIEWS_LOADER, bundle, this);
        } else {
            Toast.makeText(getContext(), getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    static class ReviewsLoader extends AsyncTaskLoader<AsyncTaskResult<List<Review>>> {
        static final String MOVIE_ID_EXTRA = "MOVIE_ID_EXTRA";

        private static final String BASE_MOVIES_ADDRESS = "http://api.themoviedb.org";
        private static final String API_VERSION_PATH_SEGMENT = "3";
        private static final String API_ENTITY_TYPE_PATH_SEGMENT = "movie";
        private static final String API_REVIEWS_PATH_SEGMENT = "reviews";
        private static final String API_KEY_QUERY_PARAM_NAME = "api_key";
        public static final String API_KEY_QUERY_PARAM_VALUE = BuildConfig.API_KEY;

        private static final int DEFAULT_MOVIE_ID = -1;
        private int movieId = DEFAULT_MOVIE_ID;

        private AsyncTaskResult<List<Review>> reviewsResult;

        public ReviewsLoader(Context context, Bundle args) {
            super(context);
            if (args != null && args.containsKey(MOVIE_ID_EXTRA)) {
                movieId = args.getInt(MOVIE_ID_EXTRA, DEFAULT_MOVIE_ID);
            }
        }

        @Override
        protected void onStartLoading() {
            if (reviewsResult != null) {
                // use cached data
                deliverResult(reviewsResult);
            } else {
                forceLoad();
            }
        }

        @Override
        public AsyncTaskResult<List<Review>> loadInBackground() {
            AsyncTaskResult<List<Review>> result = new AsyncTaskResult<>();
            String content;

            List<String> paths = new ArrayList<>();
            paths.add(API_VERSION_PATH_SEGMENT);
            paths.add(API_ENTITY_TYPE_PATH_SEGMENT);
            paths.add(Integer.toString(movieId));
            paths.add(API_REVIEWS_PATH_SEGMENT);

            // prepare the dictionary for the query params
            Map<String, String> params = new HashMap<>();
            params.put(API_KEY_QUERY_PARAM_NAME, API_KEY_QUERY_PARAM_VALUE);

            URL url = NetworkUtils.buildUrl(BASE_MOVIES_ADDRESS, paths, params);
            try {
                content = NetworkUtils.getResponseFromHttpUrl(url);
                result.setResult(JsonUtils.parseReviews(content));
            } catch (IOException e) {
                result.setException(e);
            }

            return result;
        }


        @Override
        public void deliverResult(AsyncTaskResult<List<Review>> data) {
            reviewsResult = data;
            super.deliverResult(data);
        }
    }
}

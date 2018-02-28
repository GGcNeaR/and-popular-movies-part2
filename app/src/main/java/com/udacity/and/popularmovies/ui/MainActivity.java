package com.udacity.and.popularmovies.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.and.popularmovies.BuildConfig;
import com.udacity.and.popularmovies.R;
import com.udacity.and.popularmovies.adapters.MoviesGridAdapter;
import com.udacity.and.popularmovies.models.Movie;
import com.udacity.and.popularmovies.utils.JsonUtils;
import com.udacity.and.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.udacity.and.popularmovies.ui.MainActivity.MoviesLoader.API_KEY_QUERY_PARAM_VALUE;

/**
 * I'm using this article: https://medium.com/google-developers/making-loading-data-on-android-lifecycle-aware-897e12760832
 * as a reference for the Loader implementation in combination with
 * Lesson05b-Smarter-GitHub-Repo-Search/T05b.03-Solution-PolishAsyncTask project from the previous Udacity Android course
 */

public class MainActivity extends AppCompatActivity
                            implements LoaderManager.LoaderCallbacks<AsyncTaskResult<List<Movie>>> {

    private static final int MOVIES_LOADER = 42;
    private static final int PORTRAIT_COLUMNS_COUNT = 2;
    private static final int LANDSCAPE_COLUMNS_COUNT = 3;
    private RecyclerView moviesPostersRecyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Note: only for reminder to reviewer
        if (API_KEY_QUERY_PARAM_VALUE.isEmpty()) {
            Toast.makeText(this, "Please set your API_KEY in gradle.properties", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        moviesPostersRecyclerView = findViewById(R.id.movie_posters_rv);
        progressBar = findViewById(R.id.progress_bar);

        // Note to reviewer: at least on the phones I used for testing,
        // 2 columns for portrait and 3 for landscape looks ok.
        // Please let me know if there is a better approach for serving responsive grid in my case.
        int numberOfColumns = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                            LANDSCAPE_COLUMNS_COUNT :
                            PORTRAIT_COLUMNS_COUNT;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),numberOfColumns);
        moviesPostersRecyclerView.setLayoutManager(gridLayoutManager);

        initLoader(MoviesLoader.REQUEST_TYPE_POPULAR, false);
    }

    private void initLoader(int requestType, boolean isRestart) {
        Bundle bundle = new Bundle();
        bundle.putInt(MoviesLoader.REQUEST_TYPE_EXTRA, requestType);
        if (NetworkUtils.isNetworkAvailable(this)) {
            if (isRestart) {
                getSupportLoaderManager().restartLoader(MOVIES_LOADER, bundle, this);
            } else {
                getSupportLoaderManager().initLoader(MOVIES_LOADER, bundle, this);
            }
        } else {
            Toast.makeText(this, getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_popular:
                initLoader(MoviesLoader.REQUEST_TYPE_POPULAR, true);
                break;
            case R.id.action_show_top_rated:
                initLoader(MoviesLoader.REQUEST_TYPE_TOP_RATED, true);
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public Loader<AsyncTaskResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        return new MoviesLoader(MainActivity.this, args);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onLoadFinished(Loader<AsyncTaskResult<List<Movie>>> loader, AsyncTaskResult<List<Movie>> data) {
        if (data.getException() != null) {
            Toast.makeText(MainActivity.this, getString(R.string.error_loading_movies_from_internet), Toast.LENGTH_SHORT).show();
        } else {
            MoviesGridAdapter adapter = new MoviesGridAdapter(this, data.getResult());
            adapter.setOnMoviePosterOnClickListener(new MoviePosterOnClickListener() {
                @Override
                public void onMoviePosterClicked(Movie movie) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra(Movie.MOVIE_EXTRA, movie);
                    startActivity(intent);
                }
            });

            moviesPostersRecyclerView.setAdapter(adapter);
        }

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<AsyncTaskResult<List<Movie>>> loader) {

    }

    static class MoviesLoader extends AsyncTaskLoader<AsyncTaskResult<List<Movie>>> {
        static final String REQUEST_TYPE_EXTRA = "REQUEST_TYPE_EXTRA";
        static final int REQUEST_TYPE_POPULAR = 1;
        static final int REQUEST_TYPE_TOP_RATED = 2;

        private static final String BASE_MOVIES_ADDRESS = "http://api.themoviedb.org";
        private static final String API_VERSION_PATH_SEGMENT = "3";
        private static final String API_ENTITY_TYPE_PATH_SEGMENT = "movie";
        private static final String API_POPULAR_PATH_SEGMENT = "popular";
        private static final String API_TOP_RATED_PATH_SEGMENT = "top_rated";
        private static final String API_KEY_QUERY_PARAM_NAME = "api_key";
        public static final String API_KEY_QUERY_PARAM_VALUE = BuildConfig.API_KEY;

        private AsyncTaskResult<List<Movie>> moviesResult;
        private int requestType = REQUEST_TYPE_POPULAR;

        MoviesLoader(Context context, Bundle args) {
            super(context);
            if (args != null && args.containsKey(REQUEST_TYPE_EXTRA)) {
                requestType = args.getInt(REQUEST_TYPE_EXTRA, REQUEST_TYPE_POPULAR);
            }
        }

        @Override
        protected void onStartLoading() {
            if (moviesResult != null) {
                // use cached data
                deliverResult(moviesResult);
            } else {
                forceLoad();
            }
        }

        @Override
        public AsyncTaskResult<List<Movie>> loadInBackground() {
            AsyncTaskResult<List<Movie>> result = new AsyncTaskResult<>();
            String content = null;
            // build the path, the last segment is dynamic based on the mode - popular or top-rated
            List<String> paths = new ArrayList<>();
            paths.add(API_VERSION_PATH_SEGMENT);
            paths.add(API_ENTITY_TYPE_PATH_SEGMENT);
            paths.add(getApiRequestTypePathSegment());

            // prepare the dictionary for the query params
            Map<String, String> params = new HashMap<>();
            params.put(API_KEY_QUERY_PARAM_NAME, API_KEY_QUERY_PARAM_VALUE);

            URL url = NetworkUtils.buildUrl(BASE_MOVIES_ADDRESS, paths, params);
            try {
                content = NetworkUtils.getResponseFromHttpUrl(url);
                result.setResult(JsonUtils.parseMovies(content));
            } catch (IOException e) {
                result.setException(e);
            }

            return result;
        }

        @Override
        public void deliverResult(AsyncTaskResult<List<Movie>> data) {
            moviesResult = data;
            super.deliverResult(data);
        }

        private String getApiRequestTypePathSegment() {
            return requestType == REQUEST_TYPE_POPULAR ? API_POPULAR_PATH_SEGMENT : API_TOP_RATED_PATH_SEGMENT;
        }
    }
}

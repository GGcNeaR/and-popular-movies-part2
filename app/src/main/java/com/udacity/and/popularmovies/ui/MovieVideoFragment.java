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
import com.udacity.and.popularmovies.adapters.VideosAdapter;
import com.udacity.and.popularmovies.models.Movie;
import com.udacity.and.popularmovies.models.Video;
import com.udacity.and.popularmovies.utils.JsonUtils;
import com.udacity.and.popularmovies.utils.NetworkUtils;
import com.udacity.and.popularmovies.utils.VideoUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieVideoFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<AsyncTaskResult<List<Video>>>{

    private static final int VIDEOS_LOADER = 69;
    private Movie movie;

    private RecyclerView movieVideosRecyclerView;


    public MovieVideoFragment() {
        // Required empty public constructor
    }

    public static MovieVideoFragment newInstance(Movie movie) {
        MovieVideoFragment fragment = new MovieVideoFragment();
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
        View view = inflater.inflate(R.layout.fragment_movie_video, container, false);

        movieVideosRecyclerView = view.findViewById(R.id.movie_videos_rv);
        movieVideosRecyclerView.setHasFixedSize(true);
        movieVideosRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public Loader<AsyncTaskResult<List<Video>>> onCreateLoader(int id, Bundle args) {
        return new VideosLoader(MovieVideoFragment.this.getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<AsyncTaskResult<List<Video>>> loader, AsyncTaskResult<List<Video>> data) {
        if (data.getException() != null) {
            Toast.makeText(MovieVideoFragment.this.getContext(), getString(R.string.error_loading_videos_from_internet),
                    Toast.LENGTH_SHORT).show();
        } else {
            VideosAdapter adapter = new VideosAdapter(data.getResult());
            adapter.setOnVideoClickListener(new VideoOnClickListener() {
                @Override
                public void onVideoClick(Video video) {
                    VideoUtils.playYoutubeVideo(MovieVideoFragment.this.getContext(), video.getKey());
                }
            });

            movieVideosRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<AsyncTaskResult<List<Video>>> loader) {

    }

    private void initLoader(int movieId) {
        Bundle bundle = new Bundle();
        bundle.putInt(VideosLoader.MOVIE_ID_EXTRA, movieId);
        if (NetworkUtils.isNetworkAvailable(this.getContext())) {
            getActivity().getSupportLoaderManager().initLoader(VIDEOS_LOADER, bundle, this);
        } else {
            Toast.makeText(getContext(), getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    static class VideosLoader extends AsyncTaskLoader<AsyncTaskResult<List<Video>>> {
        static final String MOVIE_ID_EXTRA = "MOVIE_ID_EXTRA";

        private static final String BASE_MOVIES_ADDRESS = "http://api.themoviedb.org";
        private static final String API_VERSION_PATH_SEGMENT = "3";
        private static final String API_ENTITY_TYPE_PATH_SEGMENT = "movie";
        private static final String API_VIDEO_PATH_SEGMENT = "videos";
        private static final String API_KEY_QUERY_PARAM_NAME = "api_key";
        public static final String API_KEY_QUERY_PARAM_VALUE = BuildConfig.API_KEY;

        private static final int DEFAULT_MOVIE_ID = -1;
        private int movieId = DEFAULT_MOVIE_ID;

        private AsyncTaskResult<List<Video>> videosResult;

        VideosLoader(Context context, Bundle args) {
            super(context);

            if (args != null && args.containsKey(MOVIE_ID_EXTRA)) {
                movieId = args.getInt(MOVIE_ID_EXTRA, DEFAULT_MOVIE_ID);
            }
        }

        @Override
        protected void onStartLoading() {
            if (videosResult != null) {
                // use cached data
                deliverResult(videosResult);
            } else {
                forceLoad();
            }
        }

        @Override
        public AsyncTaskResult<List<Video>> loadInBackground() {
            AsyncTaskResult<List<Video>> result = new AsyncTaskResult<>();
            String content = null;

            List<String> paths = new ArrayList<>();
            paths.add(API_VERSION_PATH_SEGMENT);
            paths.add(API_ENTITY_TYPE_PATH_SEGMENT);
            paths.add(Integer.toString(movieId));
            paths.add(API_VIDEO_PATH_SEGMENT);

            // prepare the dictionary for the query params
            Map<String, String> params = new HashMap<>();
            params.put(API_KEY_QUERY_PARAM_NAME, API_KEY_QUERY_PARAM_VALUE);

            URL url = NetworkUtils.buildUrl(BASE_MOVIES_ADDRESS, paths, params);
            try {
                content = NetworkUtils.getResponseFromHttpUrl(url);
                result.setResult(JsonUtils.parseVideos(content));
            } catch (IOException e) {
                result.setException(e);
            }

            return result;
        }

        @Override
        public void deliverResult(AsyncTaskResult<List<Video>> data) {
            videosResult = data;
            super.deliverResult(data);
        }
    }
}

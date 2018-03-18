package com.udacity.and.popularmovies.utils;

import android.text.TextUtils;

import com.udacity.and.popularmovies.models.Movie;
import com.udacity.and.popularmovies.models.Review;
import com.udacity.and.popularmovies.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String RESULT_KEY = "results";

    private static final String ID_KEY = "id";
    private static final String TITLE_KEY = "title";
    private static final String OVERVIEW_KEY = "overview";
    private static final String POSTER_PATH_KEY = "poster_path";
    private static final String RELEASE_DATE_KEY = "release_date";
    private static final String ORIGINAL_LANGUAGE_KEY = "original_language";
    private static final String ORIGINAL_TITLE_KEY = "original_title";
    private static final String GENRE_IDS_KEY = "genre_ids";
    private static final String VOTE_COUNT_KEY = "vote_count";
    private static final String VOTE_AVERAGE_KEY = "vote_average";
    private static final String POPULARITY_KEY = "popularity";
    private static final String BACKDROP_PATH_KEY = "backdrop_path";
    private static final String ADULT_KEY = "adult";

    private static final String KEY_KEY = "key";
    private static final String NAME_KEY = "name";
    private static final String TYPE_KEY = "type";

    private static final String AUTHOR_KEY = "author";
    private static final String CONTENT_KEY = "content";
    private static final String URL_KEY = "url";

    public static List<Movie> parseMovies(String jsonString) {
        List<Movie> movies = new ArrayList<>();

        // check if the input string is not null or empty
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                JSONObject pageResultJson = new JSONObject(jsonString);
                if (pageResultJson.has(RESULT_KEY)) {
                    JSONArray resultsJsonArray = pageResultJson.getJSONArray(RESULT_KEY);
                    for (int i = 0, len = resultsJsonArray.length(); i < len; i++) {
                        JSONObject movieJsonObject = resultsJsonArray.optJSONObject(i);
                        // check if the current results item is a valid json object
                        if (movieJsonObject != null) {
                            Movie movie = parseMovie(movieJsonObject);
                            // add only if parsing the movie json was successful
                            if (movie != null) {
                                movies.add(movie);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                // empty list will be returned
            }
        }

        return movies;
    }

    public static List<Video> parseVideos(String jsonString) {
        List<Video> videos = new ArrayList<>();

        if (!TextUtils.isEmpty(jsonString)) {
            try {
                JSONObject pageResultJson = new JSONObject(jsonString);
                if (pageResultJson.has(RESULT_KEY)) {
                    JSONArray resultsJsonArray = pageResultJson.getJSONArray(RESULT_KEY);
                    for (int i = 0, len = resultsJsonArray.length(); i < len; i++) {
                        JSONObject videoJsonObject = resultsJsonArray.optJSONObject(i);
                        if (videoJsonObject != null) {
                            Video video = parseVideo(videoJsonObject);
                            if (video != null) {
                                videos.add(video);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                // empty list will be returned
            }
        }

        return videos;
    }

    public static List<Review> parseReviews(String jsonString) {
        List<Review> reviews = new ArrayList<>();

        if (!TextUtils.isEmpty(jsonString)) {
            try {
                JSONObject pageResultJson = new JSONObject(jsonString);
                if (pageResultJson.has(RESULT_KEY)) {
                    JSONArray resultsJsonArray = pageResultJson.getJSONArray(RESULT_KEY);
                    for (int i = 0, len = resultsJsonArray.length(); i < len; i++) {
                        JSONObject videoJsonObject = resultsJsonArray.optJSONObject(i);
                        if (videoJsonObject != null) {
                            Review review = parseReview(videoJsonObject);
                            if (review != null) {
                                reviews.add(review);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                // empty list will be returned
            }
        }

        return reviews;
    }

    private static Movie parseMovie(JSONObject movieJsonObject) throws JSONException {
        Movie movie = null;
        int id;
        String title;
        String overview = null;
        String posterPath = null;
        String releaseDate = null;
        String originalLanguage = null;
        String originalTitle = null;
        int[] genreIds = null;
        int voteCount = 0;
        double voteAvg = 0.00;
        double popularity = 0.00;
        String backdropPath = null;
        boolean isAdult = false;

        if (movieJsonObject.has(ID_KEY) && movieJsonObject.has(TITLE_KEY)) {
            id = movieJsonObject.getInt(ID_KEY); // if not int, throw exception
            title = movieJsonObject.getString(TITLE_KEY); // if not string, throw exception

            if (movieJsonObject.has(OVERVIEW_KEY)) {
                overview = movieJsonObject.optString(OVERVIEW_KEY);
            }
            if (movieJsonObject.has(POSTER_PATH_KEY)) {
                posterPath = movieJsonObject.optString(POSTER_PATH_KEY);
            }
            if (movieJsonObject.has(RELEASE_DATE_KEY)) {
                releaseDate = movieJsonObject.optString(RELEASE_DATE_KEY);
            }
            if (movieJsonObject.has(ORIGINAL_LANGUAGE_KEY)) {
                originalLanguage = movieJsonObject.optString(ORIGINAL_LANGUAGE_KEY);
            }
            if (movieJsonObject.has(ORIGINAL_TITLE_KEY)) {
                originalTitle = movieJsonObject.optString(ORIGINAL_TITLE_KEY);
            }
            if (movieJsonObject.has(GENRE_IDS_KEY)) {
                JSONArray genreIdsJsonArray = movieJsonObject.optJSONArray(GENRE_IDS_KEY);
                if (genreIdsJsonArray != null) {
                    List<Integer> genreIdsList = new ArrayList<>();
                    for (int i = 0, len = genreIdsJsonArray.length(); i < len; i++) {
                        int genreId = genreIdsJsonArray.optInt(i);
                        if (genreId != 0) {
                            genreIdsList.add(genreId);
                        }
                    }
                    int genreIdsCount = genreIdsList.size();
                    genreIds = new int[genreIdsCount];
                    for (int i = 0; i < genreIdsCount; i++) {
                        genreIds[i] = genreIdsList.get(i);
                    }
                }
            }
            if (movieJsonObject.has(VOTE_COUNT_KEY)) {
                voteCount = movieJsonObject.optInt(VOTE_COUNT_KEY);
            }
            if (movieJsonObject.has(VOTE_AVERAGE_KEY)) {
                voteAvg = movieJsonObject.optDouble(VOTE_AVERAGE_KEY);
            }
            if (movieJsonObject.has(POPULARITY_KEY)) {
                popularity = movieJsonObject.optDouble(POPULARITY_KEY);
            }
            if (movieJsonObject.has(BACKDROP_PATH_KEY)) {
                backdropPath = movieJsonObject.optString(BACKDROP_PATH_KEY);
            }
            if (movieJsonObject.has(ADULT_KEY)) {
                isAdult = movieJsonObject.optBoolean(ADULT_KEY);
            }

            movie = new Movie(id, title, overview, posterPath, releaseDate,
                                originalLanguage, originalTitle, genreIds,
                                voteCount, voteAvg, popularity,
                                backdropPath, isAdult);
        }

        return movie;
    }

    private static Video parseVideo(JSONObject videoJsonObject) throws JSONException {
        Video video = null;
        String id;
        String key;
        String name;
        String type = null;

        if (videoJsonObject.has(ID_KEY) &&
            videoJsonObject.has(KEY_KEY) &&
            videoJsonObject.has(NAME_KEY)) {

            id = videoJsonObject.optString(ID_KEY);
            key = videoJsonObject.optString(KEY_KEY);
            name = videoJsonObject.optString(NAME_KEY);

            if (videoJsonObject.has(TYPE_KEY)) {
                type = videoJsonObject.optString(TYPE_KEY);
            }

            video = new Video(id, key, name, type);
        }

        return video;
    }

    private static Review parseReview(JSONObject reviewJosnObject) throws JSONException {
        Review review = null;
        String id;
        String author;
        String content;
        String url = null;

        if (reviewJosnObject.has(ID_KEY) &&
            reviewJosnObject.has(AUTHOR_KEY) &&
            reviewJosnObject.has(CONTENT_KEY)) {

            id = reviewJosnObject.optString(ID_KEY);
            author = reviewJosnObject.optString(AUTHOR_KEY);
            content = reviewJosnObject.optString(CONTENT_KEY);

            if (reviewJosnObject.has(URL_KEY)) {
                url = reviewJosnObject.optString(URL_KEY);
            }

            review = new Review(id, author, content, url);
        }

        return review;
    }
}

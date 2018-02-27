package com.udacity.and.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The base idea for the class was borrowed from the examples here
 * https://github.com/udacity/ud851-Exercises
 * and extended a bit to support my use case.
 */
public class NetworkUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }

    public static URL buildUrl(String baseAddress, List<String> pathSegments, Map<String, String> queryParams) {
        Uri.Builder builder = Uri.parse(baseAddress).buildUpon();

        if (pathSegments != null) {
            for (String path : pathSegments) {
                builder = builder.appendPath(path);
            }
        }
        if (queryParams != null) {
            for (Map.Entry<String, String> param : queryParams.entrySet()){
                builder = builder.appendQueryParameter(param.getKey(), param.getValue());
            }
        }

        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

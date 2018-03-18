package com.udacity.and.popularmovies.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created on 3/18/2018.
 */
// https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
public class VideoUtils {
    private static final String YOUTUBE_APP_URL = "vnd.youtube:";
    private static final String YOUTUBE_WEB_URL = "http://www.youtube.com/watch?v=";
    public static void playYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_APP_URL + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_WEB_URL + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}

package com.udacity.and.popularmovies.bindings;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.and.popularmovies.utils.MoviesNetworkUtils;

/**
 * Found it here: http://www.devexchanges.info/2016/09/combining-databinding-and-picasso.html
 */

public class PicassoImageBinding {
    @BindingAdapter({"bind:movieImagePath", "bind:movieImageType"})
    public static void loadImage(ImageView imageView, String imagePath, String movieImageType) {
        Log.d("IMG_BINDING", movieImageType);
        if (!TextUtils.isEmpty(imagePath)) {
            if (TextUtils.isEmpty(movieImageType)) {
                movieImageType = MoviesNetworkUtils.POSTER_PATH_SIZE;
            }
            Picasso.with(imageView.getContext()).load(MoviesNetworkUtils.getMovieImageUrl(imagePath, movieImageType)).into(imageView);
        }
    }
}

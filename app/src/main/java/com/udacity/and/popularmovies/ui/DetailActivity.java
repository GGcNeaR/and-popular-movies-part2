package com.udacity.and.popularmovies.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.and.popularmovies.R;
import com.udacity.and.popularmovies.databinding.ActivityDetailBinding;
import com.udacity.and.popularmovies.models.Movie;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Movie.MOVIE_EXTRA)) {
            Movie movie = intent.getParcelableExtra(Movie.MOVIE_EXTRA);
            renderUI(movie);
        } else {
            Toast.makeText(this, R.string.error_missing_movie_info, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return false;
    }

    private void renderUI(Movie movie) {
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setMovie(movie);
    }
}

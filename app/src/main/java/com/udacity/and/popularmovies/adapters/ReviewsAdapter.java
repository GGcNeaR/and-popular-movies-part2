package com.udacity.and.popularmovies.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.udacity.and.popularmovies.BR;
import com.udacity.and.popularmovies.R;
import com.udacity.and.popularmovies.databinding.ReviewsListRowBinding;
import com.udacity.and.popularmovies.models.Review;
import com.udacity.and.popularmovies.models.Video;

import java.util.List;

/**
 * Created on 3/18/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsListViewHolder>{

    private List<Review> reviews;

    public ReviewsAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public ReviewsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ReviewsListRowBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.reviews_list_row, parent, false);

        return new ReviewsListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ReviewsListViewHolder holder, int position) {
        final Review review = reviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewsListViewHolder extends RecyclerView.ViewHolder {
        public final ReviewsListRowBinding binding;

        public ReviewsListViewHolder(ReviewsListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Review review) {
            binding.setVariable(BR.review, review);
            binding.executePendingBindings();
        }
    }
}

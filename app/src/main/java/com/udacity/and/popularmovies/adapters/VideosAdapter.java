package com.udacity.and.popularmovies.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.and.popularmovies.BR;
import com.udacity.and.popularmovies.R;
import com.udacity.and.popularmovies.databinding.VideosListRowBinding;
import com.udacity.and.popularmovies.models.Video;
import com.udacity.and.popularmovies.ui.VideoOnClickListener;

import java.util.List;

/**
 * Created on 3/12/2018.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosListViewHolder> {

    private List<Video> videos;
    private VideoOnClickListener listener;

    public VideosAdapter(List<Video> videos) {
        this.videos = videos;
    }

    public void setOnVideoClickListener(VideoOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public VideosListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        VideosListRowBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.videos_list_row, parent, false);

        return new VideosListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(VideosListViewHolder holder, int position) {
        final Video video = videos.get(position);
        holder.bind(video);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onVideoClick(video);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideosListViewHolder extends RecyclerView.ViewHolder {
        public final VideosListRowBinding binding;

        public VideosListViewHolder(VideosListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Video video) {
            binding.setVariable(BR.video, video);
            binding.executePendingBindings();
        }
    }
}

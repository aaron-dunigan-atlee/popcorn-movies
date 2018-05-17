package com.example.android.popcornmovies.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popcornmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Dunigan AtLee on 5/11/2018.
 * RecyclerView Adapter for the grid of movie posters in Main Activity.
 */

public class PosterGridAdapter extends RecyclerView.Adapter<PosterGridAdapter.PosterGridAdapterViewHolder> {
    final private OnClickHandler posterClickHandler;
    public Context adapterContext;
    public PosterGridAdapter(Context context, OnClickHandler clickHandler) {
        adapterContext = context;
        posterClickHandler = clickHandler;
    }
    // Create a ViewHolder
    public class PosterGridAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public final ImageView posterImageView;

        public PosterGridAdapterViewHolder(View view) {
            super(view);
            posterImageView = (ImageView) view.findViewById(R.id.movie_poster_iv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            posterClickHandler.onItemClicked(adapterPosition);
        }
    }

    @NonNull
    @Override
    public PosterGridAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForGridItem = R.layout.movie_poster_grid_item;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForGridItem, parent, false);
        PosterGridAdapterViewHolder viewHolder = new PosterGridAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PosterGridAdapterViewHolder holder, int position) {
        // Load poster image into posterImageView which was initialized in the ViewHolder constructor.
        Picasso.with(adapterContext)
                .load("http://image.tmdb.org/t/p/w185/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg")
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public interface OnClickHandler {
        void onItemClicked(int position);
    }
}

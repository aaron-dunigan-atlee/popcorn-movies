package com.example.android.popcornmovies.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popcornmovies.R;
import com.example.android.popcornmovies.model.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Dunigan AtLee on 5/11/2018.
 * RecyclerView Adapter for the grid of movie posters in Main Activity.
 */

public class PosterGridAdapter extends RecyclerView.Adapter<PosterGridAdapter.PosterGridAdapterViewHolder> {
    private static int itemCount = 20;
    private OnClickHandler posterClickHandler;
    private String[] posterUrls = new String[itemCount];
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
    public PosterGridAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForGridItem = R.layout.movie_poster_grid_item;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForGridItem, parent, false);
        PosterGridAdapterViewHolder viewHolder = new PosterGridAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PosterGridAdapterViewHolder holder, int position) {
        if (position < posterUrls.length) {
            if (posterUrls[position] != null) {
                // Load poster image into posterImageView which was initialized in the ViewHolder constructor.
                Picasso.with(adapterContext)
                        .load(posterUrls[position])
                        .into(holder.posterImageView);
                return;
            }
        }
        // Else load placeholder image.
        Picasso.with(adapterContext)
                .load(R.drawable.ic_amish_aaron)
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public static void setItemCount(int itemCount) {
        PosterGridAdapter.itemCount = itemCount;
    }

    public interface OnClickHandler {
        void onItemClicked(int position);
    }

    /* From the TMDB query JSON, extract poster URLs and display all posters in the grid. */
    public void setPosterUrls(String json) {
        setItemCount(20);
        for (int i=0; i<itemCount; i++) {
            try {
                String movieJson = JsonUtils.getMovieJson(json, i);
                posterUrls[i] = JsonUtils.buildPosterUrl(movieJson);
            } catch (JSONException e) {
                Log.e("Json Error",e.toString());
            }
        }
        notifyDataSetChanged();
    }

    /* From the TMDB query JSON, extract poster URLs and display all posters in the grid. */
    public void setPosterUrls(List<Movie> movies) {
        setItemCount(movies.size());
        for (int i=0; i<itemCount; i++) {
            posterUrls[i] = movies.get(i).getPosterUrl();
        }
        notifyDataSetChanged();
    }

}

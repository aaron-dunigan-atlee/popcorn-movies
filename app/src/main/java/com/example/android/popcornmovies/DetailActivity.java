package com.example.android.popcornmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popcornmovies.model.Movie;
import com.example.android.popcornmovies.utilities.JsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    public static final String EXTRA_QUERY_JSON = "extra_query_json";
    private TextView mTitleTextView, mSynopsisTextView, mRatingTextView, mReleaseDateTextView;
    private ImageView mPosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        String queryResult = intent.getStringExtra(EXTRA_QUERY_JSON);
        try {
            String movieJson = JsonUtils.getMovieJson(queryResult, position);
            Movie movie = JsonUtils.parseMovieJson(movieJson);
            mPosterImageView = findViewById(R.id.detail_poster_iv);
            Picasso.with(this)
                    .load(movie.getPosterUrl())
                    .into(mPosterImageView);
            populateUI(movie);
        } catch(JSONException e) {
            Log.e("JSON error:",e.toString());
        }

    }

    private void populateUI(Movie movie) {
        mTitleTextView = findViewById(R.id.title_tv);
        mTitleTextView.setText(movie.getTitle());
        mSynopsisTextView = findViewById(R.id.synopsis_tv);
        mSynopsisTextView.setText(movie.getSynopsis());
        mRatingTextView = findViewById(R.id.rating_tv);
        mRatingTextView.setText(Float.toString(movie.getRating()));
        mReleaseDateTextView = findViewById(R.id.release_date_tv);
        mReleaseDateTextView.setText(movie.getReleaseDate());

    }
}

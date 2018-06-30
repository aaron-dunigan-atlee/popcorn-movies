package com.example.android.popcornmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popcornmovies.model.Movie;
import com.example.android.popcornmovies.utilities.JsonUtils;
import com.example.android.popcornmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/* This Activity displays details for a specific movie. */
public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    public static final String EXTRA_QUERY_JSON = "extra_query_json";
    private TextView mTitleTextView, mSynopsisTextView, mRatingTextView, mReleaseDateTextView;
    private ImageView mPosterImageView;
    private String mTrailerQueryResult = null;
    private Movie mMovie;
    private String movieTrailerYoutubeKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        String queryResult = intent.getStringExtra(EXTRA_QUERY_JSON);
        try {
            String movieJson = JsonUtils.getMovieJson(queryResult, position);
            mMovie = JsonUtils.parseMovieJson(movieJson);
            mPosterImageView = findViewById(R.id.detail_poster_iv);
            Picasso.with(this)
                    .load(mMovie.getPosterUrl())
                    .into(mPosterImageView);
            populateUI(mMovie);
        } catch(JSONException e) {
            Log.e("JSON error:",e.toString());
        }
        final ImageButton trailerButton = findViewById(R.id.play_trailer_button);
        trailerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playTrailer();
            }
        });
    }

    private void fetchTrailers() {
        if (NetworkUtils.deviceIsConnected(this)) {
            URL tmdbUrl = NetworkUtils.buildTrailerQueryUrl(this, mMovie.getId(),
                    MainActivity.getThemoviedbApiKey());
            Log.d("Query URL:",tmdbUrl.toString());
            MovieDbTrailerQueryTask fetchTrailersTask = new MovieDbTrailerQueryTask();
            fetchTrailersTask.execute(tmdbUrl);
        } else {
            Toast.makeText(this, "No network connection", Toast.LENGTH_LONG).show();
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

    private void playTrailer() {
        fetchTrailers();
    }


    public class MovieDbTrailerQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String queryResult) {
            super.onPostExecute(queryResult);
            mTrailerQueryResult = queryResult;
            Log.d("Trailer query:",queryResult);
            try {
                movieTrailerYoutubeKey = JsonUtils.getTrailerYoutubeKey(queryResult);
            } catch (JSONException e) {
                Log.e("JSON error:",e.toString());
            }
            if (movieTrailerYoutubeKey != null) {
                Uri youtubeUri = NetworkUtils.buildYoutubeUri(movieTrailerYoutubeKey);
                Log.d("YoutubeURI:",youtubeUri.toString());
                startActivity(new Intent(Intent.ACTION_VIEW, youtubeUri));
            } else {

            }
        }
    }
}

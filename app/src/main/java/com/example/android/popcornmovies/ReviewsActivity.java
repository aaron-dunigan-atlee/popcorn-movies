package com.example.android.popcornmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.popcornmovies.utils.NetworkUtils;
import com.example.android.popcornmovies.utils.ReviewAdapter;

import java.io.IOException;
import java.net.URL;

public class ReviewsActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_ID = "extra_movie_id";
    private static final int DEFAULT_ID = -1;
    public static final String EXTRA_MOVIE_TITLE = "extra_movie_title";
    private int movieId;
    private RecyclerView.LayoutManager reviewLayoutManager;
    private ReviewAdapter reviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Intent intent = getIntent();
        movieId = intent.getIntExtra(EXTRA_MOVIE_ID, DEFAULT_ID);
        String movieTitle = intent.getStringExtra(EXTRA_MOVIE_TITLE);
        setTitle(movieTitle + " " + getString(R.string.reviews));
        RecyclerView reviewRecyclerView = (RecyclerView) findViewById(R.id.reviews_rv);
        reviewLayoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewAdapter = new ReviewAdapter();
        reviewRecyclerView.setAdapter(reviewAdapter);
        fetchReviews();
    }

    private void fetchReviews() {
        if (NetworkUtils.deviceIsConnected(this)) {
            URL tmdbUrl = NetworkUtils.buildReviewQueryUrl(this, movieId,
                    MainActivity.getThemoviedbApiKey());
            Log.d("Query URL:",tmdbUrl.toString());
            MovieDbReviewQueryTask fetchReviewsTask = new MovieDbReviewQueryTask();
            fetchReviewsTask.execute(tmdbUrl);
        } else {
            Toast.makeText(this, "No network connection", Toast.LENGTH_LONG).show();
        }
    }

    public class MovieDbReviewQueryTask extends AsyncTask<URL, Void, String> {
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
            if (queryResult != null) {
                reviewAdapter.setReviews(queryResult);
                int reviewCount = reviewAdapter.getItemCount();
                if (reviewCount == 0) {
                    Toast.makeText(getApplicationContext(),
                            "This movie has not been reviewed yet.", Toast.LENGTH_LONG)
                            .show();
                }
            } else {
                Toast.makeText(getApplicationContext(),"Cannot show reviews.",Toast.LENGTH_LONG).show();
            }
        }
    }
}

package com.example.android.popcornmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popcornmovies.database.AppDatabase;
import com.example.android.popcornmovies.model.Movie;
import com.example.android.popcornmovies.utils.JsonUtils;
import com.example.android.popcornmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/* This Activity displays details for a specific movie. */
public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";
    private TextView mTitleTextView, mSynopsisTextView, mRatingTextView, mReleaseDateTextView;
    private ImageView mPosterImageView;
    private Movie mMovie;
    private String movieTrailerYoutubeKey = null;
    private AppDatabase favoritesDb;
    private ImageButton favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Get Movie object as Parcelable intent extra.
        Intent intent = getIntent();
        mMovie = (Movie) intent.getParcelableExtra(EXTRA_MOVIE);
        favoritesDb = AppDatabase.getInstance(getApplicationContext());
        setTitle(mMovie.getTitle());
        mPosterImageView = findViewById(R.id.detail_poster_iv);
        Picasso.with(this)
                .load(mMovie.getPosterUrl())
                .into(mPosterImageView);
        populateUI(mMovie);
        final ImageButton trailerButton = findViewById(R.id.play_trailer_button);
        trailerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playTrailer();
            }
        });
        final Button reviewsButton = findViewById(R.id.reviews_button);
        reviewsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchReviewsActivity();
            }
        });
        favoriteButton = findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleFavorite();
            }
        });
        setupViewModel();
    }

    private void playTrailer() {
        if (NetworkUtils.deviceIsConnected(getApplicationContext())) {
            URL tmdbUrl = NetworkUtils.buildTrailerQueryUrl(this, mMovie.getId(),
                    MainActivity.getThemoviedbApiKey());
            Log.d("Query URL",tmdbUrl.toString());
            MovieDbTrailerQueryTask fetchTrailersTask = new MovieDbTrailerQueryTask();
            fetchTrailersTask.execute(tmdbUrl);
        } else {
            Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_LONG).show();
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
    // AsyncTask for querying The Movie DB for videos (trailers, etc.) associated with a movie.
    // After querying, launch the first video in an implicit intent (will open in youtube app
    // if available, or in browser or other appropriate app).
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
            Toast noTrailerToast = Toast.makeText(getApplicationContext(),
                    "Cannot play trailer",Toast.LENGTH_LONG);
            if (queryResult != null) {
                Log.d("Trailer query result",queryResult);
                try {
                    movieTrailerYoutubeKey = JsonUtils.getTrailerYoutubeKey(queryResult);
                } catch (JSONException e) {
                    Log.e("JSON error", e.toString());
                }
            } else {
                noTrailerToast.show();
                return;
            }
            if (movieTrailerYoutubeKey != null) {
                Uri youtubeUri = NetworkUtils.buildYoutubeUri(movieTrailerYoutubeKey);
                Log.d("YoutubeURI",youtubeUri.toString());
                startActivity(new Intent(Intent.ACTION_VIEW, youtubeUri));
            } else {
                noTrailerToast.show();
            }
        }
    }

    // Launch the ReviewsActivity to show reviews for the current movie.
    private void launchReviewsActivity() {
        int movieId = mMovie.getId();
        String movieTitle = mMovie.getTitle();
        // Create intent for review activity.
        Intent reviewsActivityIntent = new Intent(this,ReviewsActivity.class);
        // Add position as an extra to the intent, so that we know which movie's details to get.
        reviewsActivityIntent.putExtra(ReviewsActivity.EXTRA_MOVIE_ID, movieId);
        reviewsActivityIntent.putExtra(ReviewsActivity.EXTRA_MOVIE_TITLE, movieTitle);
        // Launch activity
        startActivity(reviewsActivityIntent);
    }

    // Toggle the current movie as a favorite.
    // Star button image will be updated automatically because of the ViewModel observer.
    private void toggleFavorite() {
        if (mMovie.isFavorite()) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    favoritesDb.favoriteMovieDao().deleteFavoriteMovie(mMovie);
                }
            });
            mMovie.setFavorite(false);
            setFavoriteButtonImage(false);
        } else {
            mMovie.setFavorite(true);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    favoritesDb.favoriteMovieDao().insertFavoriteMovie(mMovie);
                }
            });
            setFavoriteButtonImage(true);
        }
    }

    // Set up ViewModel to observe when this movie is toggled as a favorite.
    private void setupViewModel() {
        RetrieveFavoriteViewModelFactory factory = new RetrieveFavoriteViewModelFactory(favoritesDb,mMovie.getId());
        final RetrieveFavoriteViewModel viewModel = ViewModelProviders.of(this, factory).get(RetrieveFavoriteViewModel.class);
        viewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    // Update member variable and UI to reflect database status.
                    mMovie.setFavorite(movie.isFavorite());
                    setFavoriteButtonImage(movie.isFavorite());
                }
            }
        });
    }

    private void setFavoriteButtonImage(boolean isFavorite) {
        if (isFavorite) {
            favoriteButton.setImageDrawable(
                    ContextCompat.getDrawable(this,android.R.drawable.btn_star_big_on));
        } else {
            favoriteButton.setImageDrawable(
                    ContextCompat.getDrawable(this,android.R.drawable.btn_star_big_off));
        }
    }
}

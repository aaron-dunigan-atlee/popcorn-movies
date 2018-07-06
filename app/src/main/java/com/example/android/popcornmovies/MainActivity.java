package com.example.android.popcornmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popcornmovies.database.AppDatabase;
import com.example.android.popcornmovies.model.Movie;
import com.example.android.popcornmovies.utilities.JsonUtils;
import com.example.android.popcornmovies.utilities.NetworkUtils;
import com.example.android.popcornmovies.utilities.PosterGridAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;


public class MainActivity
        extends AppCompatActivity
        implements PosterGridAdapter.OnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView.LayoutManager posterGridLayoutManager;
    private PosterGridAdapter gridAdapter;
    private String mMovieQueryResult = null;
    private String sortOrder;
    private static String themoviedbApiKey;
    private List<Movie> favoriteMovies;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.popcorn_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.open_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupPreferences();
        setupViewModel();
        // Set up RecyclerView with a GridLayoutManager
        RecyclerView posterGridRecyclerView = (RecyclerView) findViewById(R.id.poster_grid_rv);
        posterGridLayoutManager = new GridLayoutManager(this, 3);
        posterGridRecyclerView.setLayoutManager(posterGridLayoutManager);
        // Set adapter for the RecyclerView.
        gridAdapter = new PosterGridAdapter(this, this);
        posterGridRecyclerView.setAdapter(gridAdapter);
        // Reviewer or GitHub cloner: Add your API key in strings.xml with name themoviedb_api_key.
        themoviedbApiKey = getString(R.string.themoviedb_api_key);
        fetchMovies();

    }

    private void fetchMovies() {
        if (sortOrder.equals(getString(R.string.order_preference_favorite_value))) {
            // Fetch movies by the viewModel.
            if (favoriteMovies != null) {
                gridAdapter.setPosterUrls(favoriteMovies);
            }
        } else {
            // Fetch movies via API query.
            if (NetworkUtils.deviceIsConnected(this)) {
                URL tmdbUrl = NetworkUtils.buildMovieQueryUrl(this, sortOrder, themoviedbApiKey);
                MovieDbMovieQueryTask fetchMoviesTask = new MovieDbMovieQueryTask();
                fetchMoviesTask.execute(tmdbUrl);
            } else {
                Toast.makeText(this, "No network connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClicked(int position) {
        launchDetailActivity(position);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.order_preference_key))) {
            setSortOrder(sharedPreferences.getString(
                    getString(R.string.order_preference_key),
                    getString(R.string.order_preference_default)));
            fetchMovies();
        }
    }

    public class MovieDbMovieQueryTask extends AsyncTask<URL, Void, String> {

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
            mMovieQueryResult = queryResult;
            gridAdapter.setPosterUrls(queryResult);
        }
    }

    private void launchDetailActivity(int position) {
        Movie movie;
        if (sortOrder.equals(getString(R.string.order_preference_favorite_value))) {
            movie = favoriteMovies.get(position);
        } else {
            try {
                String movieJson = JsonUtils.getMovieJson(mMovieQueryResult, position);
                movie = JsonUtils.parseMovieJson(movieJson);

            } catch (JSONException e) {
                Log.e("JSON error", e.toString());
                Toast.makeText(this, "No details available.", Toast.LENGTH_LONG)
                        .show();
                return;
            }
        }
        // Create intent for detail activity.
        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        // Since Movie is Parcelable, we can add it as an extra to the intent.
        detailActivityIntent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        // Launch activity
        startActivity(detailActivityIntent);
    }
    // Set up all preferences by loading them from SharedPreferences,
    // and register the SharedPreferencesChangeListener so that future preference updates
    // are noticed.
    // This method is called during onCreate().
    private void setupPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setSortOrder(sharedPreferences.getString(
                getString(R.string.order_preference_key),
                getString(R.string.order_preference_default)));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    public String getSortOrder() { return sortOrder; }
    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }
    public static String getThemoviedbApiKey() { return themoviedbApiKey; }

    private void setupViewModel() {
        FavoritesViewModel viewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        viewModel.getFavorites().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                favoriteMovies = movies;
                if (sortOrder.equals(getString(R.string.order_preference_favorite_value))) {
                    gridAdapter.setPosterUrls(movies);
                }
            }
        });
    }
}

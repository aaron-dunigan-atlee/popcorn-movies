package com.example.android.popcornmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.popcornmovies.utilities.NetworkUtils;
import com.example.android.popcornmovies.utilities.PosterGridAdapter;

import java.io.IOException;
import java.net.URL;


public class MainActivity
        extends AppCompatActivity
        implements PosterGridAdapter.OnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView.LayoutManager posterGridLayoutManager;
    private PosterGridAdapter gridAdapter;
    private String mMovieQueryResult = null;
    private String sortOrder, themoviedbApiKey;


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
        URL tmdbUrl = NetworkUtils.buildUrl(this, sortOrder, themoviedbApiKey);
        MovieDbQueryTask fetchMoviesTask = new MovieDbQueryTask();
        fetchMoviesTask.execute(tmdbUrl);
    }

    @Override
    public void onItemClicked(int position) {
        launchDetailActivity(position, mMovieQueryResult);
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

    public class MovieDbQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String movieQueryJson = null;
            try {
                movieQueryJson = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieQueryJson;
        }

        @Override
        protected void onPostExecute(String queryResult) {
            super.onPostExecute(queryResult);
            mMovieQueryResult = queryResult;
            gridAdapter.setPosterUrls(queryResult);
        }
    }

    private void launchDetailActivity(int position, String tmdb_query_json) {
        // Create intent for detail activitiy.
        Intent detailActivityIntent = new Intent(this,DetailActivity.class);
        // Add position as an extra to the intent, so that we know which movie's details to get.
        detailActivityIntent.putExtra(DetailActivity.EXTRA_POSITION, position);
        detailActivityIntent.putExtra(DetailActivity.EXTRA_QUERY_JSON, tmdb_query_json);
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

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

}

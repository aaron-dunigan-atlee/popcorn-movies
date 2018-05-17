package com.example.android.popcornmovies;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popcornmovies.utilities.NetworkUtils;
import com.example.android.popcornmovies.utilities.PosterGridAdapter;

import java.io.IOException;
import java.net.URL;


public class MainActivity
        extends AppCompatActivity
        implements PosterGridAdapter.OnClickHandler {

    private TextView captionTextView, messageTextView;
    private RecyclerView.LayoutManager posterGridLayoutManager;
    private PosterGridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up RecyclerView with a GridLayoutManager
        RecyclerView posterGridRecyclerView = (RecyclerView) findViewById(R.id.poster_grid_rv);
        posterGridLayoutManager = new GridLayoutManager(this, 3);
        posterGridRecyclerView.setLayoutManager(posterGridLayoutManager);
        // Set adapter for the RecyclerView.
        gridAdapter = new PosterGridAdapter(this,this);
        posterGridRecyclerView.setAdapter(gridAdapter);

        // Reviewer or GitHub cloner: Add your API key in strings.xml with name themoviedb_api_key.
        String themoviedbApiKey = getString(R.string.themoviedb_api_key);

        //captionTextView = (TextView) findViewById(R.id.caption_tv);
        String sortOrder = "popular";
        URL tmdbUrl = NetworkUtils.buildUrl(this, sortOrder, themoviedbApiKey);
        //captionTextView.setText(tmdbUrl.toString());
        MovieDbQueryTask fetchMoviesTask = new MovieDbQueryTask();
        fetchMoviesTask.execute(tmdbUrl);
    }

    @Override
    public void onItemClicked(int position) {
        launchDetailActivity(position);
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
            /*if (queryResult != null) {
                messageTextView = findViewById(R.id.mesage_tv);
                messageTextView.setText(queryResult);
            }*/
            return;
        }
    }

    private void launchDetailActivity(int position) {
        // Create intent for detail activitiy.
        Intent detailActivityIntent = new Intent(this,DetailActivity.class);
        // Add position as an extra to the intent, so that we know which movie's details to get.
        detailActivityIntent.putExtra(DetailActivity.EXTRA_POSITION, position);
        // Launch activity
        startActivity(detailActivityIntent);
    }
}

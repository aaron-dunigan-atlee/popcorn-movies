package com.example.android.popcornmovies;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popcornmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private TextView captionTextView, messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView moviePosterImageView = (ImageView) findViewById(R.id.movie_poster_iv);
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w185/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg")
                .into(moviePosterImageView);
        // Reviewer or GitHub cloner: Add your API key in strings.xml with name themoviedb_api_key.
        String themoviedbApiKey = getString(R.string.themoviedb_api_key);

        captionTextView = (TextView) findViewById(R.id.caption_tv);
        String sortOrder = "popular";
        URL tmdbUrl = NetworkUtils.buildUrl(this, sortOrder, themoviedbApiKey);
        captionTextView.setText(tmdbUrl.toString());
    }
}

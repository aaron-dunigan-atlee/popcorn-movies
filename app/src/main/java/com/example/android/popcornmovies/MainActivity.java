package com.example.android.popcornmovies;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    // Reviewer or GitHub cloner: Add your API key in strings.xml with name themoviedb_api_key.
    // public String themoviedbApiKey = getResources().getString(R.string.themoviedb_api_key);
    private ImageView moviePosterImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviePosterImageView = (ImageView) findViewById(R.id.movie_poster_iv);
        Picasso.with(this)
                .load("https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Grilled_ham_and_cheese_014.JPG/800px-Grilled_ham_and_cheese_014.JPG")
                .into(moviePosterImageView);
    }
}

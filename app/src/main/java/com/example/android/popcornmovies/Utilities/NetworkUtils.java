package com.example.android.popcornmovies.utilities;

import android.content.Context;
import android.net.Uri;

import com.example.android.popcornmovies.R;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Dunigan AtLee on 5/7/2018.
 * Utilities for connecting to themoviedb.org api
 */

public class NetworkUtils {
    // The basic structure of this is modeled from T02.06-Exercise-AddPolish.
    static final String PARAM_KEY = "api_key";
    public static URL buildUrl(Context context, String sortOrder) {
        String baseUrl = context.getString(R.string.tmdb_base_url);
        String apiKey = context.getString(R.string.themoviedb_api_key);
        Uri tmdbUri = Uri.parse(baseUrl).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(PARAM_KEY, apiKey)
                .build();
        URL tmdbUrl = null;
        try {
            tmdbUrl = new URL(tmdbUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return tmdbUrl;
    }

}

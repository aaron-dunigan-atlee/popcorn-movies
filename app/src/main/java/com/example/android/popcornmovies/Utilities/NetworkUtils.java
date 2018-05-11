package com.example.android.popcornmovies.utilities;

import android.content.Context;
import android.net.Uri;

import com.example.android.popcornmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Dunigan AtLee on 5/7/2018.
 * Utilities for connecting to themoviedb.org api
 */

public class NetworkUtils {
    static final String PARAM_KEY = "api_key";
    // Form a URL for making the api call.
    // The basic structure of this is modeled from T02.06-Exercise-AddPolish.
    public static URL buildUrl(Context context, String sortOrder, String apiKey) {
        String baseUrl = context.getString(R.string.tmdb_base_url);
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
    /**
     * This method returns the entire result from the HTTP response.
     * Taken directly from the Sunshine project.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}

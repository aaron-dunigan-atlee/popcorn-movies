package com.example.android.popcornmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.example.android.popcornmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Dunigan AtLee on 5/7/2018.
 * Utilities for connecting to themoviedb.org api
 */

public class NetworkUtils {
    private static final String PARAM_KEY = "api_key";
    private static final String VIDEO_KEY = "v";
    // Form a URL for making the api call to get a list of movies by popularity or rating.
    // The basic structure of this is modeled from T02.06-Exercise-AddPolish.
    public static URL buildMovieQueryUrl(Context context, String sortOrder, String apiKey) {
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

    // Form a URL for making the api call to get a trailer for a given movie.
    // The basic structure of this is modeled from T02.06-Exercise-AddPolish.
    public static URL buildTrailerQueryUrl(Context context, int movieId, String apiKey) {
        String baseUrl = context.getString(R.string.tmdb_base_url);
        Uri tmdbUri = Uri.parse(baseUrl).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendPath("videos")
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

    // Form a URL for getting a youtube video.
    public static Uri buildYoutubeUri(String videoKey) {
        String baseUrl = "https://www.youtube.com";
        Uri youtubeUri = Uri.parse(baseUrl).buildUpon()
                .appendPath("watch")
                .appendQueryParameter(VIDEO_KEY, videoKey)
                .build();
        URL youtubeUrl = null;
        try {
            youtubeUrl = new URL(youtubeUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return youtubeUri;
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

    // Check if device is connected to network.
    // Taken from https://developer.android.com/training/monitoring-device-state/connectivity-monitoring#java
    // Requires permission ACCESS_NETWORK_STATE.
    public static boolean deviceIsConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting());
    }
}

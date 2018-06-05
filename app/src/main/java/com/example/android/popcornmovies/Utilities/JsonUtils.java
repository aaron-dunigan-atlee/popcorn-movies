package com.example.android.popcornmovies.utilities;

import com.example.android.popcornmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    /* Given JSON for an individual movie, return a Movie object with all fields set. */
    public static Movie parseMovieJson(String movieJson) throws JSONException {
        if (movieJson != null) {
            Movie movie = new Movie();
            JSONObject movieJsonObj = new JSONObject(movieJson);
            movie.setTitle(movieJsonObj.getString("title"));
            String ratingString = movieJsonObj.getString("vote_average");
            float ratingFloat = Float.parseFloat(ratingString);
            movie.setRating(ratingFloat);
            movie.setReleaseDate(movieJsonObj.getString("release_date"));
            movie.setSynopsis(movieJsonObj.getString("overview"));
            movie.setPosterUrl("http://image.tmdb.org/t/p/w185" + movieJsonObj.getString("poster_path"));
            return movie;
        } else {
            throw new JSONException("No JSON was retrieved. Check Internet connection.");
        }
    }

    /* Extract json string for an individual movie from the entire query result. */
    public static String getMovieJson(String tmdbQueryJson, int index) throws JSONException{
        if (tmdbQueryJson != null) {
            JSONObject queryJsonObj = new JSONObject(tmdbQueryJson);
            JSONArray movies = queryJsonObj.getJSONArray("results");
            return movies.getString(index);
        } else {
            throw new JSONException("No JSON was retrieved. Check Internet connection.");
        }
    }

    /* Build poster URL from movie JSON */
    public static String buildPosterUrl(String movieJson) throws JSONException{
        JSONObject movieJsonObj = new JSONObject(movieJson);
        String posterPath = movieJsonObj.getString("poster_path");
        return "http://image.tmdb.org/t/p/w185" + posterPath;
    }
}


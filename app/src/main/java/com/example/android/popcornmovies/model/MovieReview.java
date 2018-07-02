package com.example.android.popcornmovies.model;

import android.util.Log;

import com.example.android.popcornmovies.utilities.JsonUtils;

import org.json.JSONException;

public class MovieReview {

    private String
            author = "",
            review = "";

    public MovieReview(String reviewJson) {
        if (reviewJson != null) {
            try {
                this.author = JsonUtils.getAuthor(reviewJson);
                this.review = JsonUtils.getReview(reviewJson);
            } catch (JSONException e) {
                Log.e("Json Error:", e.toString());
            }

        }
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

}

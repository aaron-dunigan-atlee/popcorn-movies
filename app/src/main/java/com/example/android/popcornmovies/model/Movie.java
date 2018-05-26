package com.example.android.popcornmovies.model;

public class Movie {


    private String title;
    private String synopsis;
    private String releaseDate;
    private float rating;
    private String posterUrl;

    public Movie() {}

    public Movie(String title, String synopsis, String releaseDate, float rating, String posterUrl) {
        this.title = title;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getRating() {
        return rating;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}

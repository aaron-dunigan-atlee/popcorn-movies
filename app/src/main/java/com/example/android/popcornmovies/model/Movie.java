package com.example.android.popcornmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Movie {
    // Use tmdb movie id as the database primary key.
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String title;
    private String synopsis;
    private String releaseDate;
    private float rating;
    private String posterUrl;
    private String videoKey;

    @Ignore
    public Movie() {}

    public Movie(int id, String title, String synopsis, String releaseDate, float rating, String posterUrl) {
        this.id = id;
        this.title = title;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.posterUrl = posterUrl;
    }
    public int getId() { return id; }
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
    public String getVideoKey() { return videoKey; }

    public void setId(int id) { this.id = id; }
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
    public void setVideoKey(String videoKey) {this.videoKey = videoKey; }
}

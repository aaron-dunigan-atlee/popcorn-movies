package com.example.android.popcornmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "favorites")
public class Movie implements Parcelable {
    // Use tmdb movie id as the database primary key.
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String title;
    private String synopsis;
    private String releaseDate;
    private float rating;
    private String posterUrl;
    private String videoKey;
    private boolean isFavorite = false;

    @Ignore
    public Movie() {
    }

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
    public boolean isFavorite() {
        return isFavorite;
    }

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
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeStringArray(new String[]{title, synopsis, releaseDate, posterUrl, videoKey});
        dest.writeBooleanArray(new boolean[]{isFavorite});
        dest.writeFloat(rating);
    }

    @Ignore
    private Movie(Parcel in) {
        this.id = in.readInt();
        String[] fieldStrings = new String[5];
        in.readStringArray(fieldStrings);
        this.title = fieldStrings[0];
        this.synopsis = fieldStrings[1];
        this.releaseDate = fieldStrings[2];
        this.posterUrl = fieldStrings[3];
        this.videoKey = fieldStrings[4];
        boolean[] isFavoriteArray = new boolean[1];
        in.readBooleanArray(isFavoriteArray);
        this.isFavorite = isFavoriteArray[0];
        this.rating = in.readFloat();
    }

    // Boilerplate for the CREATOR constant.
    // Taken from https://guides.codepath.com/android/using-parcelable
    @Ignore
    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

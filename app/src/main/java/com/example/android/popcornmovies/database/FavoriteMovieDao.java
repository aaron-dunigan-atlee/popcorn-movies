package com.example.android.popcornmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.popcornmovies.model.Movie;

import java.util.List;

@Dao
public interface FavoriteMovieDao {
    @Query("SELECT * FROM favorites")
    LiveData<List<Movie>> loadFavoriteMovies();

    @Query("SELECT * FROM favorites WHERE id = :movieId")
    LiveData<Movie> loadMovieById(int movieId);

    @Insert
    void insertFavoriteMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavoriteMovie(Movie movie);

    @Delete
    void deleteFavoriteMovie(Movie movie);

}

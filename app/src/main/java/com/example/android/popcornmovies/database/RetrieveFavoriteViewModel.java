package com.example.android.popcornmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popcornmovies.database.AppDatabase;
import com.example.android.popcornmovies.model.Movie;

/* ViewModel for retrieving and caching a favorite movie by its id.
   Used in the DetailActivity to check whether the movie is a favorite.
   Modeled after Exercise T09b.10.
 */
public class RetrieveFavoriteViewModel extends ViewModel {
    private LiveData<Movie> movie;

    public RetrieveFavoriteViewModel(AppDatabase database, int movieId) {
        movie = database.favoriteMovieDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}

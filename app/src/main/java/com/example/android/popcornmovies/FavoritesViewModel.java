package com.example.android.popcornmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popcornmovies.database.AppDatabase;
import com.example.android.popcornmovies.model.Movie;

import java.util.List;
/* ViewModel for loading the entire favorites database. */
public class FavoritesViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> favorites;
    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favorites = database.favoriteMovieDao().loadFavoriteMovies();
    }
    public LiveData<List<Movie>> getFavorites() {
        return favorites;
    }

}

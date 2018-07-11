package com.example.android.popcornmovies.database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

/* ViewModelFactory for retrieving a single favorite from the favorites database.
 * Modeled after Exercise T09b.10.*/
public class RetrieveFavoriteViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase favoritesDb;
    private final int mMovieId;
    public RetrieveFavoriteViewModelFactory(AppDatabase database, int movieId) {
        favoritesDb = database;
        mMovieId = movieId;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RetrieveFavoriteViewModel(favoritesDb, mMovieId);
    }
}

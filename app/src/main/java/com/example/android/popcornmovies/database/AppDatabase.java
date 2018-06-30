package com.example.android.popcornmovies.database;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.popcornmovies.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorites";
    private static AppDatabase ourInstance;
    // Use singleton pattern to allow only one instance of AppDatabase.
    public static AppDatabase getInstance(Context context) {
        // If database doesn't exist, create it.
        if (ourInstance == null) {
            synchronized (LOCK) {
                ourInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        // Otherwise, just return the existing database as the instance.
        return ourInstance;
    }

    public abstract FavoriteMovieDao favoriteMovieDao();
}

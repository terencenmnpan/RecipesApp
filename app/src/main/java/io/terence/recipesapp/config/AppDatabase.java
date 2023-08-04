package io.terence.recipesapp.config;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import io.terence.recipesapp.daos.RecipeDao;
import io.terence.recipesapp.entities.Recipe;
import kotlin.jvm.Volatile;

@Database(entities = {Recipe.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    @Volatile
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                "app_database").allowMainThreadQueries().build();
    }

    public abstract RecipeDao recipeDao();
//
    //public abstract ExcursionDao excursionDao();
}

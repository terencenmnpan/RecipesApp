package io.terence.recipesapp.ui.recipes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.RecipeDao;
import io.terence.recipesapp.entities.Recipe;

public class RecipesViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
    private RecipeDao recipeDao;

    public RecipesViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(this.getApplication());
        recipeDao = appDatabase.recipeDao();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipeDao.getAllEntities();
    }
}
package io.terence.recipesapp.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.RecipeDao;
import io.terence.recipesapp.entities.Recipe;

public class SearchViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
    private RecipeDao recipeDao;

    public SearchViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(this.getApplication());
        recipeDao = appDatabase.recipeDao();
    }
    public void runSearch(String searchString){
        searchResults = recipeDao.getAllEntitiesLikeSearchString(searchString);
    }
    LiveData<List<Recipe>> searchResults;
    public LiveData<List<Recipe>> getSearchResults() {
        return searchResults;
    }
}

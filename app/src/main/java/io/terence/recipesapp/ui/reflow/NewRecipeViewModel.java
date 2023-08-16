package io.terence.recipesapp.ui.reflow;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.RecipeDao;
import io.terence.recipesapp.entities.Recipe;
import io.terence.recipesapp.entities.RecipeWithIngredientsAndSteps;

public class NewRecipeViewModel extends AndroidViewModel {

    //private final LiveData<String> mRecipeName;
    //private final LiveData<String> mRecipeDescription;
    //private final LiveData<List<IngredientDto>> mIngredients;
    //private final LiveData<List<Step>> mSteps;
    private AppDatabase appDatabase;
    private RecipeDao recipeDao;
    private int recipeId;

    public NewRecipeViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(this.getApplication());
        recipeDao = appDatabase.recipeDao();
        //mRecipeName = new MutableLiveData<>();
        //mRecipeDescription = new MutableLiveData<>();
        //mIngredients = new MutableLiveData<>(new ArrayList<>());
        //IngredientDto testIngredient = new IngredientDto();
        //testIngredient.setIngredientName("Flour");
        //testIngredient.setIngredientQuantity("1");
        //testIngredient.setIngredientUnit("cup");
        //mIngredients.getValue().add(testIngredient);
        //mSteps = new MutableLiveData<>(new ArrayList<>());
    }
    public LiveData<RecipeWithIngredientsAndSteps> getRecipeWithIngredientsAndSteps(){
        return recipeDao.loadSingleRecipeWithIngredientsAndSteps(recipeId);
    }
    public Recipe getRecipe(){
        return recipeDao.loadSingle(recipeId);
    }
    public LiveData<String> getRecipeName(){
        return recipeDao.loadSingleRecipeTitle(recipeId);
    }
    public LiveData<String> getRecipeDescription(){
        return recipeDao.loadSingleRecipeDescription(recipeId);
    }
    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
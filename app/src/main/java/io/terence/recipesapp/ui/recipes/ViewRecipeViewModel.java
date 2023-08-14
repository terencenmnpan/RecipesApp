package io.terence.recipesapp.ui.recipes;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.terence.recipesapp.entities.Ingredient;
import io.terence.recipesapp.entities.Step;

public class ViewRecipeViewModel extends ViewModel {

    //private final MutableLiveData<String> mText;

    private MutableLiveData<String> mRecipeName;
    private MutableLiveData<String> mRecipeDescription;
    private MutableLiveData<List<Ingredient>> mIngredients;
    private MutableLiveData<List<Step>> msteps;

    public ViewRecipeViewModel() {
        mRecipeName = new MutableLiveData<>();
        mRecipeDescription = new MutableLiveData<>();
        mIngredients = new MutableLiveData<>();
        msteps = new MutableLiveData<>();
        //mText.setValue("This is New Recipe fragment");
    }

    //public LiveData<String> getText() {
    //    return mText;
    //}

    public MutableLiveData<String> getmRecipeName() {
        return mRecipeName;
    }

    public void setmRecipeName(MutableLiveData<String> mRecipeName) {
        this.mRecipeName = mRecipeName;
    }

    public MutableLiveData<String> getmRecipeDescription() {
        return mRecipeDescription;
    }

    public void setmRecipeDescription(MutableLiveData<String> mRecipeDescription) {
        this.mRecipeDescription = mRecipeDescription;
    }

    public MutableLiveData<List<Step>> getMsteps() {
        return msteps;
    }

    public void setMsteps(MutableLiveData<List<Step>> msteps) {
        this.msteps = msteps;
    }

    public MutableLiveData<List<Ingredient>> getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(MutableLiveData<List<Ingredient>> mIngredients) {
        this.mIngredients = mIngredients;
    }
}
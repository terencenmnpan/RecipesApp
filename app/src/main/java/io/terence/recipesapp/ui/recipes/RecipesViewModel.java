package io.terence.recipesapp.ui.recipes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecipesViewModel extends ViewModel {

    private final MutableLiveData<List<String>> mTexts;

    public RecipesViewModel() {
        mTexts = new MutableLiveData<>();
        List<String> texts = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            texts.add("This is item # " + i);
        }
        mTexts.postValue(texts);
    }

    public LiveData<List<String>> getTexts() {
        return mTexts;
    }
}
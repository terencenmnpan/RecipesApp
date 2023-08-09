package io.terence.recipesapp.common;

import android.view.View;

import io.terence.recipesapp.entities.Recipe;

public interface RecipeClickListener {

    void onRecipeClick(Recipe recipe);
}

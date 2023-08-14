package io.terence.recipesapp.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RecipeWithIngredientsAndSteps {

    @Embedded
    public Recipe recipe;

    @Relation(
            parentColumn = "recipeId",
            entityColumn = "recipeId"
    )
    public List<Ingredient> ingredients;
    @Relation(
            parentColumn = "recipeId",
            entityColumn = "recipeId"
    )
    public List<Step> steps;
}

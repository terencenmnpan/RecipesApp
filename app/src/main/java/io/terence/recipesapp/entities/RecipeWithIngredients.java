package io.terence.recipesapp.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class RecipeWithIngredients {
    @Embedded
    public Recipe recipe;
    @Relation(
            parentColumn = "recipeId",
            entityColumn = "ingredientId",
            associateBy = @Junction(RecipeWithIngredientsCrossRef.class)
    )
    public List<Ingredient> ingredients;
}

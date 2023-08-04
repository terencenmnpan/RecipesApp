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
            entity = Recipe.class,
            entityColumn = "ingredientId",
            associateBy = @Junction(
                    value = RecipeWithIngredientsCrossRef.class,
                    parentColumn = "recipeId",
                    entityColumn = "ingredientId")
    )
    public List<RecipeWithIngredientsCrossRef> ingredients;
}

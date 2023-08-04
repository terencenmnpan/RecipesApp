package io.terence.recipesapp.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"recipeId", "ingredientId"},
        foreignKeys = {
                @ForeignKey(entity = Recipe.class, parentColumns = "recipeId", childColumns = "recipeId"),
                @ForeignKey(entity = Ingredient.class, parentColumns = "ingredientId", childColumns = "ingredientId")
        })
public class RecipeWithIngredientsCrossRef extends BaseEntity {
    int recipeId;
    int ingredientId;
    int quantity;

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

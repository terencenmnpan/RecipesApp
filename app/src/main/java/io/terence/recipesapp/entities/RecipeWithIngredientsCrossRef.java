package io.terence.recipesapp.entities;

import androidx.room.Entity;

@Entity(primaryKeys = {"recipeId", "ingredientId"})
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

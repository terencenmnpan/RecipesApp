package io.terence.recipesapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "ingredients")
public class Ingredient extends BaseEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int ingredientId;

    @NonNull
    private String ingredientName;

    private String quantity;
    private String unit;
    private int recipeId;

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return ingredientId == that.ingredientId && recipeId == that.recipeId && ingredientName.equals(that.ingredientName) && Objects.equals(quantity, that.quantity) && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, ingredientName, quantity, unit, recipeId);
    }
}

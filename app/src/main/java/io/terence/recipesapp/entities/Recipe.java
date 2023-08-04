package io.terence.recipesapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "recipes")
public class Recipe extends BaseEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int recipeId;

    @NonNull
    private String title;
    private String description;
    private List<Step> steps;

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}

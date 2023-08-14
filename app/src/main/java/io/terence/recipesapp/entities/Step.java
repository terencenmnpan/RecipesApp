package io.terence.recipesapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "steps")
public class Step extends BaseEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int stepId;
    @NonNull
    private int order;
    @NonNull
    private String description;
    private int recipeId;

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        Step step = (Step) o;
        return stepId == step.stepId && order == step.order && recipeId == step.recipeId && description.equals(step.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepId, order, description, recipeId);
    }
}

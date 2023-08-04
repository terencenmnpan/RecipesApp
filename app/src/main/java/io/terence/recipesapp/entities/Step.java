package io.terence.recipesapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "steps")
public class Step extends BaseEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int stepId;
    @NonNull
    private String order;
    @NonNull
    private String description;

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

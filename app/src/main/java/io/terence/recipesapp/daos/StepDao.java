package io.terence.recipesapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import io.terence.recipesapp.entities.Recipe;
import io.terence.recipesapp.entities.Step;

@Dao
public interface StepDao {
    @Upsert
    void upsert(Step entity);

    @Insert
    void insert(Step entity);

    @Update
    void update(Step entity);

    @Delete
    void delete(Step entity);

    @Query("SELECT count(*) FROM steps WHERE recipeId=:id")
    int getStepsCount(int id);
}

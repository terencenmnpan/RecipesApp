package io.terence.recipesapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Upsert;

import io.terence.recipesapp.entities.Ingredient;

@Dao
public interface IngredientDao {
    @Upsert
    void upsert(Ingredient entity);

    @Insert
    void insert(Ingredient entity);

    @Update
    void update(Ingredient entity);

    @Delete
    void delete(Ingredient entity);
}

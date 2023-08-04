package io.terence.recipesapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

import io.terence.recipesapp.entities.Recipe;
import io.terence.recipesapp.entities.RecipeWithIngredients;

@Dao
public interface RecipeDao {
    @Upsert
    void upsert(Recipe entity);

    @Insert
    void insert(Recipe entity);

    @Update
    void update(Recipe entity);

    @Delete
    void delete(Recipe entity);

    @Query("SELECT * FROM recipes")
    List<Recipe> getAllEntities();

    @Query("SELECT * FROM recipes WHERE recipeId=:id")
    Recipe loadSingle(int id);
    @Transaction
    @Query("SELECT * FROM recipes")
    List<RecipeWithIngredients> getAllVacationsWithExcursions();

    @Transaction
    @Query("SELECT * FROM recipes WHERE recipeId=:id")
    RecipeWithIngredients getRecipeWithIngredients(int id);

    //@Query("SELECT count(*) FROM steps WHERE vacationId=:id")
    //int countExcursions(int id);
//
    //@Query("SELECT * FROM excursions WHERE id=:id")
    //Excursion loadSingleExcursion(int id);
}

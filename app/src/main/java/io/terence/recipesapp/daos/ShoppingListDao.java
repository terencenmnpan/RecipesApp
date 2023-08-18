package io.terence.recipesapp.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

import io.terence.recipesapp.entities.ShoppingItem;

@Dao
public interface ShoppingListDao {
    @Upsert
    void upsert(ShoppingItem entity);

    @Insert
    void insert(ShoppingItem entity);

    @Update
    void update(ShoppingItem entity);

    @Delete
    void delete(ShoppingItem entity);


    @Query("SELECT * FROM shopping_list_item where bought = 0")
    LiveData<List<ShoppingItem>> getAllEntities();
}

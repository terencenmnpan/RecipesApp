package io.terence.recipesapp.ui.shoppinglist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.ShoppingListDao;
import io.terence.recipesapp.entities.ShoppingItem;

public class ShoppingListViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
    private ShoppingListDao shoppingListDao;

    public ShoppingListViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(this.getApplication());
        shoppingListDao = appDatabase.shoppingListDao();
    }

    public LiveData<List<ShoppingItem>> getShoppingList(){
        return shoppingListDao.getAllEntities();
    }
}

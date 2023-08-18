package io.terence.recipesapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = "shopping_list_item")
public class ShoppingItem extends BaseEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int shoppingListId;
    @NonNull
    private String itemName;
    private boolean bought;
    private LocalDateTime boughtDate;

    public int getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public LocalDateTime getBoughtDate() {
        return boughtDate;
    }

    public void setBoughtDate(LocalDateTime boughtDate) {
        this.boughtDate = boughtDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingItem that = (ShoppingItem) o;
        return shoppingListId == that.shoppingListId && bought == that.bought && itemName.equals(that.itemName) && Objects.equals(boughtDate, that.boughtDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shoppingListId, itemName, bought, boughtDate);
    }
}

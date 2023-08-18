package io.terence.recipesapp.ui.shoppinglist;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.terence.recipesapp.entities.ShoppingItem;
import io.terence.recipesapp.databinding.ShoppingItemBinding;

public class MyShoppingItemRecyclerViewAdapter extends ListAdapter<ShoppingItem, MyShoppingItemRecyclerViewAdapter.ViewHolder> {

    private final ShoppingListOnMenuItemClickListener shoppingListOnMenuItemClickListener;

    public MyShoppingItemRecyclerViewAdapter(ShoppingListOnMenuItemClickListener shoppingListOnMenuItemClickListener) {
        super(new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull ShoppingItem oldItem, @NonNull ShoppingItem newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull ShoppingItem oldItem, @NonNull ShoppingItem newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.shoppingListOnMenuItemClickListener = shoppingListOnMenuItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(ShoppingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), shoppingListOnMenuItemClickListener);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = getItem(position);
        holder.mShoppingItemName.setText(getItem(position).getItemName());
        holder.mShoppingItemAddedDate.setText(getItem(position).getCreateDate().toLocalDate().toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final TextView mShoppingItemName;
        public final TextView mShoppingItemAddedDate;
        private final ShoppingListOnMenuItemClickListener shoppingListOnMenuItemClickListener;
        public ShoppingItem mItem;

        public ViewHolder(ShoppingItemBinding binding, ShoppingListOnMenuItemClickListener shoppingListOnMenuItemClickListener) {
            super(binding.getRoot());
            mShoppingItemName = binding.shoppingItem;
            mShoppingItemAddedDate = binding.shoppingItemAddedDate;
            this.shoppingListOnMenuItemClickListener = shoppingListOnMenuItemClickListener;
            binding.getRoot().setOnCreateContextMenuListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mShoppingItemAddedDate.getText() + "'";
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            //menuInfo is null
            MenuItem bought = menu.add(Menu.NONE, v.getId(),
                    Menu.NONE, "Mark Bought");
            bought.setOnMenuItemClickListener(item -> {
                shoppingListOnMenuItemClickListener.onMenuItemClickListener(mItem);
                return false;});
        }
    }
}
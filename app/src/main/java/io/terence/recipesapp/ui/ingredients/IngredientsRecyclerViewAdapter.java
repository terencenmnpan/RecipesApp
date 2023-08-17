package io.terence.recipesapp.ui.ingredients;

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

import io.terence.recipesapp.databinding.IngredientItemBinding;
import io.terence.recipesapp.entities.Ingredient;

public class IngredientsRecyclerViewAdapter extends ListAdapter<Ingredient, IngredientsRecyclerViewAdapter.ViewHolder> {

    public final IngredientOnMenuItemClickListener deleteIngredientOnMenuItemClickListener;
    public final IngredientOnMenuItemClickListener editIngredientOnMenuItemClickListener;
    public IngredientsRecyclerViewAdapter(IngredientOnMenuItemClickListener deleteIngredientOnMenuItemClickListener, IngredientOnMenuItemClickListener editIngredientOnMenuItemClickListener) {

        super(new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.deleteIngredientOnMenuItemClickListener = deleteIngredientOnMenuItemClickListener;
        this.editIngredientOnMenuItemClickListener = editIngredientOnMenuItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(IngredientItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),
                deleteIngredientOnMenuItemClickListener, editIngredientOnMenuItemClickListener);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = getCurrentList().get(position);
        holder.mIngredientNameView.setText(getItem(position).getIngredientName());
        holder.mIngredientQuantityView.setText(getItem(position).getQuantity());
        holder.mIngredientUnitView.setText(getItem(position).getUnit());
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final TextView mIngredientNameView;
        public final TextView mIngredientQuantityView;
        public final TextView mIngredientUnitView;
        public Ingredient mItem;
        public final IngredientOnMenuItemClickListener deleteIngredientOnMenuItemClickListener;
        public final IngredientOnMenuItemClickListener editIngredientOnMenuItemClickListener;
        public ViewHolder(IngredientItemBinding binding, IngredientOnMenuItemClickListener deleteIngredientOnMenuItemClickListener, IngredientOnMenuItemClickListener editIngredientOnMenuItemClickListener) {
            super(binding.getRoot());
            mIngredientNameView = binding.ingredientName;
            mIngredientQuantityView = binding.ingredientQuantity;
            mIngredientUnitView = binding.ingredientUnit;
            this.deleteIngredientOnMenuItemClickListener = deleteIngredientOnMenuItemClickListener;
            this.editIngredientOnMenuItemClickListener = editIngredientOnMenuItemClickListener;
            binding.getRoot().setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            //menuInfo is null
            MenuItem delete = menu.add(Menu.NONE, v.getId(),
                    Menu.NONE, "Delete");
            delete.setOnMenuItemClickListener(item -> {
                deleteIngredientOnMenuItemClickListener.onMenuItemClickListener(mItem);
                return false;});
            MenuItem edit = menu.add(Menu.NONE, v.getId(),
                    Menu.NONE, "Edit");
            edit.setOnMenuItemClickListener(item -> {
                editIngredientOnMenuItemClickListener.onMenuItemClickListener(mItem);
                return false;});
        }
        @Override
        public String toString() {
            return "ViewHolder{" +
                    "mIngredientNameView=" + mIngredientNameView +
                    ", mIngredientQuantityView=" + mIngredientQuantityView +
                    ", mIngredientUnitView=" + mIngredientUnitView +
                    ", mItem=" + mItem +
                    '}';
        }
    }
}
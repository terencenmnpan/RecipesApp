package io.terence.recipesapp.ui.ingredients;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import io.terence.recipesapp.databinding.IngredientItemBinding;
import io.terence.recipesapp.entities.Ingredient;

public class IngredientsRecyclerViewAdapter extends ListAdapter<Ingredient, IngredientsRecyclerViewAdapter.ViewHolder> {

    public IngredientsRecyclerViewAdapter() {

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
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(IngredientItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = getCurrentList().get(position);
        holder.mIngredientNameView.setText(getItem(position).getIngredientName());
        holder.mIngredientQuantityView.setText(getItem(position).getQuantity());
        holder.mIngredientUnitView.setText(getItem(position).getUnit());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIngredientNameView;
        public final TextView mIngredientQuantityView;
        public final TextView mIngredientUnitView;
        public Ingredient mItem;

        public ViewHolder(IngredientItemBinding binding) {
            super(binding.getRoot());
            mIngredientNameView = binding.ingredientName;
            mIngredientQuantityView = binding.ingredientQuantity;
            mIngredientUnitView = binding.ingredientUnit;
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
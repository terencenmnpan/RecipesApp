package io.terence.recipesapp.ui.steps;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import io.terence.recipesapp.entities.Ingredient;
import io.terence.recipesapp.entities.Step;
import io.terence.recipesapp.placeholder.PlaceholderContent.PlaceholderItem;
import io.terence.recipesapp.databinding.FragmentStepBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class StepsRecyclerViewAdapter extends ListAdapter<Step, StepsRecyclerViewAdapter.ViewHolder> {


    public StepsRecyclerViewAdapter() {

        super(new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull Step oldItem, @NonNull Step newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Step oldItem, @NonNull Step newItem) {
                return oldItem.equals(newItem);
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentStepBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentStepBinding binding) {
            super(binding.getRoot());
            mIdView = binding.stepNumber;
            mContentView = binding.stepDescription;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
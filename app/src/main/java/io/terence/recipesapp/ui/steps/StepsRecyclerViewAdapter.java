package io.terence.recipesapp.ui.steps;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import io.terence.recipesapp.entities.Step;
import io.terence.recipesapp.databinding.FragmentStepBinding;

import java.util.List;

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
        holder.mItem = getCurrentList().get(position);
        holder.mStepDescription.setText(getItem(position).getDescription());
        holder.mStepNumber.setText(getItem(position).getOrder());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mStepNumber;
        public final TextView mStepDescription;
        public Step mItem;

        public ViewHolder(FragmentStepBinding binding) {
            super(binding.getRoot());
            mStepNumber = binding.stepNumber;
            mStepDescription = binding.stepDescription;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mStepNumber.getText() + "'"
                    + mStepDescription.getText() + "'";
        }
    }
}
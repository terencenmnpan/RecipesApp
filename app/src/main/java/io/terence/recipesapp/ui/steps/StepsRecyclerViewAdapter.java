package io.terence.recipesapp.ui.steps;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.terence.recipesapp.entities.Step;
import io.terence.recipesapp.databinding.FragmentStepBinding;

public class StepsRecyclerViewAdapter extends ListAdapter<Step, StepsRecyclerViewAdapter.ViewHolder> {


    public final StepsOnMenuItemClickListener deleteStepsOnMenuItemClickListener;
    public final StepsOnMenuItemClickListener editStepsOnMenuItemClickListener;

    public StepsRecyclerViewAdapter(StepsOnMenuItemClickListener deleteStepsOnMenuItemClickListener, StepsOnMenuItemClickListener editStepsOnMenuItemClickListener) {

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

        this.deleteStepsOnMenuItemClickListener = deleteStepsOnMenuItemClickListener;
        this.editStepsOnMenuItemClickListener = editStepsOnMenuItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentStepBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false),
                deleteStepsOnMenuItemClickListener, editStepsOnMenuItemClickListener);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = getCurrentList().get(position);
        holder.mStepDescription.setText(getItem(position).getDescription());
        holder.mStepNumber.setText(Integer.toString(getItem(position).getOrder()));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
    {
        public final TextView mStepNumber;
        public final TextView mStepDescription;
        public Step mItem;

        public final StepsOnMenuItemClickListener deleteStepsOnMenuItemClickListener;
        public final StepsOnMenuItemClickListener editStepsOnMenuItemClickListener;

        public ViewHolder(FragmentStepBinding binding, StepsOnMenuItemClickListener deleteStepsOnMenuItemClickListener, StepsOnMenuItemClickListener editStepsOnMenuItemClickListener) {
            super(binding.getRoot());
            mStepNumber = binding.stepNumber;
            mStepDescription = binding.stepDescription;
            this.deleteStepsOnMenuItemClickListener = deleteStepsOnMenuItemClickListener;
            this.editStepsOnMenuItemClickListener = editStepsOnMenuItemClickListener;
            binding.getRoot().setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            //menuInfo is null
            MenuItem delete = menu.add(Menu.NONE, v.getId(),
                    Menu.NONE, "Delete");
            delete.setOnMenuItemClickListener(item -> {
                deleteStepsOnMenuItemClickListener.onMenuItemClickListener(mItem);
                return false;});
            MenuItem edit = menu.add(Menu.NONE, v.getId(),
                    Menu.NONE, "Edit");
            edit.setOnMenuItemClickListener(item -> {
                editStepsOnMenuItemClickListener.onMenuItemClickListener(mItem);
                return false;});
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mStepNumber.getText() + "'"
                    + mStepDescription.getText() + "'";
        }
    }
}
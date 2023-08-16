package io.terence.recipesapp.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.terence.recipesapp.R;
import io.terence.recipesapp.common.RecipeClickListener;
import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.databinding.FragmentRecipesBinding;
import io.terence.recipesapp.databinding.FragmentSearchBinding;
import io.terence.recipesapp.databinding.ItemRecipeBinding;
import io.terence.recipesapp.entities.Recipe;
import io.terence.recipesapp.ui.recipes.RecipesFragment;
import io.terence.recipesapp.ui.recipes.RecipesFragmentDirections;
import io.terence.recipesapp.ui.recipes.RecipesViewModel;

public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    SearchViewModel searchViewModel;
    androidx.navigation.fragment.NavHostFragment navHostFragment;
    NavController navController;
    private FragmentSearchBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        loadTableData();
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    private void loadTableData() {

        RecyclerView recyclerView = binding.recyclerviewSearchrecipes;
        ListAdapter<Recipe, SearchFragment.RecipeViewHolder> stepsRecyclerViewAdapter = new SearchFragment.RecipeAdapter(recipe -> {
            SearchFragmentDirections.ActionNavSearchToNavNewRecipe actionNavSearchToNavNewRecipe =
                    io.terence.recipesapp.ui.search.SearchFragmentDirections.actionNavSearchToNavNewRecipe();
            actionNavSearchToNavNewRecipe.setRecipeId(recipe.getRecipeId());
            navController.navigate(actionNavSearchToNavNewRecipe);
        });
        searchViewModel.getRecipes().observe(getViewLifecycleOwner(), stepsRecyclerViewAdapter::submitList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(stepsRecyclerViewAdapter);
    }

    private static class RecipeAdapter extends ListAdapter<Recipe, SearchFragment.RecipeViewHolder> {
        private final RecipeClickListener listener;

        protected RecipeAdapter(RecipeClickListener listener) {

            super(new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
                    return oldItem.equals(newItem);
                }
            });
            this.listener = listener;
        }

        @NonNull
        @Override
        public SearchFragment.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemRecipeBinding binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new SearchFragment.RecipeViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchFragment.RecipeViewHolder holder, int position) {

            holder.textView.setText(getItem(position).getTitle());
            holder.bind(getItem(position), listener);
        }

    }

    private static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView textView;
        private final Button addToShopping;

        public RecipeViewHolder(ItemRecipeBinding binding) {
            super(binding.getRoot());
            imageView = binding.imageViewItemTransform;
            textView = binding.textViewItemTransform;
            addToShopping = binding.button;
        }

        public void bind(final Recipe recipe, final RecipeClickListener listener) {
            itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
            //TODO add on fling gesture listener for addtoshopping cart
        }
    }
}
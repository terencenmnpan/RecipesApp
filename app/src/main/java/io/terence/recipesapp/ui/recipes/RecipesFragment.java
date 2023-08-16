package io.terence.recipesapp.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import io.terence.recipesapp.R;
import io.terence.recipesapp.common.RecipeClickListener;
import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.RecipeDao;
import io.terence.recipesapp.databinding.FragmentRecipesBinding;
import io.terence.recipesapp.databinding.ItemRecipeBinding;
import io.terence.recipesapp.entities.Recipe;
import io.terence.recipesapp.entities.Step;
import io.terence.recipesapp.ui.reflow.NewRecipeFragment;
import io.terence.recipesapp.ui.reflow.NewRecipeViewModel;
import io.terence.recipesapp.ui.steps.StepsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
public class RecipesFragment extends Fragment {
    private View root;
    RecipesViewModel recipesViewModel;
    androidx.navigation.fragment.NavHostFragment navHostFragment;
    NavController navController;
    private FragmentRecipesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        recipesViewModel = new ViewModelProvider(this).get(RecipesViewModel.class);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

        binding = FragmentRecipesBinding.inflate(inflater, container, false);

        loadTableData();
        root = binding.getRoot();
        return root;
    }

    private void loadTableData() {

        RecyclerView recyclerView = binding.recyclerviewRecipes;
            ListAdapter<Recipe, RecipeViewHolder> stepsRecyclerViewAdapter = new RecipeAdapter(recipe -> {
                io.terence.recipesapp.ui.recipes.RecipesFragmentDirections.ActionNavRecipesToNavNewRecipe actionNavRecipesToNavNewRecipe =
                        RecipesFragmentDirections.actionNavRecipesToNavNewRecipe();
                actionNavRecipesToNavNewRecipe.setRecipeId(recipe.getRecipeId());
                navController.navigate(actionNavRecipesToNavNewRecipe);
            });
            recipesViewModel.getRecipes().observe(getViewLifecycleOwner(), stepsRecyclerViewAdapter::submitList);

            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recyclerView.setAdapter(stepsRecyclerViewAdapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class RecipeAdapter extends ListAdapter<Recipe, RecipeViewHolder> {
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
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemRecipeBinding binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new RecipeViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

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
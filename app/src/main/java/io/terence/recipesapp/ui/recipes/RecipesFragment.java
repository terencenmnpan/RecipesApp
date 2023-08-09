package io.terence.recipesapp.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import io.terence.recipesapp.ui.reflow.NewRecipeFragment;

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
    private RecipeDao recipeDao;
    private AppDatabase appDatabase;
    androidx.navigation.fragment.NavHostFragment navHostFragment;
    NavController navController;
    List<Recipe> entityList = new ArrayList<>();
    private FragmentRecipesBinding binding;
    private RecipeAdapter recipeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

        binding = FragmentRecipesBinding.inflate(inflater, container, false);

        appDatabase = AppDatabase.getInstance(this.getContext());
        recipeDao = appDatabase.recipeDao();
        loadTableData();
        return root;
    }

    private void loadTableData() {

        RecyclerView recyclerView = binding.recyclerviewRecipes;
        new Thread(() -> {
            RecipesViewModel recipesViewModel =
                    new ViewModelProvider(this).get(RecipesViewModel.class);
            entityList = recipeDao.getAllEntities();
            root = binding.getRoot();
            getActivity().runOnUiThread(() -> {
                recipeAdapter = new RecipeAdapter(entityList, recipe -> {
                    //Intent intent =  new Intent(this.getContext(), NewRecipeFragment.class);
                    //intent.putExtra("editRecipeId", recipe.getRecipeId());
                    //startActivity(intent);
                    io.terence.recipesapp.ui.recipes.RecipesFragmentDirections.ActionNavRecipesToNavNewRecipe actionNavRecipesToNavNewRecipe =
                            RecipesFragmentDirections.actionNavRecipesToNavNewRecipe();
                    actionNavRecipesToNavNewRecipe.setRecipeId(recipe.getRecipeId());
                    navController.navigate(actionNavRecipesToNavNewRecipe);
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
                recyclerView.setAdapter(recipeAdapter);
                //alerts();

                //recipesViewModel.getTexts().observe(RecipesFragment.this, recipeAdapter::submitList);
            });
        }).start();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class RecipeAdapter extends ListAdapter<String, RecipeViewHolder> {

        private final List<Recipe> recipes;
        private final RecipeClickListener listener;

        protected RecipeAdapter(List<Recipe> recipes, RecipeClickListener listener) {

            super(new DiffUtil.ItemCallback<String>() {
                @Override
                public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }
            });
            this.recipes = recipes;
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
            Recipe recipe = recipes.get(position);
            holder.textView.setText(recipe.getTitle());
            holder.bind(recipe, listener);
        }

        @Override
        public int getItemCount() {
            return recipes.size();
        }

    }

    private static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView textView;

        public RecipeViewHolder(ItemRecipeBinding binding) {
            super(binding.getRoot());
            imageView = binding.imageViewItemTransform;
            textView = binding.textViewItemTransform;
        }

        public void bind(final Recipe recipe, final RecipeClickListener listener) {
            itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
        }
    }
}
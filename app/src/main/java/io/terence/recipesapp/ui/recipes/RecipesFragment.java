package io.terence.recipesapp.ui.recipes;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;

import io.terence.recipesapp.R;
import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.RecipeDao;
import io.terence.recipesapp.daos.ShoppingListDao;
import io.terence.recipesapp.databinding.FragmentRecipesBinding;
import io.terence.recipesapp.databinding.ItemRecipeBinding;
import io.terence.recipesapp.entities.Ingredient;
import io.terence.recipesapp.entities.Recipe;
import io.terence.recipesapp.entities.RecipeWithIngredientsAndSteps;
import io.terence.recipesapp.entities.ShoppingItem;

/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
public class RecipesFragment extends Fragment {
    private View root;
    RecipesViewModel recipesViewModel;
    private ShoppingListDao shoppingListDao;
    private RecipeDao recipeDao;
    private AppDatabase appDatabase;
    androidx.navigation.fragment.NavHostFragment navHostFragment;
    NavController navController;
    private FragmentRecipesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        recipesViewModel = new ViewModelProvider(this).get(RecipesViewModel.class);

        appDatabase = AppDatabase.getInstance(requireActivity().getApplicationContext());
        shoppingListDao = appDatabase.shoppingListDao();
        recipeDao = appDatabase.recipeDao();

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
        ListAdapter<Recipe, RecipeViewHolder> recyclerViewAdapter = new RecipeAdapter(recipe -> {
            io.terence.recipesapp.ui.recipes.RecipesFragmentDirections.ActionNavRecipesToNavNewRecipe actionNavRecipesToNavNewRecipe =
                    RecipesFragmentDirections.actionNavRecipesToNavNewRecipe();
            actionNavRecipesToNavNewRecipe.setRecipeId(recipe.getRecipeId());
            navController.navigate(actionNavRecipesToNavNewRecipe);
        }, this::addToShoppingList);
        recipesViewModel.getRecipes().observe(getViewLifecycleOwner(), recyclerViewAdapter::submitList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void addToShoppingList(Recipe recipe){
        RecipeWithIngredientsAndSteps recipeWithIngredientsAndSteps =
                recipeDao.loadRecipeWithIngredientsAndSteps(recipe.getRecipeId());
        assert recipeWithIngredientsAndSteps != null;
        for(Ingredient ingredient : recipeWithIngredientsAndSteps.ingredients){
            ShoppingItem shoppingItem = new ShoppingItem();
            shoppingItem.setItemName(ingredient.getIngredientName());
            shoppingItem.setCreateDate(LocalDateTime.now());
            shoppingListDao.upsert(shoppingItem);
        }
    }
    private static class RecipeAdapter extends ListAdapter<Recipe, RecipeViewHolder> {
        private final RecipeClickListener listener;

        private final RecipeOnMenuItemClickListener addToShoppingListOnMenuItemClickListener;

        protected RecipeAdapter(RecipeClickListener listener, RecipeOnMenuItemClickListener addToShoppingListOnMenuItemClickListener) {

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
            this.addToShoppingListOnMenuItemClickListener = addToShoppingListOnMenuItemClickListener;
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemRecipeBinding binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new RecipeViewHolder(binding, addToShoppingListOnMenuItemClickListener);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

            holder.textView.setText(getItem(position).getTitle());
            holder.recipe = getItem(position);
            holder.bind(getItem(position), listener);
        }

    }

    private static class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final TextView textView;
        private final RecipeOnMenuItemClickListener addToShoppingListOnMenuItemClickListener;
        public Recipe recipe;

        public RecipeViewHolder(ItemRecipeBinding binding, RecipeOnMenuItemClickListener addToShoppingListOnMenuItemClickListener) {
            super(binding.getRoot());
            textView = binding.textViewItemTransform;
            this.addToShoppingListOnMenuItemClickListener = addToShoppingListOnMenuItemClickListener;
            binding.getRoot().setOnCreateContextMenuListener(this);
        }

        public void bind(final Recipe recipe, final RecipeClickListener listener) {
            itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            //menuInfo is null
            MenuItem delete = menu.add(Menu.NONE, v.getId(),
                    Menu.NONE, "Add to Shopping List");
            delete.setOnMenuItemClickListener(item -> {
                addToShoppingListOnMenuItemClickListener.onMenuItemClickListener(recipe);
                return false;});
        }
    }
}
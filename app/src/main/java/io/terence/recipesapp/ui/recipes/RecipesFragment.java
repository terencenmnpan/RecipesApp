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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import io.terence.recipesapp.R;
import io.terence.recipesapp.common.RecipeClickListener;
import io.terence.recipesapp.databinding.FragmentRecipesBinding;
import io.terence.recipesapp.databinding.ItemRecipeBinding;
import io.terence.recipesapp.entities.Recipe;
import io.terence.recipesapp.ui.reflow.NewRecipeFragment;

import java.util.Arrays;
import java.util.List;

/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
public class RecipesFragment extends Fragment {

    private FragmentRecipesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecipesViewModel recipesViewModel =
                new ViewModelProvider(this).get(RecipesViewModel.class);

        binding = FragmentRecipesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerviewRecipes;
        ListAdapter<String, RecipeViewHolder> adapter = new RecipeAdapter(null//TODO recipes
                , recipe -> {
            Intent intent =  new Intent(this.getContext(), NewRecipeFragment.class);
            intent.putExtra("editRecipeId", recipe.getRecipeId());
            startActivity(intent);});
        recyclerView.setAdapter(adapter);
        recipesViewModel.getTexts().observe(getViewLifecycleOwner(), adapter::submitList);
        return root;
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

            holder.textView.setText(getItem(position));
            holder.imageView.setImageDrawable(
                    ResourcesCompat.getDrawable(holder.imageView.getResources(),
                            drawables.get(position),
                            null));
            holder.bind(recipe, listener);
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
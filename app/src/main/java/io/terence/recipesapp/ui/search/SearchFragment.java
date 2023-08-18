package io.terence.recipesapp.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.terence.recipesapp.R;
import io.terence.recipesapp.ui.recipes.RecipeClickListener;
import io.terence.recipesapp.databinding.FragmentSearchBinding;
import io.terence.recipesapp.databinding.ItemRecipeBinding;
import io.terence.recipesapp.entities.Recipe;

public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    private View root;
    SearchViewModel searchViewModel;
    androidx.navigation.fragment.NavHostFragment navHostFragment;
    NavController navController;
    private FragmentSearchBinding binding;
    EditText searchString;
    Button searchBtn;

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
        root = binding.getRoot();
        searchString = root.findViewById(R.id.searchString);
        searchBtn = root.findViewById(R.id.searchBtn);
        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();
        searchBtn.setOnClickListener(this::searchRecipes);
        return root;
    }

    private void loadTableData() {

        RecyclerView recyclerView = binding.recyclerviewSearchrecipes;
        ListAdapter<Recipe, SearchFragment.RecipeViewHolder> recyclerViewAdapter = new SearchFragment.RecipeAdapter(recipe -> {
            SearchFragmentDirections.ActionNavSearchToNavNewRecipe actionNavSearchToNavNewRecipe =
                    io.terence.recipesapp.ui.search.SearchFragmentDirections.actionNavSearchToNavNewRecipe();
            actionNavSearchToNavNewRecipe.setRecipeId(recipe.getRecipeId());
            navController.navigate(actionNavSearchToNavNewRecipe);
        });
        searchViewModel.getSearchResults().observe(getViewLifecycleOwner(), recyclerViewAdapter::submitList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);
    }
    public void searchRecipes(View view){
        searchViewModel.runSearch(searchString.getText().toString());
        loadTableData();
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

    private static class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final TextView textView;

        public RecipeViewHolder(ItemRecipeBinding binding) {
            super(binding.getRoot());
            textView = binding.textViewItemTransform;
        }

        public void bind(final Recipe recipe, final RecipeClickListener listener) {
            itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
}
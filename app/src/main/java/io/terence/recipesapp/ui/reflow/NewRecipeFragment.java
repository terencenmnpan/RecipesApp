package io.terence.recipesapp.ui.reflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import io.terence.recipesapp.R;
import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.RecipeDao;
import io.terence.recipesapp.databinding.FragmentNewRecipeBinding;
import io.terence.recipesapp.entities.Recipe;

public class NewRecipeFragment extends Fragment {

    private FragmentNewRecipeBinding binding;

    private RecipeDao recipeDao;
    private AppDatabase appDatabase;
    private Recipe recipe;
    private EditText recipeTitle;
    private EditText recipeDescription;
    private Button saveRecipe;
    private Button addIngredient;
    private Button addStep;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewRecipeViewModel newRecipeViewModel =
                new ViewModelProvider(this).get(NewRecipeViewModel.class);


        appDatabase = AppDatabase.getInstance(requireActivity().getApplicationContext());
        recipeDao = appDatabase.recipeDao();

        binding = FragmentNewRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textNewRecipe;
        //newRecipeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //Initialize stuff here
        recipe = new Recipe();
        recipeTitle = root.findViewById(R.id.recipe_title);
        recipeDescription = root.findViewById(R.id.recipe_description);
        saveRecipe = root.findViewById(R.id.save_recipe_btn);
        saveRecipe.setOnClickListener(v -> saveRecipe());
        addIngredient = root.findViewById(R.id.add_ingredient_btn);
        addStep = root.findViewById(R.id.add_step_btn);
        return root;
    }


    public void saveRecipe(){
        //validateRecipe();
        //if(hasErrors()){
        //    return;
        //}

        recipe.setTitle(recipeTitle.getText().toString());
        recipe.setDescription(recipeDescription.getText().toString());
        appDatabase.runInTransaction(() -> recipeDao.upsert(recipe));
        Toast.makeText(getActivity(), "Recipe has been saved",
                Toast.LENGTH_LONG).show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
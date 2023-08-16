package io.terence.recipesapp.ui.reflow;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import io.terence.recipesapp.R;
import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.IngredientDao;
import io.terence.recipesapp.daos.RecipeDao;
import io.terence.recipesapp.daos.StepDao;
import io.terence.recipesapp.databinding.FragmentNewRecipeBinding;
import io.terence.recipesapp.entities.Ingredient;
import io.terence.recipesapp.entities.Recipe;
import io.terence.recipesapp.entities.Step;

public class NewRecipeFragment extends Fragment {

    private FragmentNewRecipeBinding binding;

    private RecipeDao recipeDao;
    private StepDao stepDao;
    private IngredientDao ingredientDao;
    private AppDatabase appDatabase;
    private EditText recipeTitle;
    private EditText recipeDescription;
    private Button saveRecipe;
    private Button addIngredient;
    private Button addStep;
    NewRecipeViewModel newRecipeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newRecipeViewModel = new ViewModelProvider(requireActivity()).get(NewRecipeViewModel.class);
        //Initialize stuff
        appDatabase = AppDatabase.getInstance(requireActivity().getApplicationContext());
        recipeDao = appDatabase.recipeDao();
        ingredientDao = appDatabase.ingredientDao();
        stepDao = appDatabase.stepDao();

        binding = FragmentNewRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recipeTitle = root.findViewById(R.id.recipe_title);
        recipeDescription = root.findViewById(R.id.recipe_description);
        saveRecipe = root.findViewById(R.id.save_recipe_btn);
        saveRecipe.setOnClickListener(v -> saveRecipe());
        addIngredient = root.findViewById(R.id.add_ingredient_btn);
        addIngredient.setOnClickListener(this::addIngredient);
        addStep = root.findViewById(R.id.add_step_btn);
        addStep.setOnClickListener(this::addStep);

        assert getArguments() != null;
        int recipeId = NewRecipeFragmentArgs.fromBundle(getArguments()).getRecipeId();

        newRecipeViewModel.setRecipeId(recipeId);

        //final TextView textView = binding.textNewRecipe;
        //newRecipeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        newRecipeViewModel.getRecipeName().observe(getViewLifecycleOwner(), recipeTitle::setText);
        newRecipeViewModel.getRecipeDescription().observe(getViewLifecycleOwner(), recipeDescription::setText);

        //if(recipeId != -1){
        //    recipe = recipeDao.loadSingle(recipeId);
        //    recipeTitle.setText(recipe.getTitle());
        //    recipeDescription.setText(recipe.getDescription());

        return root;
    }

    public void addIngredient(View view) {
        Context context = view.getContext();
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
        final EditText ingredientName = new EditText(context);
        ingredientName.setHint("Ingredient Name");
        layout.addView(ingredientName); // Notice this is an add method

// Add another TextView here for the "Description" label
        final EditText ingredientQuantity = new EditText(context);
        ingredientQuantity.setHint("Ingredient Quantity");
        layout.addView(ingredientQuantity); // Another add method

// Add another TextView here for the "Description" label
        final EditText ingredientUnit = new EditText(context);
        ingredientUnit.setHint("Ingredient Unit");
        layout.addView(ingredientUnit); // Another add method


        dialog.setView(layout); // Again this is a set method, not add
        dialog.setPositiveButton("Save Ingredient",
                (dialog1, which) -> {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIngredientName(ingredientName.getText().toString());
                    ingredient.setQuantity(ingredientQuantity.getText().toString());
                    ingredient.setUnit(ingredientUnit.getText().toString());
                    ingredient.setRecipeId(newRecipeViewModel.getRecipeId());
                    ingredientDao.upsert(ingredient);
                    //newRecipeViewModel.addIngredient(ingredientDto);
                    //newRecipeViewModel.getmIngredients().getValue().get(0).setIngredientQuantity("999");
                    dialog1.dismiss();
                });
        dialog.show();
    }

    public void addStep(View view) {
        Context context = view.getContext();
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
        final EditText stepDescription = new EditText(context);
        stepDescription.setHint("Step Description");
        layout.addView(stepDescription); // Notice this is an add method


        dialog.setView(layout); // Again this is a set method, not add
        dialog.setPositiveButton("Save Ingredient",
                (dialog1, which) -> {
                    Step step = new Step();
                    step.setDescription(stepDescription.getText().toString());
                    step.setOrder(stepDao.getStepsCount(newRecipeViewModel.getRecipeId()) + 1);
                    step.setRecipeId(newRecipeViewModel.getRecipeId());
                    stepDao.upsert(step);
                    dialog1.dismiss();
                });
        dialog.show();
    }
    public void saveRecipe(){
        //validateRecipe();
        //if(hasErrors()){
        //    return;
        //}
        Recipe recipe = newRecipeViewModel.getRecipe();
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
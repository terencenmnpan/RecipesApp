package io.terence.recipesapp.ui.reflow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

        newRecipeViewModel.getRecipeName().observe(getViewLifecycleOwner(), recipeTitle::setText);
        newRecipeViewModel.getRecipeDescription().observe(getViewLifecycleOwner(), recipeDescription::setText);


        return root;
    }

    public void addIngredient(View view) {
        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText ingredientName = new EditText(context);
        ingredientName.setHint("Ingredient Name");
        layout.addView(ingredientName); // Notice this is an add method

        final EditText ingredientQuantity = new EditText(context);
        ingredientQuantity.setHint("Ingredient Quantity");
        layout.addView(ingredientQuantity); // Another add method

        final EditText ingredientUnit = new EditText(context);
        ingredientUnit.setHint("Ingredient Unit");
        layout.addView(ingredientUnit); // Another add method


        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(layout)
                .setPositiveButton("Save Ingredient", null)
                .setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss()).create();
        dialog.setOnShowListener(dialogInterface -> {
            Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view1 -> {
                if(ingredientName.getText().length() == 0){
                    ingredientName.setError("Ingredient Name Must Have a Value");
                }
                if(ingredientQuantity.getText().length() == 0){
                    ingredientQuantity.setError("Ingredient Quantity Must Have a Value");
                }
                if(ingredientUnit.getText().length() == 0){
                    ingredientUnit.setError("Ingredient Unit Must Have a Value");
                }
                if (ingredientName.getError() == null &&
                        ingredientQuantity.getError() == null &&
                        ingredientUnit.getError() == null) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIngredientName(ingredientName.getText().toString());
                    ingredient.setQuantity(ingredientQuantity.getText().toString());
                    ingredient.setUnit(ingredientUnit.getText().toString());
                    ingredient.setRecipeId(newRecipeViewModel.getRecipeId());
                    ingredientDao.upsert(ingredient);
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }

    public void addStep(View view) {
        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText stepDescription = new EditText(context);
        stepDescription.setHint("Step Description");
        layout.addView(stepDescription); // Notice this is an add method

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(layout)
                .setPositiveButton("Save Ingredient", null)
                .setNegativeButton("Cancel", (dialog1, which) -> {dialog1.dismiss();})
                .create();
        dialog.setOnShowListener(dialogInterface -> {
            Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view1 -> {
                if(stepDescription.getText().length() == 0){
                    stepDescription.setError("Step Description Must Have a Value");
                }
                if(stepDescription.getError() == null){
                    Step step = new Step();
                    step.setDescription(stepDescription.getText().toString());
                    step.setOrder(stepDao.getStepsCount(newRecipeViewModel.getRecipeId()) + 1);
                    step.setRecipeId(newRecipeViewModel.getRecipeId());
                    stepDao.upsert(step);
                    dialog.dismiss();
                }
            });
        });
        dialog.show();
    }

    public void saveRecipe(){
        validateRecipe();
        if(hasRecipeErrors()){
            return;
        }
        Recipe recipe = newRecipeViewModel.getRecipe();
        recipe.setTitle(recipeTitle.getText().toString());
        recipe.setDescription(recipeDescription.getText().toString());
        appDatabase.runInTransaction(() -> recipeDao.upsert(recipe));
        Toast.makeText(getActivity(), "Recipe has been saved",
                Toast.LENGTH_LONG).show();
    }

    private boolean hasRecipeErrors() {
        return recipeTitle.getError() != null || recipeDescription.getError() != null;
    }

    private void validateRecipe() {
        if(recipeTitle.getText().toString().length() == 0){
            recipeTitle.setError("Recipe Name Must Have a Value");
        }
        if(recipeDescription.getText().toString().length() == 0){
            recipeDescription.setError("Recipe Description Must Have a Value");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
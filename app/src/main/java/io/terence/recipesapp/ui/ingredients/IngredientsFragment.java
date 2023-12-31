package io.terence.recipesapp.ui.ingredients;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.time.LocalDateTime;

import io.terence.recipesapp.R;
import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.IngredientDao;
import io.terence.recipesapp.entities.Ingredient;
import io.terence.recipesapp.ui.edit.NewRecipeViewModel;

/**
 * A fragment representing a list of Items.
 */
public class IngredientsFragment extends Fragment {

    ListAdapter<Ingredient, IngredientsRecyclerViewAdapter.ViewHolder> ingredientDtoListAdapter;
    private AppDatabase appDatabase;
    private IngredientDao ingredientDao;
    NewRecipeViewModel newRecipeViewModel;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static IngredientsFragment newInstance(int columnCount) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newRecipeViewModel = new ViewModelProvider(requireActivity()).get(NewRecipeViewModel.class);
        appDatabase = AppDatabase.getInstance(requireActivity().getApplicationContext());
        ingredientDao = appDatabase.ingredientDao();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredient_item_list, container, false);

        NewRecipeViewModel newRecipeViewModel =
                new ViewModelProvider(requireActivity()).get(NewRecipeViewModel.class);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            ingredientDtoListAdapter =
                    new IngredientsRecyclerViewAdapter(this::deleteIngredient,
                            this::editIngredient);

            newRecipeViewModel.getRecipeWithIngredientsAndSteps().observe(getViewLifecycleOwner(), recipe -> ingredientDtoListAdapter.submitList(recipe.ingredients));
            recyclerView.setAdapter(ingredientDtoListAdapter);


        }

        //newRecipeViewModel.getmIngredients().observe(getViewLifecycleOwner(), recyclerView);
        return view;
    }
    private void deleteIngredient(Ingredient ingredient){
        ingredientDao.delete(ingredient);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void editIngredient(Ingredient ingredient){
        Context context = requireActivity();
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
                    ingredient.setIngredientName(ingredientName.getText().toString());
                    ingredient.setQuantity(ingredientQuantity.getText().toString());
                    ingredient.setUnit(ingredientUnit.getText().toString());
                    ingredient.setRecipeId(newRecipeViewModel.getRecipeId());
                    ingredient.setModifiedDate(LocalDateTime.now());
                    ingredientDao.upsert(ingredient);
                    dialog.dismiss();

                    ingredientDtoListAdapter.notifyDataSetChanged();
                }
            });
        });

        dialog.show();
    }
}
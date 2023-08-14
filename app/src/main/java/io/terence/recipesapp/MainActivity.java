package io.terence.recipesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.RecipeDao;
import io.terence.recipesapp.databinding.ActivityMainBinding;
import io.terence.recipesapp.entities.Recipe;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private RecipeDao recipeDao;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize stuff
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        recipeDao = appDatabase.recipeDao();

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        if (binding.appBarMain.fab != null) {
            binding.appBarMain.fab.setOnClickListener(this::addRecipe);
        }
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        NavigationView navigationView = binding.navView;
        if (navigationView != null) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_recipes, R.id.nav_new_recipe, R.id.nav_shopping_list, R.id.nav_settings)
                    .setOpenableLayout(binding.drawerLayout)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }

        BottomNavigationView bottomNavigationView = binding.appBarMain.contentMain.bottomNavView;
        if (bottomNavigationView != null) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_recipes, R.id.nav_new_recipe, R.id.nav_shopping_list)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        // Using findViewById because NavigationView exists in different layout files
        // between w600dp and w1240dp
        NavigationView navView = findViewById(R.id.nav_view);
        if (navView == null) {
            // The navigation drawer already has the items including the items in the overflow menu
            // We only inflate the overflow menu if the navigation drawer isn't visible
            getMenuInflater().inflate(R.menu.overflow, menu);
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_settings);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void addRecipe(View view) {
        Context context = view.getContext();
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
        final EditText recipeName = new EditText(context);
        recipeName.setHint("Recipe Name");
        layout.addView(recipeName); // Notice this is an add method

// Add another TextView here for the "Description" label
        final EditText recipeTitle = new EditText(context);
        recipeTitle.setHint("Recipe Title");
        layout.addView(recipeTitle); // Another add method


        dialog.setView(layout); // Again this is a set method, not add
        dialog.setPositiveButton("Save Recipe",
                (dialog1, which) -> {
                    Recipe recipe = new Recipe();
                    recipe.setTitle(recipeName.getText().toString());
                    recipe.setDescription(recipeTitle.getText().toString());
                    recipeDao.upsert(recipe);
                    //newRecipeViewModel.addIngredient(ingredientDto);
                    //newRecipeViewModel.getmIngredients().getValue().get(0).setIngredientQuantity("999");
                    dialog1.dismiss();
                });
        dialog.show();
    }
}
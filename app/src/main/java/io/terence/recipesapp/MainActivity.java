package io.terence.recipesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;

import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.RecipeDao;
import io.terence.recipesapp.databinding.ActivityMainBinding;
import io.terence.recipesapp.entities.Recipe;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private RecipeDao recipeDao;
    private AppDatabase appDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
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


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        } else{
            redirectToLogin();
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
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }
        if (item.getItemId() == R.id.sync_to_cloud) {
            //TODO save data
            String userEmail = mAuth.getCurrentUser().getEmail();
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
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
        final EditText recipeName = new EditText(context);
        recipeName.setHint("Recipe Name");
        layout.addView(recipeName); // Notice this is an add method

// Add another TextView here for the "Description" label
        final EditText recipeDescription = new EditText(context);
        recipeDescription.setHint("Recipe Description");
        layout.addView(recipeDescription); // Another add method


        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(layout) // Again this is a set method, not add
                .setPositiveButton("Save Recipe", null)
                .setNegativeButton("Cancel", (dialog1, which) -> {dialog1.dismiss();}).create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view1 -> {
                if(recipeName.getText().toString().length() == 0){
                    recipeName.setError("Recipe Name Must Have a Value");
                }
                if(recipeDescription.getText().toString().length() == 0){
                    recipeDescription.setError("Recipe Description Must Have a Value");
                }
                if(recipeName.getError() == null && recipeDescription.getError() == null){
                    Recipe recipe = new Recipe();
                    recipe.setTitle(recipeName.getText().toString());
                    recipe.setDescription(recipeDescription.getText().toString());
                    recipe.setCreateDate(LocalDateTime.now());
                    recipeDao.upsert(recipe);
                    dialog.dismiss();
                }
            });
        });
        dialog.show();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    private void redirectToLogin() {
        Intent loginIntent =  new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        if(mAuth.getCurrentUser() != null){
            return;
        }
    }

    private void reload() { }

}
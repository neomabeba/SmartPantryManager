package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private FrameLayout contentFrame;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        contentFrame = findViewById(R.id.mainContentFrame);
        bottomNav = findViewById(R.id.bottomNavigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                showHomeTab();
                return true;
            } else if (itemId == R.id.nav_pantry) {
                startActivity(new Intent(this, PantryActivity.class));
                return false; // keep home selected
            } else if (itemId == R.id.nav_recipes) {
                startActivity(new Intent(this, SuggestedRecipesActivity.class));
                return false;
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return false;
            }
            return false;
        });

        showHomeTab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNav.getSelectedItemId() == R.id.nav_home) {
            showHomeTab();
        }
    }

    private void showHomeTab() {
        contentFrame.removeAllViews();
        View homeView = LayoutInflater.from(this).inflate(R.layout.layout_tab_home, contentFrame, false);
        contentFrame.addView(homeView);

        // Stats
        int pantryCount = db.getPantryCount();
        int expiringCount = db.getExpiringCount();
        int cookableCount = db.getCookableRecipeCount();

        ((TextView) homeView.findViewById(R.id.statPantryCount)).setText(String.valueOf(pantryCount));
        ((TextView) homeView.findViewById(R.id.statExpiringCount)).setText(String.valueOf(expiringCount));
        ((TextView) homeView.findViewById(R.id.statRecipesCount)).setText(String.valueOf(cookableCount));

        // Quick Action Buttons
        homeView.findViewById(R.id.btnQuickAddPantry).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditIngredientActivity.class)));

        homeView.findViewById(R.id.btnQuickViewRecipes).setOnClickListener(v ->
                startActivity(new Intent(this, SuggestedRecipesActivity.class)));

        homeView.findViewById(R.id.cardExpiringStat).setOnClickListener(v ->
                startActivity(new Intent(this, PantryActivity.class)));

        homeView.findViewById(R.id.cardRecipesStat).setOnClickListener(v ->
                startActivity(new Intent(this, SuggestedRecipesActivity.class)));

        homeView.findViewById(R.id.btnSeeAllExpiring).setOnClickListener(v ->
                startActivity(new Intent(this, PantryActivity.class)));

        homeView.findViewById(R.id.btnSeeAllRecipes).setOnClickListener(v ->
                startActivity(new Intent(this, SuggestedRecipesActivity.class)));

        // Expiring items list
        List<Ingredient> expiringList = db.getExpiringIngredients();
        RecyclerView rvExpiring = homeView.findViewById(R.id.rvHomeExpiring);
        TextView tvNoExpiring = homeView.findViewById(R.id.tvNoExpiringAlerts);

        if (expiringList.isEmpty()) {
            rvExpiring.setVisibility(View.GONE);
            tvNoExpiring.setVisibility(View.VISIBLE);
        } else {
            rvExpiring.setVisibility(View.VISIBLE);
            tvNoExpiring.setVisibility(View.GONE);
            rvExpiring.setLayoutManager(new LinearLayoutManager(this));
            PantryAdapter adapter = new PantryAdapter(expiringList, new PantryAdapter.Listener() {
                @Override
                public void edit(Ingredient i) {
                    Intent x = new Intent(MainActivity.this, AddEditIngredientActivity.class);
                    x.putExtra("id", i.id);
                    startActivity(x);
                }

                @Override
                public void delete(Ingredient i) {
                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle("Delete Ingredient")
                            .setMessage("Are you sure you want to delete " + i.name + "?")
                            .setPositiveButton("Delete", (d, w) -> {
                                db.deleteIngredient(i.id);
                                showHomeTab();
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            });
            rvExpiring.setAdapter(adapter);
        }

        // Cookable recipes list
        List<Recipe> cookableRecipes = db.getStrictMatches();
        RecyclerView rvRecipes = homeView.findViewById(R.id.rvHomeRecipes);
        TextView tvNoRecipes = homeView.findViewById(R.id.tvNoHomeRecipes);

        if (cookableRecipes.isEmpty()) {
            rvRecipes.setVisibility(View.GONE);
            tvNoRecipes.setVisibility(View.VISIBLE);
        } else {
            rvRecipes.setVisibility(View.VISIBLE);
            tvNoRecipes.setVisibility(View.GONE);
            rvRecipes.setLayoutManager(new LinearLayoutManager(this));
            RecipeAdapter recipeAdapter = new RecipeAdapter(cookableRecipes);
            rvRecipes.setAdapter(recipeAdapter);
        }
    }
}

package com.example.smartpantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.Locale;

public class RecipeDetailActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private long recipeId;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_recipe_detail);

        db = new DatabaseHelper(this);
        recipeId = getIntent().getLongExtra("recipeId", -1);

        loadRecipeData();
    }

    private void loadRecipeData() {
        recipe = db.getRecipe(recipeId);
        if (recipe == null) {
            finish();
            return;
        }

        ((TextView) findViewById(R.id.name)).setText(recipe.name);

        TextView tvBadge = findViewById(R.id.tvDetailMatchBadge);
        MaterialButton btnCook = findViewById(R.id.btnCookRecipe);

        if (recipe.isCookable) {
            tvBadge.setText("Ready to cook");
            tvBadge.setBackgroundResource(R.drawable.bg_badge_good);
            tvBadge.setTextColor(ContextCompat.getColor(this, R.color.fresh_text));

            btnCook.setEnabled(true);
            btnCook.setText("Cook Recipe & Deduct Pantry");
            btnCook.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.primary));
        } else {
            tvBadge.setText("Missing " + recipe.missingCount + (recipe.missingCount == 1 ? " ingredient" : " ingredients"));
            tvBadge.setBackgroundResource(R.drawable.bg_badge_warning);
            tvBadge.setTextColor(ContextCompat.getColor(this, R.color.warning_text));

            btnCook.setEnabled(false);
            btnCook.setText("Missing Ingredients");
            btnCook.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.muted));
        }

        // Render ingredients list
        LinearLayout container = findViewById(R.id.llIngredientsList);
        container.removeAllViews();

        for (RecipeIngredient ri : recipe.ingredients) {
            TextView tvRow = new TextView(this);
            tvRow.setPadding(0, 8, 0, 8);
            tvRow.setTextSize(15);

            String formattedQty = (ri.quantity % 1 == 0) ? String.format(Locale.US, "%.0f", ri.quantity) : String.valueOf(ri.quantity);

            if (ri.isAvailable) {
                tvRow.setText("✅  " + formattedQty + " " + ri.unit + " " + ri.name);
                tvRow.setTextColor(ContextCompat.getColor(this, R.color.text_dark));
            } else {
                String availText = (ri.availableQuantity > 0) ? " (have " + ri.availableQuantity + " " + ri.unit + ")" : " (missing)";
                tvRow.setText("❌  " + formattedQty + " " + ri.unit + " " + ri.name + availText);
                tvRow.setTextColor(ContextCompat.getColor(this, R.color.red));
            }

            container.addView(tvRow);
        }

        // Render steps
        TextView tvSteps = findViewById(R.id.steps);
        tvSteps.setText(recipe.steps);

        // Cook button listener
        btnCook.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Cook Recipe")
                    .setMessage("Would you like to cook " + recipe.name + "? This will deduct the required ingredient amounts from your pantry.")
                    .setPositiveButton("Cook & Deduct", (dialog, which) -> {
                        boolean success = db.cookRecipe(recipeId);
                        if (success) {
                            Toast.makeText(this, "🎉 Pantry updated! Enjoy cooking " + recipe.name + "!", Toast.LENGTH_LONG).show();
                            loadRecipeData();
                        } else {
                            Toast.makeText(this, "Could not deduct ingredients.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}

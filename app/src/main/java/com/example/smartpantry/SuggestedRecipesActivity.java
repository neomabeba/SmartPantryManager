package com.example.smartpantry;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecipeAdapter adapter;
    private View emptyView;
    private EditText etSearch;
    private TextView chipAll, chipReady, chipMissing;
    private String currentFilterType = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested);

        db = new DatabaseHelper(this);

        RecyclerView rv = findViewById(R.id.recyclerRecipes);
        emptyView = findViewById(R.id.layoutRecipeEmpty);
        etSearch = findViewById(R.id.etSearchRecipes);

        chipAll = findViewById(R.id.chipAllRecipes);
        chipReady = findViewById(R.id.chipReadyToCook);
        chipMissing = findViewById(R.id.chipMissingItems);

        rv.setLayoutManager(new LinearLayoutManager(this));

        List<Recipe> evaluated = db.getEvaluatedRecipes();
        adapter = new RecipeAdapter(evaluated);
        rv.setAdapter(adapter);

        setupSearchAndFilters();
        applyFilter();
    }

    private void setupSearchAndFilters() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        chipAll.setOnClickListener(v -> selectFilter("ALL"));
        chipReady.setOnClickListener(v -> selectFilter("COOKABLE"));
        chipMissing.setOnClickListener(v -> selectFilter("PARTIAL"));
    }

    private void selectFilter(String filterType) {
        currentFilterType = filterType;
        updateChipStyles();
        applyFilter();
    }

    private void updateChipStyles() {
        setChipStyle(chipAll, "ALL".equals(currentFilterType));
        setChipStyle(chipReady, "COOKABLE".equals(currentFilterType));
        setChipStyle(chipMissing, "PARTIAL".equals(currentFilterType));
    }

    private void setChipStyle(TextView chip, boolean isSelected) {
        if (isSelected) {
            chip.setBackgroundResource(R.drawable.bg_chip_selected);
            chip.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            chip.setBackgroundResource(R.drawable.bg_chip_unselected);
            chip.setTextColor(ContextCompat.getColor(this, R.color.text_dark));
        }
    }

    private void applyFilter() {
        String query = etSearch.getText().toString();
        adapter.filter(query, currentFilterType);
        emptyView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void load() {
        List<Recipe> evaluated = db.getEvaluatedRecipes();
        adapter.setItems(evaluated);
        applyFilter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            load();
        }
    }
}

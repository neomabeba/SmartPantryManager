package com.example.smartpantry;

import android.content.Intent;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private PantryAdapter adapter;
    private View emptyView;
    private EditText etSearch;
    private TextView chipAll, chipExpiring, chipExpired, chipFresh;
    private String currentStatusFilter = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        db = new DatabaseHelper(this);

        RecyclerView rv = findViewById(R.id.recyclerPantry);
        emptyView = findViewById(R.id.layoutPantryEmpty);
        etSearch = findViewById(R.id.etSearchPantry);

        chipAll = findViewById(R.id.chipAll);
        chipExpiring = findViewById(R.id.chipExpiring);
        chipExpired = findViewById(R.id.chipExpired);
        chipFresh = findViewById(R.id.chipFresh);

        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PantryAdapter(db.getIngredients(), new PantryAdapter.Listener() {
            @Override
            public void edit(Ingredient i) {
                Intent x = new Intent(PantryActivity.this, AddEditIngredientActivity.class);
                x.putExtra("id", i.id);
                startActivity(x);
            }

            @Override
            public void delete(Ingredient i) {
                new MaterialAlertDialogBuilder(PantryActivity.this)
                        .setTitle("Delete Ingredient")
                        .setMessage("Are you sure you want to delete " + i.name + " from your pantry?")
                        .setPositiveButton("Delete", (d, w) -> {
                            db.deleteIngredient(i.id);
                            load();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        rv.setAdapter(adapter);

        findViewById(R.id.btnAdd).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditIngredientActivity.class)));

        setupSearchAndFilters();
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
        chipExpiring.setOnClickListener(v -> selectFilter("EXPIRING_SOON"));
        chipExpired.setOnClickListener(v -> selectFilter("EXPIRED"));
        chipFresh.setOnClickListener(v -> selectFilter("FRESH"));
    }

    private void selectFilter(String status) {
        currentStatusFilter = status;
        updateChipStyles();
        applyFilter();
    }

    private void updateChipStyles() {
        setChipStyle(chipAll, "ALL".equals(currentStatusFilter));
        setChipStyle(chipExpiring, "EXPIRING_SOON".equals(currentStatusFilter));
        setChipStyle(chipExpired, "EXPIRED".equals(currentStatusFilter));
        setChipStyle(chipFresh, "FRESH".equals(currentStatusFilter));
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
        adapter.filter(query, currentStatusFilter);
        emptyView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void load() {
        List<Ingredient> ingredients = db.getIngredients();
        adapter.setItems(ingredients);
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

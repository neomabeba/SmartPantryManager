package com.example.smartpantry;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private long id = -1;
    private EditText name, qty, unit, expiry;
    private TextInputLayout tilName, tilQty, tilUnit, tilExpiry;
    private TextView title;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_add_edit);

        db = new DatabaseHelper(this);

        title = findViewById(R.id.title);
        name = findViewById(R.id.name);
        qty = findViewById(R.id.quantity);
        unit = findViewById(R.id.unit);
        expiry = findViewById(R.id.expiry);

        tilName = findViewById(R.id.tilName);
        tilQty = findViewById(R.id.tilQuantity);
        tilUnit = findViewById(R.id.tilUnit);
        tilExpiry = findViewById(R.id.tilExpiry);

        id = getIntent().getLongExtra("id", -1);

        if (id != -1) {
            title.setText("Edit Ingredient");
            Ingredient i = db.getIngredient(id);
            if (i != null) {
                name.setText(i.name);
                String formattedQty = (i.quantity % 1 == 0) ? String.format(Locale.US, "%.0f", i.quantity) : String.valueOf(i.quantity);
                qty.setText(formattedQty);
                unit.setText(i.unit);
                expiry.setText(i.expiry);
            }
        }

        // Date Picker logic
        expiry.setOnClickListener(v -> showDatePicker());
        tilExpiry.setEndIconOnClickListener(v -> showDatePicker());

        findViewById(R.id.save).setOnClickListener(v -> save());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        String current = expiry.getText().toString().trim();
        if (!current.isEmpty()) {
            Date d = DateUtils.parseDate(current);
            if (d != null) {
                cal.setTime(d);
            }
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String formattedDate = DateUtils.formatDate(selectedYear, selectedMonth, selectedDay);
            expiry.setText(formattedDate);
            tilExpiry.setError(null);
        }, year, month, day);

        dialog.show();
    }

    private void save() {
        tilName.setError(null);
        tilQty.setError(null);
        tilUnit.setError(null);
        tilExpiry.setError(null);

        String n = name.getText().toString().trim();
        String q = qty.getText().toString().trim();
        String u = unit.getText().toString().trim();
        String e = expiry.getText().toString().trim();

        boolean hasError = false;

        if (TextUtils.isEmpty(n)) {
            tilName.setError("Ingredient name is required");
            hasError = true;
        }

        if (TextUtils.isEmpty(q)) {
            tilQty.setError("Quantity is required");
            hasError = true;
        }

        if (TextUtils.isEmpty(u)) {
            tilUnit.setError("Unit is required");
            hasError = true;
        }

        double quantity = 0;
        if (!TextUtils.isEmpty(q)) {
            try {
                quantity = Double.parseDouble(q);
                if (quantity <= 0) throw new NumberFormatException();
            } catch (Exception ex) {
                tilQty.setError("Enter a valid quantity greater than zero");
                hasError = true;
            }
        }

        if (hasError) return;

        if (id == -1) {
            db.insertIngredient(n, quantity, u, e);
        } else {
            db.updateIngredient(id, n, quantity, u, e);
        }

        Toast.makeText(this, "Ingredient saved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}

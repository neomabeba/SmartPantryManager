package com.example.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_settings);

        SwitchCompat s = findViewById(R.id.expirySwitch);
        SharedPreferences p = getSharedPreferences("settings", MODE_PRIVATE);

        s.setChecked(p.getBoolean("expiryAlerts", true));
        s.setOnCheckedChangeListener((button, checked) -> p.edit().putBoolean("expiryAlerts", checked).apply());
    }
}

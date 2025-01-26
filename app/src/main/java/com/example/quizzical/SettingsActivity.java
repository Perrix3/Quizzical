package com.example.quizzical;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Apply last theme
        applySavedTheme();

        setContentView(R.layout.activity_settings);

        RadioGroup themeGroup = findViewById(R.id.themeGroup);
        RadioButton darkTheme = findViewById(R.id.darkTheme);
        RadioButton lightTheme = findViewById(R.id.lightTheme);
        RadioButton systemTheme = findViewById(R.id.systemTheme);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });

        SharedPreferences prefs = getSharedPreferences("theme_pref", MODE_PRIVATE);
        String currentTheme = prefs.getString("current_theme", "system");
        switch (currentTheme) {
            case "dark":
                darkTheme.setChecked(true);
                break;
            case "light":
                lightTheme.setChecked(true);
                break;
            case "system":
                systemTheme.setChecked(true);
                break;
        }

        //Radiogroup
        themeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences.Editor editor = prefs.edit();
            String selectedTheme = "";

            if (checkedId == R.id.darkTheme) {
                selectedTheme = "dark";
            } else if (checkedId == R.id.lightTheme) {
                selectedTheme = "light";
            } else if (checkedId == R.id.systemTheme) {
                selectedTheme = "system";
            }

            //Check if theme changes
            if (!currentTheme.equals(selectedTheme)) {
                editor.putString("current_theme", selectedTheme);
                editor.apply();
                applyThemeAndRestart(selectedTheme);
            }
        });
    }

    private void applySavedTheme() {
        SharedPreferences prefs = getSharedPreferences("theme_pref", MODE_PRIVATE);
        String theme = prefs.getString("current_theme", "system");

        int currentMode = AppCompatDelegate.getDefaultNightMode();
        int newMode;

        switch (theme) {
            case "dark":
                newMode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case "light":
                newMode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            default:
                newMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
        }

        //Apply theme if different
        if (currentMode != newMode) {
            AppCompatDelegate.setDefaultNightMode(newMode);
        }
    }

    private void applyThemeAndRestart(String selectedTheme) {
        switch (selectedTheme) {
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "system":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }

        //Smooth view restart, to fix flashing
        Intent intent = getIntent();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(intent);
    }
}

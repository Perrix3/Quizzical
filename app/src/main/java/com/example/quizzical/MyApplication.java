package com.example.quizzical;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

//Applies theme in the whole app

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        applySavedTheme();
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

        //Apply the new theme only if it's different from the current mode
        if (currentMode != newMode) {
            AppCompatDelegate.setDefaultNightMode(newMode);
        }
    }

}

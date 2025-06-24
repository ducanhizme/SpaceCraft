package com.example.spacecraft.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.spacecraft.utils.Constants;
import com.example.spacecraft.utils.Difficulty;

public class DifficultyManager {

    private SharedPreferences sharedPreferences;

    public DifficultyManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveDifficulty(Difficulty difficulty) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_DIFFICULTY, difficulty.name()); // Store enum name as string
        editor.apply();
    }

    public Difficulty loadDifficulty() {
        String difficultyName = sharedPreferences.getString(Constants.KEY_DIFFICULTY, null);
        return Difficulty.fromString(difficultyName); // Use the helper in enum to convert
    }
}

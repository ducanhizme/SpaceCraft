package com.example.spacecraft.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.spacecraft.R;

public class Constants {
    // Difficulty Levels
    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";
    private static final String PREFS_NAME = "GameSettings";
    private static final String KEY_DIFFICULTY = "selectedDifficulty";

    public static String currentDifficulty = DIFFICULTY_MEDIUM; // Default

    // Game Parameters - will be set by difficulty
    public static int currentPlayerHealth;
    public static int enemyNormalHealth;
    public static int enemyFastHealth;
    public static int enemyTankHealth;
    public static int enemyNormalSpeed; // Assuming a base speed value, actual implementation might differ
    public static int enemyFastSpeed;
    public static int enemyTankSpeed;
    public static int numberOfEnemies;
    public static int playerFiringRate; // Milliseconds

    // Existing constants
    public static String CURRENT_STATE;
    public static int[] BACKGROUND_GAME = {R.drawable.starscape00, R.drawable.starscape01, R.drawable.starscape02, R.drawable.starscape03};
    public static int PLAYER_SHIP = R.drawable.playership;
    public static int BULLET = R.drawable.laserblue;
    public static int ENEMY_SHIP_NORMAL = R.drawable.enemy_normal;
    public static int ENEMY_SHIP_FAST = R.drawable.enemy_fast;
    public static int ENEMY_SHIP_TANK = R.drawable.enemytank;
    public static int EXPLOSION = R.drawable.explosion;
    public static int HEALTH_ICON = R.drawable.player_life;
    public static int COL_EXPLOSION = 9;
    public static int EXPLOSION_FRAME_TIME = 40000;

    // Call this early in your app, e.g., MainActivity.onCreate()
    public static void loadDifficulty(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedDifficulty = prefs.getString(KEY_DIFFICULTY, DIFFICULTY_MEDIUM);
        setDifficulty(savedDifficulty, context, false); // Don't save again when just loading
    }

    public static void setDifficulty(String level, Context context, boolean save) {
        currentDifficulty = level;

        switch (level) {
            case DIFFICULTY_EASY:
                currentPlayerHealth = 5;
                enemyNormalHealth = 2;
                enemyFastHealth = 1;
                enemyTankHealth = 4;
                enemyNormalSpeed = 10; // Example values
                enemyFastSpeed = 20;   // Example values
                enemyTankSpeed = 5;    // Example values
                numberOfEnemies = 2;
                playerFiringRate = 300; // Faster firing
                break;
            case DIFFICULTY_HARD:
                currentPlayerHealth = 2;
                enemyNormalHealth = 4;
                enemyFastHealth = 3;
                enemyTankHealth = 7;
                enemyNormalSpeed = 20;
                enemyFastSpeed = 40;
                enemyTankSpeed = 15;
                numberOfEnemies = 4;
                playerFiringRate = 700; // Slower firing
                break;
            case DIFFICULTY_MEDIUM:
            default: // Default to Medium
                currentPlayerHealth = 3;
                enemyNormalHealth = 3;
                enemyFastHealth = 2;
                enemyTankHealth = 5;
                enemyNormalSpeed = 15;
                enemyFastSpeed = 30;
                enemyTankSpeed = 10;
                numberOfEnemies = 3;
                playerFiringRate = 500; // Original firing rate
                break;
        }

        if (save) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_DIFFICULTY, level);
            editor.apply();
        }
    }
}

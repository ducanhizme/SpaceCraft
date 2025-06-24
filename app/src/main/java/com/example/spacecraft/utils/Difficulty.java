package com.example.spacecraft.utils;

public enum Difficulty {
    EASY,
    MEDIUM,
    HARD;

    public static Difficulty getDefault() {
        return MEDIUM;
    }

    // Helper method to get Difficulty from a string (e.g., stored in SharedPreferences)
    public static Difficulty fromString(String difficultyString) {
        if (difficultyString == null) {
            return getDefault();
        }
        try {
            return Difficulty.valueOf(difficultyString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return getDefault(); // Default if string is invalid
        }
    }
}

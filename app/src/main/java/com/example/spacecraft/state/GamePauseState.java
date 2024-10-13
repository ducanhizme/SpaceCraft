package com.example.spacecraft.state;

import android.graphics.Canvas;

import com.example.spacecraft.base.GameState;
import com.example.spacecraft.utils.BackgroundManager;

public class GamePauseState implements GameState {
    public static final String TAG = "GamePauseState";
    private BackgroundManager backgroundManager;

    public GamePauseState(BackgroundManager backgroundManager) {
        this.backgroundManager = backgroundManager;
    }

    @Override
    public void update() {
        backgroundManager.update();
    }

    @Override
    public void draw(Canvas canvas) {

    }
}

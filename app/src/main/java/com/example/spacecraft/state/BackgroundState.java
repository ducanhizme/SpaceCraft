package com.example.spacecraft.state;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.spacecraft.utils.BackgroundManager;
import com.example.spacecraft.utils.Constants;

public class BackgroundState implements GameState {
    public static final String TAG = "BackgroundState";
    private BackgroundManager backgroundManager;

    public BackgroundState(BackgroundManager backgroundManager) {
        this.backgroundManager = backgroundManager;
        Constants.CURRENT_STATE = BackgroundState.TAG;
    }

    @Override
    public void update() {
        backgroundManager.update();
    }

    @Override
    public void draw(Canvas canvas) {
        backgroundManager.draw(canvas,new Paint());
    }
}
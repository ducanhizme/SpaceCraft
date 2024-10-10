package com.example.spacecraft.state;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.spacecraft.BackgroundManager;
import com.example.spacecraft.GameContext;

public class BackgroundState extends GameState {
    private BackgroundManager backgroundManager;

    public BackgroundState(GameContext context, BackgroundManager backgroundManager) {
        super(context);
        this.backgroundManager = backgroundManager;
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

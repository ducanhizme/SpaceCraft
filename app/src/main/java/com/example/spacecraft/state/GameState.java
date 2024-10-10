package com.example.spacecraft.state;

import android.graphics.Canvas;

import com.example.spacecraft.BackgroundManager;
import com.example.spacecraft.GameContext;
import com.example.spacecraft.model.Background;

public abstract class GameState {
    public GameContext context;

    public GameState(GameContext context) {
        this.context = context;
    }

    public abstract void update();
    public abstract void draw(Canvas canvas);

    public void setState(GameState newState) {
        context.setState(newState);
    }
}

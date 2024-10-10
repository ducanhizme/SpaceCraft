package com.example.spacecraft;

import android.graphics.Canvas;

import com.example.spacecraft.state.GameState;

public class GameContext {
    private GameState currentState;

    public GameContext(GameState gameState) {
        this.currentState = gameState;
    }

    public void setState(GameState newState) {
        this.currentState = newState;
    }

    public void update() {
        currentState.update();
    }

    public void draw(Canvas canvas) {
        currentState.draw(canvas);
    }

}

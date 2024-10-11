package com.example.spacecraft.state;

import android.graphics.Canvas;

public interface GameState {
    public abstract void update();
    public abstract void draw(Canvas canvas);
}

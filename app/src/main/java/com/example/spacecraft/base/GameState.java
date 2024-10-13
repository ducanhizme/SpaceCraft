package com.example.spacecraft.base;

import android.graphics.Canvas;

public interface GameState {
    public abstract void update();
    public abstract void draw(Canvas canvas);
}

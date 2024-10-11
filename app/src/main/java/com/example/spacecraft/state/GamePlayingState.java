package com.example.spacecraft.state;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.spacecraft.utils.BackgroundManager;
import com.example.spacecraft.models.PlayerShip;
import com.example.spacecraft.utils.Constants;

public class GamePlayingState implements GameState {
    public static final String TAG = "GamePlayingState";
    private BackgroundManager backgroundManager;
    private PlayerShip playerShip;

    public GamePlayingState(BackgroundManager backgroundManager, PlayerShip playerShip) {
        this.backgroundManager = backgroundManager;
        this.playerShip = playerShip;
        Constants.CURRENT_STATE = GamePlayingState.TAG;
    }

    @Override
    public void update() {
        backgroundManager.update();
        playerShip.update();
    }

    @Override
    public void draw(Canvas canvas) {
        backgroundManager.draw(canvas,new Paint());
        playerShip.draw(canvas,new Paint());
    }
}

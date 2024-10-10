package com.example.spacecraft;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceView;

import com.example.spacecraft.model.Background;
import com.example.spacecraft.state.BackgroundState;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private GameContext gameContext;

    public GameView(Context context, int x, int y) {
        super(context);
        int[] backgroundImages = {R.drawable.starscape00, R.drawable.starscape01, R.drawable.starscape02, R.drawable.starscape03};
        BackgroundManager backgroundManager = new BackgroundManager(x, y, 1080f / y, backgroundImages, getResources());
        gameContext = new GameContext(new BackgroundState(gameContext, backgroundManager));// Default State
    }

    @Override
    public void run() {
        while (isPlaying) {
            gameContext.update();
            draw();
            sleep();
        }
    }


    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            gameContext.draw(canvas);  // Delegate drawing to the current state
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(17);  // 60 FPS
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

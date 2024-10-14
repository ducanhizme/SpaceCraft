package com.example.spacecraft.components;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;
import com.example.spacecraft.base.GameContext;
import com.example.spacecraft.state.BackgroundState;


@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private GameContext gameContext;

    public GameView(Context context) {
        super(context);
        gameContext = new GameContext(new BackgroundState(context));
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
            gameContext.draw(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            Log.d("GameView", "sleep: " + e.getMessage());
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
            Log.d("GameView", "pause: " + e.getMessage());
        }
    }

    public GameContext getGameContext() {
        return gameContext;
    }


    public void setGameContext(GameContext gameContext) {
        this.gameContext = gameContext;
    }


}

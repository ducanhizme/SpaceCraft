package com.example.spacecraft.components;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceView;

import com.example.spacecraft.R;
import com.example.spacecraft.base.GameContext;
import com.example.spacecraft.models.game.Explosion;
import com.example.spacecraft.state.BackgroundState;
import com.example.spacecraft.state.GamePlayingState;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private GameContext gameContext;
    private Explosion explosion;

    public GameView(Context context) {
        super(context);
        gameContext = new GameContext(new BackgroundState(context));
        explosion = new Explosion(context.getResources(), R.drawable.explosion, new Point(500,500), 128, 4);
    }


    @Override
    public void run() {
        while (isPlaying) {
            gameContext.update();
            explosion.update();
            draw();
            sleep();
        }
    }


    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            gameContext.draw(canvas);
            explosion.draw(canvas, new Paint());
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

    public GameContext getGameContext() {
        return gameContext;
    }

    public void setGameContext(GameContext gameContext) {
        this.gameContext = gameContext;
    }


}

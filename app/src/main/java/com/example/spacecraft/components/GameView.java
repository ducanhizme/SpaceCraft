package com.example.spacecraft.components;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.SurfaceView;

import com.example.spacecraft.models.Coordinate;
import com.example.spacecraft.models.PlayerShip;
import com.example.spacecraft.state.GameContext;
import com.example.spacecraft.R;
import com.example.spacecraft.state.GamePauseState;
import com.example.spacecraft.utils.BackgroundManager;
import com.example.spacecraft.state.BackgroundState;
import com.example.spacecraft.state.GamePlayingState;
import com.example.spacecraft.utils.Constants;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private GameContext gameContext;
    BackgroundManager backgroundManager;
    private final float screenWidth;
    private final float screenHeight;
    private final Context context;

    public GameView(Context context, int x, int y) {
        super(context);
        this.context= context;
        int[] backgroundImages = {R.drawable.starscape00, R.drawable.starscape01, R.drawable.starscape02, R.drawable.starscape03};
        this.screenHeight = y;
        this.screenWidth =x;
        backgroundManager = new BackgroundManager(this.screenWidth, this.screenHeight, 1080f / y, backgroundImages, getResources());
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        PlayerShip playerShip = new PlayerShip(screenWidth,screenHeight,sensorManager, gyroscope, getResources(), R.drawable.ship_03, 1920f / screenWidth, 1080f / screenHeight);
        playerShip.coordinate = new Coordinate(this.screenWidth/2- (float) playerShip.WIDTH /2, this.screenHeight-playerShip.HEIGHT *2);
        gameContext = new GameContext(new GamePlayingState(backgroundManager,playerShip));
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

    public BackgroundManager getBackgroundManager() {
        return backgroundManager;
    }

    public void setBackgroundManager(BackgroundManager backgroundManager) {
        this.backgroundManager = backgroundManager;
    }


}

package com.example.spacecraft;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceView;

import com.example.spacecraft.model.Background;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private int x, y;
    private float screenRatioX, screenRatioY;
    private Paint paint;
    private final float[] backgroundOffset = {0, 0, 0, 0};
    private  float backgroundMaxScrollingSpeed;
    private Background[] backgrounds;

    public GameView(Context context, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
        this.screenRatioX = 1920f / x;
        this.screenRatioY = 1080f / y;
        paint = new Paint();
        initBackground(x, y, screenRatioY);
    }

    private void initBackground(int x, int y, float screenRatioY){
        int DEFAULT_BACKGROUND_SPEED = 100;
        backgroundMaxScrollingSpeed = DEFAULT_BACKGROUND_SPEED * screenRatioY;
        backgrounds = new Background[4];
        int[] backgroundImages = {R.drawable.starscape00, R.drawable.starscape01, R.drawable.starscape02, R.drawable.starscape03};
        for (int i=0; i < backgroundImages.length; i++) {
            backgrounds[i] = new Background(x, y, getResources(), backgroundImages[i]);
        }
        paint = new Paint();
    }

    @Override
    public void run() {
        while (isPlaying) {
            Log.d("GameView", "Game loop running");
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        for (int i = 0; i < backgroundOffset.length; i++) {
            backgroundOffset[i] += backgroundMaxScrollingSpeed / (8 - 2 * i);
        }

        for (int i = 0; i < backgrounds.length; i++) {
            if (backgroundOffset[i] > y) {
                backgroundOffset[i] = 0;
            }
        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            for (int i = 0; i < backgrounds.length; i++) {
                canvas.drawBitmap(backgrounds[i].background, 0, backgroundOffset[i], paint);
                canvas.drawBitmap(backgrounds[i].background, 0, backgroundOffset[i] - y, paint);
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

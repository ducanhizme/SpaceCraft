package com.example.spacecraft.models;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GameObject {
    public float screenWidth;
    public float screenHeight;
    public int WIDTH;
    public int HEIGHT;
    public Coordinate coordinate;
    public Bitmap background;
    public float screenRatioX;
    public float screenRatioY;
    public Resources res;

    public GameObject(float screenWidth, float screenHeight,Resources res, int drawable, float screenRatioX, float screenRatioY) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenRatioX = screenRatioX;
        this.screenRatioY = screenRatioY;
        this.res = res;
        this.background = BitmapFactory.decodeResource(res, drawable);
        this.WIDTH = (int) (background.getWidth()/1.2);
        this.HEIGHT = (int) (background.getHeight()/1.2);
        this.background = Bitmap.createScaledBitmap(this.background, (int) (WIDTH * this.screenRatioX), (int) (HEIGHT * this.screenRatioX), false);
    }

    public abstract void draw(Canvas canvas, Paint paint);

    public abstract void update();
}

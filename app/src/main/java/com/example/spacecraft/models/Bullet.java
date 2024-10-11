package com.example.spacecraft.models;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bullet extends GameObject{


    public Bullet(float screenWidth, float screenHeight, Resources res, int drawable, float screenRatioX, float screenRatioY) {
        super(screenWidth, screenHeight, res, drawable, screenRatioX, screenRatioY);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(background, coordinate.x, coordinate.y, paint);
    }

    @Override
    public void update() {
        coordinate.y -= 200 * screenRatioY;
    }
}

package com.example.spacecraft.models.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Explosion {
    private final Bitmap spriteSheet;
    private final int frameWidth;
    private final int frameHeight;
    private final int frameCount;
    private int currentFrame;
    private int frameTime;
    private int frameTimer;
    private Point point;
    private boolean isFinished;

    public Explosion(Resources res, int drawable, Point point, int frameHeight, int frameTime) {
        this.spriteSheet = BitmapFactory.decodeResource(res, drawable); // Load sprite from resources
        this.point = point;
        this.frameHeight = frameHeight;
        this.frameWidth = spriteSheet.getWidth() / 8;
        this.frameCount = 8;
        this.frameTime = frameTime;
        this.frameTimer = 0;
        this.currentFrame = 0;
        this.isFinished = false;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (!isFinished) {
            int srcX = currentFrame * frameWidth;
            int srcY = 0;
            Rect src = new Rect(srcX, srcY, srcX + frameWidth, srcY + frameWidth); // Source rectangle
            Rect dst = new Rect(point.x, point.y, point.x + frameWidth, point.y + frameWidth); // Destination rectangle
            canvas.drawBitmap(spriteSheet, src, dst, paint);
        }
    }

    public void update() {
        if (!isFinished) {
            frameTimer++;
            if (frameTimer >= frameTime) {
                frameTimer = 0;
                currentFrame++;
                if (currentFrame >= frameCount) {
                    isFinished = true;
                }
            }
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}

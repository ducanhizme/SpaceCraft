package com.example.spacecraft.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.spacecraft.R;

public class Background {
    public int WIDTH ;
    public int HEIGHT;
    public int x;
    public int y;
    public Bitmap background;

    public Background(int width, int height, Resources res,int drawable) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.background = BitmapFactory.decodeResource(res, drawable);
        this.background = Bitmap.createScaledBitmap(this.background, WIDTH, HEIGHT, false);
    }
}

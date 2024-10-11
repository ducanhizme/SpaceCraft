package com.example.spacecraft.models;


import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.spacecraft.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerShip extends GameObject implements SensorEventListener {
    private float velocityX = 0;
    private float velocityY = 0;
    private long lastUpdateTime = 0;
    private final List<Bullet> bullets = new CopyOnWriteArrayList<>();
    private long lastShotTime = 0;

    public PlayerShip(float screenWidth, float screenHeight,SensorManager sensorManager, Sensor gyroscope,Resources res, int drawable, float screenRatioX, float screenRatioY) {
        super(screenWidth,screenHeight,res, drawable, screenRatioX, screenRatioY);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME);
    }

    public void shoot() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= 500) {
            Bullet bullet = new Bullet(screenWidth, screenHeight, res, R.drawable.bullet_3, screenRatioX, screenRatioY);
            bullet.coordinate = new Coordinate(coordinate.x + (float) WIDTH / 2, coordinate.y - (float) HEIGHT);
            bullets.add(bullet);
            lastShotTime = currentTime;
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(background, coordinate.x, coordinate.y, paint);
        synchronized (bullets) {
            Iterator<Bullet> iterator = bullets.iterator();
            while (iterator.hasNext()) {
                Bullet bullet = iterator.next();
                bullet.draw(canvas, paint);
            }
        }
    }

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis()*10;
        if (lastUpdateTime != 0) {
            float deltaTime = (currentTime - lastUpdateTime) / 1000.0f;
            coordinate.x += velocityY * deltaTime;
            coordinate.y += velocityX * deltaTime;
            if (coordinate.x < 0) {
                coordinate.x = 0;
            }
            if (coordinate.x > screenWidth - WIDTH) {
                coordinate.x = screenWidth - WIDTH;
            }
            if (coordinate.y < 0) {
                coordinate.y = 0;
            }
            if (coordinate.y > screenHeight - HEIGHT) {
                coordinate.y = screenHeight - HEIGHT;
            }
        }

        lastUpdateTime = currentTime;
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float deltaX = event.values[0];
            float deltaY = event.values[1];
            velocityX += deltaX;
            velocityY += deltaY;
        }
        shoot();
    };


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


}
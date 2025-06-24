package com.example.spacecraft.models.game;


import android.content.res.Resources;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.spacecraft.base.GameObject;

public class EnemyShip extends GameObject {
    private float velocityX, velocityY;
    // private float accelerationX, accelerationY; // Acceleration will be part of velocity adjustment
    private float targetX, targetY;
    private boolean exploding = false;

    private float x_float, y_float; // Internal floating point position

    private static final float SMOOTHING_FACTOR = 0.05f; // For lerping velocity
    private static final float TARGET_REACH_THRESHOLD = 50f; // Distance to consider target reached
    private static final float WANDER_RADIUS = 150f; // Radius for new target when wandering

    public EnemyShip(float screenWidth, float screenHeight, Resources res, int drawable, float screenRatioX, float screenRatioY) {
        super(screenWidth, screenHeight, res, drawable, screenRatioX, screenRatioY);
        // Initialize float position
        this.x_float = (float) (Math.random() * (screenWidth - getWidth())); // Start within bounds
        this.y_float = (float) (Math.random() * (screenHeight / 2)); // Start in the upper half of the screen
        super.setPoint(new Point((int) x_float, (int) y_float)); // Set initial integer point for compatibility

        velocityX = 0;
        velocityY = 0;
        // accelerationX = 0;
        // accelerationY = 0;
        targetX = (float) (Math.random() * screenWidth);
        targetY = (float) (Math.random() * screenHeight);
    }

    // Linear interpolation function
    private float lerp(float start, float end, float amount) {
        return start + amount * (end - start);
    }

    @Override
    public Point getPoint() {
        // Return a Point based on the rounded float coordinates
        // This ensures that drawing uses integer coordinates as required by canvas.drawBitmap
        // and that the superclass's Point object is kept in sync if accessed directly.
        super.getPoint().x = Math.round(x_float);
        super.getPoint().y = Math.round(y_float);
        return super.getPoint();
    }

    public PointF getExactPointF() {
        return new PointF(x_float, y_float);
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (getExplosion() != null && !getExplosion().isFinished()) {
            getExplosion().draw(canvas, paint);
        } else {
            // Use the getPoint() method which now returns the rounded float position
            canvas.drawBitmap(getBackground(), getPoint().x, getPoint().y, paint);
        }
    }

    @Override
    public void update() {
        if (getExplosion() != null && !getExplosion().isFinished()) {
            getExplosion().update();
        } else {
            float directionX = targetX - x_float;
            float directionY = targetY - y_float;
            float distance = (float) Math.sqrt(directionX * directionX + directionY * directionY);

            float targetVelocityX = 0;
            float targetVelocityY = 0;

            if (distance > 1) { // Avoid division by zero and normalize
                directionX /= distance;
                directionY /= distance;
                targetVelocityX = directionX * getSpeed();
                targetVelocityY = directionY * getSpeed();
            }

            // Smoothly interpolate velocity towards target velocity
            velocityX = lerp(velocityX, targetVelocityX, SMOOTHING_FACTOR);
            velocityY = lerp(velocityY, targetVelocityY, SMOOTHING_FACTOR);

            // Update float position
            x_float += velocityX;
            y_float += velocityY;

            // Boundary checks - ensure the ship stays within screen bounds
            if (x_float < 0) {
                x_float = 0;
                velocityX = -velocityX * 0.5f; // Bounce gently
                pickNewWanderTarget(); // Pick new target if stuck at edge
            } else if (x_float > getScreenWidth() - getWidth()) {
                x_float = getScreenWidth() - getWidth();
                velocityX = -velocityX * 0.5f; // Bounce gently
                pickNewWanderTarget();
            }

            if (y_float < 0) {
                y_float = 0;
                velocityY = -velocityY * 0.5f; // Bounce gently
                pickNewWanderTarget();
            } else if (y_float > getScreenHeight() - getHeight()) {
                y_float = getScreenHeight() - getHeight();
                velocityY = -velocityY * 0.5f; // Bounce gently
                pickNewWanderTarget();
            }

            // Update the Point object in GameObject for compatibility if needed by other systems
            super.getPoint().x = Math.round(x_float);
            super.getPoint().y = Math.round(y_float);

            // Check if target is reached or very close
            if (distance < TARGET_REACH_THRESHOLD) {
                pickNewWanderTarget();
            }

            // Explosion logic (already existing)
            if (getExplosion() != null) {
                getExplosion().update();
            }
        }
    }

    private void pickNewWanderTarget() {
        // Pick a new target within a WANDER_RADIUS of the current position.
        float angle = (float) (Math.random() * 2 * Math.PI); // Random angle
        float newTargetX = x_float + (float) (Math.cos(angle) * WANDER_RADIUS);
        float newTargetY = y_float + (float) (Math.sin(angle) * WANDER_RADIUS);

        // Clamp to screen bounds, ensuring the ship's body stays within bounds
        newTargetX = Math.max(0, Math.min(getScreenWidth() - getWidth(), newTargetX));
        newTargetY = Math.max(0, Math.min(getScreenHeight() - getHeight(), newTargetY));

        targetX = newTargetX;
        targetY = newTargetY;
    }

    @Override
    public void setHealth(int health) {
        super.setHealth(health);
        if (health <= 0 && !exploding) {
            triggerExplosion(getRes());
            exploding = true;
        }
    }

    public boolean isExploding() {
        return exploding;
    }
}

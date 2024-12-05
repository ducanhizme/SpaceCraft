package com.example.spacecraft.models.app;

import com.example.spacecraft.utils.CommonHelper;

import java.io.Serializable;

public class GyroParameter implements Serializable {
    public final static String TAG = "GyroParameter";
    private String id;
    private String title;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private long createdAt;

    public GyroParameter() {
    }

    public GyroParameter(float rotationX, float rotationY, float rotationZ) {
        this.id= CommonHelper.generateUUID();
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.createdAt = System.currentTimeMillis();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getRotationX() {
        return rotationX;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}

package org.superbiz.game.model;

import java.util.Collection;

public class SnakeData {
//    string id = 1;
//    float x = 2;
//    float y = 3;
//    float rotation = 4;
//    float rotationAsked = 5;
//    uint32 length = 6;
//    repeated Part parts = 7;
//    float speed = 8;
//    string skin = 9;
//    uint64 lastProcessedOnServer = 10;


    private float x;
    private float y;
    private float speed;
    private float rotation;
    private float rotationAsked;
    private int length;
    private Collection<Part> path;
    //private float speedMultiplier;
    private String skin;
    private long lastProcessed;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotationAsked() {
        return rotationAsked;
    }

    public void setRotationAsked(float rotationAsked) {
        this.rotationAsked = rotationAsked;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Collection<Part> getPath() {
        return path;
    }

    public void setPath(Collection<Part> path) {
        this.path = path;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public long getLastProcessed() {
        return lastProcessed;
    }

    public void setLastProcessed(long lastProcessed) {
        this.lastProcessed = lastProcessed;
    }
}

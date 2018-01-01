package org.superbiz.game.msg;

public class Part {
    private float x;
    private float y;
    private float r;

    public Part(float x, float y, float rotation) {
        this.x = x;
        this.y = y;
        this.r = rotation;
    }

    public Part() {
    }

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

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }
}

package org.superbiz.game.msg;

public class Part {
    private double x;
    private double y;
    private double r;

    public Part(double x, double y, double rotation) {
        this.x = x;
        this.y = y;
        this.r = rotation;
    }

    public Part() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }
}

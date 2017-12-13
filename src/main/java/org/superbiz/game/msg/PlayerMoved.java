package org.superbiz.game.msg;

public class PlayerMoved {
    private double x;
    private double y;

    public PlayerMoved(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PlayerMoved() {
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
}

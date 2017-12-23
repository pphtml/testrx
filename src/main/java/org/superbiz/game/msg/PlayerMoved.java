package org.superbiz.game.msg;

import java.util.Collection;

public class PlayerMoved {
    private double x;
    private double y;
    private Collection<Part> path;
    private long sent;

    public PlayerMoved(double x, double y, Collection<Part> path, long sent) {
        this.x = x;
        this.y = y;
        this.path = path;
        this.sent = sent;
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

    public Collection<Part> getPath() {
        return path;
    }

    public void setPath(Collection<Part> path) {
        this.path = path;
    }

    public long getSent() {
        return sent;
    }

    public void setSent(long sent) {
        this.sent = sent;
    }
}

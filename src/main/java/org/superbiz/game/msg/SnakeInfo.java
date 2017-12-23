package org.superbiz.game.msg;

import java.util.Collection;

public class SnakeInfo {
    private Collection<Part> path;
    private String skin;
    private double rotation;
    private double speed;

    public SnakeInfo(Collection<Part> path, String skin, double rotation, double speed) {
        this.path = path;
        this.skin = skin;
        this.rotation = rotation;
        this.speed = speed;
    }

    public SnakeInfo() {
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

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SnakeInfo{");
        sb.append("path=").append(path);
        sb.append(", skin='").append(skin).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

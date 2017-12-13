package org.superbiz.game.msg;

public class WorldInfo {
    private int radius;

    public WorldInfo(int radius) {
        this.radius = radius;
    }

    public WorldInfo() {
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WorldInfo{");
        sb.append("radius=").append(radius);
        sb.append('}');
        return sb.toString();
    }
}

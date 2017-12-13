package org.superbiz.game.msg;

public class Resize {
    private int width;
    private int height;

    public Resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Resize() {
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Resize{");
        sb.append("width=").append(width);
        sb.append(", height=").append(height);
        sb.append('}');
        return sb.toString();
    }
}

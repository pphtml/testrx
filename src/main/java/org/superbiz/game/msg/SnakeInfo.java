package org.superbiz.game.msg;

import java.util.Collection;

public class SnakeInfo {
    private Collection<Part> path;
    private String skin;

    public SnakeInfo(Collection<Part> path, String skin) {
        this.path = path;
        this.skin = skin;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SnakeInfo{");
        sb.append("path=").append(path);
        sb.append(", skin='").append(skin).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

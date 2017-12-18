package org.superbiz.game.msg;

import java.util.Map;

public class SnakesUpdate {
    private Map<String, SnakeInfo> snakes;

    public SnakesUpdate(Map<String, SnakeInfo> snakes) {
        this.snakes = snakes;
    }

    public SnakesUpdate() {
    }

    public Map<String, SnakeInfo> getSnakes() {
        return snakes;
    }

    public void setSnakes(Map<String, SnakeInfo> snakes) {
        this.snakes = snakes;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SnakesUpdate{");
        sb.append("snakes=").append(snakes);
        sb.append('}');
        return sb.toString();
    }
}

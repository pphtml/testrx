package org.superbiz.game.computation;

import java.util.List;

public interface WormMovement {
    WormMovementJavascript.MoveSnakeResult moveSnake(List<WormMovementJavascript.Part> snakePath, float angle, float distance, float partDistance);

    float computeAllowedAngle(float askedAngle, float lastAngle, long time, float baseSpeed, float speed);
}

package org.superbiz.game.computation;

import org.superbiz.game.model.Part;

import java.util.List;

public interface WormMovement {
    WormMovementJavascript.MoveSnakeResult moveSnake(List<Part> snakePath, float angle, float distance, float partDistance);

    float computeAllowedAngle(float askedAngle, float lastAngle, long time, float baseSpeed, float speed);
}

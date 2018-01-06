package org.superbiz.game;

//import com.google.inject.Singleton;
import org.superbiz.game.computation.WormMovement;
import org.superbiz.game.computation.WormMovementJavascript;
import org.superbiz.game.model.MoveSnakeResult;
import org.superbiz.game.model.Part;
import org.superbiz.game.model.SnakeData;
import org.superbiz.game.proto.Msg;
import rx.subjects.PublishSubject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.logging.Logger;

@Singleton
public class SnakePositions {
    @Inject
    Logger logger;

    private static final int LENGTH_PER_PART = 10;
    private static final int INITIAL_DEFAULT_LENGTH = 150;
    private static final float INITIAL_PART_DISTANCE = 20.0f;


    public SnakePositions() {
    }

    private final PublishSubject<Msg.SnakesUpdate> observableSnakes = PublishSubject.create();

    public PublishSubject<Msg.SnakesUpdate> getObservableSnakes() {
        return observableSnakes;
    }

    private Map<String, SnakeData> map = new LinkedHashMap<>();

    private WormMovement wormMovement = new WormMovementJavascript();

//    public void update(Player player, Msg.PlayerMoved playerMoved) {
//        Msg.SnakeInfo snakeInfo = Msg.SnakeInfo.newBuilder()
//                .addAllPath(playerMoved.getPartsList())
//                .setSkin(playerMoved.getSkin())
//                .setRotation(playerMoved.getRotation())
//                .setSpeed(playerMoved.getSpeed())
//                .setId(player.getId())
//                .build();
//        //Msg.SnakeInfo snakeInfo = new SnakeInfo(playerMoved.getPath(), playerMoved.getSkin(), playerMoved.getRotation(), playerMoved.getSpeed());
//        map.put(player.getId(), snakeInfo);
//        Msg.SnakesUpdate snakesUpdate = Msg.SnakesUpdate.newBuilder().addAllSnakes(map.values()).build();
//        observableSnakes.onNext(snakesUpdate);
//    }

    public void remove(String playerId) {
        logger.info(String.format("Removing player: %s", playerId));
        map.remove(playerId);
    }

    public SnakeData createSnake(Player player) {
        final List path = new ArrayList<>();
        for (int index = 0, lengthIndex = 1; lengthIndex <= INITIAL_DEFAULT_LENGTH; index++, lengthIndex += LENGTH_PER_PART) {
            path.add(Part.create(-index * INITIAL_PART_DISTANCE, 0.0f, 0.0f));
        }

        final SnakeData snakeData = new SnakeData();
        return snakeData
                .setX(0.0f)
                .setY(0.0f)
                .setSpeed(10.0f)
                .setRotation(0.0f)
                .setRotationAsked(0.0f)
                .setLength(150)
                .setPath(path)
                .setSkin(player.getSkin())
                .setLastProcessed(System.currentTimeMillis());

    }

    public SnakeData createAndRegisterSnake(Player player) {
        final SnakeData snakeData = createSnake(player);
        this.map.put(player.getId(), snakeData);
        return snakeData;
    }

    public Optional<SnakeData> moveSnake(String id, Msg.PlayerUpdateReq updateReq) {
        SnakeData snakeData = this.map.get(id);
        if (snakeData == null) {
            logger.severe(String.format("Couldn't find snake with id: %s", id));
            return Optional.empty();
        } else {
            long now = System.currentTimeMillis();
            long elapsedTime = now - snakeData.getLastProcessed();

            float baseSpeed = 1.0f;

//            let baseSpeed = 1.0;
//            let speed = 5.0 * (this.gameContext.controls.isMouseDown() ? baseSpeed * 2 : baseSpeed); // * elapsedTime * 0.06;
//            let angle = Controls.computeAllowedAngle(askedAngle, this.lastAngle, elapsedTime, this.gameContext, baseSpeed, speed);

            float speed = 5.0f * updateReq.getSpeedMultiplier();   // * elapsedTime * 0.06;

            //float askedAngle, float lastAngle, long time, float baseSpeed, float speed
            float newRotation = this.wormMovement.computeAllowedAngle(updateReq.getRotationAsked(), // askedAngle
                    snakeData.getRotation(), // lastAngle
                    elapsedTime, // time
                    baseSpeed, // baseSpeed
                    speed); //speed
            //List<Part> snakePath, float angle, float distance, float partDistance
            MoveSnakeResult movedSnake = this.wormMovement.moveSnake(snakeData.getPath(), // snakePath
                    newRotation, // angle
                    speed, // distance
                    INITIAL_PART_DISTANCE); // partDistance

            snakeData.setLastProcessed(now)
                .setRotation(newRotation)
                .setRotationAsked(updateReq.getRotationAsked())
                .setX(movedSnake.getX())
                .setY(movedSnake.getY())
                .setPath(movedSnake.getPath());
            return Optional.of(snakeData);
        }
    }
}
package org.superbiz.game;

//import com.google.inject.Singleton;
import org.superbiz.game.model.SnakeData;
import org.superbiz.game.proto.Msg;
import rx.subjects.PublishSubject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
public class SnakePositions {
    @Inject
    Logger logger;

    public SnakePositions() {
    }

    private final PublishSubject<Msg.SnakesUpdate> observableSnakes = PublishSubject.create();

    public PublishSubject<Msg.SnakesUpdate> getObservableSnakes() {
        return observableSnakes;
    }

    private Map<String, SnakeData> map = new LinkedHashMap<>();

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

    public void createSnake(Player player) {
    }
}
package org.superbiz.game;

//import com.google.inject.Singleton;
import org.superbiz.game.msg.PlayerMoved;
import org.superbiz.game.msg.SnakeInfo;
import org.superbiz.game.msg.SnakesUpdate;
import rx.subjects.PublishSubject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
public class SnakePositions {
    //private static final Logger logger = Logger.getLogger(SnakePositions.class.getName());

    @Inject
    Logger logger;

    public SnakePositions() {
        //logger.info(String.format("CCCCCCCCCCCCCCCCCCCCCCCCCCC %s", observableSnakes));
        //System.err.println("XXXXXXXXXXXXXXXXXXX");

//        observableSnakes.subscribe(a -> {
//            logger.info(String.format("UPDATE SNAKES: %s", a));
//        });
    }

    private final PublishSubject<SnakesUpdate> observableSnakes = PublishSubject.create();

    public PublishSubject<SnakesUpdate> getObservableSnakes() {
        return observableSnakes;
    }

    private Map<String, SnakeInfo> map = new LinkedHashMap<>();

    public void update(Player player, PlayerMoved playerMoved) {
        String skin = findSkin(player.getId());
        SnakeInfo snakeInfo = new SnakeInfo(playerMoved.getPath(), skin, playerMoved.getRotation(), playerMoved.getSpeed());
        map.put(player.getId(), snakeInfo);
        observableSnakes.onNext(new SnakesUpdate(map));
    }

    private String findSkin(String id) {
        return "red"; // TODO udelat mapu na skiny
    }

    public void remove(String playerId) {
        logger.info(String.format("Removing player: %s", playerId));
        map.remove(playerId);
    }
}

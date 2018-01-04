package org.superbiz.game;

//import com.google.inject.Singleton;
import org.superbiz.game.proto.Msg;
import rx.subjects.PublishSubject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
public class SnakePositions {
    //private static final Logger logger = Logger.getLogger(SnakePositions.class.getName());
    //private final JSONMapper mapper = getJSONMapper();

    @Inject
    Logger logger;

    public SnakePositions() {
        //logger.info(String.format("CCCCCCCCCCCCCCCCCCCCCCCCCCC %s", observableSnakes));
        //System.err.println("XXXXXXXXXXXXXXXXXXX");

//        observableSnakes.subscribe(a -> {
//            logger.info(String.format("UPDATE SNAKES: %s", a));
//        });
        // {"snakesUpdate":{"snakes":{"snakeA":{"path":[{"x":10.6,"y":30.5,"r":3.14},{"x":10.8,"y":30.4,"r":1.67},{"x":10.1,"y":30.2,"r":-3.14}],"skin":"blue"},"snakeB":{"path":[{"x":10.7,"y":30.6,"r":4.14},{"x":10.8,"y":30.4,"r":1.67},{"x":10.1,"y":30.2,"r":-3.14}],"skin":"red"}}}}
        //

    //        try {
    //            String json = "{\"path\":[{\"x\":29.85,\"y\":2.78,\"r\":0.11},{\"x\":9.9,\"y\":1.29,\"r\":0.07},{\"x\":-10.08,\"y\":0.53,\"r\":0.04},{\"x\":-30.08,\"y\":0.19,\"r\":0.02},{\"x\":-50.08,\"y\":0.07,\"r\":0.01},{\"x\":-70.08,\"y\":0.02,\"r\":0.0},{\"x\":-90.08,\"y\":0.01,\"r\":0.0},{\"x\":-110.08,\"y\":0.0,\"r\":0.0},{\"x\":-130.08,\"y\":0.0,\"r\":0.0},{\"x\":-150.08,\"y\":0.0,\"r\":0.0},{\"x\":-170.08,\"y\":0.0,\"r\":0.0},{\"x\":-190.08,\"y\":0.0,\"r\":0.0},{\"x\":-210.08,\"y\":0.0,\"r\":0.0},{\"x\":-230.08,\"y\":0.0,\"r\":0.0},{\"x\":-250.08,\"y\":0.0,\"r\":0.0}],\"skin\":\"red\",\"rotation\":0.0,\"speed\":0.25}";
    //            SnakeInfo snakeInfo = mapper.reader().forType(SnakeInfo.class).readValue(json);
    //            map.put("HJlnnghGz", snakeInfo);
    //        } catch (IOException e) {
    //            logger.log(Level.SEVERE, e.getMessage(), e);
    //        }
        //assertNotNull(message.getSnakesUpdate());

    }

    private final PublishSubject<Msg.SnakesUpdate> observableSnakes = PublishSubject.create();

    public PublishSubject<Msg.SnakesUpdate> getObservableSnakes() {
        return observableSnakes;
    }

    private Map<String, Msg.SnakeInfo> map = new LinkedHashMap<>();

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
}
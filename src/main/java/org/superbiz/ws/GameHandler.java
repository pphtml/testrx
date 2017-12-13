package org.superbiz.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.superbiz.game.GameDataService;
import org.superbiz.game.Player;
import org.superbiz.game.msg.Message;
import org.superbiz.game.msg.MessageBuilder;
import org.superbiz.game.msg.WorldInfo;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.websocket.*;
import rx.Subscription;
import rx.subjects.PublishSubject;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameHandler implements Handler {
    private static final Logger logger = Logger.getLogger(GameHandler.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    private GameDataService gameDataService;

    // List of all currently connected clients
    private final Map<String, Player> players = new HashMap<>();

    // Subject that all clients subscribe to for events
    private final PublishSubject<String> events = PublishSubject.create();

    // Mapping of client to subscription to the events subject
    private final Map<String, Subscription> subscriptions = new HashMap<>();

    @Override
    public void handle(Context ctx) {
        WebSockets.websocket(ctx, new WebSocketHandler<String>() {

            @Override
            public String onOpen(WebSocket webSocket) throws Exception {
                String playerId = ctx.getRequest().getQueryParams().get("id");

                logger.info(String.format("Websocket opened for player: %s", playerId));

                if (playerId == null || playerId.isEmpty()) {
                    webSocket.close(500, "Client id is required");
                } else if (players.containsKey(playerId)) {
                    webSocket.close(500, "Client is already connected");
                } else {
                    Player player = new Player(playerId, webSocket);
                    players.put(playerId, player);

                    player.getPeriodicUpdate().subscribe(update -> {
                        gameDataService.processPeriodicUpdate(update.left, update.right);
                    });
                    /*Map<String, Object> initEvent = new HashMap<>();
                    initEvent.put("type", "init");
                    initEvent.put("client", client);
                    initEvent.put("success", true);
                    initEvent.put("connectedClients", Collections.unmodifiableSet(clients));

                    webSocket.send(mapper.writer().writeValueAsString(initEvent));*/

//                    String data = gameDataService.getDotsUpdate(player);
//                    logger.info(data);
//                    webSocket.send(data);

                    String jsonMsg = MessageBuilder.create().setWorldInfo(new WorldInfo(3000)).toJson();
                    webSocket.send(jsonMsg);


//                    Map<String, Object> clientConnectEvent = new HashMap<>();
//                    clientConnectEvent.put("type", "clientconnect");
//                    clientConnectEvent.put("client", playerId);
//                    events.onNext(mapper.writer().writeValueAsString(clientConnectEvent));

                    subscriptions.put(playerId, events.subscribe(webSocket::send));

                    logger.info(String.format("Client %s subscribed to event stream", playerId));
                }

                return null;
            }

            @Override
            public void onClose(WebSocketClose<String> close) throws Exception {
                String playerId = ctx.getRequest().getQueryParams().get("id");
//                Player player = players.get(playerId);

                players.remove(playerId);

                Map<String, Object> event = new HashMap<>();
                event.put("type", "clientdisconnect");
                event.put("id", playerId);

                events.onNext(mapper.writer().writeValueAsString(event));

                subscriptions.remove(playerId);

                logger.info(String.format("Websocket closed for player: %s", playerId));
            }

            @Override
            public void onMessage(WebSocketMessage<String> frame) {
                String playerId = ctx.getRequest().getQueryParams().get("id");
                Player player = players.get(playerId);
                if (player == null) {
                    logger.warning(String.format("Player '%s' cannot be found.", playerId));
                } else {
                    //logger.info(String.format("Inc -->: %s", frame.getText()));
                    try {
                        Message message = mapper.reader().forType(Message.class).readValue(frame.getText());
                        gameDataService.processMessage(message, player);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, e.getMessage(), e);
                    }
                    //events.onNext("ble");
                    //events.onNext(frame.getText());
                }
            }
        });
    }
}

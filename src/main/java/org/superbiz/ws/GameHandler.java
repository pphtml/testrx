package org.superbiz.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import org.superbiz.game.GameDataService;
import org.superbiz.game.Player;
import org.superbiz.game.SnakePositions;
import org.superbiz.game.proto.Msg;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.server.ServerConfig;
import ratpack.websocket.*;
import ratpack.websocket.internal.MyWebSocketEngine;
import rx.Subscription;
import rx.subjects.PublishSubject;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameHandler implements Handler {
    private static final Logger logger = Logger.getLogger(GameHandler.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();
    private final SnakePositions snakePositions;

    //@Inject
    private GameDataService gameDataService;

    @Inject
    public GameHandler(GameDataService gameDataService, SnakePositions snakePositions) {
        this.gameDataService = gameDataService;
        this.snakePositions = snakePositions;
        logger.info(String.format("STREAM: %s", gameDataService.getSnakeUpdate()));
        gameDataService.getSnakeUpdate().subscribe(snakesUpdate -> {
            byte[] msg = Msg.Message.newBuilder().setSnakesUpdate(snakesUpdate).build().toByteArray();
            events.onNext(Unpooled.wrappedBuffer(msg));
        });
    }

    // List of all currently connected clients
    private final Map<String, Player> players = new HashMap<>();

    // Subject that all clients subscribe to for events
    private final PublishSubject<ByteBuf> events = PublishSubject.create();

    // Mapping of client to subscription to the events subject
    private final Map<String, Subscription> subscriptions = new HashMap<>();

    @Override
    public void handle(Context ctx) {
        ChannelPipeline pipeline = ctx.getDirectChannelAccess().getChannel().pipeline();

        MyWebSocketEngine.connect(ctx, "/", ctx.get(ServerConfig.class).getMaxContentLength(), //handler);


        /*WebSockets.websocket(ctx,*/ new WebSocketHandler<ByteBuf>() {

            @Override
            public ByteBuf onOpen(WebSocket webSocket) throws Exception {
                ChannelPipeline pipeline = ctx.getDirectChannelAccess().getChannel().pipeline();

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

                    Msg.WorldInfo.Builder worldInfo = Msg.WorldInfo.newBuilder().setRadius(3000);
                    byte[] msgBytes = Msg.Message.newBuilder().setWorldInfo(worldInfo).build().toByteArray();
                    webSocket.send(Unpooled.wrappedBuffer(msgBytes));

//                    webSocket.send(new String(msgBytes, "ASCII"));
                    //ByteBuf byteBuf = Unpooled.wrappedBuffer(msgBytes);

//                    ByteBuf byteBuf = Unpooled.directBuffer(20);
//                    byteBuf.writeLong(Long.MAX_VALUE);
//                    webSocket.send(byteBuf);

//                    String jsonMsg = MessageBuilder.create().setWorldInfo(new WorldInfo(3000)).toJson();
//                    webSocket.send(jsonMsg);

//                    ByteBuf byteBuf = Unpooled.directBuffer(5000);
//                    ByteBufUtil.writeUtf8(byteBuf, jsonMsg);
//                    webSocket.send(byteBuf);


//                    Map<String, Object> clientConnectEvent = new HashMap<>();
//                    clientConnectEvent.put("type", "clientconnect");
//                    clientConnectEvent.put("client", playerId);
//                    events.onNext(mapper.writer().writeValueAsString(clientConnectEvent));

                    subscriptions.put(playerId, events.subscribe(webSocket::send));

                    logger.info(String.format("Client %s subscribed to event stream", playerId));
                }

                return Unpooled.wrappedBuffer("abcdef".getBytes());
            }

            @Override
            public void onClose(WebSocketClose<ByteBuf> close) throws Exception {
                String playerId = ctx.getRequest().getQueryParams().get("id");
//                Player player = players.get(playerId);

                players.remove(playerId);
                snakePositions.remove(playerId);
                //gameDataService.remove(playerId);

                Map<String, Object> event = new HashMap<>();
                event.put("type", "clientdisconnect");
                event.put("id", playerId);

                byte[] msg = Msg.ClientDisconnect.newBuilder().setId(playerId).build().toByteArray();
                events.onNext(Unpooled.wrappedBuffer(msg));

                subscriptions.remove(playerId);

                logger.info(String.format("Websocket closed for player: %s", playerId));
            }

            @Override
            public void onMessage(WebSocketMessage<ByteBuf> frame) {
                String playerId = ctx.getRequest().getQueryParams().get("id");
                Player player = players.get(playerId);
                if (player == null) {
                    logger.warning(String.format("Player '%s' cannot be found.", playerId));
                } else {
                    logger.info(String.format("Incoming frame -->: %s", frame));
                    try {
                        Msg.Message message = Msg.Message.parseFrom(frame.getOpenResult().array());
                        logger.info(String.format("Incoming message -->: %s", message));
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }
//                    try {
//                        Message message = mapper.reader().forType(Message.class).readValue(frame.getText());
//                        gameDataService.processMessage(message, player);
//                    } catch (Exception e) {
//                        logger.log(Level.SEVERE, e.getMessage(), e);
//                    }
                    //events.onNext("ble");
                    //events.onNext(frame.getText());
                }
            }
        });
    }
}

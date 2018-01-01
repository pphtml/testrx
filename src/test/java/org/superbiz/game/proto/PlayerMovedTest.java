package org.superbiz.game.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;
import org.superbiz.game.msg.JSONMapper;
import org.superbiz.game.msg.Message;
import org.superbiz.game.msg.Part;
import org.superbiz.game.msg.PlayerMoved;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.superbiz.game.msg.JSONMapper.getJSONMapper;

public class PlayerMovedTest {
    public static final double ACCEPTABLE_DELTA = 0.000001;

    @Test
    public void msgToJson() throws InvalidProtocolBufferException {
        List<Part> path = Arrays.asList(new Part(10.6f, 30.5f, 3.14f), new Part(10.8f, 30.4f, 1.67f),
                new Part(10.1f, 30.2f, -3.14f));
        List<Msg.PlayerMoved.Part> protoPath = path.stream()
                .map(part -> Msg.PlayerMoved.Part.newBuilder().setX(part.getX()).setY(part.getY()).setR(part.getR()).build())
                .collect(Collectors.toList());

//        required float x = 1;
//        required float y = 2;
//        required float rotation = 3;
//        required float speed = 4;
//        optional string skin = 5;
//        repeated Part parts = 6;
//        optional int64 sent = 7;

        Msg.PlayerMoved.Builder playerMovedSource = Msg.PlayerMoved.newBuilder()
                .setX(10.6f)
                .setY(30.5f)
                .setRotation(3.14f)
                .setSpeed(20f)
                .setSkin("red")
                .setSent(9876543210L)
                .addAllParts(protoPath);

        byte[] bytesMessage = Msg.Message.newBuilder().setPlayerMoved(playerMovedSource).build().toByteArray();

        Msg.Message decodedMessage = Msg.Message.parseFrom(bytesMessage);
        assertNotNull(decodedMessage.getPlayerMoved());
        Msg.PlayerMoved playerMoved = decodedMessage.getPlayerMoved();

        assertEquals(10.6, playerMoved.getX(), ACCEPTABLE_DELTA);
        assertEquals(30.5, playerMoved.getY(), ACCEPTABLE_DELTA);
        List<Msg.PlayerMoved.Part> partsList = playerMoved.getPartsList();
        assertNotNull(partsList);
        assertEquals(3, partsList.size());
        Msg.PlayerMoved.Part part = partsList.iterator().next();
        assertNotNull(part);
        assertEquals(10.6, part.getX(), ACCEPTABLE_DELTA);
        assertEquals(30.5, part.getY(), ACCEPTABLE_DELTA);
        assertEquals(3.14, part.getR(), ACCEPTABLE_DELTA);


//        Message message = new Message();
//        message.setPlayerMoved(new PlayerMoved(10.6, 30.5, path, 9876543210L, "blue"));
//        String json = message.toJson();
//        //assertEquals("{\"playerMoved\":{\"x\":10.6,\"y\":30.5}}", json);
//        assertEquals("{\"playerMoved\":{\"x\":10.6,\"y\":30.5,\"path\":[{\"x\":10.6,\"y\":30.5,\"r\":3.14},{\"x\":10.8,\"y\":30.4,\"r\":1.67},{\"x\":10.1,\"y\":30.2,\"r\":-3.14}],\"sent\":9876543210,\"rotation\":0.0,\"speed\":0.0,\"skin\":\"blue\"}}", json);
    }

//    @Test
//    public void jsonToMsg() throws IOException {
//        //String json = "{\"playerMoved\":{\"x\":10,\"y\":30}}";
//        String json = "{\"playerMoved\":{\"x\":10.6,\"y\":30.5,\"path\":[{\"x\":10.6,\"y\":30.5,\"r\":3.14},{\"x\":10.8,\"y\":30.4,\"r\":1.67},{\"x\":10.1,\"y\":30.2,\"r\":-3.14}]}}";
//        Message message = mapper.reader().forType(Message.class).readValue(json);
//        assertNotNull(message.getPlayerMoved());
//        assertEquals(10.6, message.getPlayerMoved().getX(), ACCEPTABLE_DELTA);
//        assertEquals(30.5, message.getPlayerMoved().getY(), ACCEPTABLE_DELTA);
//        Collection<Part> path = message.getPlayerMoved().getPath();
//        assertNotNull(path);
//        assertEquals(3, path.size());
//        Part part = path.iterator().next();
//        assertNotNull(part);
//        assertEquals(10.6, part.getX(), ACCEPTABLE_DELTA);
//        assertEquals(30.5, part.getY(), ACCEPTABLE_DELTA);
//        assertEquals(3.14, part.getR(), ACCEPTABLE_DELTA);
//    }
}

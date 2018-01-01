package org.superbiz.game.msg;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.superbiz.game.msg.JSONMapper.getJSONMapper;

public class PlayerMovedTest {
    public static final double ACCEPTABLE_DELTA = 0.000001;
    private final JSONMapper mapper = getJSONMapper();

    @Test
    public void msgToJson() {
        List<Part> path = Arrays.asList(new Part(10.6f, 30.5f, 3.14f),
                new Part(10.8f, 30.4f, 1.67f),
                new Part(10.1f, 30.2f, -3.14f));

        Message message = new Message();
        message.setPlayerMoved(new PlayerMoved(10.6, 30.5, path, 9876543210L, "blue"));
        String json = message.toJson();
        //assertEquals("{\"playerMoved\":{\"x\":10.6,\"y\":30.5}}", json);
        assertEquals("{\"playerMoved\":{\"x\":10.6,\"y\":30.5,\"path\":[{\"x\":10.6,\"y\":30.5,\"r\":3.14},{\"x\":10.8,\"y\":30.4,\"r\":1.67},{\"x\":10.1,\"y\":30.2,\"r\":-3.14}],\"sent\":9876543210,\"rotation\":0.0,\"speed\":0.0,\"skin\":\"blue\"}}", json);
    }

    @Test
    public void jsonToMsg() throws IOException {
        //String json = "{\"playerMoved\":{\"x\":10,\"y\":30}}";
        String json = "{\"playerMoved\":{\"x\":10.6,\"y\":30.5,\"path\":[{\"x\":10.6,\"y\":30.5,\"r\":3.14},{\"x\":10.8,\"y\":30.4,\"r\":1.67},{\"x\":10.1,\"y\":30.2,\"r\":-3.14}]}}";
        Message message = mapper.reader().forType(Message.class).readValue(json);
        assertNotNull(message.getPlayerMoved());
        assertEquals(10.6, message.getPlayerMoved().getX(), ACCEPTABLE_DELTA);
        assertEquals(30.5, message.getPlayerMoved().getY(), ACCEPTABLE_DELTA);
        Collection<Part> path = message.getPlayerMoved().getPath();
        assertNotNull(path);
        assertEquals(3, path.size());
        Part part = path.iterator().next();
        assertNotNull(part);
        assertEquals(10.6, part.getX(), ACCEPTABLE_DELTA);
        assertEquals(30.5, part.getY(), ACCEPTABLE_DELTA);
        assertEquals(3.14, part.getR(), ACCEPTABLE_DELTA);
    }
}

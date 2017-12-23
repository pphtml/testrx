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
        List<Part> path = Arrays.asList(new Part(10.6, 30.5, 3.14), new Part(10.8, 30.4, 1.67), new Part(10.1, 30.2, -3.14));

        Message message = new Message();
        message.setPlayerMoved(new PlayerMoved(10.6, 30.5, path, 9876543210L));
        String json = message.toJson();
        //assertEquals("{\"playerMoved\":{\"x\":10.6,\"y\":30.5}}", json);
        assertEquals("{\"playerMoved\":{\"x\":10.6,\"y\":30.5,\"path\":[{\"x\":10.6,\"y\":30.5,\"r\":3.14},{\"x\":10.8,\"y\":30.4,\"r\":1.67},{\"x\":10.1,\"y\":30.2,\"r\":-3.14}]}}", json);
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

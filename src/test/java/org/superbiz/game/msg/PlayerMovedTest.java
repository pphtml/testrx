package org.superbiz.game.msg;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.superbiz.game.msg.JSONMapper.getJSONMapper;

public class PlayerMovedTest {
    private final JSONMapper mapper = getJSONMapper();

    @Test
    public void msgToJson() {
        Message message = new Message();
        message.setPlayerMoved(new PlayerMoved(10.6, 30.5));
        String json = message.toJson();
        assertEquals("{\"playerMoved\":{\"x\":10.6,\"y\":30.5}}", json);
    }

    @Test
    public void jsonToMsg() throws IOException {
        String json = "{\"playerMoved\":{\"x\":10,\"y\":30}}";
        Message message = mapper.reader().forType(Message.class).readValue(json);
        assertNotNull(message.getPlayerMoved());
        assertEquals(10, message.getPlayerMoved().getX(), 0.000001);
        assertEquals(30, message.getPlayerMoved().getY(), 0.000001);
    }
}

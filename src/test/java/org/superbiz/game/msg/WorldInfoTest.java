package org.superbiz.game.msg;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.superbiz.game.msg.JSONMapper.getJSONMapper;

public class WorldInfoTest {
    private final JSONMapper mapper = getJSONMapper();

    @Test
    public void msgToJson() {
        Message message = new Message();
        message.setWorldInfo(new WorldInfo(3000));
        String json = message.toJson();
        assertEquals("{\"worldInfo\":{\"radius\":3000}}", json);
    }

    @Test
    public void jsonToMsg() throws IOException {
        String json = "{\"worldInfo\":{\"radius\":3000}}";
        Message message = mapper.reader().forType(Message.class).readValue(json);
        assertNotNull(message.getWorldInfo());
        assertEquals(3000, message.getWorldInfo().getRadius());
    }
}

package org.superbiz.game.msg;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.superbiz.game.msg.JSONMapper.getJSONMapper;

public class ResizeTest {
    private final JSONMapper mapper = getJSONMapper();

    @Test
    public void msgToJson() {
        Message message = new Message();
        message.setResize(new Resize(800, 600));
        String json = message.toJson();
        assertEquals("{\"resize\":{\"width\":800,\"height\":600}}", json);
    }

    @Test
    public void jsonToMsg() throws IOException {
        String json = "{\"resize\":{\"width\":800,\"height\":600}}";
        Message message = mapper.reader().forType(Message.class).readValue(json);
        assertNotNull(message.getResize());
        assertEquals(800, message.getResize().getWidth());
        assertEquals(600, message.getResize().getHeight());
    }
}

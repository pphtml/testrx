package org.superbiz.game.msg;

import org.junit.Test;
import org.superbiz.game.Dot;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.superbiz.game.msg.JSONMapper.getJSONMapper;

public class DotsUpdateTest {
    private final JSONMapper mapper = getJSONMapper();

    @Test
    public void msgToJson() {
        Message message = new Message();
        Collection<Dot> dots = Arrays.asList(Dot.create(30, 20, 3, 3),
                Dot.create(40, 30, 4, 4));
        message.setDotsUpdate(new DotsUpdate(dots));
        String json = message.toJson();
        assertEquals("{\"dotsUpdate\":{\"dots\":[{\"x\":30,\"y\":20,\"c\":3,\"l\":3},{\"x\":40,\"y\":30,\"c\":4,\"l\":4}]}}", json);
    }

    @Test
    public void jsonToMsg() throws IOException {
        String json = "{\"dotsUpdate\":{\"dots\":[{\"x\":30,\"y\":20,\"c\":3,\"l\":3},{\"x\":40,\"y\":30,\"c\":4,\"l\":4}]}}";
        Message message = mapper.reader().forType(Message.class).readValue(json);
        assertNotNull(message.getDotsUpdate());
        Collection<Dot> dots = message.getDotsUpdate().getDots();
        assertEquals(2, dots.size());
    }
}

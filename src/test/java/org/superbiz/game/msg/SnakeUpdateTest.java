package org.superbiz.game.msg;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.superbiz.game.msg.JSONMapper.getJSONMapper;

public class SnakeUpdateTest {
    private final JSONMapper mapper = getJSONMapper();

    @Test
    public void msgToJson() {
        Message message = new Message();

        final List<Part> pathA = Arrays.asList(
                new Part(10.6f, 30.5f, 3.14f),
                new Part(10.8f, 30.4f, 1.67f),
                new Part(10.1f, 30.2f, -3.14f));
        final SnakeInfo snakeInfoA = new SnakeInfo(pathA, "blue", 0.0, 10.0);

        final List<Part> pathB = Arrays.asList(
                new Part(10.7f, 30.6f, 4.14f),
                new Part(10.8f, 30.4f, 1.67f),
                new Part(10.1f, 30.2f, -3.14f));
        final SnakeInfo snakeInfoB = new SnakeInfo(pathB, "red", 3.14, 20.0);

        final Map<String, SnakeInfo> snakes = new LinkedHashMap<>();
        snakes.put("snakeA", snakeInfoA);
        snakes.put("snakeB", snakeInfoB);

        final SnakesUpdate snakesUpdate = new SnakesUpdate(snakes);

        message.setSnakesUpdate(snakesUpdate);
        String json = message.toJson();
        assertEquals("{\"snakesUpdate\":{\"snakes\":{\"snakeA\":{\"path\":[{\"x\":10.6,\"y\":30.5,\"r\":3.14},{\"x\":10.8,\"y\":30.4,\"r\":1.67},{\"x\":10.1,\"y\":30.2,\"r\":-3.14}],\"skin\":\"blue\",\"rotation\":0.0,\"speed\":10.0},\"snakeB\":{\"path\":[{\"x\":10.7,\"y\":30.6,\"r\":4.14},{\"x\":10.8,\"y\":30.4,\"r\":1.67},{\"x\":10.1,\"y\":30.2,\"r\":-3.14}],\"skin\":\"red\",\"rotation\":3.14,\"speed\":20.0}}}}", json);
    }

    @Test
    public void jsonToMsg() throws IOException {
        String json = "{\"snakesUpdate\":{\"snakes\":{\"snakeA\":{\"path\":[{\"x\":10.6,\"y\":30.5,\"r\":3.14},{\"x\":10.8,\"y\":30.4,\"r\":1.67},{\"x\":10.1,\"y\":30.2,\"r\":-3.14}],\"skin\":\"blue\"},\"snakeB\":{\"path\":[{\"x\":10.7,\"y\":30.6,\"r\":4.14},{\"x\":10.8,\"y\":30.4,\"r\":1.67},{\"x\":10.1,\"y\":30.2,\"r\":-3.14}],\"skin\":\"red\"}}}}";
        Message message = mapper.reader().forType(Message.class).readValue(json);
        assertNotNull(message.getSnakesUpdate());
        Map<String, SnakeInfo> snakes = message.getSnakesUpdate().getSnakes();
        assertEquals(2, snakes.size());
        assertTrue(snakes.containsKey("snakeA"));
        assertTrue(snakes.containsKey("snakeB"));
    }
}

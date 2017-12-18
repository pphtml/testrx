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

        final List<Part> pathA = Arrays.asList(new Part(10.6, 30.5, 3.14), new Part(10.8, 30.4, 1.67), new Part(10.1, 30.2, -3.14));
        final SnakeInfo snakeInfoA = new SnakeInfo(pathA, "blue");

        final List<Part> pathB = Arrays.asList(new Part(10.7, 30.6, 4.14), new Part(10.8, 30.4, 1.67), new Part(10.1, 30.2, -3.14));
        final SnakeInfo snakeInfoB = new SnakeInfo(pathB, "red");

        final Map<String, SnakeInfo> snakes = new LinkedHashMap<>();
        snakes.put("snakeA", snakeInfoA);
        snakes.put("snakeB", snakeInfoB);

        final SnakesUpdate snakesUpdate = new SnakesUpdate(snakes);

        message.setSnakesUpdate(snakesUpdate);
        String json = message.toJson();
        assertEquals("{\"snakesUpdate\":{\"snakes\":{\"snakeA\":{\"path\":[{\"x\":10.6,\"y\":30.5,\"r\":3.14},{\"x\":10.8,\"y\":30.4,\"r\":1.67},{\"x\":10.1,\"y\":30.2,\"r\":-3.14}],\"skin\":\"blue\"},\"snakeB\":{\"path\":[{\"x\":10.7,\"y\":30.6,\"r\":4.14},{\"x\":10.8,\"y\":30.4,\"r\":1.67},{\"x\":10.1,\"y\":30.2,\"r\":-3.14}],\"skin\":\"red\"}}}}", json);
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

package org.superbiz.game.computation;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WormMovementJavascript implements WormMovement {
    private static final Logger logger = Logger.getLogger(WormMovementJavascript.class.getName());

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Invocable NASHORN_INVOCABLE;

    static {
        System.setProperty("nashorn.args", "--language=es6");
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        try {
            String content = new String(Files.readAllBytes(Paths.get("src/main/frontend/src/component/wormMovement.js")));
            engine.eval(content);
            NASHORN_INVOCABLE = (Invocable) engine;
        } catch (IOException | ScriptException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    static class Part {
        private float x;
        private float y;
        private float r;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getR() {
            return r;
        }

        public void setR(float r) {
            this.r = r;
        }

        public static Part create(float x, float y, float r) {
            Part part = new Part();
            part.x = x;
            part.y = y;
            part.r = r;
            return part;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Part{");
            sb.append("x=").append(x);
            sb.append(", y=").append(y);
            sb.append(", r=").append(r);
            sb.append('}');
            return sb.toString();
        }
    }

    static class MoveSnakeResult {
        private List<Part> path;
        private float x;
        private float y;

        public List<Part> getPath() {
            return path;
        }

        public void setPath(List<Part> path) {
            this.path = path;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MoveSnakeResult{");
            sb.append("path=").append(path);
            sb.append(", x=").append(x);
            sb.append(", y=").append(y);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public MoveSnakeResult moveSnake(List<Part> snakePath, float angle, float distance, float partDistance) {
        final Map<String, Object> map = new HashMap<>();
        map.put("snakePath", snakePath);
        map.put("angle", angle);
        map.put("distance", distance);
        map.put("partDistance", partDistance);
        try {
            final String json = OBJECT_MAPPER.writeValueAsString(map);
            final String result = (String) NASHORN_INVOCABLE.invokeFunction("moveSnakeJava", json);
            return OBJECT_MAPPER.readValue(result, MoveSnakeResult.class);
        } catch (IOException | NoSuchMethodException | ScriptException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public float computeAllowedAngle(float askedAngle, float lastAngle, long time, float baseSpeed, float speed) {
        final Map<String, Object> map = new HashMap<>();
        map.put("askedAngle", askedAngle);
        map.put("lastAngle", lastAngle);
        map.put("time", time);
        map.put("baseSpeed", baseSpeed);
        map.put("speed", speed);
        try {
            final String json = OBJECT_MAPPER.writeValueAsString(map);
            final String result = (String) NASHORN_INVOCABLE.invokeFunction("computeAllowedAngleJava", json);
            return Float.parseFloat(result);
        } catch (IOException | NoSuchMethodException | ScriptException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

//    final static private Pattern REGEX = Pattern.compile("(module\\.)?exports\\.(\\w+)");
//    private String handleExports(String content) {
//        Matcher m = REGEX.matcher(content);
//        StringBuffer sb = new StringBuffer();
//        while (m.find()) {
//            m.appendReplacement(sb, String.format("var %s", m.group(2)));
//        }
//        m.appendTail(sb);
//        return sb.toString();
//    }
}

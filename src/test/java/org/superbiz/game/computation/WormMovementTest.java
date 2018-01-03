package org.superbiz.game.computation;

import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WormMovementTest {
    @Test
    public void movementInJs() throws IOException, ScriptException, NoSuchMethodException {
        System.setProperty("nashorn.args", "--language=es6");
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
//        engine.eval(new FileReader("src/main/resources/loader/jvm-npm.js"));
//        engine.eval(new FileReader("src/main/frontend/src/component/wormMovement.js"));

        String content = new String(Files.readAllBytes(Paths.get("src/main/frontend/src/component/wormMovement.js")));
        String replaced = handleExports(content);
        engine.eval(replaced);

        Invocable invocable = (Invocable) engine;

        //Object result = invocable.invokeFunction("moveSnake", Arrays.asList(new Object()), 1.23f, 10.0f, 20.0f);
        Object result = invocable.invokeFunction("moveSnake");
//        //Object result = engine.eval("moveSnake(snakePath, angle, distance, partDistance)");
        //Object result = engine.eval("moveSnake([], 1.23, 10.0, 20.0)");
        System.out.println(result);
    }

    final static private Pattern REGEX = Pattern.compile("(module\\.)?exports\\.(\\w+)");
    private String handleExports(String content) {
        Matcher m = REGEX.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, String.format("var %s", m.group(2)));
        }
        m.appendTail(sb);
        return sb.toString();
    }
}

package org.superbiz.game.computation;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class WormMovementTest {
    @Test
    public void movementInJs() throws FileNotFoundException, ScriptException {
        System.setProperty("nashorn.args", "--language=es6");

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        Object r = engine.eval(new FileReader("/local/sx724/workspace/jboss/testrx/src/main/frontend/src/component/wormMovement.js"));
        System.out.println(r);
    }
}

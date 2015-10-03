import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Cody on 10/3/2015.
 */
public class ScriptInterpreter {

    private static ScriptInterpreter interpreter;
    private static ScriptEngine engine;

    private ScriptInterpreter() {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("nashorn");
    }

    public static ScriptInterpreter getInstance() {
        if (interpreter == null) {
            interpreter = new ScriptInterpreter();
        }
        return interpreter;
    }

    public static void eval(String script, Object[] args) {
        try {
            engine.eval(script);
            Invocable invocable = (Invocable) engine;
            invocable.invokeFunction("fun1", args);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}

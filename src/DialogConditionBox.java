import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 10/2/2015.
 */
public class DialogConditionBox implements DialogBox {

    private String name;
    private IndexedLinkedHashMap<Method, Object[]> conditionMethods;
    private Integer[] conditionMap;
    public int nextBox;

    public DialogConditionBox(String name) {
        conditionMethods = DialogCache.getConditionMethods(name);
        conditionMap = DialogCache.getConditionMap(name);
        nextBox = -1;
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {

    }

    @Override
    public void draw(Graphics2D g2d, Character character) {

    }

    @Override
    public void draw(Graphics2D g2d) {

    }

    @Override
    public void startDialog() {
        for (int i = 0; i < conditionMethods.size(); i++) {
            Method condition = conditionMethods.getIndexedKey(i);
            Object[] args = conditionMethods.get(condition);
            boolean exit = false;
            try {
                if (condition == null) {
                    nextBox = conditionMap[i];
                    exit = true;
                } else if ((boolean)condition.invoke(null, args)) {
                    nextBox = conditionMap[i];
                    exit = true;
                } else {
                    Object obj = condition.invoke(null, args);
                    boolean bool = (boolean) obj;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if (exit) {
                break;
            }
        }
        endDialog();
    }

    @Override
    public void progressDialog() {

    }

    @Override
    public void endDialog() {
        DialogManager.progressDialog();
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setActive(boolean bool) {

    }
}

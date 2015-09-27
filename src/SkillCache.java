import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/27/2015.
 */
public class SkillCache {

    private static final Map<String, Color> skillColors = new HashMap<String, Color>() {
        {
            put("Fire", new Color(242, 48, 22));
            put("Ice", new Color(25, 245, 241));
            put("Earth", new Color(166, 109, 10));
        }
    };


    public static Color getColor(String name) {
        return skillColors.get(name);
    }
}

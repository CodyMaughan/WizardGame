import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/18/2015.
 */
public class DialogCache {

    private static final Map<String, String> interactionDialog = new HashMap<String, String>() {
        {
            put("Item_Berries", "Press SPACE to pick berries.");
            put("Item_Mushroom", "Press SPACE to pick mushroom.");

            put("Equipment_Novice Robes Chest", "Press Space to open the chest.");

            put("Start_Game_Menu_Instructions", "Press ENTER to open the game menu.");
        }
    };

    public static String getInteractionDialog(String name) {
        return interactionDialog.get(name);
    }

}

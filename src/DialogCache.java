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
            put("Speak_Character_Instructions", "Press SPACE to speak to the person.");

            put("Character1", "Have you seen the cave outside of town? \n My mother won't let me go there," +
                    " she says it's too dangerous. \n I wonder what's in there...");
            put("Character2", "Berries are so delicious and scrumptious. I love eating them." +
                    " \n They also can be used to make potions. \n But I prefer to just eat them.");
            put("Character4", "I would like to be a wizard too...");
            put("AlchemistVendor_MageCity", "I've got potions galore! Come take a look!");
        }
    };

    private static final Map<String, String> choiceDialog = new HashMap<String, String>() {
        {
            put("AlchemistVendor_MageCity", "Shop from the alchemy vendor?");
        }
    };

    private static final Map<String, String[]> choices = new HashMap<String, String[]>() {
        {
            put("AlchemistVendor_MageCity", new String[]{"Yes", "No"});
        }
    };

    public static String getInteractionDialog(String name) {
        return interactionDialog.get(name);
    }

    public static String getChoiceDialog(String name) { return choiceDialog.get(name); }

    public static String[] getChoices(String name) { return choices.get(name); }


}

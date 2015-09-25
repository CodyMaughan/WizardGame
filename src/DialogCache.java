import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/18/2015.
 */
public class DialogCache {

    private static final Map<String, String> interactionDialog = new HashMap<String, String>() {
        {
            // Getting Items
            put("Item_Berries", "Press SPACE to pick berries.");
            put("Item_Mushroom", "Press SPACE to pick mushroom.");

            // Opening Chests
            put("Equipment_Novice Robes Chest", "Press Space to open the chest.");

            // Instructions
            put("Start_Game_Menu_Instructions", "Press ENTER to open the game menu.");
            put("Speak_Character_Instructions", "Press SPACE to speak to the person.");

            // Character Dialog
            put("Character1", "Have you seen the cave outside of town? \n My mother won't let me go there," +
                    " she says it's too dangerous. \n I wonder what's in there...");
            put("Character2", "Berries are so delicious and scrumptious. I love eating them." +
                    " \n They also can be used to make potions. \n But I prefer to just eat them.");
            put("Character3", "Hey, get out of the way! Can't you see I'm walking here?");
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

    private static final Map<String, GameEvent[]> choiceEvents = new HashMap<String, GameEvent[]>() {
        {

        }
    };

    private static final Map<String, ArrayList<String>> sequenceTypes = new HashMap<String, ArrayList<String>>() {
        {
            put("AlchemistVendor_MageCity", new ArrayList<String>() {
                {
                    add("ScrollDialogBox");
                    add("ChoiceBox");
                }
            });
        }
    };

    public static String getInteractionDialog(String name) {
        return interactionDialog.get(name);
    }

    public static String getChoiceDialog(String name) { return choiceDialog.get(name); }

    public static String[] getChoices(String name) { return choices.get(name); }


    public static ArrayList<DialogBox> getDialogSequence(String name, Font font, int bufferX, int bufferY, Graphics2D g2d) {
        ArrayList<DialogBox> boxes = new ArrayList<DialogBox>();

        return null;
    }
}

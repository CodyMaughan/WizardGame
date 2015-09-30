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
            put("Character1_0", "Have you seen the cave outside of town? \nMy mother won't let me go there," +
                    " she says it's too dangerous. \nI wonder what's in there...");
            put("Character2_0", "Berries are so delicious and scrumptious. I love eating them." +
                    " \nThey also can be used to make potions. \nBut I prefer to just eat them.");
            put("Character3_0", "Hey, get out of the way! Can't you see I'm walking here?");
            put("Character4_0", "I would like to be a wizard too...");
            put("AlchemistVendor_MageCity_0", "I've got potions galore! Come take a look!");
            put("AlchemistVendor_MageCity_2_Branch0", "Come back again if you need anymore potions!");
            put("AlchemistVendor_MageCity_2_Branch1", "You don't need any potions!? \nAlright then... Come back soon!");
            put("Mage1_MageCity_0", "The wizard academy is older than dirt itself! \nThere are all types of magic to be learned there! ");
            put("Mage2_MageCity_0", "I used to be a traveler like you, \nbut then I took a fireball to the knee...");
            put("Mage3_MageCity_0", "This here is the wizard academy. \nMages from all over this country come here \nso that they can learn all types of arcane magic.");
            put("Mage3_MageCity_2_Branch0", "It's a lot of work to become a mage, \nbut it is worth it in the end.");
            put("Mage3_MageCity_2_Branch1", "Well it's a long road to start without being completely sure about it. \nMaybe you should consider it more before deciding...");
            put("Mage3_MageCity_2_Branch2", "Hey, are you making fun of my profession? \nWhy I oughta...");
            put("Pervert1_MageCity_0", "Mmm... The innkeeper's daughter is so pretty... \nHey, what are you doing snoopin' around kid! \nScram, go on, get out of here!");
            put("PalaceGuard1_MageCity_0", "Hey, no one is allowed into the palace garden without special permission!");
            put("PalaceGuard2_MageCity_0", "Hey, no one is allowed into the palace garden without special permission!");
            put("Necromancer_0", "You fool, I will crush you!");
        }
    };

    private static final Map<String, String> choiceDialog = new HashMap<String, String>() {
        {
            put("AlchemistVendor_MageCity_1", "Shop from the alchemy vendor?");
            put("Mage3_MageCity_1", "Are you here to learn magic too?");
        }
    };

    private static final Map<String, String[]> choices = new HashMap<String, String[]>() {
        {
            put("AlchemistVendor_MageCity_1", new String[]{"Yes", "No"});
            put("Mage3_MageCity_1", new String[]{"Of course!", "I'm not sure...", "No way!"});
        }
    };

    private static final Map<String, String[]> sequenceTypes = new HashMap<String, String[]>() {
        {
            put("Character1", new String[] {"ScrollDialogBox"});
            put("Character2", new String[] {"ScrollDialogBox"});
            put("Character3", new String[] {"ScrollDialogBox"});
            put("Character4", new String[] {"ScrollDialogBox"});
            put("AlchemistVendor_MageCity", new String[] {
                    "ScrollDialogBox",
                    "ChoiceBox",
                    "ScrollDialogBox_ScrollDialogBox"
            });
            put("Mage1_MageCity", new String[] {"ScrollDialogBox"});
            put("Mage2_MageCity", new String[] {"ScrollDialogBox"});
            put("Mage3_MageCity", new String[] {
                    "ScrollDialogBox",
                    "ChoiceBox",
                    "ScrollDialogBox_ScrollDialogBox_ScrollDialogBox"
            });
            put("Pervert1_MageCity", new String[]{"ScrollDialogBox"});
            put("PalaceGuard1_MageCity", new String[]{"ScrollDialogBox"});
            put("PalaceGuard2_MageCity", new String[]{"ScrollDialogBox"});
            put("Necromancer", new String[] {"ScrollDialogBox"});
        }
    };

    public static String getInteractionDialog(String name) {
        return interactionDialog.get(name);
    }

    public static String getChoiceDialog(String name) { return choiceDialog.get(name); }

    public static String[] getChoices(String name) { return choices.get(name); }


    public static ArrayList<DialogBox> getDialogSequence(String name, Font font, int bufferX, int bufferY, Graphics2D g2d) {
        ArrayList<DialogBox> boxes = new ArrayList<DialogBox>();
        String[] types = sequenceTypes.get(name);
        for (int i = 0; i < types.length; i++) {
            String[] split = types[i].split("_");
            if (split.length == 1) {
                switch (split[0]) {
                    case ("ScrollDialogBox"):
                        boxes.add(new ScrollDialogBox(name + "_" + i, font, bufferX, bufferY, g2d, true));
                        break;
                    case ("ChoiceBox"):
                        boxes.add(new ChoiceBox(name + "_" + i, font, bufferX, bufferY, g2d, true));
                        break;
                    case ("TimedDialogBox"):
                        boxes.add(new TimedDialogBox(name + "_" + i, 3000000L, font, bufferX, bufferY, g2d, true));
                        break;
                    case ("InteractionDialogBox"):
                        boxes.add(new InteractionDialogBox(name + "_" + i, font, bufferX, bufferY, g2d, true));
                        break;
                }
            } else {
                for (int j = 0; j < Integer.valueOf(split.length); j++) {
                    switch (split[j]) {
                        case ("ScrollDialogBox"):
                            boxes.add(new ScrollDialogBox(name + "_" + i + "_Branch" + j, font, bufferX, bufferY, g2d, true));
                            break;
                        case ("ChoiceBox"):
                            boxes.add(new ChoiceBox(name + "_" + i + "_Branch" + j, font, bufferX, bufferY, g2d, true));
                            break;
                        case ("TimedDialogBox"):
                            boxes.add(new TimedDialogBox(name + "_" + i + "_Branch" + j, 3000000L, font, bufferX, bufferY, g2d, true));
                            break;
                        case ("InteractionDialogBox"):
                            boxes.add(new InteractionDialogBox(name + "_" + i + "_Branch" + j, font, bufferX, bufferY, g2d, true));
                            break;
                    }
                }
            }
        }
        return boxes;
    }
}

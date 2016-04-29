import java.awt.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

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
            // Mage City
            put("AlchemistVendor_MageCity_0", "I've got potions galore! Come take a look!");
            put("AlchemistVendor_MageCity_2", "Come back again if you need anymore potions!");
            put("AlchemistVendor_MageCity_3", "You don't need any potions!? \nAlright then... Come back soon!");
            put("Innkeeper_MageCity_0", "Welcome to Burner's Inn, the finest inn in the entire town! \nHehe, it's also the only inn in the entire town. \nIt's nice to have no competition.");
            put("InnkeeperDaughter_MageCity_0", "It's just my father and I who run this inn. \nWe mostly get wizards who are traveling here to visit the academy.");
            put("ItemShopKeeper_MageCity_0", "Welcome to Illidan's Items!");
            put("ItemShopKeeper_MageCity_2", "Thank you for your consumerization! \nCome again anytime!");
            put("ItemShopKeeper_MageCity_3", "Come back anytime you need to itemize!");
            put("Mage1_MageCity_0", "The wizard academy is older than dirt itself! \nThere are all types of magic to be learned there! ");
            put("Mage2_MageCity_0", "I used to be a traveler like you, \nbut then I took a fireball to the knee...");
            put("Mage3_MageCity_0", "This here is the wizard academy. \nMages from all over this country come here \nso that they can learn all types of arcane magic.");
            put("Mage3_MageCity_2", "It's a lot of work to become a mage, \nbut it is worth it in the end.");
            put("Mage3_MageCity_3", "Well it's a long road to start without being completely sure about it. \nMaybe you should consider it more before deciding...");
            put("Mage3_MageCity_4", "Hey, are you making fun of my profession? \nWhy I oughta...");
            put("Mage4_MageCity_0", "This town square is a great place to buy supplies! \nThere are potions galore, equipment a-plenty, and, if you are lucky... \nMagicians will sometimes be here, selling powerful magic cards!");
            put("Cleric1_MageCity_0", "I like to sit here on this bench and watch the people come and go. \nThe bench itself is a testament of my frequenting here. \nThe shape of my buttocks is engraven in the wood!");
            put("Annabelle_MageCity_1", "Are you here to join the wizard academy? \nI want to join it sooooo badly! \nBut my father won't let me...");
            put("Annabelle_MageCity_2", "You are part of the Academy now?! \nOh, I'm so angry! My father won't let me enter the academy yet! \nEven though he's a wizard there, he thinks I'm too young! \nI need to prove to him that I know enough about magic.");
            put("Annabelle_MageCity_5", "Well, you can't help me then...");
            put("Annabelle_MageCity_6", "Hmph, I'm not too young! I'm almost 11!");
            put("Annabelle_MageCity_7", "Well " + MainCharacter.characterName + ", we should go talk to my father, Roldan!");
            put("Annabelle_MageCity_8", "What, that's not even a real name! \nYou won't help me, will you...");
            put("Annabelle_MageCity_9", "What, you don't want to help me? \nWhy not? What did I do?");
            put("Annabelle_MageCity_11", "Alright, let's go!");
            put("Annabelle_MageCity_12", "Okay, let me know when you are ready!");
            put("CityGuard1_MageCity_0", "While this city is generally pretty quite, some brawls have broken out in this square. \nWe guard it here now to prevent that from happening.");
            put("CityGuard2_MageCity_0", "It's so nice and peaceful in this ctiy. \nThat's why I love being a guard here!");
            put("CityGuard3_MageCity_0", "Guarding this city is easy! It's full of a bunch of magic geeks!");
            put("CityGuard4_MageCity_0", "Why did the patrol have to send me here? I hate magic! \nDo you practice magic? Cuz if you do, then I hate you too!");
            put("CityGuard5_MageCity_0", "There's this mage from the academy, he's so dreamy... \nI mean uh... move along pipsqueak! No time for riff-raff!");
            put("CityGuard6_MageCity_0", "No messing around in the town square, got it? \nWe take lawbreakers here very seriously, so buy your stuff then move along.");
            put("CityGuard7_MageCity_0", "Welcome to mage city, the town where we guards don't put up with nothing! \nYou better stay on your best behaviour or you'll have to deal with me!");
            put("CityGuard8_MageCity_0", "Welcome to mage city, the most boring town in the world.\nSeriously, it's just filled with a bunch of mages who fiddle with magic. \nMaybe, just once, something interesting could happen here... Sigh...");
            put("Pervert1_MageCity_0", "Mmm... The innkeeper's daughter is so pretty... \nHey, what are you doing snoopin' around kid! \nScram, go on, get out of here!");
            put("PalaceGuard1_MageCity_0", "Hey, no one is allowed into the palace garden without special permission!");
            put("PalaceGuard2_MageCity_0", "Hey, no one is allowed into the palace garden without special permission!");
            put("Necromancer_0", "You fool, I will crush you!");
        }
    };

    private static final Map<String, String> choiceDialog = new HashMap<String, String>() {
        {
            put("AlchemistVendor_MageCity_1", "Shop from the alchemy vendor?");
            put("ItemShopKeeper_MageCity_1", "Would you like to customize your itemization?");
            put("Mage3_MageCity_1", "Are you here to learn magic too?");
            put("Annabelle_MageCity_3", "Will you help me?");
            put("Annabelle_MageCity_4", "Great! I'm Annabelle! What's your name?");
            put("Annabelle_MageCity_10", "Are you ready to go talk to my father?");
        }
    };

    private static final Map<String, String[]> choices = new HashMap<String, String[]>() {
        {
            put("AlchemistVendor_MageCity_1", new String[]{"Yes", "No"});
            put("ItemShopKeeper_MageCity_1", new String[]{"Yes", "No"});
            put("Mage3_MageCity_1", new String[]{"Of course!", "I'm not sure...", "No way!"});
            put("Annabelle_MageCity_3", new String[]{"Sure will!", "I don't know magic though.", "You look too young to me..."});
            put("Annabelle_MageCity_4", new String[]{MainCharacter.characterName, "Jar Jar Binks", "Nevermind..."});
            put("Annabelle_MageCity_10", new String[]{"Let's go!", "I'm not ready yet..."});
        }
    };

    private static final Map<String, String[]> sequenceTypes = new HashMap<String, String[]>() {
        {
            put("Character1", new String[] {"ScrollDialogBox"});
            put("Character2", new String[] {"ScrollDialogBox"});
            put("Character3", new String[] {"ScrollDialogBox"});
            put("Character4", new String[] {"ScrollDialogBox"});
            // Mage City
            put("AlchemistVendor_MageCity", new String[] {
                    "ScrollDialogBox",
                    "ChoiceBox",
                    "ScrollDialogBox", // Yes/Choice 0
                    "ScrollDialogBox"  // No/Choice 1
            });
            put("Innkeeper_MageCity", new String[] {"ScrollDialogBox"});
            put("InnkeeperDaughter_MageCity", new String[] {"ScrollDialogBox"});
            put("ItemShopKeeper_MageCity", new String[] {
                    "ScrollDialogBox",
                    "ChoiceBox",
                    "ScrollDialogBox", // Yes/Choice 0
                    "ScrollDialogBox"  // No/Choice 1
            });
            put("Mage1_MageCity", new String[] {"ScrollDialogBox"});
            put("Mage2_MageCity", new String[] {"ScrollDialogBox"});
            put("Mage3_MageCity", new String[] {
                    "ScrollDialogBox",
                    "ChoiceBox",
                    "ScrollDialogBox", // Of Course/Choice0
                    "ScrollDialogBox", // I don't know/Choice1
                    "ScrollDialogBox"  // No way/Choice2
            });
            put("Mage4_MageCity", new String[]{"ScrollDialogBox"});
            put("Annabelle_MageCity", new String[] {
                    "DialogConditionBox",
                    "ScrollDialogBox",
                    "ScrollDialogBox",
                    "ChoiceBox",
                    "ChoiceBox",
                    "ScrollDialogBox",
                    "ScrollDialogBox",
                    "ScrollDialogBox",
                    "ScrollDialogBox",
                    "ScrollDialogBox",
                    "ChoiceBox",
                    "ScrollDialogBox",
                    "ScrollDialogBox"
            });
            put("Pervert1_MageCity", new String[]{"ScrollDialogBox"});
            put("PalaceGuard1_MageCity", new String[]{"ScrollDialogBox"});
            put("PalaceGuard2_MageCity", new String[]{"ScrollDialogBox"});
            put("CityGuard1_MageCity", new String[]{"ScrollDialogBox"});
            put("CityGuard2_MageCity", new String[]{"ScrollDialogBox"});
            put("CityGuard3_MageCity", new String[]{"ScrollDialogBox"});
            put("CityGuard4_MageCity", new String[]{"ScrollDialogBox"});
            put("CityGuard5_MageCity", new String[]{"ScrollDialogBox"});
            put("CityGuard6_MageCity", new String[]{"ScrollDialogBox"});
            put("CityGuard7_MageCity", new String[]{"ScrollDialogBox"});
            put("CityGuard8_MageCity", new String[]{"ScrollDialogBox"});
            put("Necromancer", new String[] {"ScrollDialogBox"});
        }
    };

    private static final Map<String, Integer[][]> sequencePath = new HashMap<String, Integer[][]>() {
        {
            put("AlchemistVendor_MageCity", new Integer[][]{
                    {1},
                    {2, 3},
                    {null},
                    {null}
            });
            put("ItemShopKeeper_MageCity", new Integer[][]{
                    {1},
                    {2, 3},
                    {null},
                    {null}
            });
            put("Mage3_MageCity", new Integer[][]{
                    {1},
                    {2,3,4},
                    {null},
                    {null},
                    {null}
            });
            put("Annabelle_MageCity", new Integer[][]{
                    {null}, //Dialog Condition Box, will be handled by the various condition statements
                    {null}, //Not able to start the quest (not part of the wizard academy yet)
                    {3},    //Dialog that allows the code to start the quest
                    {4,5,6},
                    {7,8,9},
                    {null},
                    {null},
                    {10},
                    {null},
                    {null},
                    {11,12},
                    {null},
                    {null}
            });
        }
    };

    private static final HashMap<String, IndexedLinkedHashMap<Method, Object[]>> conditionMethods = new HashMap<String, IndexedLinkedHashMap<Method, Object[]>>() {
        {
            put("Annabelle_MageCity_0", new IndexedLinkedHashMap<Method, Object[]>() {
                {
                    try {
                        put(QuestManager.class.getMethod("questIsActive", String.class), new Object[]{"Annabelle's Quest"});
                        put(QuestManager.class.getMethod("questIsComplete", String.class), new Object[]{"Join_Wizard_Academy"});
                        put(null, null);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private static final HashMap<String, Integer[]> conditionMaps = new HashMap<String, Integer[]>() {
        {
            put("Annabelle_MageCity_0", new Integer[] { 10,2,1 });
        }
    };

    public static IndexedLinkedHashMap<Method, Object[]> getConditionMethods(String name) {
        return conditionMethods.get(name);
    }

    public static Integer[] getConditionMap(String name) {
        return conditionMaps.get(name);
    }

    public static String getInteractionDialog(String name) {
        return interactionDialog.get(name);
    }

    public static String getChoiceDialog(String name) { return choiceDialog.get(name); }

    public static String[] getChoices(String name) { return choices.get(name); }

    public static ArrayList<DialogBox> getDialogBoxes(String name, Font font, int bufferX, int bufferY, Graphics2D g2d) {
        ArrayList<DialogBox> boxes = new ArrayList<>();
        String[] types = sequenceTypes.get(name);
        for (int i = 0; i < types.length; i++) {
            switch (types[i]) {
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
                case ("DialogConditionBox"):
                    boxes.add(new DialogConditionBox(name + "_" + i));
                    break;
            }
        }
        return boxes;
    }

    public static Integer[][] getDialogSequence(String name) {
        return sequencePath.get(name);
    }
}

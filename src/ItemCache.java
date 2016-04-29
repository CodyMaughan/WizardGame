import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/24/2015.
 */
public class ItemCache {

    public static final Map<String, String> explanations = new HashMap<String, String>() {
        {
            put("Berries", "Tasty berries that grow on bushes in the wild. They restore 15 health eaten raw, or can be used to create potions.");
            put("Mushroom", "Okay tasting mushrooms that can be found growing in the wild. They restore 10 mana eaten raw, or can be used to create potions.");
            put("Weak Potion", "A weak potion brewed from berries that restores up to 25 health.");
            put("Mild Potion", "A mild potion brewed from various herbs that restores up to 40 health.");
            put("Weak Ether", "A weak ether brewed from mushrooms that restores up to 15 mana.");
            put("Mild Ether", "A mild ether brewed from various herbs that restores up to 25 mana.");
            put("Novice Robes", "Robes for a novice mage. They provide minimal defense, but a slight increase to agility and wisdom.");
            put("Apprentice Robes", "Robes for an apprentice mage. They provide minimal defense, but a small increase to agility, wisdom, and intelligence.");
            put("Fire Novice Gloves", "Gloves for a novice mage. They provide a slight increase to the fire skill of the mage wearing them.");
            put("Ice Novice Gloves", "Gloves for a novice mage. They provide a slight increase to the ice skill of the mage wearing them.");
            put("Earth Novice Gloves", "Gloves for a novice mage. They provide a slight increase to the earth skill of the mage wearing them.");
            put("Fire Novice Sandals", "Sandals for a novice mage. They provide a slight increase to the fire skill of the mage wearing them.");
            put("Ice Novice Sandals", "Sandals for a novice mage. They provide a slight increase to the ice skill of the mage wearing them.");
            put("Earth Novice Sandals", "Sandals for a novice mage. They provide a slight increase to the earth skill of the mage wearing them.");
        }
    };

    public static final Map<String, Item> items = new HashMap<String, Item>() {
        {
            put("Berries", new Item("Berries", 20, "Ingredient, Food", new ItemEffect(new String[] {"Heal"}, new int[] {15})));
            put("Mushroom", new Item("Mushroom", 30, "Ingredient, Food", new ItemEffect(new String[] {"Restore Mana"}, new int[]  {10})));
            put("Weak Potion", new Item("Weak Potion", 40, "Potion", new ItemEffect(new String[] {"Heal"}, new int[] {25})));
            put("Mild Potion", new Item("Mild Potion", 60, "Potion", new ItemEffect(new String[] {"Heal"}, new int[] {40})));
            put("Weak Ether", new Item("Weak Ether", 50, "Potion", new ItemEffect(new String[] {"Restore Mana"}, new int[] {15})));
            put("Mild Ether", new Item("Mild Ether", 80, "Potion", new ItemEffect(new String[] {"Restore Mana"}, new int[] {25})));
        }
    };

    public static final Map<String, Equipment> equipment = new HashMap<String, Equipment>() {
        {
            put("Novice Robes", new Equipment("Novice Robes", 100, "Robe"));
            put("Novice Robes Chest", new Equipment("Novice Robes", 100, "Robe"));
            put("Apprentice Robes", new Equipment("Apprentice Robes", 250, "Robe"));
            put("Fire Novice Gloves", new Equipment("Fire Novice Gloves", 180, "Gloves"));
            put("Ice Novice Gloves", new Equipment("Ice Novice Gloves", 180, "Gloves"));
            put("Earth Novice Gloves", new Equipment("Earth Novice Gloves", 180, "Gloves"));
            put("Fire Novice Sandals", new Equipment("Fire Novice Sandals", 160, "Shoes"));
            put("Ice Novice Sandals", new Equipment("Ice Novice Sandals", 160, "Shoes"));
            put("Earth Novice Sandals", new Equipment("Earth Novice Sandals", 160, "Shoes"));
        }
    };

    public static Item getItem(String name) {
        return items.get(name);
    }

    public static Equipment getEquipment(String name) {
        return equipment.get(name);
    }

    public static Vendable getVendable(String name) {
        if (items.get(name) != null) {
            return items.get(name);
        } else if (equipment.get(name) != null) {
            return equipment.get(name);
        } else {
            return null;
        }
    }

    public static String getExplanation(String name) {
        return explanations.get(name);
    }
}

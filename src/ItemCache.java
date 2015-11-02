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
            put("Novice Robes", "Clothing for novice mage. These robes provide minimal defense, but a slight increase to agility and wisdom.");
        }
    };

    public static final Map<String, Item> items = new HashMap<String, Item>() {
        {
            put("Berries", new Item("Berries", 20, "Ingredient, Food", new ItemEffect(new String[] {"Heal"}, new int[] {15})));
            put("Mushroom", new Item("Mushroom", 30, "Ingredient, Food", new ItemEffect(new String[] {"Restore Mana"}, new int[]  {10})));
        }
    };

    public static final Map<String, Equipment> equipment = new HashMap<String, Equipment>() {
        {
            put("Novice Robes", new Equipment("Novice Robes", 100, "Clothing"));
            put("Novice Robes Chest", new Equipment("Novice Robes", 100, "Clothing"));
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

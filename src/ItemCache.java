import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/24/2015.
 */
public class ItemCache {

    public static final Map<String, Item> items = new HashMap<String, Item>() {
        {
            put("Berries", new Item("Berries", 20, "Ingredient, Food"));
            put("Mushroom", new Item("Mushroom", 30, "Ingredient, Food"));
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

}

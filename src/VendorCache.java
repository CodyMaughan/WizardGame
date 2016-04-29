import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/24/2015.
 */
public class VendorCache {

    private static final HashMap<String, IndexedTreeMap<String, Vendable>> vendableList = new HashMap<String, IndexedTreeMap<String, Vendable>>() {
        {
            put("AlchemistVendor_MageCity", new IndexedTreeMap<String, Vendable>() {
                {
                    put("Berries", ItemCache.getItem("Berries"));
                    put("Mushroom", ItemCache.getItem("Mushroom"));
                    put("Weak Potion", ItemCache.getItem("Weak Potion"));
                    put("Mild Potion", ItemCache.getItem("Mild Potion"));
                    put("Weak Ether", ItemCache.getItem("Weak Ether"));
                    put("Mild Ether", ItemCache.getItem("Mild Ether"));
                }
            });
            put("ItemShopKeeper_MageCity", new IndexedTreeMap<String, Vendable>() {
                {
                    put("Novice Robes", ItemCache.getEquipment("Novice Robes"));
                    put("Apprentice Robes", ItemCache.getEquipment("Apprentice Robes"));
                    put("Fire Novice Gloves", ItemCache.getEquipment("Fire Novice Gloves"));
                    put("Ice Novice Gloves", ItemCache.getEquipment("Ice Novice Gloves"));
                    put("Earth Novice Gloves", ItemCache.getEquipment("Earth Novice Gloves"));
                    put("Fire Novice Sandals", ItemCache.getEquipment("Fire Novice Sandals"));
                    put("Ice Novice Sandals", ItemCache.getEquipment("Ice Novice Sandals"));
                    put("Earth Novice Sandals", ItemCache.getEquipment("Earth Novice Sandals"));
                }
            });
        }
    };

    private static final HashMap<String, HashMap<String, Integer>> vendableCountList = new HashMap<String, HashMap<String, Integer>>() {
        {
            put("AlchemistVendor_MageCity", new HashMap<String, Integer>() {
                {
                    put("Berries", 4);
                    put("Mushroom", 3);
                    put("Weak Potion", 3);
                    put("Mild Potion", 2);
                    put("Weak Ether", 3);
                    put("Mild Ether", 2);
                }
            });
            put("ItemShopKeeper_MageCity", new HashMap<String, Integer>() {
                {
                    put("Novice Robes", 2);
                    put("Apprentice Robes", 1);
                    put("Fire Novice Gloves", 1);
                    put("Ice Novice Gloves", 2);
                    put("Earth Novice Gloves", 2);
                    put("Fire Novice Sandals", 2);
                    put("Ice Novice Sandals", 1);
                    put("Earth Novice Sandals", 1);
                }
            });
        }
    };

    private static final HashMap<String, Integer> vendorMoney = new HashMap<String, Integer>() {
        {
            put("AlchemistVendor_MageCity", 300);
            put("ItemShopKeeper_MageCity", 500);
        }
    };

    public static IndexedTreeMap<String, Vendable> getVendables(String name) {
        return vendableList.get(name);
    }

    public static HashMap<String, Integer> getVendableCount(String name) {
        return vendableCountList.get(name);
    }

    public static int getMoney(String name) {
        return vendorMoney.get(name);
    }

    public static void putVendables(String id, IndexedTreeMap<String, Vendable> vendables) {
        vendableList.put(id, vendables);
    }

    public static void putVendableCount(String id, HashMap<String, Integer> vendableCount) {
        vendableCountList.put(id, vendableCount);
    }

    public static void putVendorMoney(String id, int money) {
        vendorMoney.put(id, money);
    }
}

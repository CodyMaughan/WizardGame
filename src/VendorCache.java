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
                }
            });
        }
    };

    private static final HashMap<String, Integer> vendorMoney = new HashMap<String, Integer>() {
        {
            put("AlchemistVendor_MageCity", 300);
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

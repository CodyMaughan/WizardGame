import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/24/2015.
 */
public class VendorCache {

    private static final HashMap<String, HashMap<String, Vendable>> vendableList = new HashMap<String, HashMap<String, Vendable>>() {
        {
            put("AlchemistVendor_MageCity", new HashMap<String, Vendable>() {
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

    public static HashMap<String, Vendable> getVendables(String name) {
        return vendableList.get(name);
    }

    public static HashMap<String, Integer> getVendableCount(String name) {
        return vendableCountList.get(name);
    }
}

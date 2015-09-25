import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/24/2015.
 */
public class VendorCache {

    private static final Map<String, Vendable[]> vendableList = new HashMap<String, Vendable[]>() {
        {
            put("AlchemistVendor_MageCity", new Vendable[]{
                    ItemCache.getItem("Berries"),
                    ItemCache.getItem("Mushroom")
            });
        }
    };

    public static Vendable[] getVendables(String name) {
        return vendableList.get(name);
    }
}

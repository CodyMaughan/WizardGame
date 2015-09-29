import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/24/2015.
 */
public class EventCache {

    private static final Map<String, GameEvent[]> events = new HashMap<String, GameEvent[]>() {
        {
            put("AlchemistVendor_MageCity_1", new GameEvent[]{
                    new VendorState("AlchemistVendor_MageCity", "Alchemist Vendor", VendorCache.getVendables("AlchemistVendor_MageCity"),
                            VendorCache.getVendableCount("AlchemistVendor_MageCity"), VendorCache.getMoney("AlchemistVendor_MageCity"), "Ingredients, Potions"),
                    null
            });
            put("Mage3_MageCity_1", new GameEvent[]{
                   null, null, null
            });
        }
    };

    public static GameEvent[] getEvents(String name){
        return events.get(name);
    }

}

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
            put("Annabelle_MageCity_3", new GameEvent[] {
                    null, null, null
            });
            put("Annabelle_MageCity_4", new GameEvent[] {
                    new Quest("Annabelle's Quest"),
                    null, null
            });
            put("Annabelle_MageCity_10", new GameEvent[] {
                    null, null
            });
        }
    };

    public static GameEvent[] getEvents(String name){
        return events.get(name);
    }

}

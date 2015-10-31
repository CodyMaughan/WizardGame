import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 10/30/2015.
 */
public class CreatureCache {

    private static Map<String, CreatureInfoTest2> creatures = new HashMap<String, CreatureInfoTest2>() {
        {
            put("GiantSpider1", new CreatureInfoTest2("GiantSpider1", "/resources/images/Creatures/LPC_Spiders/spider01_battle.png", "", 64, 64, 4, 15, 15, 5, 5));
            put("GiantSpider2", new CreatureInfoTest2("GiantSpider2", "/resources/images/Creatures/LPC_Spiders/spider02_battle.png", "", 64, 64, 4, 20, 20, 5, 5));
        }
    };

    public static CreatureInfoTest2 getCreatureInfo(String name) {
        return creatures.get(name);
    }

}

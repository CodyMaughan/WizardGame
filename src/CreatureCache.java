import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 10/30/2015.
 */
public class CreatureCache {

    private static Map<String, CreatureInfo> creatures = new HashMap<String, CreatureInfo>() {
        {
            put("Giant Black Spider", new CreatureInfo("Giant Black Spider", "/resources/images/Creatures/LPC_Spiders/spider01_battle.png", " <Moves>Spider Bite,Shoot Web<Moves>", 64, 64, 4, 15, 15, 5, 5));
            put("Giant Navy Spider", new CreatureInfo("Giant Navy Spider", "/resources/images/Creatures/LPC_Spiders/spider02_battle.png", " <Moves>Spider Bite,Shoot Web<Moves>", 64, 64, 4, 20, 20, 5, 5));
        }
    };

    public static CreatureInfo getCreatureInfo(String name) {
        return creatures.get(name);
    }

}

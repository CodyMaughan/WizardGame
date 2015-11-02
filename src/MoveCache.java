import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 10/30/2015.
 */
public class MoveCache {

    private static Map<String, BattleMove> moves = new HashMap<String, BattleMove>() {
        {
            put("Spider Bite", new BattleMove("Spider Bite", new BattleMoveEffect(new String[] {"Damage_Physical"}, new int[] {5}, new int[] {100})));
            put("Shoot Web", new BattleMove("Shoot Web", new BattleMoveEffect(new String[] {"Paralyze_Creature"}, new int[] {1}, new int[] {40})));
        }
    };

    public static BattleMove getMove(String name) {
        return moves.get(name);
    }

}

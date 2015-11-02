import java.awt.image.BufferedImage;

/**
 * Created by Cody on 10/30/2015.
 */
public abstract class BattleEnemy {

    public String name;
    public int health;
    public int maxHealth;
    public int mana;
    public int maxMana;
    public int level;
    public int experience;
    public IndexedLinkedHashMap<String, Integer> stats;
    public IndexedLinkedHashMap<String, Integer> skills;
    public int paralyzeCount;

    public abstract BufferedImage getBattleImage();

    public abstract String getName();

    public boolean isParalyzed() {
        if (paralyzeCount > 0) {
            return true;
        } else {
            paralyzeCount = 0;
            return false;
        }
    }
}

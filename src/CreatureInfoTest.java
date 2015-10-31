/**
 * Created by Cody on 10/30/2015.
 */
public class CreatureInfoTest {

    private String name;
    private String path;
    private String script;
    private int width;
    private int height;
    private int animationFrames;
    private int health;
    private int maxHealth;
    private int mana;
    private int maxMana;
    private int test;

    public CreatureInfoTest(String name, String path, String script, int width, int height, int animationFrames,
                            int health, int maxHealth, int mana, int maxMana) {
        this.name = name;
        this.path = path;
        this.script = script;
        this.width = width;
        this.height = height;
        this.animationFrames = animationFrames;
        this.health = health;
        this.maxHealth = maxHealth;
        this.mana = mana;
        this.maxMana = maxMana;
    }

    public String getPath() {
        return path;
    }

    public String getScript() {
        return script;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getAnimationFrames() { return animationFrames; }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }
}

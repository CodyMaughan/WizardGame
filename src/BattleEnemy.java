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

    public abstract BufferedImage getBattleImage();

    public abstract String getName();
}

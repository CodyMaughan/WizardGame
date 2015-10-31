import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 10/30/2015.
 */
public class Creature extends BattleEnemy {

    public int creatureWidth;
    public int creatureHeight;
    public BufferedImage image;
    private CreatureInfoTest info;
    private int imageWidth;
    private int imageHeight;
    public int direction; // Down = 0, Left = 1, Right = 2, Up = 3
    public IndexedTreeMap<String, Item> items;
    public Map<String, Integer> itemCount;
    public IndexedTreeMap<String, Equipment> equipment;
    public Map<String, Integer> equipmentCount;
    public int level;
    public int experience;
    public IndexedLinkedHashMap<String, Integer> stats;
    public IndexedLinkedHashMap<String, Integer> skills;
    public int maxAnimationFrames;

    public Creature(String name, int posX, int posY, Framework framework) {
        this.name = name;
        info = CreatureCache.getCreatureInfo(name);
        try {
            image = ImageIO.read(this.getClass().getResource(info.getPath()));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        this.creatureWidth = info.getWidth();
        this.creatureHeight = info.getHeight();
        maxAnimationFrames = info.getAnimationFrames();
        items = new IndexedTreeMap<>();
        equipment = new IndexedTreeMap<>();
        stats = new IndexedLinkedHashMap<>();
        stats.put("Wisdom", 3);
        stats.put("Strength", 3);
        stats.put("Intelligence", 3);
        stats.put("Defense", 3);
        stats.put("Willpower", 3);
        stats.put("Agility", 3);
        stats.put("Luck", 3);
        skills = new IndexedLinkedHashMap<>();
        skills.put("Fire", 1);
        skills.put("Ice", 1);
        skills.put("Earth", 1);
        level = 1;
        experience = 0;
        health = info.getHealth();
        maxHealth = info.getMaxHealth();
        mana = info.getMana();
        maxMana = info.getMaxMana();
    }

    public String getName() {
        return name;
    }

    public BufferedImage getBattleImage() {
        BufferedImage temp = new BufferedImage(imageWidth/maxAnimationFrames, imageHeight/4, image.getType());
        Graphics2D gr = temp.createGraphics();
        gr.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), 0, 0, temp.getWidth(), temp.getHeight(), null);
        gr.dispose();
        return temp;
    }

}

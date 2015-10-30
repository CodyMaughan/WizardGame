import sun.plugin.dom.css.Rect;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 * Created by Cody on 10/30/2015.
 */
public class CreatureSpawnBox {

    private String name;
    private Rectangle box;
    private LinkedHashMap<String, Double> creatureProbability;

    public CreatureSpawnBox(String name, Rectangle box, LinkedHashMap<String, Double> creatureProbability) {
        this.name = name;
        this.box = box;
        this.creatureProbability = creatureProbability;
    }

    public Rectangle getRect() {
        return box;
    }

    public String spawnCreature() {
        Random rand = new Random();
        double t = rand.nextDouble();
        double d = 0;
        for (String creature: creatureProbability.keySet()) {
            if (creatureProbability.get(creature) + d >= t) {
                return creature;
            } else {
                d += creatureProbability.get(creature);
            }
        }
        return null;
    }
}

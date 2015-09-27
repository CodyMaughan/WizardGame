import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/13/2015.
 */
public class MainCharacter {

    public static String characterName;
    public static int x;
    public static int y;
    public static int vX;
    public static int vY;
    public static int characterWidth;
    public static int characterHeight;
    public static Rectangle collisionBox;
    public static BufferedImage image;
    private static int imageWidth;
    private static int imageHeight;
    public static int direction; // Down = 0, Left = 1, Right = 2, Up = 3
    private static int animationFrame;
    private static int maxAnimationFrames;
    private static long walkingTimer;
    public static int money;
    public static IndexedTreeMap<String, Item> items;
    public static Map<String, Integer> itemCount;
    public static IndexedTreeMap<String, Equipment> equipment;
    public static Map<String, Integer> equipmentCount;
    public static IndexedTreeMap<String, Vendable> vendables;
    public static Map<String, Integer> vendableCount;
    public static int level;
    public static int experience;
    public static IndexedLinkedHashMap<String, Integer> stats;
    public static IndexedLinkedHashMap<String, Integer> skills;
    private static String travelState;
    private static boolean canSwim;
    private static boolean stop;
    private static boolean talking;

    private final long animationTime = 80000L;
    private final int moveSpeed = 8;

    public MainCharacter(String name, BufferedImage image, int posX, int posY, int characterWidth, int characterHeight){

        this.characterName = name;
        this.image = image;
        this.x = posX;
        this.y = posY;
        this.characterWidth = characterWidth;
        this.characterHeight = characterHeight;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        animationFrame = 0;
        direction = 0;
        maxAnimationFrames = imageWidth/characterWidth;
        walkingTimer = 0;
        collisionBox = new Rectangle(posX + characterWidth/6, posY + characterHeight/2, 2*characterWidth/3, characterHeight/2);
        vX = 0;
        vY = 0;
        items = new IndexedTreeMap<>();
        itemCount = new HashMap<>();
        equipment = new IndexedTreeMap<>();
        equipmentCount = new HashMap<>();
        vendables = new IndexedTreeMap<>();
        vendableCount = new HashMap<>();
        stats = new IndexedLinkedHashMap<>();
        stats.put("Wisdom", 3);
        stats.put("Intelligence", 3);
        stats.put("Defense", 3);
        stats.put("Willpower", 3);
        stats.put("Agility", 3);
        stats.put("Luck", 3);
        skills = new IndexedLinkedHashMap<>();
        skills.put("Fire", 1);
        skills.put("Ice", 1);
        skills.put("Earth", 1);
        money = 0;
        level = 1;
        experience = 0;
        canSwim = false;
        stop = false;
        travelState = "Walk";
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, x, y, x + characterWidth, y + characterHeight, characterWidth*animationFrame,
                characterHeight*direction, (animationFrame + 1)*characterWidth, (direction + 1)*characterHeight, null);
        g2d.draw(collisionBox);
    }

    public void update(float elapsedTime, boolean[][] keyboardstate) {
        vX = 0;
        vY = 0;
        // Determine The Direction to Move the Character base on KeyboardState
        // The order here is important, as it prioritizes the Up/Down Character animation over Right/Left
        if (!stop) {
            if (keyboardstate[KeyEvent.VK_A][0]) {// Move Left
                vX -= 1;
                direction = 1;
            }
            if (keyboardstate[KeyEvent.VK_D][0]) { // Move Right
                vX += 1;
                direction = 2;
            }
            if (keyboardstate[KeyEvent.VK_W][0]) { // Move Up
                vY -= 1;
                direction = 3;
            }
            if (keyboardstate[KeyEvent.VK_S][0]) { // Move Down
                vY += 1;
                direction = 0;
            }
        }
        // Make the velocity vector a unit vector and then multiply by the moveSpeed
        if (vX != 0 || vY != 0) {
            if (Math.abs(vX) == 1 && Math.abs(vY) == 1) {
                vX = vX*moveSpeed*7/10;
                vY = vY*moveSpeed*7/10;
            } else {
                vX = vX*moveSpeed;
                vY = vY*moveSpeed;
            }
            walkingTimer += elapsedTime;
            if (walkingTimer > animationTime) { // If enough time has passed to elicit a new animationFrame
                animationFrame += 1;             // Then we move to the next frame.
                walkingTimer = 0;                // Reset the walkingTimer for the next animationFrame
                if (animationFrame >= maxAnimationFrames) { // Loop back to the first frame if we are already at the last frame
                    animationFrame = 0;
                }
            }
        } else if (vX == 0 && vY == 0) { // If the character isn't moving then change the animation frame to 0
            animationFrame = 0;          // And set the walkingTimer to 0;
            walkingTimer = 0;
        }
        // Move the character with the velocity vector
        translate(vX, vY);
    }

    public static void translate(int dx, int dy) {
        x += dx;
        y += dy;
        collisionBox.translate(dx, dy);
    }

    public static void setPosition(int posX, int posY) {
        x = posX;
        y = posY;
        collisionBox.setLocation(x + characterWidth / 4, y + characterHeight / 2);
    }

    public static void addItem(String name, Item item){
        if (items.containsKey(name)) {
            itemCount.put(name, itemCount.get(name) + 1);
            if (!item.getVendableType().equals("None")) {
                vendableCount.put(name, vendableCount.get(name) + 1);
            }
        } else {
            items.put(name, item);
            itemCount.put(name, 1);
            if (!item.getVendableType().equals("None")) {
                vendables.put(name, item);
                vendableCount.put(name, 1);
            }
        }
    }

    public static void useItem(String name) {
        items.get(name).use();
    }

    public static void dropItem(String name) {
        itemCount.put(name, itemCount.get(name) - 1);
        if (!items.get(name).getVendableType().equals("None")) {
            vendableCount.put(name, itemCount.get(name));
        }
        if (itemCount.get(name) <= 0) {
            items.remove(name);
            vendables.remove(name);
        }
    }

    public static void sellItem(String name, int price) {
        money += price;
        itemCount.put(name, itemCount.get(name) - 1);
        vendableCount.put(name, itemCount.get(name));
        if (itemCount.get(name) <= 0) {
            items.remove(name);
            vendables.remove(name);
        }
    }

    public static void buyItem(String name, int price) {
        money -= price;
        addItem(name, ItemCache.getItem(name));
    }

    public static void addEquipment(String name, Equipment object) {
        name = name.replace(" Chest", "");
        if (equipment.containsKey(name)) {
            equipmentCount.put(name, equipmentCount.get(name) + 1);
            if (!object.getVendableType().equals("None")) {
                vendableCount.put(name, vendableCount.get(name) + 1);
            }
        } else {
            equipment.put(name, object);
            equipmentCount.put(name, 1);
            if (!object.getVendableType().equals("None")) {
                vendables.put(name, object);
                vendableCount.put(name, 1);
            }
        }
    }

    public static boolean isStop() {return stop; }

    public static void setStop(boolean bool) {
        stop = bool; }

    public static boolean canSwim() {
        return canSwim;
    }

    public void changeTravelMethod(String method) {
        this.travelState = method;
    }

    public static boolean isTalking() {
        return talking;
    }

    public static void setTalking(boolean bool) {
        talking = bool;
    }
}

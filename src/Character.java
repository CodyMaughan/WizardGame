import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/19/2015.
 */
public class Character extends BattleEnemy {

    public int x;
    public int y;
    public int vX;
    public int vY;
    public int characterWidth;
    public int characterHeight;
    public Rectangle collisionBox;
    public BufferedImage image;
    private CharacterInfo info;
    private int imageWidth;
    private int imageHeight;
    public int direction; // Down = 0, Left = 1, Right = 2, Up = 3
    private int animationFrame;
    private int maxAnimationFrames;
    private long walkingTimer;
    public IndexedTreeMap<String, Item> items;
    public Map<String, Integer> itemCount;
    public IndexedTreeMap<String, Equipment> equipment;
    public Map<String, Integer> equipmentCount;
    public IndexedTreeMap<String, Vendable> vendables;
    public Map<String, Integer> vendableCount;
    public int level;
    public int experience;
    public IndexedLinkedHashMap<String, Integer> stats;
    public IndexedLinkedHashMap<String, Integer> skills;
    public int money;
    private static String travelState;
    private boolean canSwim;

    private String pathType;
    private Rectangle pathRect;
    private Rectangle interactionBox;
    private boolean drawn;
    private boolean stop;
    private long randomTimer;
    private DialogSequenceBox dialogBox;

    private final long animationTime = 80000L;
    private final long randomTime = 400000;
    private final int moveSpeed = 4;

    public Character(String name, int posX, int posY, Framework framework){
        this.name = name;
        info = CharacterCache.getCharacterInfo(name);
        try {
            image = ImageIO.read(this.getClass().getResource(info.getPath()));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.pathType = "None";
        this.pathRect = null;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        this.characterWidth = info.getWidth();
        this.characterHeight = info.getHeight();
        this.x = posX - characterWidth/4; // Centers the characters collisionBox in the character spawn point
        this.y = posY - characterHeight/2;
        animationFrame = 0;
        direction = 0;
        maxAnimationFrames = imageWidth/characterWidth;
        walkingTimer = 0;
        collisionBox = new Rectangle(x + characterWidth/6, y + characterHeight/2, 2*characterWidth/3, characterHeight/2);
        interactionBox = new Rectangle (x - characterWidth/6, y + characterHeight/4, 4*characterWidth/3, characterHeight);
        dialogBox = new DialogSequenceBox(name, new Font("Arial", Font.PLAIN, 11), 5, 5, (Graphics2D)framework.getGraphics());
        DialogManager.addDialogBox(name, dialogBox);
        vX = 0;
        vY = 0;
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
        money = 0;
        level = 1;
        experience = 0;
        health = 30;
        maxHealth = 30;
        mana = 20;
        maxMana = 20;
        canSwim = false;
        stop = false;
        travelState = "Walk";
        randomTimer = 0;
        if (info.getScript().contains("increaseInteractionBoxHeight")) {
            interactionBox.height += characterHeight/2;
        }
    }

    public void draw(Graphics2D g2d, int xOffset, int yOffset) {
        translate(-xOffset, -yOffset);
        g2d.drawImage(image, x, y, x + characterWidth, y + characterHeight, characterWidth * animationFrame,
                characterHeight * direction, (animationFrame + 1) * characterWidth, (direction + 1) * characterHeight, null);
        g2d.setColor(Color.WHITE);
        g2d.draw(collisionBox);
        g2d.setColor(Color.BLUE);
        g2d.draw(interactionBox);
        g2d.setColor(Color.RED);
        if (pathRect != null) {
            pathRect.translate(-xOffset, -yOffset);
            g2d.draw(pathRect);
            pathRect.translate(xOffset, yOffset);
        }
        if (dialogBox.isActive()) {
            dialogBox.draw(g2d, this);
        }
        translate(xOffset, yOffset);
    }

    public void update(float elapsedTime, boolean[][] keyboardstate) {
        // Determine The Direction to Move the Character
        // The order here is important, as it prioritizes the Up/Down Character animation over Right/Left
        if (dialogBox.isActive()) {
            dialogBox.update(elapsedTime, keyboardstate);
        }
        if (!stop) {
            // Normalize the velocity vector
            if (vX < 0) {
                vX = -1;
            } else if (vX > 0) {
                vX = 1;
            }
            if (vY < 0) {
                vY = -1;
            } else if (vY > 0) {
                vY = 1;
            }
            // Determine if the current path needs to be changed
            if (pathType.equals("FaceDown")) {
                direction = 0;
            } else if (pathType.equals("FaceLeft")) {
                direction = 1;
            } else if (pathType.equals("FaceRight")) {
                direction = 2;
            } else if (pathType.equals("FaceUp")) {
                direction = 3;
            } else if (pathType.equals("Random")) {
                randomTimer += elapsedTime;
                if (randomTimer > randomTime) {
                    randomTimer = 0;
                    Random rand = new Random();
                    int r = rand.nextInt(8);
                    int distance = 0;
                    switch (r) {
                        case (0):
                            direction = 0;
                            vX = 0;
                            vY = 1;
                            break;
                        case (1):
                            direction = 1;
                            vX = -1;
                            vY = 0;
                            break;
                        case (2):
                            direction = 2;
                            vX = 1;
                            vY = 0;
                            break;
                        case (3):
                            direction = 3;
                            vX = 0;
                            vY = -1;
                            break;
                        case (4):
                            vX = 0;
                            vY = 0;
                            break;
                        case (5):
                            vX = 0;
                            vY = 0;
                            break;
                        case (6):
                            vX = 0;
                            vY = 0;
                            break;
                        case (7):
                            vX = 0;
                            vY = 0;
                            break;
                    }
                }
                int distance = 0;
                switch (direction) {
                    case (0):
                        if (collisionBox.y + collisionBox.height >= pathRect.y + pathRect.height) {
                            distance = (collisionBox.y + collisionBox.height) - (pathRect.y + pathRect.height);
                            translate(0, -distance);
                            vX = 0;
                            vY = 0;
                        }
                        break;
                    case (1):
                        if (collisionBox.x <= pathRect.x) {
                            distance = pathRect.x - collisionBox.x;
                            translate(distance, 0);
                            vX = 0;
                            vY = 0;
                        }
                        break;
                    case (2):
                        if (collisionBox.x + collisionBox.width >= pathRect.x + pathRect.width) {
                            distance = (collisionBox.x + collisionBox.width) - (pathRect.x + pathRect.width);
                            translate(-distance, 0);
                            vX = 0;
                            vY = 0;
                        }
                        break;
                    case (3):
                        if (collisionBox.y <= pathRect.y) {
                            distance = pathRect.y - collisionBox.y;
                            translate(0, distance);
                            vX = 0;
                            vY = 0;
                        }
                        break;
                }
            } else if (pathType.equals("Box_Edge_Clockwise")) {
                if (collisionBox.x < pathRect.x && direction == 1) {
                    vX = 0;
                    vY = -1;
                    direction = 3;
                } else if (collisionBox.y <= pathRect.y && direction == 3) {
                    vX = 1;
                    vY = 0;
                    direction = 2;
                } else if (collisionBox.x + collisionBox.width >= pathRect.x + pathRect.width && direction == 2) {
                    vX = 0;
                    vY = 1;
                    direction = 0;
                } else if(collisionBox.y + collisionBox.height >= pathRect.y + pathRect.height && direction == 0) {
                    vX = -1;
                    vY = 0;
                    direction = 1;
                } else {
                    if (direction == 0) {
                        vX = 0;
                        vY = 1;
                    } else if (direction == 1) {
                        vX = -1;
                        vY = 0;
                    } else if (direction == 2) {
                        vX = 1;
                        vY = 0;
                    } else if (direction == 3) {
                        vX = 0;
                        vY = -1;
                    }
                }
            } else if (pathType.equals("Box_Edge_CounterClockwise")) {
                if (collisionBox.x <= pathRect.x && direction == 1) {
                    vX = 0;
                    vY = 1;
                    direction = 0;
                } else if (collisionBox.y <= pathRect.y && direction == 3) {
                    vX = -1;
                    vY = 0;
                    direction = 1;
                } else if (collisionBox.x + collisionBox.width >= pathRect.x + pathRect.width && direction == 2) {
                    vX = 0;
                    vY = -1;
                    direction = 3;
                } else if(collisionBox.y + collisionBox.height >= pathRect.y + pathRect.height && direction == 0) {
                    vX = 1;
                    vY = 0;
                    direction = 2;
                } else {
                    if (direction == 0) {
                        vX = 0;
                        vY = 1;
                    } else if (direction == 1) {
                        vX = -1;
                        vY = 0;
                    } else if (direction == 2) {
                        vX = 1;
                        vY = 0;
                    } else if (direction == 3) {
                        vX = 0;
                        vY = -1;
                    }
                }
            } else if (pathType.equals("Box_FillX")) {

            } else if (pathType.equals("Box_FillY")) {

            } else if (pathType.equals("LineX")) {
                if (collisionBox.x <= pathRect.x) {
                    vX = 1;
                    direction = 2;
                } else if(collisionBox.x + collisionBox.width >= pathRect.x + pathRect.width) {
                    vX = -1;
                    direction = 1;
                } else {
                    if (direction == 2) {
                        vX = 1;
                    } else if (direction == 1) {
                        vX = -1;
                    } else if (direction == 0 || direction == 3) {
                        Random rand = new Random();
                        int r = rand.nextInt(2);
                        if (r == 0) {
                            direction = 1;
                            vY = 0;
                            vX = -1;
                        } else if (r == 1) {
                            direction = 2;
                            vY = 0;
                            vX = 1;
                        }
                    }
                }
            } else if (pathType.equals("LineY")) {
                if (collisionBox.y <= pathRect.y) {
                    vY = 1;
                    direction = 0;
                } else if(collisionBox.y + collisionBox.height >= pathRect.y + pathRect.height) {
                    vY = -1;
                    direction = 3;
                } else {
                    if (direction == 0) {
                        vY = 1;
                    } else if (direction == 3) {
                        vY = -1;
                    } else if (direction == 1 || direction == 2) {
                        Random rand = new Random();
                        int r = rand.nextInt(2);
                        if (r == 0) {
                            direction = 0;
                            vX = 0;
                            vY = 1;
                        } else if (r == 1) {
                            direction = 3;
                            vX = 0;
                            vY = -1;
                        }
                    }
                }
            }
        } else {
            vX = 0;
            vY = 0;
        }
        // Make the velocity vector a unit vector and then multiply by the moveSpeed
        if (vX != 0 || vY != 0) {
            if (Math.abs(vX) == 1 && Math.abs(vY) == 1) {
                vX = vX*moveSpeed*7/10;
                vY = vY*moveSpeed*7/10;
                this.translate(vX, vY);
            } else {
                vX = vX*moveSpeed;
                vY = vY*moveSpeed;
                this.translate(vX, vY);
            }
            walkingTimer += elapsedTime;
            if (walkingTimer > animationTime) { // If enough time has passed to elicit a new animationFrame
                animationFrame += 1;             // Then we move to the next frame.
                walkingTimer = 0;                // Reset the walkingTimer for the next animationFrame
                if (animationFrame >= maxAnimationFrames) { // Loop back to the first frame if we are already at the last frame
                    animationFrame = 0;
                }
            }
        } else { // If the character isn't moving then change the animation frame to 0
            animationFrame = 0;          // And set the walkingTimer to 0;
            walkingTimer = 0;
        }
    }

    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
        collisionBox.translate(dx, dy);
        interactionBox.translate(dx, dy);
    }

    public void setPosition(int posX, int posY) {
        x = posX;
        y = posY;
        collisionBox.setLocation(x + characterWidth / 4, y + characterHeight / 2);
        interactionBox.setLocation(x - characterWidth / 6, y + characterHeight / 4);
    }

    public void addItem(String name, Item item){
        items.put(name, item);
    }

    public void useItem(String name) {
        items.get(name).use();
    }

    public void addEquipment(String name, Equipment object) {
        equipment.put(name, object);
    }

    public void setWalkPath(String pathType, Rectangle pathRect) {
        this.pathType = pathType;
        this.pathRect = pathRect;
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

    public void setDrawn(boolean bool) {
        drawn = bool;
    }

    public boolean isDrawn() {
        return drawn;
    }

    public boolean canSwim() {
        return canSwim;
    }

    public void changeTravelMethod(String method) {
        this.travelState = method;
    }

    public void setStop(boolean bool) {
        stop = bool;
    }

    public boolean isStop() {
        return stop;
    }

    public Rectangle getInteractionRect() {
        return interactionBox;
    }

    public void startDiaolog(int dir) {
        direction = dir;
        stop = true;
        DialogManager.startDialog(name);
        //dialogBox.setActive(true);
    }

    public DialogSequenceBox getDialogBox() {
        return dialogBox;
    }
}

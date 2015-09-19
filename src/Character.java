import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/19/2015.
 */
public class Character {

    public String characterName;
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
    private Map<String, Item> items;
    private Map<String, Equipment> equipment;
    private static String travelState;
    private boolean canSwim;

    private String pathType;
    private Rectangle pathRect;
    private boolean drawn;
    private boolean stop;

    private final long animationTime = 80000L;
    private final int moveSpeed = 4;

    public Character(String name, int posX, int posY){

        this.characterName = name;
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
        vX = 0;
        vY = 0;
        items = new HashMap<>();
        equipment = new HashMap<>();
        canSwim = false;
        travelState = "Walk";
    }

        public void draw(Graphics2D g2d, int xOffset, int yOffset) {
        g2d.drawImage(image, x - xOffset, y - yOffset, x - xOffset + characterWidth, y - yOffset + characterHeight, characterWidth * animationFrame,
                characterHeight * direction, (animationFrame + 1) * characterWidth, (direction + 1) * characterHeight, null);
        g2d.setColor(Color.WHITE);
        collisionBox.translate(-xOffset, -yOffset);
        g2d.draw(collisionBox);
        collisionBox.translate(xOffset, yOffset);
        g2d.setColor(Color.RED);
        if (pathRect != null) {
            pathRect.translate(-xOffset, -yOffset);
            g2d.draw(pathRect);
            pathRect.translate(xOffset, yOffset);
        }
    }

    public void update(float elapsedTime) {
        // Determine The Direction to Move the Character
        // The order here is important, as it prioritizes the Up/Down Character animation over Right/Left
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
            if (pathType.equals("Random")) {

            } else if (pathType.equals("Box_Edge")) {

            } else if (pathType.equals("LineX")) {

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
                    }
                }
            } else if (pathType.equals("Box_Fill")) {

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
    }

    public void setPosition(int posX, int posY) {
        x = posX;
        y = posY;
        collisionBox.setLocation(x + characterWidth / 4, y + characterHeight / 2);
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
}

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Cody on 9/10/2015.
 */
public class MenuPointer {
    private int width; // draw width
    private int height; // draw Height
    private int x; // Drawing left position
    private int y; // Drawing top position of the menu options (doesn't change with scrolling)
    private int menuSpacing; // Space between each menu option it's pointing to
    private BufferedImage image; // Image to be drawn
    private int menuCount; // How many menu options are there
    public int count; // Current option that the pointer is pointing to (1 - menuCount)
    public float scrollTimer; // The amount of time the pointer has been scrolling in one direction
    public int scrollDirection; // The direction the pointer is currently scrolling

    public final float TIMER_MAX = 600000L;

    public MenuPointer(BufferedImage image, int posX, int posY, int menuSpacing, int menuCount, int count) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.x = posX;
        this.y = posY;
        this.menuSpacing = menuSpacing;
        this.scrollDirection = 0;
        this.scrollTimer = 0;
        this.menuCount = menuCount;
        this.count = count;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMenuCount() { return menuCount; }

    public void setPosition(int posX, int posY) {
        x = posX;
        y = posY;
    }

    public void setWidth(int w) {
        width = w;
    }

    public void setHeight(int h) {
        height = h;
    }

    public void draw(Graphics2D g2d) {
        // Draws the image at the position based on how far down the menu the pointer has scrolled
        g2d.drawImage(image, x, y + (count - 1) * menuSpacing, width, height, null);
    }

    public void update(float elapsedTime) {


    }

    public void scrollDown() {
        count += 1; // Moves down the menu options by one (increases current countX by one)
        if (count > menuCount) {
            count = 1; // If the pointer goes past the last menu option it moves to the first option
        }
        SoundManager.restartSound("Scroll_1");
    }

    public void scrollUp() {
        count -= 1; // Moves up the menu options by one (decreases current countX by one)
        if (count < 1) {
            count = menuCount; // If the pointer goes past the first menu option it moves to the last option
        }
        SoundManager.restartSound("Scroll_1");
    }

    public void setMenuCount(int menuCount) {
        this.menuCount = menuCount;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

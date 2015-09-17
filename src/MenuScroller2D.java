import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Cody on 9/14/2015.
 */
public class MenuScroller2D {

    private int width; // Draw width
    private int height; // Draw Height
    private int x; // Drawing left position
    private int y; // Drawing top position of the menu options (doesn't change with scrolling)
    private int menuSpacingX; // Space between each menu option it's pointing to
    private int menuSpacingY;
    private BufferedImage image; // Image to be drawn
    private int menuCountX; // How many menu options are there
    private int menuCountY;
    private int objectCount;
    private int countX; // Current option that the pointer is pointing to (1 - menuCount)
    private int countY;
    private int lastCol;
    private int lastRow;
    public float scrollTimer; // The amount of time the pointer has been scrolling in one direction
    public int scrollDirection; // The direction the pointer is currently scrolling, Down = 0, Left = 1, Right = 2, Up = 3, None = -1

    public final float TIMER_MAX = 400000L;

    public MenuScroller2D(BufferedImage image, int posX, int posY, int menuSpacingX, int menuSpacingY,
                          int menuCountX, int menuCountY, int objectCount, int countX, int countY) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.x = posX;
        this.y = posY;
        this.menuSpacingX = menuSpacingX;
        this.menuSpacingY = menuSpacingY;
        this.scrollDirection = 0;
        this.scrollTimer = 0;
        this.menuCountX = menuCountX;
        this.menuCountY = menuCountY;
        this.countX = countX;
        this.countY = countY;
        this.objectCount = objectCount;
        lastCol = 1;
        lastRow = 1;
        int temp = objectCount - menuCountX;
        while (temp > 0) {
            lastRow++;
            temp -= menuCountX;
        }
        lastCol = temp + menuCountX;
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

    public int getCountX() {
        return countX;
    }

    public int getCountY() {
        return countY;
    }

    public int getLastRow() {
        return lastRow;
    }

    public int getLastCol() {
        return lastCol;
    }

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
        g2d.drawImage(image, x + (countX - 1)*menuSpacingX, y + (countY - 1) * menuSpacingY, width, height, null);
    }

    public void update(float elapsedTime) {


    }

    public void scrollDown() {
        countY += 1; // Moves down the menu options by one (increases current countX by one)
        if (countX > lastCol) {
            if (countY > menuCountY - 1) {
                countY = 1; // If the pointer goes past the last menu option it moves to the first option
            }
        } else {
            if (countY > menuCountY) {
                countY = 1; // If the pointer goes past the last menu option it moves to the first option
            }
        }
        SoundManager.restartSound("Scroll_1");
    }

    public void scrollUp() {
        countY -= 1; // Moves up the menu options by one (decreases current countX by one)
        if (countX > lastCol) {
            if (countY < 1) {
                countY = menuCountY - 1; // If the pointer goes past the first menu option it moves to the last option
            }
        } else {
            if (countY < 1) {
                countY = menuCountY; // If the pointer goes past the first menu option it moves to the last option
            }
        }
        SoundManager.restartSound("Scroll_1");
    }

    public void scrollRight() {
        countX += 1; // Moves down the menu options by one (increases current countX by one)
        if (countY == lastRow) {
            if (countX > lastCol) {
                countX = 1; // If the pointer goes past the last menu option it moves to the first option
            }
        } else {
            if (countX > menuCountX) {
                countX = 1; // If the pointer goes past the last menu option it moves to the first option
            }
        }
        SoundManager.restartSound("Scroll_1");
    }

    public void scrollLeft() {
        countX -= 1; // Moves up the menu options by one (decreases current countX by one)
        if (countY == lastRow) {
            if (countX < 1) {
                countX = lastCol;
            }
        } else {
            if (countX < 1) {
                countX = menuCountX; // If the pointer goes past the first menu option it moves to the last option
            }
        }
        SoundManager.restartSound("Scroll_1");
    }


}

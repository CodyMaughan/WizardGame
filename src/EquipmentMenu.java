import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/26/2015.
 */
public class EquipmentMenu implements Menu {

    private MainCharacter character;
    private int windowWidth;
    private int windowHeight;
    private int menuX;
    private int menuY;
    private String title;
    private Font titleFont;
    private int titleWidth;
    private int titleHeight;
    private boolean active;
    private MenuPointer scroller;

    public EquipmentMenu(MainCharacter character, Framework framework) {
        this.character = character;
        windowWidth = framework.getWidth();
        windowHeight = framework.getHeight();
        menuX = windowWidth/3;
        menuY = 0;
        title = character.characterName + "'s Items";
        titleFont = new Font("Arial", Font.BOLD, 30);
        Graphics2D g2d = (Graphics2D)framework.getGraphics();
        titleWidth = (int)(titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
        titleHeight = (int)(titleFont.getStringBounds(title,g2d.getFontRenderContext()).getHeight());
        active = false;
        BufferedImage scrollerImage = null;
        // Load the Scroller Image
        try {
            scrollerImage = ImageIO.read(this.getClass().getResource("/resources/images/pointerFinger.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        scroller = new MenuPointer(scrollerImage, windowWidth/3 + 15, 100 + (15), (15 + 20), MainCharacter.equipment.size(), 1);
        scroller.setWidth(20);
        scroller.setHeight(20);
        if (MainCharacter.equipment.size() == 0) {
            scroller.setMenuCount(1);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(menuX, menuY, 2 * windowWidth / 3, windowHeight);
        g2d.setColor(Color.BLACK);
        g2d.setFont(titleFont);
        g2d.drawString(title, menuX + (windowWidth/3 - titleWidth/2), menuY + 50);
        Font headerFont = new Font("Arial", Font.BOLD, 20);
        g2d.setFont(headerFont);
        int countWidth = (int)(headerFont.getStringBounds("Count", g2d.getFontRenderContext()).getWidth());
        Font itemFont = new Font("Arial", Font.BOLD, 15);
        int textWidth = (int)(headerFont.getStringBounds("Value", g2d.getFontRenderContext()).getWidth());
        g2d.drawString("Equipment", windowWidth / 3 + 50, 100);
        g2d.drawString("Value", windowWidth - 60 - countWidth - textWidth, 100);
        g2d.drawString("Count", windowWidth - 30 - countWidth, 100);
        g2d.setFont(itemFont);
        int i = 0;
        for (Equipment equipment : MainCharacter.equipment.values()) {
            g2d.drawString(equipment.name, windowWidth / 3 + 50, 100 + (15 + 20) * (i + 1));
            g2d.drawString(String.valueOf(equipment.price), windowWidth - 60 - countWidth - textWidth, 100 + (15 + 20)*(i + 1));
            g2d.drawString(String.valueOf(MainCharacter.equipmentCount.get(equipment.name)), windowWidth - 30 - countWidth, 100 + (15 + 20)*(i + 1));
            i++;
        }
        if (active) {
            scroller.draw(g2d);
        }
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {
        if (keyboardstate[KeyEvent.VK_S][0]) { // Handle the S Key is down
            // Decide whether to scroll down or not
            if (scroller.scrollDirection == 1) {                 // If the scroller was already scrolling down...
                scroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                if (scroller.scrollTimer > scroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                    scroller.scrollTimer -= scroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                    scroller.scrollDown();                       // And finally we scroll down.
                }
            } else {                                             // If the scroller was not already scrolling down...
                scroller.scrollDown();                           // We scroll down...
                scroller.scrollDirection = 1;                    // Set the scroll direction to down...
                scroller.scrollTimer = 0;                        // And reset the scrollTimer.
            }
        }
        if (keyboardstate[KeyEvent.VK_W][0]) { // Handle the W Key is down
            // Decide whether to scroll up or not
            if (scroller.scrollDirection == -1) {                // If the scroller was already scrolling up...
                scroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                if (scroller.scrollTimer > scroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                    scroller.scrollTimer -= scroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                    scroller.scrollUp();                         // And finally we scroll up.
                }
            } else {                                             // If the scroller was not already scrolling up...
                scroller.scrollUp();                             // We scroll up...
                scroller.scrollDirection = -1;                   // Set the scroll direction to up...
                scroller.scrollTimer = 0;                        // And reset the scrollTimer.
            }
        }
        if (!keyboardstate[KeyEvent.VK_S][0] && !keyboardstate[KeyEvent.VK_W][0]) { // Handle the absence of scrolling
            // Reset the scroller action variables
            scroller.scrollDirection = 0;
            scroller.scrollTimer = 0;
        }
    }

    @Override
    public void setActive(boolean bool) {
        active = bool;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/18/2015.
 */
public class ItemsMenu implements Menu {

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
    private Item lastItem;
    private int state;
    private String status;
    private ArrayList<String> statusLines;

    public ItemsMenu(Framework framework) {
        windowWidth = framework.getWidth();
        windowHeight = framework.getHeight();
        menuX = windowWidth/3;
        menuY = 0;
        title = MainCharacter.characterName + "'s Items";
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
        scroller = new MenuPointer(scrollerImage, windowWidth/3 + 15, 100 + (15), (15 + 20), MainCharacter.items.size(), 1);
        scroller.setWidth(20);
        scroller.setHeight(20);
        if (MainCharacter.items.size() == 0) {
            scroller.setMenuCount(1);
        }
        status = "";
        state = 0;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(menuX, menuY, 2 * windowWidth / 3, windowHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(menuX, menuY, 2 * windowWidth / 3 - 1, windowHeight - 1);
        g2d.setFont(titleFont);
        g2d.drawString(title, menuX + (windowWidth / 3 - titleWidth / 2), menuY + 50);
        Font headerFont = new Font("Arial", Font.BOLD, 20);
        g2d.setFont(headerFont);
        int countWidth = (int)(headerFont.getStringBounds("Count", g2d.getFontRenderContext()).getWidth());
        Font itemFont = new Font("Arial", Font.BOLD, 15);
        int textWidth = (int)(headerFont.getStringBounds("Value", g2d.getFontRenderContext()).getWidth());
        g2d.drawString("Item", windowWidth / 3 + 50, 100);
        g2d.drawString("Value", windowWidth - 60 - countWidth - textWidth, 100);
        g2d.drawString("Count", windowWidth - 30 - countWidth, 100);
        g2d.setFont(itemFont);
        int i = 0;
        for (Item item : MainCharacter.items.values()) {
            g2d.drawString(item.name, windowWidth / 3 + 50, 100 + (15 + 20) * (i + 1));
            g2d.drawString(String.valueOf(item.price), windowWidth - 60 - countWidth - textWidth, 100 + (15 + 20)*(i + 1));
            g2d.drawString(String.valueOf(MainCharacter.itemCount.get(item.name)), windowWidth - 30 - countWidth, 100 + (15 + 20)*(i + 1));
            i++;
        }
        if (active) {
            g2d.setColor(Color.BLACK);
            g2d.drawRect(menuX, 3*menuY/4, 2 * windowWidth / 3 - 1, 3*windowHeight/4 - 1);
            int maxMessageWidth = 2*windowWidth/3 - 40;
            Font messageFont = new Font("Arial", Font.BOLD, 15);
            g2d.setFont(messageFont);
            ArrayList<String> lines = new ArrayList<>();
            if (MainCharacter.items.size() > 0) {
                String text = ItemCache.getExplanation((MainCharacter.items.getIndexed(scroller.getCount() - 1).name));
                textWidth = (int) (messageFont.getStringBounds(text, g2d.getFontRenderContext()).getWidth());
                if (textWidth > maxMessageWidth) {
                    String[] split = text.split("\\s+");
                    int tempWidth = 0;
                    int maxWidth = 0;
                    String line = "";
                    for (i = 0; i < split.length; i++) {
                        tempWidth += (int) (messageFont.getStringBounds(split[i] + "_", g2d.getFontRenderContext()).getWidth());
                        if (tempWidth > maxMessageWidth) {
                            tempWidth = (int) (messageFont.getStringBounds(split[i] + "_", g2d.getFontRenderContext()).getWidth());
                            lines.add(line);
                            line = split[i] + " ";
                        } else {
                            line = line + split[i] + " ";
                        }
                        if (tempWidth > maxWidth) {
                            maxWidth = tempWidth;
                        }
                    }
                    lines.add(line);
                } else {
                    lines.add(text);
                }
                for (i = 0; i < lines.size(); i++) {
                    g2d.drawString(lines.get(i), menuX + 20, 3 * windowHeight / 4 + 18 + 25 + i * (18 + 10));
                }
            }
            if (state == 0) {
                scroller.draw(g2d);
            } else if (state == 1) {
                g2d.setColor(Color.BLACK);
                g2d.fillRoundRect(menuX + 20, windowHeight / 2 - 100, 2 * windowWidth / 3 - 40, 200, 10, 10);
                statusLines  = new ArrayList<>();
                textWidth = (int)(messageFont.getStringBounds(status, g2d.getFontRenderContext()).getWidth());
                maxMessageWidth = 2*windowWidth/3 - 80;
                if (textWidth > maxMessageWidth) {
                    String[] split = status.split("\\s+");
                    int tempWidth = 0;
                    int maxWidth = 0;
                    String line = "";
                    for (i = 0; i < split.length; i++) {
                        tempWidth += (int)(messageFont.getStringBounds(split[i] + "_",g2d.getFontRenderContext()).getWidth());
                        if (tempWidth > maxMessageWidth) {
                            tempWidth = (int) (messageFont.getStringBounds(split[i] + "_", g2d.getFontRenderContext()).getWidth());
                            statusLines.add(line);
                            line = split[i] + " ";
                        } else {
                            line = line + split[i] + " ";
                        }
                        if (tempWidth > maxWidth) {
                            maxWidth = tempWidth;
                        }
                    }
                    statusLines.add(line);
                } else {
                    statusLines.add(status);
                }
                g2d.setColor(Color.WHITE);
                for (i = 0; i < statusLines.size(); i++) {
                    g2d.drawString(statusLines.get(i), menuX + 40, windowHeight/2 - 100 + 15 + 20 + i*(15 + 10));
                }
            }
        }
        lastItem = null;
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {
        if (keyboardstate[KeyEvent.VK_ENTER][1]) {
            status = MainCharacter.items.getIndexed(scroller.getCount() - 1).use();
            if (state == 0) {
                if (status.contains("<>")) {
                    status = status.substring(0, status.length() - 2);
                } else {
                    String name = MainCharacter.items.getIndexed(scroller.getCount() - 1).name;
                    lastItem = MainCharacter.items.getIndexed(scroller.getCount() - 1);
                    MainCharacter.removeItem(name);
                }
                state = 1;
            } else if (state == 1) {
                state = 0;
            }
        } else if (state == 0){
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
    }

    @Override
    public void setActive(boolean bool) {
        active = bool;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Item getLastItem() {
        return lastItem;
    }
}

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/24/2015.
 */
public class VendorState implements GameEvent, IState {

    private int windowWidth;
    private int windowHeight;
    private int maxButtonWidth;
    private int maxButtonHeight;
    private int yButtonSpacing;

    private String id;
    private String name;
    private String vendorType;
    private int vendorMoney;
    private Map<String, Vendable> vendables;
    private Map<String, Integer> vendableCount;
    private MenuButton buyButton;
    private MenuButton sellButton;
    private MenuButton exitButton;
    private MenuPointer leftScroller;

    private int state;
    private Font titleFont;
    private Font vendableFont;
    private String title;
    private int titleWidth;
    private int titleHeight;
    private MenuPointer rightScroller;

    public VendorState(String id, String name, HashMap<String, Vendable> vendables, HashMap<String, Integer> vendableCount, int vendorMoney, String vendorType) {
        this.windowWidth = StateMachine.getFramework().getWidth();
        this.windowHeight = StateMachine.getFramework().getHeight();
        this.id = id;
        this.name = name;
        this.vendables = vendables;
        this.vendableCount = vendableCount;
        this.vendorMoney = vendorMoney;
        this.vendorType = vendorType;
        Font buttonFont = new Font("Arial", Font.BOLD, 15);
        buyButton = new MenuButton("Buy Items", 0, 0, buttonFont, (Graphics2D)StateMachine.getFramework().getGraphics(), 5, 5);
        sellButton = new MenuButton("Sell Items", 0, 0, buttonFont, (Graphics2D)StateMachine.getFramework().getGraphics(), 5, 5);
        exitButton = new MenuButton("Exit", 0, 0, buttonFont, (Graphics2D)StateMachine.getFramework().getGraphics(), 5, 5);
        maxButtonWidth = sellButton.getWidth();
        maxButtonHeight = sellButton.getHeight();
        int xButtonSpacing = (windowWidth/3 - maxButtonWidth)/2;
        int x = xButtonSpacing;
        yButtonSpacing = (windowHeight - maxButtonHeight*8)/9;
        int y = yButtonSpacing;
        buyButton.setPosition(x, y);
        buyButton.setWidth(maxButtonWidth);
        sellButton.setPosition(x, y + (yButtonSpacing + maxButtonHeight));
        sellButton.setWidth(maxButtonWidth);
        exitButton.setPosition(x, y + 2 * (yButtonSpacing + maxButtonHeight));
        exitButton.setWidth(maxButtonWidth);
        BufferedImage scrollerImage = null;
        // Load the Scroller Image
        try {
            scrollerImage = ImageIO.read(this.getClass().getResource("/resources/images/pointerFinger.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        leftScroller = new MenuPointer(scrollerImage, xButtonSpacing/4, y, yButtonSpacing + maxButtonHeight, 3, 1);
        leftScroller.setWidth(xButtonSpacing / 2);
        leftScroller.setHeight(maxButtonHeight);
        state = 0;
        vendableFont = new Font("Arial", Font.BOLD, 15);
        titleFont = new Font("Arial", Font.BOLD, 30);
        title = name + "'s Items";
        Graphics2D g2d = (Graphics2D)StateMachine.getFramework().getGraphics();
        titleWidth = (int)(titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
        titleHeight = (int)(titleFont.getStringBounds(title, g2d.getFontRenderContext()).getHeight());
        rightScroller = new MenuPointer(scrollerImage, windowWidth/3 + 15, 100 + (15), (15 + 20), 1, 1);
        rightScroller.setWidth(20);
        rightScroller.setHeight(20);
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {
        if (state == 0) {
            if (keyboardstate[KeyEvent.VK_ENTER][1]) { // Handle the Enter Key is pressed
                switch (leftScroller.count) {
                    case (1):
                        //Buy Menu
                        state = 1;
                        rightScroller.setMenuCount(vendables.size());
                        rightScroller.setCount(1);
                        break;
                    case (2):
                        //Sell Menu
                        state = 1;
                        rightScroller.setMenuCount(MainCharacter.vendables.size());
                        rightScroller.setCount(1);
                        break;
                    case (3):
                        //Exit the Vendor State
                        endEvent();
                        break;
                }
            } else {
                if (keyboardstate[KeyEvent.VK_S][0]) { // Handle the S Key is down
                    // Decide whether to scroll down or not
                    if (leftScroller.scrollDirection == 1) {                 // If the scroller was already scrolling down...
                        leftScroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                        if (leftScroller.scrollTimer > leftScroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                            leftScroller.scrollTimer -= leftScroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                            leftScroller.scrollDown();                       // And finally we scroll down.
                        }
                    } else {                                             // If the scroller was not already scrolling down...
                        leftScroller.scrollDown();                           // We scroll down...
                        leftScroller.scrollDirection = 1;                    // Set the scroll direction to down...
                        leftScroller.scrollTimer = 0;                        // And reset the scrollTimer.
                    }
                }
                if (keyboardstate[KeyEvent.VK_W][0]) { // Handle the W Key is down
                    // Decide whether to scroll up or not
                    if (leftScroller.scrollDirection == -1) {                // If the scroller was already scrolling up...
                        leftScroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                        if (leftScroller.scrollTimer > leftScroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                            leftScroller.scrollTimer -= leftScroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                            leftScroller.scrollUp();                         // And finally we scroll up.
                        }
                    } else {                                             // If the scroller was not already scrolling up...
                        leftScroller.scrollUp();                             // We scroll up...
                        leftScroller.scrollDirection = -1;                   // Set the scroll direction to up...
                        leftScroller.scrollTimer = 0;                        // And reset the scrollTimer.
                    }
                }
                if (!keyboardstate[KeyEvent.VK_S][0] && !keyboardstate[KeyEvent.VK_W][0]) { // Handle the absence of scrolling
                    // Reset the scroller action variables
                    leftScroller.scrollDirection = 0;
                    leftScroller.scrollTimer = 0;
                }
            }
            // update the scroller (Animations, ect.);
            leftScroller.update(elapsedTime);
            Graphics2D g2d = (Graphics2D) StateMachine.getFramework().getGraphics();
            switch (leftScroller.count) {
                case (1):
                    // Buy Items Screen
                    title = name + "'s Items";
                    titleWidth = (int) (titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
                    titleHeight = (int) (titleFont.getStringBounds(title, g2d.getFontRenderContext()).getHeight());
                    break;
                case (2):
                    // Sell Items Screen
                    title = MainCharacter.characterName + "'s Items";
                    titleWidth = (int) (titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
                    titleHeight = (int) (titleFont.getStringBounds(title, g2d.getFontRenderContext()).getHeight());
                    break;
                case (3):
                    // Exit Screen
                    title = "Exit " + name + "'s Shop";
                    titleWidth = (int) (titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
                    titleHeight = (int) (titleFont.getStringBounds(title, g2d.getFontRenderContext()).getHeight());
                    break;
            }
        } else {
            if (keyboardstate[KeyEvent.VK_BACK_SPACE][1]) {
                state = 0;
            } else if (keyboardstate[KeyEvent.VK_ENTER][1]) { // Handle the Enter Key is pressed
                // Buy/Sell the item
                switch (leftScroller.count) {
                    case (0):
                        // Buy the item

                        break;
                    case (1):
                        // Sell the item

                        break;
                }
            } else {
                if (keyboardstate[KeyEvent.VK_S][0]) { // Handle the S Key is down
                    // Decide whether to scroll down or not
                    if (rightScroller.scrollDirection == 1) {                 // If the scroller was already scrolling down...
                        rightScroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                        if (rightScroller.scrollTimer > rightScroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                            rightScroller.scrollTimer -= rightScroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                            rightScroller.scrollDown();                       // And finally we scroll down.
                        }
                    } else {                                             // If the scroller was not already scrolling down...
                        rightScroller.scrollDown();                           // We scroll down...
                        rightScroller.scrollDirection = 1;                    // Set the scroll direction to down...
                        rightScroller.scrollTimer = 0;                        // And reset the scrollTimer.
                    }
                }
                if (keyboardstate[KeyEvent.VK_W][0]) { // Handle the W Key is down
                    // Decide whether to scroll up or not
                    if (rightScroller.scrollDirection == -1) {                // If the scroller was already scrolling up...
                        rightScroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                        if (rightScroller.scrollTimer > rightScroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                            rightScroller.scrollTimer -= rightScroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                            rightScroller.scrollUp();                         // And finally we scroll up.
                        }
                    } else {                                             // If the scroller was not already scrolling up...
                        rightScroller.scrollUp();                             // We scroll up...
                        rightScroller.scrollDirection = -1;                   // Set the scroll direction to up...
                        rightScroller.scrollTimer = 0;                        // And reset the scrollTimer.
                    }
                }
                if (!keyboardstate[KeyEvent.VK_S][0] && !keyboardstate[KeyEvent.VK_W][0]) { // Handle the absence of scrolling
                    // Reset the scroller action variables
                    rightScroller.scrollDirection = 0;
                    rightScroller.scrollTimer = 0;
                }
            }
            // update the scroller (Animations, ect.);
            rightScroller.update(elapsedTime);
            Graphics2D g2d = (Graphics2D) StateMachine.getFramework().getGraphics();
            switch (rightScroller.count) {
                case (1):
                    // Buy Items Screen
                    title = name + "'s Items";
                    titleWidth = (int) (titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
                    titleHeight = (int) (titleFont.getStringBounds(title, g2d.getFontRenderContext()).getHeight());
                    break;
                case (2):
                    // Sell Items Screen
                    title = MainCharacter.characterName + "'s Items";
                    titleWidth = (int) (titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
                    titleHeight = (int) (titleFont.getStringBounds(title, g2d.getFontRenderContext()).getHeight());
                    break;
                case (3):
                    // Exit Screen
                    title = "Exit " + name + "'s Shop";
                    titleWidth = (int) (titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
                    titleHeight = (int) (titleFont.getStringBounds(title, g2d.getFontRenderContext()).getHeight());
                    break;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 0, windowWidth / 3, windowHeight);
        buyButton.draw(g2d);
        sellButton.draw(g2d);
        exitButton.draw(g2d);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(windowWidth / 3, 0, 2 * windowWidth / 3, windowHeight);
        g2d.setColor(Color.BLACK);
        g2d.setFont(titleFont);
        g2d.drawString(title, windowWidth / 3 + (windowWidth / 3 - titleWidth / 2), 50);
        Font headerFont = new Font("Arial", Font.BOLD, 20);
        g2d.setFont(headerFont);
        int textWidth;
        int countWidth = (int)(headerFont.getStringBounds("Count", g2d.getFontRenderContext()).getWidth());
        Font itemFont = new Font("Arial", Font.BOLD, 15);
        int i = 0;
        switch (leftScroller.count) {
            case (1):
                // Buy Items Screen
                g2d.drawString("Items/Equipment", windowWidth / 3 + 50, 100);
                textWidth = (int)(headerFont.getStringBounds("Buy Price", g2d.getFontRenderContext()).getWidth());
                g2d.drawString("Buy Price", windowWidth - 60 - countWidth - textWidth, 100);
                g2d.drawString("Count", windowWidth - 30 - countWidth, 100);
                g2d.setFont(itemFont);
                for (Vendable vendable : vendables.values()) {
                    g2d.drawString(vendable.name, windowWidth / 3 + 50, 100 + (15 + 20)*(i + 1));
                    g2d.drawString(String.valueOf(vendable.price), windowWidth - 60 - countWidth - textWidth, 100 + (15 + 20)*(i + 1));
                    g2d.drawString(String.valueOf(vendableCount.get(vendable.name)), windowWidth - 30 - countWidth, 100 + (15 + 20)*(i + 1));
                    i++;
                }
                break;
            case (2):
                // Sell Items Screen
                g2d.drawString("Items/Equipment", windowWidth / 3 + 50, 100);
                textWidth = (int)(headerFont.getStringBounds("Sell Price", g2d.getFontRenderContext()).getWidth());
                g2d.drawString("Sell Price", windowWidth - 60 - countWidth - textWidth, 100);
                g2d.drawString("Count", windowWidth - 30 - countWidth, 100);
                boolean canSell = false;
                g2d.setFont(itemFont);
                for (Vendable vendable : MainCharacter.vendables.values()) {
                    String[] split = vendable.type.split(", ");
                    for (int j = 0; j < split.length; j++) {
                        if (vendorType.contains(split[j])) {
                            canSell = true;
                            break;
                        }
                    }
                    if (canSell) {
                        g2d.drawString(vendable.name, windowWidth / 3 + 50, 100 + (15 + 20) * (i + 1));
                        g2d.drawString(String.valueOf(vendable.price), windowWidth - 60 - countWidth - textWidth, 100 + (15 + 20)*(i + 1));
                        g2d.drawString(String.valueOf(MainCharacter.vendableCount.get(vendable.name)), windowWidth - 30 - countWidth, 100 + (15 + 20)*(i + 1));
                        i++;
                    }
                    canSell = false;
                }
                break;
            case (3):
                // Exit Screen

                break;
        }
        if (state == 0) {
            leftScroller.draw(g2d);
        } else {
            rightScroller.draw(g2d);
        }
    }

    @Override
    public void onEnter(Framework framework) {

    }

    @Override
    public void onExit() {

    }

    @Override
    public void startEvent() {
        StateMachine.Add("VendorState", this);
        StateMachine.Change("VendorState");
    }

    @Override
    public void progressEvent() {

    }

    @Override
    public void endEvent() {
        StateMachine.Change("StartGame");
        StateMachine.Remove("VendorState");
    }
}

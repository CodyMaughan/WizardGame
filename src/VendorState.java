import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
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

    private String name;
    private Vendable[] vendables;
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

    public VendorState(String name, Vendable[] vendables) {
        this.windowWidth = StateMachine.getFramework().getWidth();
        this.windowHeight = StateMachine.getFramework().getHeight();
        this.name = name;
        this.vendables = vendables;
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
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {
        if (keyboardstate[KeyEvent.VK_ENTER][1]) { // Handle the Enter Key is pressed
            switch (leftScroller.count) {
                case (1):
                    //Buy Menu

                    break;
                case (2):
                    //Sell Menu

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
        Graphics2D g2d = (Graphics2D)StateMachine.getFramework().getGraphics();
        switch (leftScroller.count) {
            case (1):
                // Buy Items Screen
                title = name + "'s Items";
                titleWidth = (int)(titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
                titleHeight = (int)(titleFont.getStringBounds(title, g2d.getFontRenderContext()).getHeight());
                break;
            case (2):
                // Sell Items Screen
                title =  MainCharacter.characterName + "'s Items";
                titleWidth = (int)(titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
                titleHeight = (int)(titleFont.getStringBounds(title, g2d.getFontRenderContext()).getHeight());
                break;
            case (3):
                // Exit Screen
                title = "Exit " + name + "'s Shop";
                titleWidth = (int)(titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
                titleHeight = (int)(titleFont.getStringBounds(title, g2d.getFontRenderContext()).getHeight());
                break;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 0, windowWidth / 3, windowHeight);
        buyButton.draw(g2d);
        sellButton.draw(g2d);
        exitButton.draw(g2d);
        leftScroller.draw(g2d);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(windowWidth / 3, 0, 2 * windowWidth / 3, windowHeight);
        g2d.setColor(Color.BLACK);
        g2d.setFont(titleFont);
        g2d.drawString(title, windowWidth / 3 + (windowWidth / 3 - titleWidth / 2), 50);
        switch (leftScroller.count) {
            case (1):
                // Buy Items Screen

                break;
            case (2):
                // Sell Items Screen

                break;
            case (3):
                // Exit Screen

                break;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/14/2015.
 */
public class MenuState implements IState {

    private int windowWidth;
    private int windowHeight;

    // Left side buttons
    private MenuButton cardsButton;
    private MenuButton itemsButton;
    private MenuButton statusButton;
    private MenuButton journalButton;
    private MenuButton worldMapButton;
    private MenuButton saveButton;
    private MenuButton exitButton;
    private int maxButtonWidth;
    private int maxButtonHeight;
    private int yButtonSpacing;
    private MenuPointer leftScroller;

    // Objects used for the right side of the menu screen
    private CardsMenu cardsMenu;
    private ItemsMenu itemsMenu;
    private StatusMenu statusMenu;
    private JournalMenu journalMenu;
    private WorldMapMenu worldMapMenu;
    private SaveMenu saveMenu;
    private ExitMenu exitMenu;
    private DrawableObject currentMenu;

    // Right side screens


    public MenuState(Framework framework, MainCharacter character) {
        windowWidth = framework.getWidth();
        windowHeight = framework.getHeight();
        Font buttonFont = new Font("Arial", Font.BOLD, 15);
        cardsButton = new MenuButton("Cards", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        itemsButton = new MenuButton("Items", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        statusButton = new MenuButton("Status", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        journalButton = new MenuButton("Journal", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        worldMapButton = new MenuButton("World Map", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        saveButton = new MenuButton("Save/Load Game", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        exitButton = new MenuButton("Exit Menu", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        maxButtonWidth = saveButton.getWidth();
        maxButtonHeight = saveButton.getHeight();
        int xButtonSpacing = (windowWidth/3 - maxButtonWidth)/2;
        int x = xButtonSpacing;
        yButtonSpacing = (windowHeight - saveButton.getHeight()*7)/8;
        int y = yButtonSpacing;
        cardsButton.setPosition(x, y);
        cardsButton.setWidth(maxButtonWidth);
        itemsButton.setPosition(x, y + (yButtonSpacing + maxButtonHeight));
        itemsButton.setWidth(maxButtonWidth);
        statusButton.setPosition(x, y + 2 * (yButtonSpacing + maxButtonHeight));
        statusButton.setWidth(maxButtonWidth);
        journalButton.setPosition(x, y + 3 * (yButtonSpacing + maxButtonHeight));
        journalButton.setWidth(maxButtonWidth);
        worldMapButton.setPosition(x, y + 4 * (yButtonSpacing + maxButtonHeight));
        worldMapButton.setWidth(maxButtonWidth);
        saveButton.setPosition(x, y + 5 * (yButtonSpacing + maxButtonHeight));
        exitButton.setPosition(x, y + 6*(yButtonSpacing + maxButtonHeight));
        exitButton.setWidth(maxButtonWidth);
        BufferedImage scrollerImage = null;
        // Load the Scroller Image
        try {
            scrollerImage = ImageIO.read(this.getClass().getResource("/resources/images/pointerFinger.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        leftScroller = new MenuPointer(scrollerImage, xButtonSpacing/4, y, yButtonSpacing + maxButtonHeight, 7, 1);
        leftScroller.setWidth(xButtonSpacing/2);
        leftScroller.setHeight(maxButtonHeight);
        cardsMenu = new CardsMenu(character, framework);
        itemsMenu = new ItemsMenu(character, framework);
        statusMenu = new StatusMenu(character, framework);
        journalMenu = new JournalMenu(character, framework);
        worldMapMenu = new WorldMapMenu(character, framework);
        saveMenu = new SaveMenu(character, framework);
        exitMenu = new ExitMenu(character, framework);
        currentMenu = cardsMenu;
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {
        if (keyboardstate[KeyEvent.VK_ENTER][1]) { // Handle the Enter Key is pressed
            switch (leftScroller.count) {
                case (1):
                    //Cards Menu
                    break;
                case (2):
                    //Items Menu
                    break;
                case (3):
                    //Status Menu
                    break;
                case (4):
                    //Journal Menu
                    break;
                case (5):
                    //WorldMap Menu
                    break;
                case (6):
                    //Save/Load Menu
                    break;
                case (7):
                    //Exit the Menu State
                    StateMachine.Change("StartGame", StateMachine.getFramework());
                    StateMachine.Remove("GameMenu");
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
        // Get the current menu
        switch (leftScroller.count) {
            case (1):
                //Cards Menu
                currentMenu = cardsMenu;
                break;
            case (2):
                //Items Menu
                currentMenu = itemsMenu;
                break;
            case (3):
                //Status Menu
                currentMenu = statusMenu;
                break;
            case (4):
                //Journal Menu
                currentMenu = journalMenu;
                break;
            case (5):
                //WorldMap Menu
                currentMenu = worldMapMenu;
                break;
            case (6):
                //Save/Load Menu
                currentMenu = saveMenu;
                break;
            case (7):
                //Exit the Menu State
                currentMenu = exitMenu;
                break;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, windowWidth/3, windowHeight);
        cardsButton.draw(g2d);
        itemsButton.draw(g2d);
        statusButton.draw(g2d);
        journalButton.draw(g2d);
        worldMapButton.draw(g2d);
        saveButton.draw(g2d);
        exitButton.draw(g2d);
        leftScroller.draw(g2d);
        currentMenu.draw(g2d);
    }

    @Override
    public void onEnter(Framework framework) {

    }

    @Override
    public void onExit() {

    }
}

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
    private MenuButton equipmentButton;
    private MenuButton statusButton;
    private MenuButton journalButton;
    private MenuButton worldMapButton;
    private MenuButton optionsButton;
    private MenuButton saveButton;
    private MenuButton exitButton;
    private int maxButtonWidth;
    private int maxButtonHeight;
    private int yButtonSpacing;
    private MenuPointer leftScroller;
    private int state;

    // Objects used for the right side of the menu screen
    private CardsMenu cardsMenu;
    private ItemsMenu itemsMenu;
    private EquipmentMenu equipmentMenu;
    private StatusMenu statusMenu;
    private JournalMenu journalMenu;
    private WorldMapMenu worldMapMenu;
    private OptionsMenu optionsMenu;
    private SaveMenu saveMenu;
    private ExitMenu exitMenu;
    private Menu currentMenu;

    // Right side screens


    public MenuState(Framework framework, MainCharacter character) {
        windowWidth = framework.getWidth();
        windowHeight = framework.getHeight();
        Font buttonFont = new Font("Arial", Font.BOLD, 15);
        cardsButton = new MenuButton("Cards", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        itemsButton = new MenuButton("Items", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        equipmentButton = new MenuButton("Equipment", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        statusButton = new MenuButton("Status", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        journalButton = new MenuButton("Journal", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        worldMapButton = new MenuButton("World Map", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        optionsButton = new MenuButton("Options", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        saveButton = new MenuButton("Save/Load Game", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        exitButton = new MenuButton("Exit Menu", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        maxButtonWidth = saveButton.getWidth();
        maxButtonHeight = saveButton.getHeight();
        int xButtonSpacing = (windowWidth/3 - maxButtonWidth)/2;
        int x = xButtonSpacing;
        yButtonSpacing = (windowHeight - saveButton.getHeight()*9)/10;
        int y = yButtonSpacing;
        cardsButton.setPosition(x, y);
        cardsButton.setWidth(maxButtonWidth);
        itemsButton.setPosition(x, y + (yButtonSpacing + maxButtonHeight));
        itemsButton.setWidth(maxButtonWidth);
        equipmentButton.setPosition(x, y + 2 * (yButtonSpacing + maxButtonHeight));
        equipmentButton.setWidth(maxButtonWidth);
        statusButton.setPosition(x, y + 3 * (yButtonSpacing + maxButtonHeight));
        statusButton.setWidth(maxButtonWidth);
        journalButton.setPosition(x, y + 4 * (yButtonSpacing + maxButtonHeight));
        journalButton.setWidth(maxButtonWidth);
        worldMapButton.setPosition(x, y + 5 * (yButtonSpacing + maxButtonHeight));
        worldMapButton.setWidth(maxButtonWidth);
        optionsButton.setPosition(x, y + 6 * (yButtonSpacing + maxButtonHeight));
        optionsButton.setWidth(maxButtonWidth);
        saveButton.setPosition(x, y + 7 * (yButtonSpacing + maxButtonHeight));
        exitButton.setPosition(x, y + 8 * (yButtonSpacing + maxButtonHeight));
        exitButton.setWidth(maxButtonWidth);
        BufferedImage scrollerImage = null;
        // Load the Scroller Image
        try {
            scrollerImage = ImageIO.read(this.getClass().getResource("/resources/images/pointerFinger.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        leftScroller = new MenuPointer(scrollerImage, xButtonSpacing / 4, y, yButtonSpacing + maxButtonHeight, 9, 1);
        leftScroller.setWidth(xButtonSpacing/2);
        leftScroller.setHeight(maxButtonHeight);
        state = 0;
        cardsMenu = new CardsMenu(character, framework);
        itemsMenu = new ItemsMenu(framework);
        equipmentMenu = new EquipmentMenu(character, framework);
        statusMenu = new StatusMenu(character, framework);
        journalMenu = new JournalMenu(character, framework);
        worldMapMenu = new WorldMapMenu(character, framework);
        optionsMenu = new OptionsMenu(character, framework);
        saveMenu = new SaveMenu(character, framework);
        exitMenu = new ExitMenu(character, framework);
        currentMenu = cardsMenu;
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {
        if (state == 0) {
            if (keyboardstate[KeyEvent.VK_ENTER][1]) { // Handle the Enter Key is pressed
                if (leftScroller.count == 9) {
                    //Exit the Menu State
                    StateMachine.Change("StartGame");
                    StateMachine.Remove("GameMenu");
                } else {
                    currentMenu.setActive(true);
                    state = 1;
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
                    //Equipment Menu
                    currentMenu = equipmentMenu;
                    break;
                case (4):
                    //Status Menu
                    currentMenu = statusMenu;
                    break;
                case (5):
                    //Journal Menu
                    currentMenu = journalMenu;
                    break;
                case (6):
                    //WorldMap Menu
                    currentMenu = worldMapMenu;
                    break;
                case (7):
                    //Options Menu
                    currentMenu = optionsMenu;
                    break;
                case (8):
                    //Save/Load Menu
                    currentMenu = saveMenu;
                    break;
                case (9):
                    //Exit the Menu State
                    currentMenu = exitMenu;
                    break;
            }
        } else {
            if (keyboardstate[KeyEvent.VK_BACK_SPACE][1]) {
                state = 0;
                currentMenu.setActive(false);
            }
            currentMenu.update(elapsedTime, keyboardstate);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, windowWidth / 3, windowHeight);
        cardsButton.draw(g2d);
        itemsButton.draw(g2d);
        equipmentButton.draw(g2d);
        statusButton.draw(g2d);
        journalButton.draw(g2d);
        worldMapButton.draw(g2d);
        optionsButton.draw(g2d);
        saveButton.draw(g2d);
        exitButton.draw(g2d);
        currentMenu.draw(g2d);
        if (state == 0) {
            leftScroller.draw(g2d);
        } else {

        }
    }

    @Override
    public void onEnter(Framework framework) {

    }

    @Override
    public void onExit() {

    }
}

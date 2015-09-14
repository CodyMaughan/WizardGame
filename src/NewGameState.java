import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/11/2015.
 */
public class NewGameState implements IState{

    public static int frameWidth; // window width
    public static int frameHeight; // window height
    private int scrollerBuffer; // distance the scroller is placed from the widest menu option
    private BufferedImage backgroundImg; // bacground image
    private MenuButton nameMenuButton; // the name button
    private MenuButton startMenuButton; // the start button
    private MenuButton mainmenuMenuButton; // the main menu button
    private MenuPointer scroller; // the menu scroller
    private TypingText wizardName; // the wizard naming typing text
    private boolean inNamingMode;
    private long nameTime;
    private final long cursorTimer = 800000000L;

    public NewGameState(Framework framework) {

        // Get the size of the window.
        frameWidth = framework.getWidth();
        frameHeight = framework.getHeight();

        // Load the Background Image.
        try {
            URL backgroundImgUrl = this.getClass().getResource("/resources/images/background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }

        Font buttonFont = new Font("Arial", Font.BOLD, 20); //Font used for the buttons
        // Create the Name MenuButton
        nameMenuButton = new MenuButton("Name Your Wizard", 0, 250, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        nameMenuButton.setPosition(frameWidth / 2 - nameMenuButton.getWidth() / 2, 250);
        // Create the Start MenuButton
        startMenuButton = new MenuButton("Start Your Adventure", 0, 320, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        startMenuButton.setPosition(frameWidth / 2 - startMenuButton.getWidth() / 2, 320); //Center the MenuButton Horizontally
        // Create the Back to Main Menu MenuButton
        mainmenuMenuButton = new MenuButton("Back to Main Menu", 0, 390, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        mainmenuMenuButton.setPosition(frameWidth / 2 - mainmenuMenuButton.getWidth() / 2, 390); //Center the MenuButton Horizontally

        // This code is used to determine the widest button and it's width
        int[] widths = new int[3];
        widths[0] = nameMenuButton.getWidth();
        widths[1] = startMenuButton.getWidth();
        widths[2] = mainmenuMenuButton.getWidth();
        int maxWidth = widths[0];
        for (int i = 1; i < 3; i++) {
            if (widths[i] > maxWidth) {
                maxWidth = widths[i];
            }
        }

        scrollerBuffer = 40; //Set the distance we want the scroller from the widest button
        // Initialize the BufferedImage variable that will be used to create the scroller
        BufferedImage scrollerImage = null;
        // Load the Scroller Image
        try {
            scrollerImage = ImageIO.read(this.getClass().getResource("/resources/images/pointerFinger.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Create the Menu Scroller
        scroller = new MenuPointer(scrollerImage, 0, 0, 70, 3, 1);
        // Set the scroller to the left of the New Game MenuButton
        scroller.setPosition(frameWidth/2 - maxWidth/2 - scrollerBuffer - scroller.getWidth(), nameMenuButton.getY());
        // Initialize the wizard name string
        wizardName = new TypingText("My Wizard's Name: ", "", 0, 160, new Font("Arial", Font.PLAIN, 40), Color.BLACK, (Graphics2D)framework.getGraphics());
        wizardName.setPosition(frameWidth/2 - wizardName.getWidth()/2, 160);
        wizardName.setTypingFont(new Font("Rage Italic", Font.BOLD, 40));
        inNamingMode = false;
    }

    @Override
    public void Update(float elapsedTime, boolean[][] keyboardstate, StateMachine gameStateMachine) {
        // Handle Input from the Keyboard
        if (inNamingMode) { // If the user has clicked the Name MenuButton and is naming the hero
            nameTime += elapsedTime;
            if (keyboardstate[KeyEvent.VK_ENTER][1]) {
                inNamingMode = false; // Pressing Enter Exits Naming Mode
            } else if (keyboardstate[KeyEvent.VK_BACK_SPACE][1]) {
                wizardName.removeLastCharacter(); // Pressing Backspace Removes the Last Letter in the Name
            } else {
                KeyMapper.getInstance(); // Makes Sure the KeyMapper (Singleton) Class is initialized
                for (Integer key : KeyMapper.getTextMap().keySet()) { // Goes through the KeyEvent to Text Map to Check if any Text Keys have been pressed.
                    if (keyboardstate[key][1]) {
                        wizardName.append(KeyMapper.getText(key)); // Pressing any other text button adds it to the name.
                    }
                }
            }

            if (nameTime > cursorTimer) {
                nameTime = 0;
            } else if (nameTime > cursorTimer/2){
                wizardName.setDrawCursor(false);
            } else {
                wizardName.setDrawCursor(true);
            }
            wizardName.resetTextSize((Graphics2D)gameStateMachine.getFramework().getGraphics());
            wizardName.setPosition(frameWidth/2 - wizardName.getWidth()/2, 160);
            if (!inNamingMode) { // This branch will only activate if we have exited naming mode
                wizardName.setDrawCursor(false); // Set draw typing cursor false
            }
        } else { // If the user is not naming the character (allows menu scrolling, ect.)
            if (keyboardstate[KeyEvent.VK_ENTER][1]) { // Handle the Enter Key is pressed
                switch (scroller.count) {
                    case (1):
                        inNamingMode = true;
                        nameTime = 0;
                        break;
                    case (2):
                        // Start Your Game
                        if (gameStateMachine.isState("StartGame")) {
                            gameStateMachine.Change("StartGame", gameStateMachine.getFramework());
                        } else {
                            BufferedImage chosenImage = null;
                            try {
                                chosenImage = ImageIO.read(this.getClass().getResource("/resources/images/Character1.png"));
                            }
                            catch (IOException ex) {
                                Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            gameStateMachine.Add("StartGame", new StartGameState(gameStateMachine.getFramework(), wizardName.getTypingText(), chosenImage));
                            gameStateMachine.Change("StartGame", gameStateMachine.getFramework());
                        }
                        break;
                    case (3):
                        if (gameStateMachine.isState("MainMenu")) {
                            gameStateMachine.Change("MainMenu", gameStateMachine.getFramework());
                        } else {
                            gameStateMachine.Add("MainMenu", new MainMenuState(gameStateMachine.getFramework()));
                            gameStateMachine.Change("MainMenu", gameStateMachine.getFramework());
                        }
                        break;
                }
            } else {
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
        // Update the scroller (Animations, ect.);
        scroller.update(elapsedTime);
    }

    @Override
    public void Draw(Graphics2D g2d) {
        g2d.drawImage(backgroundImg, 0, 0, frameWidth, frameHeight, null); // Draw the Background Image
        wizardName.draw(g2d);
        nameMenuButton.draw(g2d); // Draw the Name MenuButton
        startMenuButton.draw(g2d); // Draw the New Game MenuButton
        mainmenuMenuButton.draw(g2d); // Draw the Load Game MenuButton
        if (!inNamingMode) {
            scroller.draw(g2d); // Draw the Scroller if not in naming mode
        }
    }

    @Override
    public void OnEnter(Framework framework) {

    }

    @Override
    public void OnExit() {

    }
}

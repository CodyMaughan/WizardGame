import javax.imageio.ImageIO;
import javax.naming.AuthenticationException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/10/2015.
 */
public class MainMenuState implements IState {


    public static int frameWidth; // window width
    public static int frameHeight; // window height
    private int scrollerBuffer; // distance the scroller is placed from the widest menu option
    private BufferedImage backgroundImg; // image used for the background
    private Button newgameButton; // the new game button
    private Button loadgameButton; // the load game button
    private Button optionsButton; // the options button
    private Button exitButton; // the exit button
    private MenuPointer scroller; // the menu scroller

    public MainMenuState(Framework framework) {

        // Get the window size
        frameWidth = framework.getWidth();
        frameHeight = framework.getHeight();
        // Load the Background Image
        try {
            URL moonLanderMenuImgUrl = this.getClass().getResource("/resources/images/menu.jpg");
            backgroundImg = ImageIO.read(moonLanderMenuImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }

        Font buttonFont = new Font("Arial", Font.BOLD, 15); //Font used for the buttons
        // Create the New Game Button
        newgameButton = new Button("New Game", 0, 300, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        newgameButton.setPosition(frameWidth / 2 - newgameButton.getWidth() / 2, 300);//Center the button
        // Create the Load Game Button
        loadgameButton = new Button("Load Game", 0, 360, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        loadgameButton.setPosition(frameWidth / 2 - loadgameButton.getWidth() / 2, 360);//Center the button
        // Create the Options Button
        optionsButton = new Button("Options", 0, 420, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        optionsButton.setPosition(frameWidth / 2 - optionsButton.getWidth() / 2, 420);//Center the button
        // Create the Exit Button
        exitButton = new Button("Exit Game", 0, 480, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        exitButton.setPosition(frameWidth / 2 - exitButton.getWidth() / 2, 480);//Center the button

        // This code is used to determine the widest button and it's width
        int[] widths = new int[4];
        widths[0] = newgameButton.getWidth();
        widths[1] = loadgameButton.getWidth();
        widths[2] = optionsButton.getWidth();
        widths[3] = exitButton.getWidth();
        int maxWidth = widths[0];
        for (int i = 1; i < 4; i++) {
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
        scroller = new MenuPointer(scrollerImage, 0, 0, 60, 4, 1);
        // Set the scroller to the left of the New Game Button
        scroller.setPosition(frameWidth/2 - maxWidth/2 - scrollerBuffer - scroller.getWidth(), newgameButton.getY());
        BufferedImage tileTest = null;
        try {
            tileTest = ImageIO.read(this.getClass().getResource("/resources/images/tilemap1.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        int testWidth = tileTest.getWidth();
        int testHeight = tileTest.getHeight();
        TileMap test = new TestMap(tileTest, testWidth/3, testHeight/5);
        test.toString();
    }


    @Override
    // Handle Input from the Keyboard
    public void Update(float elapsedTime, boolean[][] keyboardstate, StateMachine gameStateMachine) {
        if (keyboardstate[KeyEvent.VK_ENTER][1]) { // Handle the Enter Key is pressed
            switch (scroller.count) {
                case (1):
                    gameStateMachine.Add("NewGame", new NewGameState(gameStateMachine.getFramework()));
                    gameStateMachine.Change("NewGame", gameStateMachine.getFramework());
                    break;
                case (2):
                    //Load Game
                    break;
                case (3):
                    //Options
                    break;
                case (4):
                    System.exit(0); // I think this is the best option. It terminates the JVM completely.
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
        // Update the scroller (Animations, ect.);
        scroller.update(elapsedTime);
    }

    @Override
    public void Draw(Graphics2D g2d) {
        g2d.drawImage(backgroundImg, 0, 0, frameWidth, frameHeight, null); // Draw the Background Image
        newgameButton.draw(g2d); // Draw the New Game Button
        loadgameButton.draw(g2d); // Draw the Load Game Button
        optionsButton.draw(g2d); // Draw the Options Button
        exitButton.draw(g2d); // Draw the Exit Button
        scroller.draw(g2d); // Draw the Scroller
    }

    @Override
    public void OnEnter(Framework framework) {

    }

    @Override
    public void OnExit() {

    }

}

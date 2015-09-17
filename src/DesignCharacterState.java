import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/14/2015.
 */
public class DesignCharacterState implements IState {

    private int windowWidth;
    private int windowHeight;
    private int width;
    private int height;
    private BufferedImage[] characters;
    private int characterWidth;
    private int characterHeight;
    private int spacing;
    private MenuScroller2D scroller;
    private Font titleFont;
    private int designState; // 0 = Character Selection, 1 = Character Confirmation
    private int characterChoice = 0;
    // Variables Used for Character Confirmation
    private MenuButton yesButton;
    private MenuButton noButton;
    private MenuButton cancelButton;
    private MenuScroller2D scroller2;
    private String name;
    private Font buttonFont;
    private int maxButtonWidth;
    private Font messageFont;

    private final int characterCount = 6;
    private final int characterAnimationFrames = 3;
    private final int directions = 4;
    private final int drawScale = 1;
    private final int maxCol = 4;
    private final String title = "Character Selection";
    private final String confirmationMessage = "Choose this Character?";

    public DesignCharacterState(Framework framework, String name) {
        windowWidth = framework.getWidth();
        windowHeight = framework.getHeight();
        width = 8*windowWidth/10;
        height = 8*windowHeight/10;
        characters = new BufferedImage[characterCount];
        for (int i = 0; i < characterCount; i++) {
            BufferedImage source = null;
            try {
                source = ImageIO.read(this.getClass().getResource("/resources/images/Character" + String.valueOf(i) + ".png"));
            }
            catch (IOException ex) {
                Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
            }
            characterWidth = source.getWidth()/characterAnimationFrames;
            characterHeight = source.getHeight()/directions;
            characters[i] = new BufferedImage(characterWidth, characterHeight, source.getType());
            Graphics2D gr = characters[i].createGraphics();
            gr.drawImage(source, 0, 0, characterWidth, characterHeight, 0, 0, characterWidth, characterHeight, null);
            gr.dispose();
        }
        characterWidth = characters[0].getWidth();
        characterHeight = characters[0].getWidth();
        characterWidth = drawScale*characterWidth;
        characterHeight = drawScale*characterHeight;
        spacing = (width - maxCol*characterWidth)/(maxCol + 1);
        BufferedImage scrollerImage = null;
        // Load the Scroller Image
        try {
            scrollerImage = ImageIO.read(this.getClass().getResource("/resources/images/pointerFinger.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        int scrollerBuffer = spacing/4;
        scroller = new MenuScroller2D(scrollerImage, (windowWidth - width)/2 + scrollerBuffer, (windowHeight - height) / 2 + 2*spacing + characterHeight/4,
                spacing + characterWidth, spacing + characterHeight, 4, Math.floorDiv(characterCount, 4) + 1, characterCount, 1, 1);
        scroller.setWidth(spacing / 2);
        scroller.setHeight(characterHeight/2);
        titleFont = new Font("Arial", Font.BOLD, spacing/2);
        designState = 0;
        // Variables Used for Character Confirmation
        this.name = name;
        buttonFont = new Font("Arial", Font.BOLD, spacing/4);
        yesButton = new MenuButton("Yes!", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        noButton = new MenuButton("No", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        cancelButton = new MenuButton("Cancel", 0, 0, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        maxButtonWidth = cancelButton.getWidth();
        int xButtonSpacing = (width - 3*maxButtonWidth)/4;
        int x = (windowWidth - width)/2 + xButtonSpacing;
        int y = height - (2*spacing + scroller.getLastRow()*(spacing + characterHeight));
        int yButtonSpacing = (y - cancelButton.getHeight())/2;
        y = (windowHeight - height)/2 + 2*spacing + scroller.getLastRow()*(spacing + characterHeight) + yButtonSpacing;
        yesButton.setPosition(x, y);
        yesButton.setWidth(maxButtonWidth);
        noButton.setPosition(x + xButtonSpacing + maxButtonWidth, y);
        noButton.setWidth(maxButtonWidth);
        cancelButton.setPosition(x + 2 * (xButtonSpacing + maxButtonWidth), y);
        scroller2 = new MenuScroller2D(scrollerImage, 0, 0, xButtonSpacing + maxButtonWidth, 0, 3, 1, 3, 1, 1);
        scroller2.setHeight(yesButton.getHeight());
        scroller2.setWidth(scroller2.getHeight());
        scrollerBuffer = (yesButton.getX() - (windowWidth - width)/2 - scroller2.getWidth())/2;
        scroller2.setPosition((windowWidth - width)/2 + scrollerBuffer, yesButton.getY());
        messageFont = buttonFont;
    }

    @Override
    public void Update(float elapsedTime, boolean[][] keyboardstate, StateMachine gameStateMachine) {
        if (designState == 0) {
            if (keyboardstate[KeyEvent.VK_ENTER][1]) {
                designState = 1;
                characterChoice = 4*(scroller.getCountY() - 1) + (scroller.getCountX() - 1);
            } else {
                if (!keyboardstate[KeyEvent.VK_S][0] && !keyboardstate[KeyEvent.VK_W][0] && !keyboardstate[KeyEvent.VK_A][0] && !keyboardstate[KeyEvent.VK_D][0]) { // Handle the absence of scrolling
                    // Reset the scroller action variables
                    scroller.scrollDirection = -1;
                    scroller.scrollTimer = 0;
                } else {
                    if (keyboardstate[KeyEvent.VK_S][0] && keyboardstate[KeyEvent.VK_W][0]) {
                        scroller.scrollDirection = -1;
                        scroller.scrollTimer = 0;
                    } else if (keyboardstate[KeyEvent.VK_S][0]) {
                        if (scroller.scrollDirection == 0) {                 // If the scroller was already scrolling down...
                            scroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                            if (scroller.scrollTimer > scroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                                scroller.scrollTimer -= scroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                scroller.scrollDown();                       // And finally we scroll down.
                            }
                        } else {                                             // If the scroller was not already scrolling down...
                            scroller.scrollDown();                           // We scroll down...
                            scroller.scrollDirection = 0;                    // Set the scroll direction to down...
                            scroller.scrollTimer = 0;                        // And reset the scrollTimer.
                        }
                    } else if (keyboardstate[KeyEvent.VK_W][0]) {
                        if (scroller.scrollDirection == 3) {                // If the scroller was already scrolling up...
                            scroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                            if (scroller.scrollTimer > scroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                                scroller.scrollTimer -= scroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                scroller.scrollUp();                         // And finally we scroll up.
                            }
                        } else {                                             // If the scroller was not already scrolling up...
                            scroller.scrollUp();                             // We scroll up...
                            scroller.scrollDirection = 3;                   // Set the scroll direction to up...
                            scroller.scrollTimer = 0;                        // And reset the scrollTimer.
                        }
                    }
                    if (keyboardstate[KeyEvent.VK_A][0] && keyboardstate[KeyEvent.VK_D][0]) {
                        scroller.scrollDirection = -1;
                        scroller.scrollTimer = 0;
                    } else if (keyboardstate[KeyEvent.VK_A][0]) {
                        if (scroller.scrollDirection == 1) {                 // If the scroller was already scrolling down...
                            scroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                            if (scroller.scrollTimer > scroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                                scroller.scrollTimer -= scroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                scroller.scrollLeft();                       // And finally we scroll down.
                            }
                        } else {                                             // If the scroller was not already scrolling down...
                            scroller.scrollLeft();                           // We scroll down...
                            scroller.scrollDirection = 1;                    // Set the scroll direction to down...
                            scroller.scrollTimer = 0;                        // And reset the scrollTimer.
                        }
                    } else if (keyboardstate[KeyEvent.VK_D][0]) {
                        if (scroller.scrollDirection == 2) {                // If the scroller was already scrolling up...
                            scroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                            if (scroller.scrollTimer > scroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                                scroller.scrollTimer -= scroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                scroller.scrollRight();                         // And finally we scroll up.
                            }
                        } else {                                             // If the scroller was not already scrolling up...
                            scroller.scrollRight();                             // We scroll up...
                            scroller.scrollDirection = 2;                   // Set the scroll direction to up...
                            scroller.scrollTimer = 0;                        // And reset the scrollTimer.
                        }
                    }
                }
            }
        } else if (designState == 1) {
            if(keyboardstate[KeyEvent.VK_ENTER][1]) {
                switch(scroller2.getCountX()) {
                    case(1):
                        BufferedImage chosenImage = null;
                        try {
                            chosenImage = ImageIO.read(this.getClass().getResource("/resources/images/Character" + String.valueOf(characterChoice) + ".png"));
                        }
                        catch (IOException ex) {
                            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        gameStateMachine.Add("StartGame", new StartGameState(gameStateMachine.getFramework(), name, chosenImage));
                        SoundManager.getInstance().stopSound("MainMenuMusic");
                        SoundManager.remove("MainMenuMusic");
                        gameStateMachine.Change("StartGame", gameStateMachine.getFramework());
                        break;
                    case(2):
                        designState = 0;
                        scroller2.scrollLeft();
                        break;
                    case(3):
                        gameStateMachine.Change("NewGame", gameStateMachine.getFramework());
                        break;
                }
            } else {
                if (!keyboardstate[KeyEvent.VK_A][0] && !keyboardstate[KeyEvent.VK_D][0]) { // Handle the absence of scrolling
                    // Reset the scroller action variables
                    scroller2.scrollDirection = -1;
                    scroller2.scrollTimer = 0;
                } else {
                    if (keyboardstate[KeyEvent.VK_A][0]) { // Handle the A Key is down
                        // Decide whether to scroll left or not
                        if (scroller2.scrollDirection == 1) {                 // If the scroller was already scrolling left...
                            scroller2.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                            if (scroller2.scrollTimer > scroller2.TIMER_MAX) { // And determine if we've waited to long enough ...
                                scroller2.scrollTimer -= scroller2.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                scroller2.scrollLeft();                       // And finally we scroll left.
                            }
                        } else {                                             // If the scroller was not already scrolling left...
                            scroller2.scrollLeft();                           // We scroll left...
                            scroller2.scrollDirection = 1;                    // Set the scroll direction to left...
                            scroller2.scrollTimer = 0;                        // And reset the scrollTimer.
                        }
                    }
                    if (keyboardstate[KeyEvent.VK_D][0]) { // Handle the D Key is down
                        // Decide whether to scroll right or not
                        if (scroller2.scrollDirection == 2) {                // If the scroller was already scrolling right...
                            scroller2.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                            if (scroller2.scrollTimer > scroller2.TIMER_MAX) { // And determine if we've waited to long enough ...
                                scroller2.scrollTimer -= scroller2.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                scroller2.scrollRight();                         // And finally we scroll right.
                            }
                        } else {                                             // If the scroller was not already scrolling right...
                            scroller2.scrollRight();                             // We scroll right...
                            scroller2.scrollDirection = 2;                   // Set the scroll direction to right...
                            scroller2.scrollTimer = 0;                        // And reset the scrollTimer.
                        }
                    }
                }
            }
        }
    }

    @Override
    public void Draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect((windowWidth - width) / 2, (windowHeight - height) / 2, width, height, spacing / 2, spacing / 2);
        int width1 = 0;
        int height1 = 0;
        if (designState == 0) {
            scroller.draw(g2d);
        } else if (designState == 1) {
            g2d.setColor(Color.YELLOW);
            g2d.fillRoundRect((windowWidth - width) / 2 + spacing + (scroller.getCountX() - 1) * (spacing + characterWidth),
                    (windowHeight - height) / 2 + 2 * spacing + (scroller.getCountY() - 1) * (spacing + characterHeight),
                    characterWidth, characterHeight, characterWidth / 8, characterHeight / 8);
            yesButton.draw(g2d);
            noButton.draw(g2d);
            cancelButton.draw(g2d);
            scroller2.draw(g2d);
            g2d.setFont(messageFont);
            g2d.setColor(Color.BLACK);
            width1 = (int)(messageFont.getStringBounds(confirmationMessage,g2d.getFontRenderContext()).getWidth());
            height1 = (int)(messageFont.getStringBounds(confirmationMessage,g2d.getFontRenderContext()).getHeight());
            g2d.drawString(confirmationMessage, (windowWidth - width1) / 2, yesButton.getY() - (spacing - height1)/2);
        }
        int row = 0;
        int col = 0;
        for (int i = 0; i < characterCount; i++) {
            int x = (windowWidth - width) / 2 + spacing + col*(spacing + characterWidth);
            int y = (windowHeight - height) / 2 + 2*spacing + row*(spacing + characterHeight);
            g2d.drawImage(characters[i], x, y, characterWidth, characterHeight, null);
            col++;
            if (col >= maxCol) {
                col = 0;
                row++;
            }
        }
        g2d.setColor(Color.BLACK);
        g2d.setFont(titleFont);
        width1 = (int)(titleFont.getStringBounds(title,g2d.getFontRenderContext()).getWidth());
        height1 = (int)(titleFont.getStringBounds(title,g2d.getFontRenderContext()).getHeight());
        g2d.drawString(title, (windowWidth - width1) / 2, (windowHeight - height + spacing - height1) / 2 + spacing);
    }

    @Override
    public void OnEnter(Framework framework) {

    }

    @Override
    public void OnExit() {

    }

    public void setTitleFont(Font font) {
        this.titleFont = font;
    }
}

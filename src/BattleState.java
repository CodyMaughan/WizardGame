import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/16/2015.
 */
public class BattleState implements IState, GameEvent {

    // Define Global Variable Here
    private int windowWidth;
    private int windowHeight;
    private MenuButton cardButton;
    private MenuButton itemButton;
    private MenuButton drawButton;
    private MenuButton runButton;
    private int buttonWidth;
    private MenuScroller2D actionScroller;
    private int turn; // 0 = MainCharacter turn, 1 = Enemy turn, 2 = MainCharacter Victory
    private int turnState; // 0 = actionDecision, 1 = chooseCard to play, 2 = chooseItem to play, 3 = drawCard, 4 = run
                           // 5 = play card,
    private BufferedImage enemyImage;
    private BufferedImage mainCharacterImage;
    private Character enemy;
    private IndexedLinkedHashMap<String, Card> hand;
    private int deckCount;
    private MenuScroller2D cardScroller;
    private Card playedCard;

    // Initialization of the Battle State
    public BattleState(Framework framework, Character character) {
        windowWidth = framework.getWidth();
        windowHeight = framework.getHeight();
        enemy = character;
        turn = 0;
        turnState = 0;
        Font buttonFont = new Font("Arial", Font.BOLD, 15);
        int xButtonSpacing = 40;
        int yButtonSpacing = 35;
        cardButton = new MenuButton("Cast Card", xButtonSpacing, 3 * windowHeight / 4 + yButtonSpacing, buttonFont,
                (Graphics2D)framework.getGraphics(), 5, 5);
        itemButton = new MenuButton("Use Item", xButtonSpacing, cardButton.getY() + cardButton.getHeight() + yButtonSpacing,
                buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        drawButton = new MenuButton("Draw Card", cardButton.getX() + cardButton.getWidth() + xButtonSpacing,
                cardButton.getY(), buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        runButton = new MenuButton("Run", cardButton.getX() + cardButton.getWidth() + xButtonSpacing,
                cardButton.getY() + cardButton.getHeight() + yButtonSpacing, buttonFont, (Graphics2D)framework.getGraphics(), 5, 5);
        buttonWidth = drawButton.getWidth();
        int buttonHeight = drawButton.getHeight();
        cardButton.setWidth(buttonWidth);
        itemButton.setWidth(buttonWidth);
        runButton.setWidth(buttonWidth);
        drawButton.setPosition(cardButton.getX() + cardButton.getWidth() + xButtonSpacing, drawButton.getY());
        runButton.setPosition(cardButton.getX() + cardButton.getWidth() + xButtonSpacing, runButton.getY());
        int scrollerBuffer = 5;
        BufferedImage scrollerImage = null;
        // Load the Scroller Image
        try {
            scrollerImage = ImageIO.read(this.getClass().getResource("/resources/images/pointerFinger.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        actionScroller = new MenuScroller2D(scrollerImage, cardButton.getX() - scrollerBuffer, cardButton.getY(),
                buttonWidth + xButtonSpacing, buttonHeight + yButtonSpacing, 2, 2, 4, 1, 1);
        actionScroller.setWidth(cardButton.getHeight());
        actionScroller.setHeight(cardButton.getHeight());
        actionScroller.setPosition(cardButton.getX() - actionScroller.getWidth() - scrollerBuffer, cardButton.getY());
        enemyImage = character.getBattleImage();
        mainCharacterImage = MainCharacter.getBattleImage();
        SoundManager.add("BattleMusic",
                new Sound(this.getClass().getResource("/resources/sounds/Woodland_Fantasy_0.wav"), 0));
        hand = new IndexedLinkedHashMap<>();
        deckCount = 0;
        if (MainCharacter.maxHand <= MainCharacter.deck.size()) {
            for (int i = 0; i < MainCharacter.maxHand; i++) {
                hand.put(MainCharacter.deck.get(i).getName(), MainCharacter.deck.get(i));
                deckCount++;
            }
        } else {
            for (int i = 0; i < MainCharacter.deck.size(); i++) {
                hand.put(MainCharacter.deck.get(i).getName(), MainCharacter.deck.get(i));
                deckCount++;
            }
        }
        cardScroller = new MenuScroller2D(scrollerImage, drawButton.getX() + drawButton.getWidth() + 10,
                drawButton.getY() + hand.getIndexed(0).getHeight()/2 - 10, hand.getIndexed(0).getWidth() + 40, 0, deckCount, 1, deckCount, 1, 1);
        cardScroller.setWidth(actionScroller.getWidth());
        cardScroller.setHeight(actionScroller.getHeight());
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {
        // All the main battle logic will go here
        if (turn == 0) { // If it's the MainCharacter turn we do something
            if (turnState == 0) {
                if (keyboardstate[KeyEvent.VK_ENTER][1]) {
                    if (actionScroller.getCountX() == 1 && actionScroller.getCountY() == 1) {
                        turnState = 1;
                        System.out.println(turnState);
                    } else if (actionScroller.getCountX() == 1 && actionScroller.getCountY() == 2) {
                        turnState = 2;
                        System.out.println(turnState);
                    } else if (actionScroller.getCountX() == 2 && actionScroller.getCountY() == 1) {
                        turnState = 3;
                        System.out.println(turnState);
                    } else if (actionScroller.getCountX() == 2 && actionScroller.getCountY() == 2) {
                        turnState = 4;
                        System.out.println(turnState);
                    }
                } else {
                    if (!keyboardstate[KeyEvent.VK_S][0] && !keyboardstate[KeyEvent.VK_W][0] && !keyboardstate[KeyEvent.VK_A][0] && !keyboardstate[KeyEvent.VK_D][0]) { // Handle the absence of scrolling
                        // Reset the scroller action variables
                        actionScroller.scrollDirection = -1;
                        actionScroller.scrollTimer = 0;
                    } else {
                        if (keyboardstate[KeyEvent.VK_S][0] && keyboardstate[KeyEvent.VK_W][0]) {
                            actionScroller.scrollDirection = -1;
                            actionScroller.scrollTimer = 0;
                        } else if (keyboardstate[KeyEvent.VK_S][1]) {
                            if (actionScroller.scrollDirection == 0) {                 // If the scroller was already scrolling down...
                                actionScroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                                if (actionScroller.scrollTimer > actionScroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                                    actionScroller.scrollTimer -= actionScroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                    actionScroller.scrollDown();                       // And finally we scroll down.
                                }
                            } else {                                             // If the scroller was not already scrolling down...
                                actionScroller.scrollDown();                           // We scroll down...
                                actionScroller.scrollDirection = 0;                    // Set the scroll direction to down...
                                actionScroller.scrollTimer = 0;                        // And reset the scrollTimer.
                            }
                        } else if (keyboardstate[KeyEvent.VK_W][1]) {
                            if (actionScroller.scrollDirection == 3) {                // If the scroller was already scrolling up...
                                actionScroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                                if (actionScroller.scrollTimer > actionScroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                                    actionScroller.scrollTimer -= actionScroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                    actionScroller.scrollUp();                         // And finally we scroll up.
                                }
                            } else {                                             // If the scroller was not already scrolling up...
                                actionScroller.scrollUp();                             // We scroll up...
                                actionScroller.scrollDirection = 3;                   // Set the scroll direction to up...
                                actionScroller.scrollTimer = 0;                        // And reset the scrollTimer.
                            }
                        }
                        if (keyboardstate[KeyEvent.VK_A][0] && keyboardstate[KeyEvent.VK_D][0]) {
                            actionScroller.scrollDirection = -1;
                            actionScroller.scrollTimer = 0;
                        } else if (keyboardstate[KeyEvent.VK_A][1]) {
                            if (actionScroller.scrollDirection == 1) {                 // If the scroller was already scrolling down...
                                actionScroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                                if (actionScroller.scrollTimer > actionScroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                                    actionScroller.scrollTimer -= actionScroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                    actionScroller.scrollLeft();                       // And finally we scroll down.
                                }
                            } else {                                             // If the scroller was not already scrolling down...
                                actionScroller.scrollLeft();                           // We scroll down...
                                actionScroller.scrollDirection = 1;                    // Set the scroll direction to down...
                                actionScroller.scrollTimer = 0;                        // And reset the scrollTimer.
                            }
                        } else if (keyboardstate[KeyEvent.VK_D][1]) {
                            if (actionScroller.scrollDirection == 2) {                // If the scroller was already scrolling up...
                                actionScroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                                if (actionScroller.scrollTimer > actionScroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                                    actionScroller.scrollTimer -= actionScroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                    actionScroller.scrollRight();                         // And finally we scroll up.
                                }
                            } else {                                             // If the scroller was not already scrolling up...
                                actionScroller.scrollRight();                             // We scroll up...
                                actionScroller.scrollDirection = 2;                   // Set the scroll direction to up...
                                actionScroller.scrollTimer = 0;                        // And reset the scrollTimer.
                            }
                        }
                    }
                }
            } else if (turnState == 1) {
                if (keyboardstate[KeyEvent.VK_BACK_SPACE][1]) {
                    turnState = 0;
                } else if (keyboardstate[KeyEvent.VK_ENTER][1]) {
                    turnState = 5;
                    String name = hand.getIndexed(cardScroller.getCountX() - 1).getName();
                    playedCard = hand.get(name);
                    hand.remove(name);
                    cardScroller.setMenuCountX(hand.size());
                    cardScroller.setCountX(cardScroller.getCountX() - 1);
                    if (cardScroller.getCountX() <= 0) {
                        cardScroller.setCountX(1);
                    }
                } else {
                    if (!keyboardstate[KeyEvent.VK_S][0] && !keyboardstate[KeyEvent.VK_W][0] && !keyboardstate[KeyEvent.VK_A][0] && !keyboardstate[KeyEvent.VK_D][0]) { // Handle the absence of scrolling
                        // Reset the scroller action variables
                        cardScroller.scrollDirection = -1;
                        cardScroller.scrollTimer = 0;
                    } else {
                        if (keyboardstate[KeyEvent.VK_S][0] && keyboardstate[KeyEvent.VK_W][0]) {
                            cardScroller.scrollDirection = -1;
                            cardScroller.scrollTimer = 0;
                        } else if (keyboardstate[KeyEvent.VK_S][1]) {
                            if (cardScroller.scrollDirection == 0) {                 // If the scroller was already scrolling down...
                                cardScroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                                if (cardScroller.scrollTimer > cardScroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                                    cardScroller.scrollTimer -= cardScroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                    cardScroller.scrollDown();                       // And finally we scroll down.
                                }
                            } else {                                             // If the scroller was not already scrolling down...
                                cardScroller.scrollDown();                           // We scroll down...
                                cardScroller.scrollDirection = 0;                    // Set the scroll direction to down...
                                cardScroller.scrollTimer = 0;                        // And reset the scrollTimer.
                            }
                        } else if (keyboardstate[KeyEvent.VK_W][1]) {
                            if (cardScroller.scrollDirection == 3) {                // If the scroller was already scrolling up...
                                cardScroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                                if (cardScroller.scrollTimer > cardScroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                                    cardScroller.scrollTimer -= cardScroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                    cardScroller.scrollUp();                         // And finally we scroll up.
                                }
                            } else {                                             // If the scroller was not already scrolling up...
                                cardScroller.scrollUp();                             // We scroll up...
                                cardScroller.scrollDirection = 3;                   // Set the scroll direction to up...
                                cardScroller.scrollTimer = 0;                        // And reset the scrollTimer.
                            }
                        }
                        if (keyboardstate[KeyEvent.VK_A][0] && keyboardstate[KeyEvent.VK_D][0]) {
                            cardScroller.scrollDirection = -1;
                            cardScroller.scrollTimer = 0;
                        } else if (keyboardstate[KeyEvent.VK_A][1]) {
                            if (cardScroller.scrollDirection == 1) {                 // If the scroller was already scrolling down...
                                cardScroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                                if (cardScroller.scrollTimer > cardScroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                                    cardScroller.scrollTimer -= cardScroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                    cardScroller.scrollLeft();                       // And finally we scroll down.
                                }
                            } else {                                             // If the scroller was not already scrolling down...
                                cardScroller.scrollLeft();                           // We scroll down...
                                cardScroller.scrollDirection = 1;                    // Set the scroll direction to down...
                                cardScroller.scrollTimer = 0;                        // And reset the scrollTimer.
                            }
                        } else if (keyboardstate[KeyEvent.VK_D][1]) {
                            if (cardScroller.scrollDirection == 2) {                // If the scroller was already scrolling up...
                                cardScroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                                if (cardScroller.scrollTimer > cardScroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                                    cardScroller.scrollTimer -= cardScroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                                    cardScroller.scrollRight();                         // And finally we scroll up.
                                }
                            } else {                                             // If the scroller was not already scrolling up...
                                cardScroller.scrollRight();                             // We scroll up...
                                cardScroller.scrollDirection = 2;                   // Set the scroll direction to up...
                                cardScroller.scrollTimer = 0;                        // And reset the scrollTimer.
                            }
                        }
                    }
                }
            } else if (turnState == 2) {
                if (keyboardstate[KeyEvent.VK_BACK_SPACE][1]) {
                    turnState = 0;
                }
            } else if (turnState == 3) {
                if (keyboardstate[KeyEvent.VK_BACK_SPACE][1]) {
                    turnState = 0;
                }
            } else if (turnState == 4) {
                if (keyboardstate[KeyEvent.VK_BACK_SPACE][1]) {
                    turnState = 0;
                }
            } else if (turnState == 5) {
                if (turn == 0) {
                    enemy.health -= playedCard.getDamage();
                    if (enemy.health > 0) {
                        turn = 1;
                        turnState = 0;
                    } else {
                        enemy.health = 0;
                        turn = 2;
                    }
                } else {
                    turn = 0;
                }
            }
        } else if (turn == 1) { // If it's the Enemy's turn we can't do anything, and the AI decides what to do
            try {
                // Waits a little on the enemy turn, this is just for testing and will be removed when AI is written
                Thread.sleep(2000);
            } catch (InterruptedException ex) { }
            turn = 0;
        } else if (turn == 2) {
            try {
                // Waits a little on the enemy turn, this is just for testing and will be removed when AI is written
                Thread.sleep(2000);
            } catch (InterruptedException ex) { }
            StateMachine.Change("StartGame");
            StateMachine.Remove("BattleState");
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        // After updating we will draw all the objects to the screen
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, windowWidth, windowHeight);
        g2d.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.BOLD, 30);
        g2d.setFont(font);
        int textWidth = (int)(font.getStringBounds("You Are Now In Battle State", g2d.getFontRenderContext()).getWidth());
        g2d.drawString("You Are Now In Battle State", windowWidth / 2 - textWidth / 2, windowHeight / 2);
        // Draw the battle images of the enemy and MainCharacter
        g2d.drawImage(mainCharacterImage, 60, 3 * windowHeight / 4 - 2 * 60, mainCharacterImage.getWidth() * 2, mainCharacterImage.getHeight() * 2, null);
        g2d.drawImage(enemyImage, windowWidth - 60 - enemyImage.getWidth(), 60, enemyImage.getWidth(), enemyImage.getHeight(), null);
        // Draw an action box at the bottom where we decide our actions on each turn
        g2d.setColor(Color.CYAN);
        g2d.fillRect(0, 3 * windowHeight / 4, windowWidth, windowHeight / 4 + 2);
        // Draw the hp and mp bars of the MainCharacter and the Enemy
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 3 * windowHeight / 4, windowWidth, windowHeight / 4 + 2);
        Font hpFont = new Font("Arial", Font.BOLD, 15);
        g2d.setFont(hpFont);
        String text = "Health: " + String.valueOf(MainCharacter.health) + "/" + String.valueOf(MainCharacter.maxHealth);
        textWidth = (int)(hpFont.getStringBounds(text, g2d.getFontRenderContext()).getWidth());
        g2d.drawString(text, 60 + mainCharacterImage.getWidth() * 2 + 45, 3 * windowHeight / 4 - 30 - 15 - 20);
        text = "Mana:  " + String.valueOf(MainCharacter.mana) + "/" + String.valueOf(MainCharacter.maxMana);
        g2d.drawString(text, 60 + mainCharacterImage.getWidth() * 2 + 45, 3 * windowHeight / 4 - 30);
        g2d.setColor(Color.GRAY);
        g2d.fillRoundRect(60 + mainCharacterImage.getWidth()*2 + 45 + textWidth + 20, 3*windowHeight/4 - 45 - 15 - 20, 100, 15, 3, 3);
        g2d.fillRoundRect(60 + mainCharacterImage.getWidth() * 2 + 45 + textWidth + 20, 3 * windowHeight / 4 - 45, 100, 15, 3, 3);
        g2d.setColor(Color.GREEN);
        g2d.fillRoundRect(60 + mainCharacterImage.getWidth() * 2 + 45 + textWidth + 20, 3 * windowHeight / 4 - 45 - 15 - 20,
                (int) (((float) MainCharacter.health / (float) MainCharacter.maxHealth) * 100), 15, 3, 3);
        g2d.setColor(Color.BLUE);
        g2d.fillRoundRect(60 + mainCharacterImage.getWidth() * 2 + 45 + textWidth + 20, 3 * windowHeight / 4 - 45,
                (int) (((float) MainCharacter.mana / (float) MainCharacter.maxMana) * 100), 15, 3, 3);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(60 + mainCharacterImage.getWidth() * 2 + 45 + textWidth + 20, 3 * windowHeight / 4 - 45 - 15 - 20, 100, 15, 3, 3);
        g2d.drawRoundRect(60 + mainCharacterImage.getWidth() * 2 + 45 + textWidth + 20, 3 * windowHeight / 4 - 45, 100, 15, 3, 3);
        // Enemy health and mana bars
        text = "Health: " + String.valueOf(enemy.health) + "/" + String.valueOf(enemy.maxHealth);
        textWidth = (int)(hpFont.getStringBounds(text, g2d.getFontRenderContext()).getWidth());
        g2d.drawString(text, windowWidth - 60 - enemyImage.getWidth() - 45 - 100 - 20 - textWidth, 60 + 15);
        text = "Mana:  " + String.valueOf(enemy.mana) + "/" + String.valueOf(enemy.maxMana);
        g2d.drawString(text, windowWidth - 60 - enemyImage.getWidth() - 45 - 100 - 20 - textWidth, 60 + 2*15 + 20);
        g2d.setColor(Color.GRAY);
        g2d.fillRoundRect(windowWidth - 60 - enemyImage.getWidth() - 45 - 100, 60, 100, 15, 3, 3);
        g2d.fillRoundRect(windowWidth - 60 - enemyImage.getWidth() - 45 - 100, 60 + 15 + 20, 100, 15, 3, 3);
        g2d.setColor(Color.GREEN);
        g2d.fillRoundRect(windowWidth - 60 - enemyImage.getWidth() - 45 - 100, 60,
                (int) (((float) enemy.health / (float) enemy.maxHealth) * 100), 15, 3, 3);
        g2d.setColor(Color.BLUE);
        g2d.fillRoundRect(windowWidth - 60 - enemyImage.getWidth() - 45 - 100, 60 + 15 + 20,
                (int) (((float) enemy.mana / (float) enemy.maxMana) * 100), 15, 3, 3);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(windowWidth - 60 - enemyImage.getWidth() - 45 - 100, 60, 100, 15, 3, 3);
        g2d.drawRoundRect(windowWidth - 60 - enemyImage.getWidth() - 45 - 100, 60 + 15 + 20, 100, 15, 3, 3);
        // If it is MainCharacter turn then we draw the actions in the action box
        if (turn == 0) {
            drawActionBox(g2d);
        } else if (turn == 1) {
            drawEnemyMessage(g2d);
        } else if (turn == 2) {
            drawVictoryMessage(g2d);
        }
        int x = drawButton.getX() + drawButton.getWidth() + 40;
        int y = drawButton.getY();
        int i = 0;
        for (Card card : hand.values()) {
            card.draw(g2d, x + i * (card.getWidth() + 40), y, 0, 0);
            i++;
        }
    }

    private void drawActionBox(Graphics2D g2d) {
        // Draw the MainCharacter and enemy images
        // Draw the buttons
        cardButton.draw(g2d);
        itemButton.draw(g2d);
        drawButton.draw(g2d);
        runButton.draw(g2d);
        if (turnState == 0) {
            actionScroller.draw(g2d);
        } else if (turnState == 1) {
            cardScroller.draw(g2d);
        }
    }

    private void drawEnemyMessage(Graphics2D g2d) {
        Font messageFont = new Font("Arial", Font.BOLD, 15);
        g2d.setFont(messageFont);
        g2d.drawString("The Enemy is deciding", cardButton.getX(), cardButton.getY() + 15);
        g2d.drawString("their move...", cardButton.getX(), cardButton.getY() + 15 + 30);
    }

    private void drawVictoryMessage(Graphics2D g2d) {
        Font messageFont = new Font("Arial", Font.BOLD, 30);
        g2d.setFont(messageFont);
        g2d.drawString("VICTORY!!!", cardButton.getX(), cardButton.getY() + 30);
    }

    @Override
    public void startEvent() {
        // Do entrance animations
    }

    @Override
    public void progressEvent() {
        // We'll use this method to move from one stage of the battle to the next
        // Examples: from entrance animations, to actual battling, to ending the battle and updates of experience and money are shown
    }

    @Override
    public void endEvent() {

    }

    @Override
    public void onEnter(Framework framework) {
        // Start the battle music
    }

    @Override
    public void onExit() {
        // Stop the battle music
    }
}

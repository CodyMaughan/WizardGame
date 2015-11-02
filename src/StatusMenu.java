import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/18/2015.
 */
public class StatusMenu implements Menu {

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
    private MenuPointer healthScroller;
    private MenuPointer statScroller;
    private MenuPointer levelScroller;
    private MenuPointer skillScroller;
    private int state;

    public StatusMenu(MainCharacter character, Framework framework) {
        this.character = character;
        windowWidth = framework.getWidth();
        windowHeight = framework.getHeight();
        menuX = windowWidth/3;
        menuY = 0;
        title = character.characterName + "'s Status";
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
        healthScroller = new MenuPointer(scrollerImage, windowWidth / 3 + 10, 100 + 3 * (20 + 15) / 4 - 17, 3 * (20 + 15) / 2, 2, 1);
        healthScroller.setWidth(20);
        healthScroller.setHeight(20);
        statScroller = new MenuPointer(scrollerImage, windowWidth / 3 + 10, 100 + (15 + 20) * (4) - 20, (20 + 15), StartGameState.character.stats.size(), 1);
        statScroller.setWidth(20);
        statScroller.setHeight(20);
        levelScroller = new MenuPointer(scrollerImage, 2*windowWidth / 3 - 10, 100 - 20 , (20 + 15), 3, 1);
        levelScroller.setWidth(20);
        levelScroller.setHeight(20);
        skillScroller = new MenuPointer(scrollerImage, 2*windowWidth / 3 - 10, 100 + (15 + 20) * (4) - 20, (20 + 15), StartGameState.character.skills.size(), 1);
        skillScroller.setWidth(20);
        skillScroller.setHeight(20);
        state = 0;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(menuX, menuY, 2 * windowWidth / 3, windowHeight);
        g2d.setColor(Color.BLACK);
        g2d.setFont(titleFont);
        g2d.drawString(title, menuX + (windowWidth / 3 - titleWidth / 2), menuY + 50);
        Font headerFont = new Font("Arial", Font.BOLD, 20);
        g2d.setFont(headerFont);
        g2d.drawString("Health", windowWidth / 3 + 40, 100);
        g2d.drawString("Mana", windowWidth / 3 + 40, 100 + 3 * (20 + 15) / 2);
        g2d.drawString("Level:", 2 * windowWidth / 3 + 20, 100);
        g2d.drawString("Stats", windowWidth / 3 + 40, 100 + 3 * (20 + 15) + 5);
        g2d.drawString("Skills", 2 * windowWidth / 3 + 20, 100 + 3 * (20 + 15) + 5);
        Font itemFont = new Font("Arial", Font.BOLD, 15);
        g2d.setFont(itemFont);
        g2d.drawString("Total Exp:", 2*windowWidth / 3 + 20, 100 + (20 + 15));
        g2d.drawString("Exp to nxt Lvl:", 2 * windowWidth / 3 + 20, 100 + 2 * (20 + 15));
        int textWidth = (int)(itemFont.getStringBounds(String.valueOf(StartGameState.character.level), g2d.getFontRenderContext()).getWidth());
        g2d.drawString(String.valueOf(StartGameState.character.level), windowWidth - 40 - textWidth, 100);
        textWidth = (int)(itemFont.getStringBounds(String.valueOf(StartGameState.character.experience), g2d.getFontRenderContext()).getWidth());
        g2d.drawString(String.valueOf(StartGameState.character.experience), windowWidth - 40 - textWidth, 100 + (20 + 15));
        textWidth = (int)(itemFont.getStringBounds(String.valueOf(StartGameState.character.experience), g2d.getFontRenderContext()).getWidth());
        g2d.drawString(String.valueOf(StartGameState.character.experience), windowWidth - 40 - textWidth, 100 + 2 * (20 + 15));
        String text = String.valueOf(StartGameState.character.health) + "/" + String.valueOf(StartGameState.character.maxHealth);
        textWidth = (int)(itemFont.getStringBounds(text, g2d.getFontRenderContext()).getWidth());
        g2d.drawString(text, 2*windowWidth / 3 - 20 - textWidth, 100);
        text = String.valueOf(StartGameState.character.mana) + "/" + String.valueOf(StartGameState.character.maxMana);
        textWidth = (int)(itemFont.getStringBounds(text, g2d.getFontRenderContext()).getWidth());
        g2d.drawString(text, 2*windowWidth / 3 - 20 - textWidth, 100 + 3 * (20 + 15) / 2);
        g2d.setColor(Color.GRAY);
        g2d.fillRoundRect(windowWidth / 3 + 40, 100 + 3 * (20 + 15) / 4 - 17, windowWidth / 3 - 60, 17, 3, 3);
        g2d.fillRoundRect(windowWidth / 3 + 40, 100 + 9 * (20 + 15) / 4 - 17, windowWidth / 3 - 60, 17, 3, 3);
        g2d.setColor(Color.GREEN);
        g2d.fillRoundRect(windowWidth / 3 + 40, 100 + 3 * (20 + 15) / 4 - 17,
                (int) (((float) StartGameState.character.health / (float) StartGameState.character.maxHealth) * (windowWidth / 3 - 60)), 17, 3, 3);
        g2d.setColor(Color.BLUE);
        g2d.fillRoundRect(windowWidth / 3 + 40, 100 + 9 * (20 + 15) / 4 - 17,
                (int) (((float) StartGameState.character.mana / (float) StartGameState.character.maxMana) * (windowWidth / 3 - 60)), 17, 3, 3);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(windowWidth / 3 + 40, 100 + 3 * (20 + 15) / 4 - 17, windowWidth / 3 - 60, 17, 3, 3);
        g2d.drawRoundRect(windowWidth / 3 + 40, 100 + 9 * (20 + 15) / 4 - 17, windowWidth / 3 - 60, 17, 3, 3);
        int i = 4;
        for (String name : StartGameState.character.stats.keySet()) {
            g2d.drawString(name, windowWidth / 3 + 40, 100 + (15 + 20) * (i));
            textWidth = (int)(itemFont.getStringBounds(String.valueOf(StartGameState.character.stats.get(name)), g2d.getFontRenderContext()).getWidth());
            g2d.drawString(String.valueOf(StartGameState.character.stats.get(name)), 2*windowWidth/3 - 20 - textWidth, 100 + (15 + 20)*(i));
            i++;
        }
        i = 4;
        for (String name : StartGameState.character.skills.keySet()) {
            g2d.setColor(SkillCache.getColor(name));
            g2d.fillRoundRect(2 * windowWidth / 3, 100 + 13 + (15 + 20) * (i - 1), windowWidth / 3, 15 + 20 - 2 * 3, 5, 5);
            g2d.setColor(Color.BLACK);
            g2d.drawString(name, 2*windowWidth / 3 + 20, 100 + (15 + 20) * (i));
            textWidth = (int)(itemFont.getStringBounds(String.valueOf(StartGameState.character.skills.get(name)), g2d.getFontRenderContext()).getWidth());
            g2d.drawString(String.valueOf(StartGameState.character.skills.get(name)), windowWidth - 40 - textWidth, 100 + (15 + 20)*(i));
            i++;
        }
        if (active) {
            switch (state) {
                case (0):
                    healthScroller.draw(g2d);
                    break;
                case (1):
                    statScroller.draw(g2d);
                    break;
                case (2):
                    levelScroller.draw(g2d);
                    break;
                case (3):
                    skillScroller.draw(g2d);
                    break;
            }
        }

    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {
        switch (state) {
            case (0):
                if (keyboardstate[KeyEvent.VK_A][1] || keyboardstate[KeyEvent.VK_D][1]) {
                    state = 2;
                    levelScroller.setCount(healthScroller.getCount() + 1);
                } else if (keyboardstate[KeyEvent.VK_W][1] && healthScroller.getCount() == 1) {
                    state = 1;
                    statScroller.setCount(statScroller.getMenuCount());
                } else if (keyboardstate[KeyEvent.VK_S][1] && healthScroller.getCount() == healthScroller.getMenuCount()) {
                    state = 1;
                    statScroller.setCount(1);
                } else {
                    updateScroller(elapsedTime, keyboardstate, healthScroller);
                }
                break;
            case (1):
                if (keyboardstate[KeyEvent.VK_A][1] || keyboardstate[KeyEvent.VK_D][1]) {
                    state = 3;
                    skillScroller.setCount(statScroller.getCount());
                    if (skillScroller.getCount() > skillScroller.getMenuCount()) {
                        skillScroller.setCount(skillScroller.getMenuCount());
                    }
                } else if (keyboardstate[KeyEvent.VK_W][1] && statScroller.getCount() == 1) {
                    state = 0;
                    healthScroller.setCount(healthScroller.getMenuCount());
                } else if (keyboardstate[KeyEvent.VK_S][1] && statScroller.getCount() == statScroller.getMenuCount()) {
                    state = 0;
                    healthScroller.setCount(1);
                } else {
                    updateScroller(elapsedTime, keyboardstate, statScroller);
                }
                break;
            case (2):
                if (keyboardstate[KeyEvent.VK_A][1] || keyboardstate[KeyEvent.VK_D][1]) {
                    state = 0;
                    healthScroller.setCount(levelScroller.getCount() - 1);
                } else if (keyboardstate[KeyEvent.VK_W][1] && levelScroller.getCount() == 1) {
                    state = 3;
                    skillScroller.setCount(skillScroller.getMenuCount());
                } else if (keyboardstate[KeyEvent.VK_S][1] && levelScroller.getCount() == levelScroller.getMenuCount()) {
                    state = 3;
                    skillScroller.setCount(1);
                } else {
                    updateScroller(elapsedTime, keyboardstate, levelScroller);
                }
                break;
            case (3):
                if (keyboardstate[KeyEvent.VK_A][1] || keyboardstate[KeyEvent.VK_D][1]) {
                    state = 1;
                    statScroller.setCount(skillScroller.getCount());
                    if (statScroller.getCount() > statScroller.getMenuCount()) {
                        statScroller.setCount(statScroller.getMenuCount());
                    }
                } else if (keyboardstate[KeyEvent.VK_W][1] && skillScroller.getCount() == 1) {
                    state = 2;
                    levelScroller.setCount(healthScroller.getMenuCount());
                } else if (keyboardstate[KeyEvent.VK_S][1] && skillScroller.getCount() == skillScroller.getMenuCount()) {
                    state = 2;
                    levelScroller.setCount(1);
                } else {
                    updateScroller(elapsedTime, keyboardstate, skillScroller);
                }
                break;
        }
    }

    public void updateScroller(float elapsedTime, boolean[][] keyboardstate, MenuPointer scroller) {
        if (keyboardstate[KeyEvent.VK_S][1]) { // Handle the S Key is down
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
        if (keyboardstate[KeyEvent.VK_W][1]) { // Handle the W Key is down
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

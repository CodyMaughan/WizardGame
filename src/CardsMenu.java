import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Cody on 9/18/2015.
 */
public class CardsMenu implements DrawableObject {

    private MainCharacter character;
    private int windowWidth;
    private int windowHeight;
    private int menuX;
    private int menuY;
    private String title;
    private Font titleFont;
    private int titleWidth;
    private int titleHeight;

    public CardsMenu(MainCharacter character, Framework framework) {
        this.character = character;
        windowWidth = framework.getWidth();
        windowHeight = framework.getHeight();
        menuX = windowWidth/3;
        menuY = 0;
        title = character.characterName + "'s Cards";
        titleFont = new Font("Arial", Font.BOLD, 30);
        Graphics2D g2d = (Graphics2D)framework.getGraphics();
        titleWidth = (int)(titleFont.getStringBounds(title, g2d.getFontRenderContext()).getWidth());
        titleHeight = (int)(titleFont.getStringBounds(title,g2d.getFontRenderContext()).getHeight());
        //Get Card Information Here
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(menuX, menuY, 2*windowWidth/3, windowHeight);
        g2d.setColor(Color.BLACK);
        g2d.setFont(titleFont);
        g2d.drawString(title, menuX + (windowWidth/3 - titleWidth/2), menuY + 50);
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {

    }
}

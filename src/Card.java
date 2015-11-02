import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/27/2015.
 */
public class Card extends BattleMove {

    private String id;
    private String cardName;
    private int magicCost;
    private String path;
    private BufferedImage image;
    private int width;
    private int height;
    private String explanation;

    public Card(String id, String cardName, int magicCost, BattleMoveEffect effect, String path) {
        super(cardName, effect);
        this.id = id;
        this.cardName = cardName;
        this.magicCost = magicCost;
        this.path = path;
        try {
            image = ImageIO.read(this.getClass().getResource(path));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.width = image.getWidth();
        this.height = image.getHeight();
        disposeImage();
        explanation = CardCache.getCardExplanation(id);
    }

    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        if (image == null) {
            try {
                image = ImageIO.read(this.getClass().getResource(path));
            }
            catch (IOException ex) {
                Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (width <= 0) {
            width = image.getWidth();
        }
        if (height <= 0) {
            height = image.getHeight();
        }
        g2d.drawImage(image, x, y, width, height, null);
    }

    @Override
    public String applyEffect(BattleEnemy attacker, BattleEnemy defender) {
        StartGameState.character.mana -= magicCost;
        return effect.applyEffect(attacker, defender, this);
    }

    public String getName() {
        return cardName;
    }

    public void disposeImage() {
        image = null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getManaCost() {
        return magicCost;
    }

    public String getExplanation() {
        return explanation;
    }

    public String[] getEffectStrings() {
        return effect.getEffectStrings();
    }
}

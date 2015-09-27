import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Cody on 9/13/2015.
 */
public interface Menu {

    void draw(Graphics2D g2d);
    void update(float elapsedTime, boolean[][] keyboardstate);
    void setActive(boolean bool);
    boolean isActive();

}

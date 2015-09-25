import java.awt.*;

/**
 * Created by Cody on 9/24/2015.
 */
public interface DialogBox {

    public void update(float elapsedTime, boolean[][] keyboardstate);
    public void draw(Graphics2D g2d, Character character);
    public void draw(Graphics2D g2d);

}

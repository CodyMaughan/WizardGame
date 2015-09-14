import java.awt.*;

/**
 * Created by Cody on 9/13/2015.
 */
public interface DrawableObject {

    public void draw(Graphics2D g2d);
    public void update(float elapsedTime, boolean[][] keyboardstate);

}

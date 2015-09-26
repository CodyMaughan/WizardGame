import java.awt.*;

/**
 * Created by Cody on 9/24/2015.
 */
public interface GameEvent {

    public void update(float elapsedTime, boolean[][] keyboardstate);
    public void draw(Graphics2D g2d);
    public void startEvent();
    public void progressEvent();
    public void endEvent();

}

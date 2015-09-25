import java.awt.*;

/**
 * Created by Cody on 9/10/2015.
 */
public interface IState {
    // This interface is used for each game state
    void update(float elapsedTime, boolean[][] keyboardstate);
    void draw(Graphics2D g2d);
    void onEnter(Framework framework);
    void onExit();
}

import java.awt.*;

/**
 * Created by Cody on 9/24/2015.
 */
public interface DialogBox {

    void update(float elapsedTime, boolean[][] keyboardstate);
    void draw(Graphics2D g2d, Character character);
    void draw(Graphics2D g2d);
    void startDialog();
    void progressDialog();
    void endDialog();
    boolean isActive();
    void setActive(boolean bool);
}

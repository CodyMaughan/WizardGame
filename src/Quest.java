import java.awt.*;

/**
 * Created by Cody on 10/1/2015.
 */
public class Quest implements GameEvent {

    private String name;
    private int id;

    public Quest(String name) {
        this.name = name;
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {

    }

    @Override
    public void draw(Graphics2D g2d) {

    }

    @Override
    public void startEvent() {

    }

    @Override
    public void progressEvent() {

    }

    @Override
    public void endEvent() {

    }

    public String getName() {
        return name;
    }
}

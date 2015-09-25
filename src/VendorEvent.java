import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Cody on 9/24/2015.
 */
public class VendorEvent implements GameEvent {

    private String name;
    private Vendable[] vendables;

    public VendorEvent(String name, Vendable[] vendables) {
        this.name = name;
        this.vendables = vendables;
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {

    }

    @Override
    public void draw(Graphics2D g2d) {

    }

    @Override
    public void progressEvent() {

    }

    @Override
    public void endEvent() {

    }
}

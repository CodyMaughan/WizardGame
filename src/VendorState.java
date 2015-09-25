import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Cody on 9/24/2015.
 */
public class VendorState implements GameEvent, IState {

    private String name;
    private Vendable[] vendables;

    public VendorState(String name, Vendable[] vendables) {
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
    public void onEnter(Framework framework) {

    }

    @Override
    public void onExit() {

    }

    @Override
    public void progressEvent() {

    }

    @Override
    public void endEvent() {

    }
}

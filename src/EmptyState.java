import java.awt.*;

/**
 * Created by Cody on 9/10/2015.
 */
public class EmptyState implements IState {

    EmptyState() {

    }

    public void update(float elapsedTime, boolean[][] keyboardstate) {
        // Nothing to update in the empty state.
    }

    public void draw(Graphics2D g2d) {
        // Nothing to render in the empty state
    }

    public void onEnter(Framework framework) {
        // No action to take when the state is entered
    }

    public void onExit() {
        // No action to take when the state is exited
    }
}

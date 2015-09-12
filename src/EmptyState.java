import java.awt.*;

/**
 * Created by Cody on 9/10/2015.
 */
public class EmptyState implements IState {

    EmptyState() {

    }

    public void Update(float elapsedTime, boolean[][] keyboardstate, StateMachine gameStateMachine) {
        // Nothing to update in the empty state.
    }

    public void Draw(Graphics2D g2d) {
        // Nothing to render in the empty state
    }

    public void OnEnter(Framework framework) {
        // No action to take when the state is entered
    }

    public void OnExit() {
        // No action to take when the state is exited
    }
}

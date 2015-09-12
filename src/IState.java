import java.awt.*;

/**
 * Created by Cody on 9/10/2015.
 */
public interface IState {
    // This interface is used for each game state
    public void Update(float elapsedTime, boolean[][] keyboardstate, StateMachine gameStateMachine);
    public void Draw(Graphics2D g2d);
    public void OnEnter(Framework framework);
    public void OnExit();
}

import java.awt.*;

/**
 * Created by Cody on 9/12/2015.
 */
public class StartGameState implements IState {

    private TileMap map;
    private final String mapPath = "\\C:\\Users\\Cody\\IdeaProjects\\WizardGame\\WizardGame\\src\\resources\\tmxfiles\\testmap1.tmx";

    public StartGameState(Framework framework) {
        map = new TileMap(framework.getWidth(), framework.getHeight(), mapPath);
    }

    @Override
    public void Update(float elapsedTime, boolean[][] keyboardstate, StateMachine gameStateMachine) {

    }

    @Override
    public void Draw(Graphics2D g2d) {
        map.draw(g2d);
    }

    @Override
    public void OnEnter(Framework framework) {

    }

    @Override
    public void OnExit() {

    }
}

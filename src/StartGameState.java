import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Cody on 9/12/2015.
 */
public class StartGameState implements IState {

    private TileMap map;
    private final String mapPath = "\\C:\\Users\\Cody\\IdeaProjects\\WizardGame\\WizardGame\\src\\resources\\tmxfiles\\testmap1.tmx";
    private MainCharacter character;

    public StartGameState(Framework framework, String wizardName, BufferedImage characterImage) {
        map = new TileMap(framework.getWidth(), framework.getHeight(), mapPath);
        character = new MainCharacter(wizardName, characterImage, map.getMainSpawnX(0), map.getMainSpawnY(0),
                characterImage.getWidth()/3, characterImage.getHeight()/4);
    }

    @Override
    public void Update(float elapsedTime, boolean[][] keyboardstate, StateMachine gameStateMachine) {
        // Update the main character position and animation
        character.update(elapsedTime, keyboardstate);
        // Move the map if necessary
        map.moveMap(character);
        // Handle collision detections
        // Collision Detection of character and map
        map.resolveCollisions(character);
    }

    @Override
    public void Draw(Graphics2D g2d) {
        map.drawBottomLayer(g2d);
        character.draw(g2d);
        map.drawTopLayer(g2d);
    }

    @Override
    public void OnEnter(Framework framework) {

    }

    @Override
    public void OnExit() {

    }
}

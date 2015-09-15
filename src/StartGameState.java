import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Created by Cody on 9/12/2015.
 */
public class StartGameState implements IState {

    protected static TileMap map;
    protected static String mapPath;
    protected static MainCharacter character;
    protected static TileMap nextMap;

    public StartGameState(Framework framework, String wizardName, BufferedImage characterImage) {
        //mapPath = "\\C:\\Users\\Cody\\IdeaProjects\\WizardGame\\WizardGame\\src\\resources\\tmxfiles\\testmap1.tmx";
        URL temp = this.getClass().getResource("/resources/tmxfiles/testmap1.tmx");
        mapPath = temp.getPath();
        map = new TileMap(framework.getWidth(), framework.getHeight(), mapPath, null);
        character = new MainCharacter(wizardName, characterImage, map.getMainSpawnX(), map.getMainSpawnY(),
                characterImage.getWidth()/3, characterImage.getHeight()/4);
        nextMap = null;
    }

    @Override
    public void Update(float elapsedTime, boolean[][] keyboardstate, StateMachine gameStateMachine) {
        // Update the main character position and animation
        character.update(elapsedTime, keyboardstate);
        // Move the map if necessary
        map.moveMap(character);
        // Handle collision detections
        // Collision Detection of character and map
        // NOTE: Due to the way the map change function works, THIS NEEDS TO BE THE LAST UPDATED FUNCTION
        map.resolveCollisions(character);
        if (nextMap != null) {
            map = nextMap;
        }
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

    public static void changeMap(MapConnection connection) {
        mapPath = connection.getMapPath();
        nextMap = new TileMap(map.windowWidth, map.windowHeight, mapPath, connection);
        character.setPosition(nextMap.getMainSpawnX(), nextMap.getMainSpawnY());
        System.out.println("x = " + character.x + ",  y = " + character.y);
    };
}

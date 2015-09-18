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
    private static Framework framework;

    public StartGameState(Framework framework, String wizardName, BufferedImage characterImage) {
        //mapPath = "\\C:\\Users\\Cody\\IdeaProjects\\WizardGame\\WizardGame\\src\\resources\\tmxfiles\\testmap1.tmx";
        this.framework = framework;
        URL temp = this.getClass().getResource("/resources/tmxfiles/testmap1.tmx");
        mapPath = temp.getPath();
        map = new TileMap(framework, mapPath, null);
        character = new MainCharacter(wizardName, characterImage, map.getMainSpawnX(), map.getMainSpawnY(),
                characterImage.getWidth()/3, characterImage.getHeight()/4);
        nextMap = null;
        SoundManager.getInstance().add("Woodland",
                new Sound(this.getClass().getResource("/resources/sounds/Woodland_Fantasy_0.wav"), 0));
    }

    @Override
    public void Update(float elapsedTime, boolean[][] keyboardstate, StateMachine gameStateMachine) {
        // Update the main character position and animation
        character.update(elapsedTime, keyboardstate);
        // Update things like dialog boxes and events on the map
        map.update();
        // Move the map if necessary
        map.moveMap(character);
        // Handle collision detections
        // Collision Detection of character and map
        // NOTE: Due to the way the map change function works, resolveInteraction NEEDS TO BE THE LAST COMMAND
        map.resolveCollisions(character);
        map.resolveInteraction(character, keyboardstate);
        if (nextMap != null) {
            map = nextMap;
        }
    }

    @Override
    public void Draw(Graphics2D g2d) {
        map.drawBottomLayer(g2d);
        character.draw(g2d);
        map.drawTopLayer(g2d, character);
    }

    @Override
    public void OnEnter(Framework framework) {
        SoundManager.loopSound("Woodland");
    }

    @Override
    public void OnExit() {

    }

    public static void changeMap(MapConnection connection) {
        mapPath = connection.getMapPath();
        MapManager.addMapData(map);
        nextMap = new TileMap(framework, mapPath, connection);
        character.setPosition(nextMap.getMainSpawnX(), nextMap.getMainSpawnY());
        System.out.println("x = " + character.x + ",  y = " + character.y);
    };
}

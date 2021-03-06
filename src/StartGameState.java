import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Created by Cody on 9/12/2015.
 */
public class StartGameState implements IState {

    protected static TileMap map;
    protected static String mapPath;
    public static MainCharacter character;
    protected static TileMap nextMap;
    private static Framework framework;
    private static TimedDialogBox entranceDialogBox;

    public StartGameState(Framework framework, String wizardName, BufferedImage characterImage) {
        //mapPath = "\\C:\\Users\\Cody\\IdeaProjects\\WizardGame\\WizardGame\\src\\resources\\tmxfiles\\testmap1.tmx";
        this.framework = framework;
        character = new MainCharacter(wizardName, characterImage, 0, 0,
                characterImage.getWidth()/3, characterImage.getHeight()/4);
        mapPath = "/resources/tmxfiles/testmap1.tmx";
        map = new TileMap(framework, mapPath, null);
        character.setPosition(map.getMainSpawnX(), map.getMainSpawnY());
        nextMap = null;
        SoundManager.add("Woodland",
                new Sound(this.getClass().getResource("/resources/sounds/Woodland_Fantasy_0.wav"), 0));
        entranceDialogBox = new TimedDialogBox("Start_Game_Menu_Instructions", 3500000,
                new Font("Arial", Font.PLAIN, 10), 5, 5, (Graphics2D)framework.getGraphics(), true);
        entranceDialogBox.startDialog();
        //This is for testing purposes
        QuestManager.addCompletedQuest(new Quest("Join_Wizard_Academy"));
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {
        // Check whether or not the main menu should be opened
        if (keyboardstate[KeyEvent.VK_ENTER][1]) {
            StateMachine.Add("GameMenu", new MenuState(framework, character));
            StateMachine.Change("GameMenu");
        }
        // Test battle state
        if (keyboardstate[KeyEvent.VK_SHIFT][1]) {
            StateMachine.Add("BattleState", new BattleState(framework, new Character("Necromancer", 0, 0, framework)));
            StateMachine.Change("BattleState");
            SoundManager.getInstance().stopSound("Woodland");
        }
        // Check whether the entrance dialog box should be replaced by another dialog box
        if (entranceDialogBox.isActive()) {
            entranceDialogBox.addTimer(elapsedTime);
            for (InteractionDialogBox dialogBox : map.interactionDialogBoxes.values()) {
                if (dialogBox.isActive()) {
                    entranceDialogBox.setActive(false);
                    entranceDialogBox.resetTimer();
                    break;
                }
            }
        }
        // update the main character position and animation
        character.update(elapsedTime, keyboardstate);
        // Move the map if necessary
        map.moveMap(character);
        // update things like dialog boxes, characters, and events on the map
        map.update(elapsedTime, character, keyboardstate);
        // Sort the mapCharacters in a way that allows them to be drawn correctly
        map.mapCharacters = MapUtility.sortCharactersByY(map.mapCharacters);
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
    public void draw(Graphics2D g2d) {
        map.drawBottomLayer(g2d, MainCharacter.y);
        character.draw(g2d);
        map.drawTopLayer(g2d, character);
        if (entranceDialogBox.isActive()) {
            entranceDialogBox.draw(g2d);
        }
    }

    @Override
    public void onEnter(Framework framework) {
        SoundManager.loopSound("Woodland");
    }

    @Override
    public void onExit() {

    }

    public static void changeMap(MapConnection connection) {
        mapPath = connection.getMapPath();
        MapManager.addMapData(map);
        nextMap = new TileMap(framework, mapPath, connection);
        MainCharacter.setPosition(nextMap.getMainSpawnX(), nextMap.getMainSpawnY());
    }

    public static void startBattleState(Creature creature) {
        StateMachine.Add("BattleState", new BattleState(framework, creature));
        StateMachine.Change("BattleState");
        SoundManager.getInstance().stopSound("Woodland");
    }
}

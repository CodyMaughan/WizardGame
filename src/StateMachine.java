import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/10/2015.
 */
public class StateMachine {

    private static Map<String, IState> mStates; // List of all the possible game states
    private static IState mCurrentState; // Current state that is updated and drawn
    private static Framework framework; // The framework for which this game state machine is working
    private static StateMachine machine;

    private StateMachine(Framework framework) {
        mStates = new HashMap<>(); // List of all the possible game states
        mCurrentState = new EmptyState(); // Current state that is updated and drawn
        StateMachine.framework = framework;
    }

    public static StateMachine getInstance(Framework framework) {
        if (machine == null) {
            machine = new StateMachine(framework);
        }
        return machine;
    }

    public static void Update(float elapsedTime, boolean[][] keyboardstate)
    {
        mCurrentState.update(elapsedTime, keyboardstate); // Updates the current game state
    }

    public static void Draw(Graphics2D g2d) {
        mCurrentState.draw(g2d); // Draws the current game state
    }

    public static void Change(String stateName) { // Changes the current game state
        mCurrentState.onExit(); // Lets the game state do anything it needs to do before leaving
        mCurrentState = mStates.get(stateName); // Changes the current game state
        mCurrentState.onEnter(framework); // Lets the new game state do anything it needs to do when it starts
    }

    public static void Add(String name, IState state) {
        mStates.put(name, state); // Adds a new game state to the possible game state list
    }

    public static IState getmCurrentState(){
        return mCurrentState;
    }

    public static Framework getFramework() {
        return framework;
    }

    public static boolean isState(String stateName) {
        if (mStates.get(stateName) == null) {
            return false;
        } else{
            return true;
        }
    }

    public static void Remove(String stateName) {
        mStates.remove(stateName);
    }
}

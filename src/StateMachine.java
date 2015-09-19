import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/10/2015.
 */
public class StateMachine {

    private Map<String, IState> mStates; // List of all the possible game states
    private IState mCurrentState; // Current state that is updated and drawn
    private Framework framework; // The framework for which this game state machine is working

    public StateMachine(Framework framework) {
        mStates = new HashMap<String, IState>(); // List of all the possible game states
        mCurrentState = new EmptyState(); // Current state that is updated and drawn
        this.framework = framework;
    }

    public void Update(float elapsedTime, boolean[][] keyboardstate)
    {
        mCurrentState.Update(elapsedTime, keyboardstate, this); // Updates the current game state
    }

    public void Draw(Graphics2D g2d) {
        mCurrentState.Draw(g2d); // Draws the current game state
    }

    public void Change(String stateName, Framework framework) { // Changes the current game state
        mCurrentState.OnExit(); // Lets the game state do anything it needs to do before leaving
        mCurrentState = mStates.get(stateName); // Changes the current game state
        mCurrentState.OnEnter(framework); // Lets the new game state do anything it needs to do when it starts
    }

    public void Add(String name, IState state) {
        mStates.put(name, state); // Adds a new game state to the possible game state list
    }

    public IState getmCurrentState(){
        return mCurrentState;
    }

    public Framework getFramework() {
        return framework;
    }

    public boolean isState(String stateName) {
        if (mStates.get(stateName) == null) {
            return false;
        } else{
            return true;
        }
    }

    public void Remove(String stateName) {
        mStates.remove(stateName);
    }
}

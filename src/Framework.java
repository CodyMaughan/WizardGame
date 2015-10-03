import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Framework that controls the game loop and also holds the JPanel which is being drawn in the game loop
 * 
 * @author www.gametutorial.net
 * Revised by Cody Maughan
 */

public class Framework extends MyCanvas {
    
    /**
     * Width of the frame.
     */
    public static int frameWidth;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;
    
    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L;
    
    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 16;
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

    public static StateMachine gameStateMachine; // Handles all the gamestates
    
    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;
    // It is used for calculating elapsed time.
    private long lastTime;
    
    
    public Framework ()
    {
        super();
        //We start game in new thread.
        Thread gameThread = new Thread() {
            @Override
            public void run(){
                myGameLoop();
            }
        };
        gameThread.start();
    }
    
    
   /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in Game.java.
     */
    private void Initialize()
    {
        SoundManager.getInstance();
        MapManager.getInstance();
        DialogManager.getInstance(this);
        QuestManager.getInstance(this);
        ScriptInterpreter.getInstance();
        gameStateMachine = StateMachine.getInstance(this);
        gameStateMachine.Add("MainMenu", new MainMenuState(this));
        gameStateMachine.Change("MainMenu");
    }
    
    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class, files for the actual game can be loaded in Game.java.
     */
    private void LoadContent()
    {

    }

    private void Update() {
        long elapsedTime = System.nanoTime() - lastTime;
        gameTime += elapsedTime;
        elapsedTime = elapsedTime/1000; //change the time to microseconds instead of nanoseconds
        gameStateMachine.Update(elapsedTime, keyboardState); // update the game state through the State Machine
        // Here we are going to change the meaning of the key released booleans.
        // The changes made here will register it as single time occurrences (when the key went up).
        // By resetting the key released booleans to false after every update it makes the variable only
        // pass through the update the first time it happens.
        //
        // The keyboard pressed boolean is also set to false so that it is only registered when it is pressed.
        // After testing of the keyPressed method it was determined that it activates as if you were typing in a word doc.
        for (int i = 0; i < keyboardState.length; i++) {
            keyboardState[i][1] = false;
            keyboardState[i][2] = false;
        }
        lastTime = System.nanoTime();
    }

    private void myGameLoop()
    {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

        // This variables are used for calculating the time that defines for how long we should put thread to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;

        // Visualize Game (Note from gametutorial.net)
        // "On Ubuntu OS (when I tested on my old computer) this.getWidth() method doesn't return the correct value immediately (eg. for frame that should be 800px width, returns 0 than 790 and at last 798px).
        // So we wait one second for the window/frame to be set to its correct size. Just in case we
        // also insert 'this.getWidth() > 1' condition in case when the window/frame size wasn't set in time,
        // so that we although get approximately size." -gametutorial.net
        System.out.println("game loop started");
        while(this.getWidth() < 1) {
            try {
                // Without this sleep the loop may use up all the memory and prevent the window from initializing.
                Thread.sleep(1);
            } catch (InterruptedException ex) { }
            visualizingTime += System.nanoTime() - lastVisualizingTime;
            lastVisualizingTime = System.nanoTime();
        }
        System.out.println("visualization done");
        // Now that the window is actually created we can get its width and height
        frameWidth = this.getWidth();
        frameHeight = this.getHeight();

        Initialize(); // Initialize everything we need for the framework class
        LoadContent(); // Load anything we need for the framework class (I WILL PROBABLY SCRAP THIS)
        // Start keeping track of the time
        gameTime = 0;
        lastTime = System.nanoTime();
        // Begin the update, draw, and sleep loop
        while(true) // At this point the game ends by closing the window which stops all threads (and therefore this loop)
        {
            beginTime = System.nanoTime(); // Determines when the loop has started
            Update(); // update through the game state machine
            repaint(); // Repaint the screen.

            // Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
            if (timeLeft < 10)
                timeLeft = 10; //set a minimum
            try {
                //Provides the necessary delay and also yields control so that other thread can do work.
                Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }
    /**
     * draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d)
    {
        if (gameStateMachine == null) {
            if (frameWidth > 1) {
                g2d.setColor(Color.RED);
                g2d.drawString("Game is Loading", frameWidth/2 - 50, frameHeight/2);
            }
        } else {
            gameStateMachine.Draw(g2d);
        }
    }

    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {

    }
    
    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        
    }
}

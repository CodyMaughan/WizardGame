import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Creates frame and set its properties.
 * 
 * @author www.gametutorial.net
 * Revised by Cody Maughan
 */

public class Window extends JFrame{
        
    public Window()
    {
        // Sets the title for this frame.
        this.setTitle("Wizard Game");
        
        // Sets size of the frame.
        if(false) // Full screen mode
        {
            // Disables decorations for this frame.
            this.setUndecorated(true);
            // Puts the frame to full screen.
            this.setExtendedState(this.MAXIMIZED_BOTH);
        }
        else // Window mode
        {
            // Size of the frame.
            this.setSize(768, 640);
            // Puts frame to center of the screen.
            this.setLocationRelativeTo(null);
            // So that frame cannot be resizable by the user.
            this.setResizable(false);
        }
        
        // Exit the application when user close frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Makes the framework JPanel the object that is displayed in the window.
        this.setContentPane(new Framework());
        // Makes the window visible
        this.setVisible(true);
    }

    // At the moment I am not using this main method to launch the game even though it would technically work.
    // The reason I don't use it is honestly just because I don't know what the SwingUtilities.invokeLater thing does.
    // I'll do some research on it and then most likely implement it in the Wizard Game class if I think it is useful.
    public static void main(String[] args)
    {
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
}

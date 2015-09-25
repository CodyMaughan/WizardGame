import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Cody on 9/24/2015.
 */
public class DialogSequenceBox implements DialogBox {

    private String name;
    private ArrayList<DialogBox> dialogBoxes;

    public DialogSequenceBox(String name, Font font, int bufferX, int bufferY, Graphics2D g2d) {
        //dialogBoxes = DialogCache.getDialogSequence(name);
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {

    }

    @Override
    public void draw(Graphics2D g2d, Character character) {

    }

    @Override
    public void draw(Graphics2D g2d) {

    }
}

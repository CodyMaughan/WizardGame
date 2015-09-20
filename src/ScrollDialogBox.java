import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Cody on 9/19/2015.
 */
public class ScrollDialogBox {

    private String dialog;
    private int textBufferX;
    private int textBufferY;
    private boolean active;
    private Font font;
    private ArrayList<InteractionDialogBox> dialogBoxes;
    private int count;

    private final int lineLength = 100;

    public ScrollDialogBox(String name, Font font, int bufferX, int bufferY, Graphics2D g2d, boolean useCache) {
        if (useCache) {
            dialog = DialogCache.getInteractionDialog(name);
        } else {
            dialog = name;
        }
        this.font = font;
        fitRects2Text(g2d, bufferX, bufferY);
        active = false;
        count = 0;
    }

    private void fitRects2Text(Graphics2D g2d, int bufferX, int bufferY) {
        textBufferX = bufferX;
        textBufferY = bufferY;
        String[] split = dialog.split("\n");
        dialogBoxes = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            dialogBoxes.add(new InteractionDialogBox(split[i], font, bufferX, bufferY, g2d, false));
        }
    }

    public void update(float elapsedTime, boolean[][] keyboardstate) {
        if (keyboardstate[KeyEvent.VK_SPACE][1]) { // If the user scrolls down
            count += 1;
        }
        if (count >= dialogBoxes.size()) {
            count = 0;
            active = false;
            MainCharacter.setStop(false);
            MainCharacter.setTalking(false);
            // This line prevents any interactions for the rest of this update
            // Without this there were problems of the dialog looping
            keyboardstate[KeyEvent.VK_SPACE][1] = false;
        }
    }

    public void draw(Graphics2D g2d, Character character) {
        if (dialogBoxes.size() > 0 && active) {
            dialogBoxes.get(count).draw(g2d, character);
        }
    }

    public boolean isActive() { return active; }

    public void setActive(boolean bool) {
        active = bool;
    }
}

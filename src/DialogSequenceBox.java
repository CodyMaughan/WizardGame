import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Cody on 9/24/2015.
 */
public class DialogSequenceBox implements DialogBox {

    private String name;
    private ArrayList<DialogBox> dialogBoxes;
    private Integer[][] dialogSequence;
    private int count;
    private boolean active;
    private int lastBranchSize;

    public DialogSequenceBox(String name, Font font, int bufferX, int bufferY, Graphics2D g2d) {
        this.name = name;
        dialogBoxes = DialogCache.getDialogBoxes(name, font, bufferX, bufferY, g2d);
        count = 0;
        active = false;
        lastBranchSize = 1;
        dialogSequence = DialogCache.getDialogSequence(name);
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {
        dialogBoxes.get(count).update(elapsedTime, keyboardstate);
    }

    @Override
    public void draw(Graphics2D g2d, Character character) {
        dialogBoxes.get(count).draw(g2d, character);
    }

    @Override
    public void draw(Graphics2D g2d) {
        dialogBoxes.get(count).draw(g2d);
    }

    @Override
    public void startDialog() {
        count = 0;
        active = true;
        dialogBoxes.get(count).startDialog();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean bool) {
        active = bool;
    }

    @Override
    public void progressDialog() {
        int branch = DialogManager.getBranchCount();
        if (dialogSequence == null) {
            endDialog();
        } else if (dialogSequence[count][branch] == null) {
            endDialog();
        } else {
            count = dialogSequence[count][branch];
            dialogBoxes.get(count).startDialog();
        }
        DialogManager.setBranch(0, 1);
    }

    @Override
    public void endDialog() {
        count = 0;
        active = false;
        MainCharacter.setStop(false);
        MainCharacter.setTalking(false);
        DialogManager.endDialog();
    }
}

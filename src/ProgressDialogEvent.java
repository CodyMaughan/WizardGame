import java.awt.*;

/**
 * Created by Cody on 9/25/2015.
 */
public class ProgressDialogEvent implements GameEvent {

    private static ProgressDialogEvent event;

    private ProgressDialogEvent() {

    }

    public static ProgressDialogEvent getInstance() {
        if (event == null) {
            event = new ProgressDialogEvent();
        }
        return event;
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {

    }

    @Override
    public void draw(Graphics2D g2d) {

    }

    @Override
    public void startEvent() {
        progressEvent();
    }

    @Override
    public void progressEvent() {
        DialogManager.progressDialog();
    }

    @Override
    public void endEvent() {
        DialogManager.endDialog();
    }
}

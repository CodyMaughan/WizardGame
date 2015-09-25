import java.awt.*;

/**
 * Created by Cody on 9/16/2015.
 */
public class BattleState implements IState {

    // Define Global Variable Here
    private int windowWidth;
    private int windowHeight;

    // Initialization of the Battle State
    public BattleState(Framework framework) {
        windowWidth = framework.getWidth();
        windowHeight = framework.getHeight();

    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {

    }

    @Override
    public void draw(Graphics2D g2d) {
        Font font = new Font("Arial", Font.BOLD, 30);
        g2d.setFont(font);
        int textWidth = (int)(font.getStringBounds("You Are Now In Battle State",g2d.getFontRenderContext()).getWidth());
        g2d.setColor(Color.WHITE);
        g2d.drawString("You Are Now In Battle State", windowWidth/2 - textWidth/2, windowHeight/2);
    }

    @Override
    public void onEnter(Framework framework) {

    }

    @Override
    public void onExit() {

    }
}

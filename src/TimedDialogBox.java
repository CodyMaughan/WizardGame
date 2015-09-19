import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Cody on 9/18/2015.
 */
public class TimedDialogBox {

    private String dialog;
    private float time;
    private float timer;
    private int textWidth;
    private int textHeight;
    private int width;
    private int height;
    private int textBufferX;
    private int textBufferY;
    private int lineSpacing;
    private boolean active;
    private ArrayList<String> lines;
    private Font font;

    private final int lineLength = 100;

    public TimedDialogBox(String name, float time, Font font, int bufferX, int bufferY, Graphics2D g2d) {
        dialog = DialogCache.getInteractionDialog(name);
        this.time = time;
        this.font = font;
        fitRect2Text(g2d, bufferX, bufferY);
        active = false;
    }

    public int[] fitRect2Text(Graphics2D g2d, int bufferX, int bufferY) {
        textBufferX = bufferX;
        textBufferY = bufferY;
        int[] size = new int[2];
        size[0] = 0;
        size[1] = 0;
        g2d.setFont(font);
        lines = new ArrayList<>();
        // Determines width and height of only the text
        width = (int)(font.getStringBounds(dialog,g2d.getFontRenderContext()).getWidth());
        if (width > lineLength) {
            String[] split = dialog.split("\\s+");
            int tempWidth = 0;
            int maxWidth = 0;
            String line = "";
            for (int i = 0; i < split.length; i++) {
                tempWidth += (int)(font.getStringBounds(split[i] + "_",g2d.getFontRenderContext()).getWidth());
                if (tempWidth > lineLength) {
                    tempWidth = (int) (font.getStringBounds(split[i] + "_", g2d.getFontRenderContext()).getWidth());
                    lines.add(line);
                    line = split[i] + " ";
                } else {
                    line = line + split[i] + " ";
                }
                if (tempWidth > maxWidth) {
                    maxWidth = tempWidth;
                }
            }
            lines.add(line);
            width = maxWidth;
        } else {
            lines.add(dialog);
        }
        height = (int)(font.getStringBounds(dialog,g2d.getFontRenderContext()).getHeight());
        textHeight = height;
        textWidth = width;
        lineSpacing = height/3;
        height = height*lines.size() + lineSpacing*(lines.size() - 1);
        // Adds the buffer settings to the width and height
        width += 2*bufferX;
        height += 2*bufferY;
        size[0] = width;
        size[1] = height;
        return size;
    }

    public void draw(Graphics2D g2d, MainCharacter character) {
        // Find out x and y by centering the dialog box above the head of the character
        int x = (character.x + character.characterWidth/2) - width/2;
        int y = (character.y - textBufferY - height);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(x, y, width, height, textBufferX, textBufferY);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(x, y, width, height, textBufferX, textBufferY);
        for (int i = 0; i < lines.size(); i++) {
            g2d.drawString(lines.get(i), x + textBufferX, y + textBufferY + textHeight + (textHeight + lineSpacing)*i);
        }
    }

    public boolean isActive() { return active; }

    public void setActive(boolean bool) {
        active = bool;
    }

    public void resetTimer() {
        timer = 0L;
    }

    public void addTimer(float elapsedTime) {
        timer += elapsedTime;
        if (timer > time) {
            active = false;
        }
    }

    public float getTimer() {
        return timer;
    }

    public void startDialogBox() {
        timer = 0L;
        active = true;
    }

}

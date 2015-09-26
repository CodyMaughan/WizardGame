import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/24/2015.
 */
public class ChoiceBox implements DialogBox {

    private String dialog;
    private String[] choices;
    private GameEvent[] choiceEvents;
    private ArrayList<String> lines;
    private int textBufferX;
    private int textBufferY;
    private int width;
    private int height;
    private int textHeight;
    private int textWidth;
    private int lineSpacing;
    private boolean active;
    private Font font;
    private int count;
    private MenuPointer scroller;
    private int scrollerWidth;

    private final int lineLength = 100;

    public ChoiceBox(String name, Font font, int bufferX, int bufferY, Graphics2D g2d, boolean useCache) {
        if (useCache) {
            dialog = DialogCache.getChoiceDialog(name);
        } else {
            dialog = name;
        }
        choices = DialogCache.getChoices(name);
        choiceEvents = EventCache.getEvents(name);
        this.font = font;
        fitRect2Text(g2d, bufferX, bufferY);
        active = false;
        scrollerWidth = textHeight;
        BufferedImage scrollerImage = null;
        // Load the Scroller Image
        try {
            scrollerImage = ImageIO.read(this.getClass().getResource("/resources/images/pointerFinger.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Create the Choice Scroller
        scroller = new MenuPointer(scrollerImage, textBufferX, textBufferY, lineSpacing + textHeight, choices.length, 1);
        scroller.setWidth(textHeight);
        scroller.setHeight(textHeight);
    }

    public int[] fitRect2Text(Graphics2D g2d, int bufferX, int bufferY) {
        textBufferX = bufferX;
        textBufferY = bufferY;
        int[] size = new int[2];
        size[0] = 0;
        size[1] = 0;
        g2d.setFont(font);
        int tempWidth = 0;
        int maxWidth = 0;
        int tempHeight = 0;
        int maxHeight = 0;
        lines = new ArrayList<>();
        // Determines width and height of only the text
        width = (int)(font.getStringBounds(dialog,g2d.getFontRenderContext()).getWidth());
        if (width > lineLength) {
            String[] split = dialog.split("\\s+");
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
        maxHeight = (int)(font.getStringBounds(dialog,g2d.getFontRenderContext()).getHeight());

        for (int i = 0; i < choices.length; i++) {
            tempWidth = (int)(font.getStringBounds(choices[i],g2d.getFontRenderContext()).getWidth()) + textBufferX + maxHeight;
            tempHeight = (int)(font.getStringBounds(choices[i],g2d.getFontRenderContext()).getHeight());
            if (tempWidth > maxWidth) {
                maxWidth = tempWidth;
            }
            if (tempHeight > maxHeight) {
                maxHeight = tempHeight;
            }
        }
        width = maxWidth;
        height = maxHeight;
        textHeight = height;
        textWidth = width;
        lineSpacing = height/3;
        height = height*choices.length + height*lines.size() + lineSpacing*(lines.size() + choices.length - 1);
        // Adds the buffer settings to the width and height
        width += 3*bufferX + scrollerWidth;
        height += 2*bufferY;
        size[0] = width;
        size[1] = height;
        return size;
    }

    @Override
    public void update(float elapsedTime, boolean[][] keyboardstate) {
        if (keyboardstate[KeyEvent.VK_SPACE][1]) {
            keyboardstate[KeyEvent.VK_SPACE][1] = false;
            if (choiceEvents[scroller.count - 1] != null) {
                choiceEvents[scroller.count - 1].startEvent();
            }
            progressDialog();
        } else {
            if (keyboardstate[KeyEvent.VK_S][0]) { // Handle the S Key is down
                // Decide whether to scroll down or not
                if (scroller.scrollDirection == 1) {                 // If the scroller was already scrolling down...
                    scroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                    if (scroller.scrollTimer > scroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                        scroller.scrollTimer -= scroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                        scroller.scrollDown();                       // And finally we scroll down.
                    }
                } else {                                             // If the scroller was not already scrolling down...
                    scroller.scrollDown();                           // We scroll down...
                    scroller.scrollDirection = 1;                    // Set the scroll direction to down...
                    scroller.scrollTimer = 0;                        // And reset the scrollTimer.
                }
            }
            if (keyboardstate[KeyEvent.VK_W][0]) { // Handle the W Key is down
                // Decide whether to scroll up or not
                if (scroller.scrollDirection == -1) {                // If the scroller was already scrolling up...
                    scroller.scrollTimer += elapsedTime;             // We add the elapsed time to the scroll timer...
                    if (scroller.scrollTimer > scroller.TIMER_MAX) { // And determine if we've waited to long enough ...
                        scroller.scrollTimer -= scroller.TIMER_MAX;  // To justify a scroll. We reset the scrollTimer...
                        scroller.scrollUp();                         // And finally we scroll up.
                    }
                } else {                                             // If the scroller was not already scrolling up...
                    scroller.scrollUp();                             // We scroll up...
                    scroller.scrollDirection = -1;                   // Set the scroll direction to up...
                    scroller.scrollTimer = 0;                        // And reset the scrollTimer.
                }
            }
            if (!keyboardstate[KeyEvent.VK_S][0] && !keyboardstate[KeyEvent.VK_W][0]) { // Handle the absence of scrolling
                // Reset the scroller action variables
                scroller.scrollDirection = 0;
                scroller.scrollTimer = 0;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Find out x and y by centering the dialog box above the head of the character
        int x = (MainCharacter.x + MainCharacter.characterWidth/2) - width/2;
        int y = (MainCharacter.y - textBufferY - height);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(x, y, width, height, textBufferX, textBufferY);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(x, y, width, height, textBufferX, textBufferY);
        for (int i = 0; i < lines.size(); i++) {
            g2d.drawString(lines.get(i), x + textBufferX, y + textBufferY + textHeight + (textHeight + lineSpacing)*i);
        }
        for (int i = 0; i < choices.length; i++) {
            g2d.drawString(choices[i], x + 2*textBufferX + scrollerWidth, y + textBufferY + textHeight + (textHeight + lineSpacing)*(lines.size() + i));
        }
        scroller.setPosition(x + textBufferX, y + textBufferY + (textHeight + lineSpacing)*lines.size());
        scroller.draw(g2d);
    }

    @Override
    public void draw(Graphics2D g2d, Character character) {
        draw(g2d);
    }

    @Override
    public void startDialog() {
        active = true;
    }

    @Override
    public void progressDialog() {
        DialogManager.setBranch(scroller.count - 1, scroller.getMenuCount());
        endDialog();
    }

    @Override
    public void endDialog() {
        active = false;
        DialogManager.progressDialog();
    }

    @Override
    public boolean isActive() { return active; }

    @Override
    public void setActive(boolean bool) {
        active = bool;
    }

}

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Cody on 9/11/2015.
 */
public class TypingText {

    private String finalText;
    private char[] dynamicText;
    private int x;
    private int y;
    private int width;
    private int height;
    private Font finalFont;
    private Font dynamicFont;
    private Color color;
    private boolean drawCursor;

    public TypingText(String nontypingText, String typingText, int posX, int posY, Font font, Color color, Graphics2D g2d) {
        finalText = nontypingText;
        dynamicText = typingText.toCharArray();
        this.x = posX;
        this.y = posY;
        this.finalFont = font;
        this.dynamicFont = font;
        this.color = color;
        resetTextSize(g2d); // Determines the text width and height
    }

    public void resetTextSize(Graphics2D g2d) {
        int width1 = (int)(finalFont.getStringBounds(finalText,g2d.getFontRenderContext()).getWidth());
        int height1 = (int)(finalFont.getStringBounds(finalText,g2d.getFontRenderContext()).getHeight());
        int width2 = (int)(dynamicFont.getStringBounds(String.valueOf(dynamicText),g2d.getFontRenderContext()).getWidth());
        int height2 = (int)(dynamicFont.getStringBounds(String.valueOf(dynamicText),g2d.getFontRenderContext()).getHeight());
        width = width1 + width2;
        if (height1 > height2) {
            height = height1;
        } else{
            height = height2;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color); // Sets the text color
        g2d.setFont(finalFont); // Sets the nontypingText font
        g2d.drawString(finalText, x, y); // Draws the button nontypingText
        g2d.setFont(dynamicFont); // Sets the typingText font
        int width1 = (int)(finalFont.getStringBounds(finalText,g2d.getFontRenderContext()).getWidth());
        int height1 = (int)(finalFont.getStringBounds(finalText,g2d.getFontRenderContext()).getHeight());
        g2d.drawString(String.valueOf(dynamicText), x + width1, y + (height - height1)/2); // Draws the typingText next to the nontypingText
        if (drawCursor) {
            int width2 = (int)(dynamicFont.getStringBounds(String.valueOf(dynamicText),g2d.getFontRenderContext()).getWidth());
            g2d.drawString("|", x + width1 + width2, y + (height - height1)/2);
        }
    }

    public void update() {

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int posX, int posY) {
        x = posX;
        y = posY;
    }

    public void setDrawCursor(boolean draw) {
        drawCursor = draw;
    }

    public void setFont(Font font) {
        finalFont = font;
        dynamicFont = font;
    }

    public void setNontypingFont(Font font){
        finalFont = font;
    }

    public void setTypingFont(Font font) {
        dynamicFont = font;
    }

    public void append(String string) {
        char[] stringChars = string.toCharArray();
        char[] newText = new char[dynamicText.length + stringChars.length];
        for (int i = 0; i < dynamicText.length; i++) {
            newText[i] = dynamicText[i];
        }
        int j = 0;
        for (int i = dynamicText.length; i < dynamicText.length + stringChars.length; i++) {
            newText[i] = stringChars[j];
            j += 1;
        }
        dynamicText = newText;
    }

    public void removeLastCharacter() {
        if (dynamicText.length > 0) {
            dynamicText = Arrays.copyOf(dynamicText, dynamicText.length - 1);
        }
    }


}

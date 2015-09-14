import javafx.scene.shape.Rectangle;

import java.awt.*;

/**
 * Created by Cody on 9/10/2015.
 */
public class MenuButton {

    private int width; // width of the button
    private int height; // height of the button
    private int x; // drawing left position
    private int y; // drawing top position
    private String text; // button text
    private Color backgroundColor; // button box color
    private Color textColor; // button text color
    private Font font; // font of the button text
    private int textBufferLeft; // distance between the side of the button and the text in the horizontal direction
    private int textBufferBottom; // distance between the side of the button and the text in the vertical direction

    public MenuButton(String text, int posX, int posY, Font font, Graphics2D g2d, int bufferX, int bufferY) {
        this.text = text;
        this.x = posX;
        this.y = posY;
        this.font = font;
        fitRect2Text(g2d, bufferX, bufferY); // Sets the button width and height based on the size of the text
        backgroundColor = Color.BLACK;
        textColor = Color.WHITE;
    }

    public String getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTextBufferLeft() {
        return textBufferLeft;
    }

    public int getTextBufferBottom() {
        return textBufferBottom;
    }

    public void setBackgroundColor(Color c) {
        backgroundColor = c;
    }

    public void setTextColor(Color c) {
        textColor = c;
    }

    public void setFont(Font f) {
        font = f;
    }

    public void setPosition(int posX, int posY) {
        x = posX;
        y = posY;
    }

    public void setWidth(int w) {
        width = w;
    }

    public void setHeight(int h) {
        height = h;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(backgroundColor); // Sets color to the box color
        g2d.fillRoundRect(x, y, width, height, textBufferLeft, textBufferBottom); // Draws the button box (roundrect)
        g2d.setColor(textColor); // Sets color to text color
        g2d.setFont(font); // Sets the font of the text
        g2d.drawString(text, x + textBufferLeft, y + height/2 + textBufferBottom); // Draws the button text
    }

    public int[] fitRect2Text(Graphics2D g2d) {
        // Option to exclude buffer variables
        return fitRect2Text(g2d, 5, 5);
    }

    public int[] fitRect2Text(Graphics2D g2d, int bufferX, int bufferY) {
        textBufferLeft = bufferX;
        textBufferBottom = bufferY;
        int[] size = new int[2];
        size[0] = 0;
        size[1] = 0;
        g2d.setFont(font);
        // Determines width and height of only the text
        width = (int)(font.getStringBounds(text,g2d.getFontRenderContext()).getWidth());
        height = (int)(font.getStringBounds(text,g2d.getFontRenderContext()).getHeight());
        // Adds the buffer settings to the width and height
        width += 2*bufferX;
        height += 2*bufferY;
        size[0] = width;
        size[1] = height;
        return size;
    }

}

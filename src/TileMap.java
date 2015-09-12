import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/12/2015.
 */
public abstract class TileMap {

    private int[] layer1;
    private BufferedImage[] tilePallet;
    private int frameWidth;
    private int frameHeight;
    private int tileWidth;
    private int tileHeight;
    private int mapWidth;
    private int mapHeight;

    public TileMap(BufferedImage image, int tileWidth, int tileHeight) {
        tilePallet = SpliceImage(image, tileWidth, tileHeight);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public BufferedImage[] SpliceImage(BufferedImage image, int tileWidth, int tileHeight) {
        int width = image.getWidth();
        int height = image.getHeight();
        int tileCols = width/tileWidth;
        int tileRows = height/tileHeight;
        BufferedImage[] tileArray = new BufferedImage[tileCols*tileRows];
        int count = 0;
        for (int col = 0; col < tileCols; col++) {
            for (int row = 0; row < tileRows; row++) {
                tileArray[count] = new BufferedImage(tileWidth, tileHeight, image.getType());
                Graphics2D gr = tileArray[count++].createGraphics();
                gr.drawImage(image, 0, 0, tileWidth, tileHeight, tileWidth * col, tileHeight * row, tileWidth * col + tileWidth, tileHeight * col + tileHeight, null);
                gr.dispose();
            }
        }
        return tileArray;
    }

    public abstract void update();
    public abstract void draw();

}

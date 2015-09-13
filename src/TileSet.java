import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/12/2015.
 */
public class TileSet {

    public String name;
    public int tileWidth;
    public int tileHeight;
    public int tileCount;
    protected String imageSource;
    public BufferedImage[] tiles;

    public TileSet(String name, int tileWidth, int tileHeight, int tileCount, String imageSource) {
        this.name = name;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tileCount = tileCount;
        this.imageSource = imageSource;
        BufferedImage sourceImage = null;
        try {
            sourceImage = ImageIO.read(this.getClass().getResource(imageSource));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        tiles = SpliceImage(sourceImage, tileWidth, tileHeight);
    }

    private BufferedImage[] SpliceImage(BufferedImage image, int tileWidth, int tileHeight) {
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

    public BufferedImage getTile(int id) {
        return tiles[id];
    }


}

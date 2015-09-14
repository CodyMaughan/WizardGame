import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/12/2015.
 */
public class TileSet {

    public String name;
    public int imageWidth;
    public int imageHeight;
    public int tileWidth;
    public int tileHeight;
    public int tileCount;
    protected String imageSource;
    public BufferedImage[] tiles;
    public int firstgid;
    public int tileCols;
    public int tileRows;

    public TileSet(String name, int firstgid, int tileWidth, int tileHeight, int tileCount, String imageSource) {
        this.name = name;
        this.firstgid = firstgid;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tileCount = tileCount;
        this.imageSource = imageSource;
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResource(imageSource));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        tileCols = imageWidth/tileWidth;
        tileRows = imageHeight/tileHeight;
        tiles = SpliceImage(image, tileWidth, tileHeight);
    }

    private BufferedImage[] SpliceImage(BufferedImage image, int tileWidth, int tileHeight) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage[] tileArray = new BufferedImage[tileCols*tileRows];
        int count = 0;
        for (int row = 0; row < tileRows; row++) {
            for (int col = 0; col < tileCols; col++) {
                tileArray[count] = new BufferedImage(tileWidth, tileHeight, image.getType());
                Graphics2D gr = tileArray[count++].createGraphics();
                gr.drawImage(image, 0, 0, tileWidth, tileHeight, tileWidth * col, tileHeight * row, tileWidth * col + tileWidth, tileHeight * row + tileHeight, null);
                gr.dispose();
            }
        }

        // This code is a test to see if the tiles are being spliced correctly
        //for (int i = 0; i < tileArray.length; i++) {
        //    try {
        //        ImageIO.write(tileArray[i], "jpg", new File("img" + i + ".jpg"));
        //    } catch (IOException ex) {
        //
        //    }
        //}

        return tileArray;
    }

    public void drawTile(Graphics2D g2d, int gid, AffineTransform afx) {
        gid = gid - firstgid;
        int row = Math.floorDiv(gid, tileCols);
        int col = gid - row*tileCols;
        g2d.drawImage(tiles[gid], afx, null);
    }

    public BufferedImage getTile(int id) {
        return tiles[id];
    }

    public int getFirstGid() {
        return firstgid;
    }


}

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
        return tileArray;
    }

    public void drawTile(Graphics2D g2d, int gid, AffineTransform afx) {
        gid = gid - firstgid;
        int row = Math.floorDiv(gid, tileCols);
        int col = gid - row*tileCols;
        g2d.drawImage(tiles[gid], afx, null);
    }

    private BufferedImage transformTile(BufferedImage image, boolean[] tmxRotations) {
        AffineTransform tx = null;
        AffineTransformOp op = null;
        if (tmxRotations[0]) {
            if (tmxRotations[1]) {
                if (tmxRotations[2]) {
                    // Rotated 270 degrees clockwise,
                    tx = AffineTransform.getRotateInstance(Math.toRadians(90));
                    // Then flipped horizontally
                    tx.scale(-1, 1);
                    tx.translate(-image.getWidth(), -image.getHeight());
                    op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    image = op.filter(image, null);
                } else {
                    // Rotated 180 degrees
                    tx = AffineTransform.getRotateInstance(Math.toRadians(180));
                    tx.translate(-image.getWidth(null), -image.getHeight());
                    op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    image = op.filter(image, null);
                }
            } else {
                if (tmxRotations[2]) {
                    // Rotated 90 degrees clockwise
                    tx = AffineTransform.getRotateInstance(Math.toRadians(90));
                    tx.translate(0, -image.getHeight());
                    op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    image = op.filter(image, null);
                } else {
                    // Flipped Horizontally (x direction)
                    tx = AffineTransform.getScaleInstance(-1, 1);
                    tx.translate(-image.getWidth(null), 0);
                    op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    image = op.filter(image, null);
                }
            }
        } else {
            if (tmxRotations[1]) {
                if (tmxRotations[2]) {
                    // Rotated 270 degrees clockwise
                    tx = AffineTransform.getRotateInstance(Math.toRadians(270));
                    tx.translate(-image.getWidth(), 0);
                    op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    image = op.filter(image, null);
                } else {
                    // Flipped Vertically
                    tx = AffineTransform.getScaleInstance(1, -1);
                    tx.translate(0, -image.getHeight(null));
                    op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    image = op.filter(image, null);
                }
            } else {
                if (tmxRotations[2]) {
                    // Rotated 90 degress clockwise
                    tx = AffineTransform.getRotateInstance(Math.toRadians(270));
                    // Then flipped horizontally
                    tx.scale(-1, 1);
                    tx.translate(0, 0);
                    op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    image = op.filter(image, null);
                } else {
                    //Do Nothing
                }
            }
        }
        return image;
    }

    public BufferedImage getTile(int id) {
        return tiles[id];
    }

    public int getFirstGid() {
        return firstgid;
    }


}

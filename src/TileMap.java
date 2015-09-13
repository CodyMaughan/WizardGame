import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/12/2015.
 */
public class TileMap {

    protected int windowWidth;
    protected int windowHeight;
    protected int tileWidth;
    protected int tileHeight;
    protected int mapTileWidth;
    protected int mapTileHeight;
    protected int tilesetCount;
    protected TileSet[] tilesets;
    protected ArrayList<Integer> firstGid;
    protected int[] backgroundLayerMap;
    protected ArrayList<boolean[]> backgroundRotations;
    protected int[] foregroundLayerMap;
    protected int[] collectiblesLayerMap;
    protected int[] topLayerMap;
    protected BufferedImage bottomLayer;
    protected BufferedImage topLayer;

    public TileMap(int windowWidth, int windowHeight, String tmxFilePath) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        readtmxFile(tmxFilePath);
    }

    private void readtmxFile(String filePath) {
        try {

            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            //doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            // Gets the atrributes from the document element (in this case, the map element)
            mapTileWidth = Integer.parseInt(doc.getDocumentElement().getAttribute("width"));
            mapTileHeight = Integer.parseInt(doc.getDocumentElement().getAttribute("height"));
            tileWidth = Integer.parseInt(doc.getDocumentElement().getAttribute("tilewidth"));
            tileHeight = Integer.parseInt(doc.getDocumentElement().getAttribute("tileheight"));

            // Gets us a list of the tileset elements used for the map
            NodeList nList = doc.getElementsByTagName("tileset");
            // Iterates through the list of tilesets
            tilesetCount = nList.getLength();
            tilesets = new TileSet[tilesetCount];
            firstGid = new ArrayList<>();
            for (int temp = 0; temp < tilesetCount; temp++) {
                // Grabs the current tilesets node
                Node nNode = nList.item(temp);
                // Checks if the node is an element
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    // Gets the element of the node
                    Element eElement = (Element) nNode;
                    // We keep track of the first gid for each corresponding tileset
                    firstGid.add(temp, Integer.valueOf(eElement.getAttribute("firstgid")));
                    // Gets the image source element of the tilemap
                    Node imageNode = eElement.getElementsByTagName("image").item(0);
                    Element imageElement = (Element) imageNode;
                    System.out.println("image source: " + imageElement.getAttribute("source"));
                    String stringPath = imageElement.getAttribute("source");
                    stringPath = stringPath.substring(2, stringPath.length());
                    stringPath = "/resources" + stringPath;
                    System.out.println(stringPath);

                    // Gets each of the attributes of the tileset and creates a new TileSet with them
                    tilesets[temp] = new TileSet(eElement.getAttribute("name"), Integer.parseInt(eElement.getAttribute("tilewidth")),
                        Integer.parseInt(eElement.getAttribute("tileheight")), Integer.parseInt(eElement.getAttribute("tilecount")),
                            stringPath);

                }
            }

            // Gets us a list of the layer elements used for the map
            nList = doc.getElementsByTagName("layer");

            for (int i = 0; i < nList.getLength(); i++) {
                // Grabs the current layer node
                Element eElement =(Element)nList.item(i);
                Element dataElement = (Element)eElement.getElementsByTagName("data").item(0);
                NodeList tileData = dataElement.getElementsByTagName("tile");
                int tileDataLength = tileData.getLength();
                Element tileElement = null;
                // Populates each of the layers with the gid information
                long gid = 0L;
                switch (eElement.getAttribute("name")) {
                    case ("Background"):
                        backgroundLayerMap = new int[tileDataLength];
                        backgroundRotations = new ArrayList<>();
                        for (int j = 0; j < (tileDataLength); j++) {
                            tileElement = (Element)tileData.item(j);
                            gid = Long.parseLong(tileElement.getAttribute("gid"));
                            boolean[] rotations = new boolean[3];
                            if ((gid & (1 << 31)) !=0) { // Checks Horizontal Flipping
                                rotations[0] = true;
                                gid = gid - (long)Math.pow(2, 31);
                            }
                            if ((gid & (1 << 30)) !=0) { // Checks Vertical Flipping
                                rotations[1] = true;
                                gid = gid - (long)Math.pow(2, 30);
                            }
                            if ((gid & (1 << 29)) !=0) { // Checks Diagonal Flipping
                                rotations[2] = true;
                                gid = gid - (long)Math.pow(2, 29);
                            }
                            if (gid < 0 || gid > 140) {
                                System.out.println(gid);
                                System.out.println("Index: " + j);
                            }
                            backgroundLayerMap[j] = (int)gid;
                            backgroundRotations.add(rotations);
                        }
                        break;
                    case ("Foreground"):
                        foregroundLayerMap = new int[tileDataLength];
                        for (int j = 0; j < (tileDataLength); j++) {
                            tileElement = (Element)tileData.item(j);
                        }
                        break;
                    case ("Top"):
                        topLayerMap = new int[tileDataLength];
                        for (int j = 0; j < (tileDataLength); j++) {
                            tileElement = (Element)tileData.item(j);
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finished Loading Tile Data");
    }

    public void update() {

    };

    public void draw(Graphics2D g2d) {
        int tileCounter = 0;
        int gid = 0;
        for (int i = 0; i < mapTileHeight; i++) {
            for (int j = 0; j < mapTileWidth; j++) {
                gid = backgroundLayerMap[tileCounter]; // Get the stored gid
                // Rotate/Flip the drawing if necessary
                BufferedImage temp = transformTile(getTileSet(gid).getTile(gid - firstGid.get(getTileSetIndex(gid))),
                        backgroundRotations.get(tileCounter)); // Get the Tile
                g2d.drawImage(temp, j*tileWidth, i*tileHeight, tileWidth, tileHeight, null);// Draw the Tile
                tileCounter += 1;
            }
        }
    };

    private TileSet getTileSet(int gid) {
        int i = 0;
        while (gid >= firstGid.get(i)) {
            i += 1;
            if (i == tilesets.length) {
                break;
            }
        }
        i -= 1;
        if (i == -1) {
            return null;
        } else {
            return tilesets[i];
        }
    }

    private int getTileSetIndex(int gid) {
        int i = 0;
        while (gid >= firstGid.get(i)) {
            i += 1;
            if (i == tilesets.length) {
                break;
            }
        }
        i -= 1;
        return i;
    }

    private BufferedImage transformTile(BufferedImage image, boolean[] tmxRotations) {
        AffineTransform tx = null;
        AffineTransformOp op = null;
        if (tmxRotations[0]) {
            if (tmxRotations[1]) {
                if (tmxRotations[2]) {
                    // Rotated 270 degrees clockwise,
                    tx = AffineTransform.getRotateInstance(Math.toRadians(270));
                    // Then flipped horizontally
                    tx.scale(-1, 1);
                    tx.translate(0, 0);
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
                    tx = AffineTransform.getRotateInstance(Math.toRadians(90));
                    // Then flipped horizontally
                    tx.scale(-1, 1);
                    tx.translate(-image.getWidth(), -image.getHeight());
                    op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    image = op.filter(image, null);
                } else {
                    //Do Nothing
                }
            }
        }
        return image;
    }

}

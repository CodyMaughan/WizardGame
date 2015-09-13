import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
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
    protected int[] backgroundLayer;
    protected int[] foregroundLayer;
    protected int[] collectiblesLayer;
    protected int[] topLayer;

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
            for (int temp = 0; temp < tilesetCount; temp++) {
                // Grabs the current tilesets node
                Node nNode = nList.item(temp);
                // Checks if the node is an element
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    // Gets the element of the node
                    Element eElement = (Element) nNode;
                    // Gets each of the attributes of the tileset
                    System.out.println("firstgid : " + eElement.getAttribute("firstgid"));
                    // Gets the image source element of the tilemap
                    Node imageNode = eElement.getElementsByTagName("image").item(0);
                    Element imageElement = (Element) imageNode;
                    System.out.println("image source: " + imageElement.getAttribute("source"));
                    tilesets[temp] = new TileSet(eElement.getAttribute("name"), Integer.parseInt(eElement.getAttribute("tilewidth")),
                        Integer.parseInt(eElement.getAttribute("tileheight")), Integer.parseInt(eElement.getAttribute("tilecount")),
                            imageElement.getAttribute("source"));

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
                switch (eElement.getAttribute("name")) {
                    case ("Background"):
                        backgroundLayer = new int[tileDataLength];
                        for (int j = 0; j < (tileDataLength); j++) {
                            backgroundLayer[j] = Integer.parseInt(tileElement.getAttribute("gid"));
                            boolean test = (backgroundLayer[j] & (1 << 31)) !=0;
                            if (test) {
                                System.out.println("Flipped Horizontal");
                            } else {
                                System.out.println("Not Flipped Horizontal");
                            }
                        }
                        break;
                    case ("Foreground"):
                        foregroundLayer = new int[tileDataLength];
                        for (int j = 0; j < (tileDataLength); j++) {
                            foregroundLayer[j] = Integer.parseInt(tileElement.getAttribute("gid"));
                        }
                        break;
                    case ("Top"):
                        topLayer = new int[tileDataLength];
                        for (int j = 0; j < (tileDataLength); j++) {
                            topLayer[j] = Integer.parseInt(tileElement.getAttribute("gid"));
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void update() {

    };
    public void draw() {

    };

}

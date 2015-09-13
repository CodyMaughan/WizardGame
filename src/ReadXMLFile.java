import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.net.URL;

/**
 * Created by Cody on 9/12/2015.
 */
public class ReadXMLFile {

    public static void readTMXFile(String filePath) {

        try {

            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            //doc.getDocumentElement().normalize();

            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            // Gets the atrributes from the document element (in this case, the map element)

            //mapTileWidth = Integer.parseInt(doc.getDocumentElement().getAttribute("width"));
            System.out.println("width: " + doc.getDocumentElement().getAttribute("width"));
            //mapTileHeight = Integer.parseInt(doc.getDocumentElement().getAttribute("height"));
            System.out.println("height: " + doc.getDocumentElement().getAttribute("height"));
            //tileWidth = Integer.parseInt(doc.getDocumentElement().getAttribute("tilewidth"));
            System.out.println("tilewidth: " + doc.getDocumentElement().getAttribute("tilewidth"));
            //tileHeight = Integer.parseInt(doc.getDocumentElement().getAttribute("tileheight"));
            System.out.println("tileheight: " + doc.getDocumentElement().getAttribute("tileheight"));

            // Gets us a list of the tileset elements used for the map
            NodeList nList = doc.getElementsByTagName("tileset");
            // Iterates through the list of tilesets
            for (int temp = 0; temp < nList.getLength(); temp++) {
                // Grabs the first tilesets node
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element: " + nNode.getNodeName());
                // Checks if the node is an element
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    // Gets the element of the node
                    Element eElement = (Element) nNode;
                    // Gets each of the attributes of the tileset
                    System.out.println("firstgid : " + eElement.getAttribute("firstgid"));
                    System.out.println("name: " + eElement.getAttribute("name"));
                    System.out.println("tilewidth: " + eElement.getAttribute("tilewidth"));
                    System.out.println("tileheight: " + eElement.getAttribute("tileheight"));
                    System.out.println("tilecount: " + eElement.getAttribute("tilecount"));
                    // Gets the image element of the
                    Node imageNode = eElement.getElementsByTagName("image").item(0);
                    Element imageElement = (Element) imageNode;
                    System.out.println("image source: " + imageElement.getAttribute("source"));
                }
            }

            // Gets us a list of the layer elements used for the map
            nList = doc.getElementsByTagName("layer");
            for (int i = 0; i < nList.getLength(); i++) {
                // Grabs the current layer node
                Element eElement =(Element)nList.item(i);
                Element dataElement = (Element)eElement.getElementsByTagName("data").item(0);
                NodeList tileData = dataElement.getElementsByTagName("tile");
                Element tileElement = null;
                long data = 0;
                switch (eElement.getAttribute("name")) {
                    case ("Background"):
                        System.out.println(i);
                        System.out.println("Background layer can be set.");
                        for (int j = 0; j < (10); j++) {
                            tileElement = (Element)tileData.item(j);
                            data = Long.parseLong(tileElement.getAttribute("gid"));
                                System.out.println("Tile #" + j);
                            boolean test = (data & (1 << 31)) !=0;
                            if (test) {
                                System.out.println("Flipped Horizontal");
                            } else {
                                System.out.println("Not Flipped Horizontal");
                            }
                            test = (data & (1 << 30)) !=0;
                            if (test) {
                                System.out.println("Flipped Vertical");
                            } else {
                                System.out.println("Not Flipped Vertical");
                            }
                            test = (data & (1 << 29)) !=0;
                            if (test) {
                                System.out.println("Flipped Diagonal");
                            } else {
                                System.out.println("Not Flipped Diaganol");
                            }
                        }
                        break;
                    case ("Foreground"):
                        System.out.println(i);
                        System.out.println("Foreground layer can be set.");
                        break;
                    case ("Top"):
                        System.out.println(i);
                        System.out.println("Top layer can be set.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

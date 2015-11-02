import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Cody on 9/12/2015.
 */
public class TileMap {

    private Framework framework;
    protected String name; // The name of the map
    protected int windowWidth; // The width of the game window
    protected int windowHeight; // The height of the game window
    protected int windowTileWidth; // The width of the game window in tiles
    protected int windowTileHeight; // the height of the game window in tiles
    protected int tileWidth; // The width of a single tile
    protected int tileHeight; // The height of a single tile
    protected int mapTileWidth; // The width of the map in tiles
    protected int mapTileHeight; // The height of the map in tiles
    protected int tilesetCount; // The number of tilesets used for the map
    protected TileSet[] tilesets; // The list of tilesets used for the map
    protected ArrayList<Integer> firstGid; // A list of the firstgid corresponding to each tileset (may be removed)
    protected int xOffset; // The xOffset in terms of pixels that the window is moved from the left side of the map
    protected int yOffset; // The yOffset in terms of pixels that the window is moved from the top of the map
    protected int maxXOffset; // The maximum xOffset allowed by the map (determined by mapwidth - windowwidth)
    protected int maxYOffset; // The maximum yOffset allowed by the map (determined by mapheight - windowheight)
    protected int maxXTileOffset; // The maximum xOffset allowed by the map in tiles
    protected int maxYTileOffset; // The maximum yOffset allowed by the map in tiles
    protected int[] backgroundLayerMap; // The gid data for the tiles used in the background
    protected ArrayList<boolean[]> backgroundRotations; // The tile rotation data used for the background layer tiles
    protected Map<Integer, Integer> foregroundLayer; // A mapping of tile location to gid data used for the foreground layer
    protected Map<Integer, boolean[]> foregroundRotations; // A mapping of tile location to rotation data used for the foreground layer
    protected Map<Integer, Integer> topLayer; // A mapping of the tile location to gid data used for the top layer
    protected Map<Integer, boolean[]> topRotations; // A mapping of the tile location to rotation data used for the top layer
    protected Map<Integer, Integer> foregroundLayer2; // A mapping of tile location to gid data used for the foreground layer
    protected Map<Integer, boolean[]> foregroundRotations2; // A mapping of tile location to rotation data used for the foreground layer
    protected Map<Integer, Integer> topLayer2; // A mapping of the tile location to gid data used for the top layer
    protected Map<Integer, boolean[]> topRotations2; // A mapping of the tile location to rotation data used for the top layer
    protected ArrayList<Rectangle>  collisionBoxes; // A list of all the collisionBoxes for the map
    protected int playerMoveDistance; // A set distance from the edges of the window where the map begins to move with the player.
    protected int[] mainSpawnPoint; // A list of the main spawn points |||| NOTE: (This object is subject to being replaced by mapConnections)
    protected ArrayList<MapConnection> mapConnections; // A map of the object boxes that indicate map connections to the corresponding map file path
    protected ArrayList<RemovableObject> mapRemovables;
    protected ArrayList<TerrainChange> mapTerrainChanges;
    protected Map<String, InteractionDialogBox> interactionDialogBoxes;
    protected IndexedLinkedHashMap<String, Character> mapCharacters;
    protected HashMap<String, Creature> mapCreatures;
    protected HashMap<String, CreatureSpawnBox> mapCreatureSpawnBoxes;

    public TileMap(Framework framework, String tmxFilePath, MapConnection connection) {
        this.framework = framework;
        int charID1 = tmxFilePath.indexOf("/resources/tmxfiles/");
        int charID2 = tmxFilePath.indexOf(".tmx");
        this.name = tmxFilePath.substring(charID1 + 20, charID2);
        this.windowWidth = framework.getWidth();
        this.windowHeight = framework.getHeight();
        mapConnections = new ArrayList<>();
        mainSpawnPoint = new int[2];
        mapRemovables = new ArrayList<>();
        mapTerrainChanges = new ArrayList<>();
        mapCharacters = new IndexedLinkedHashMap<>();
        mapCreatures = new HashMap<>();
        mapCreatureSpawnBoxes = new HashMap<>();
        interactionDialogBoxes = new HashMap<>();
        System.out.println(tmxFilePath);
        readtmxFile(tmxFilePath);
        mapCharacters = MapUtility.sortCharactersByY(mapCharacters);
        interactionDialogBoxes.put("Speak_Character_Instructions", new InteractionDialogBox("Speak_Character_Instructions",
                new Font("Arial", Font.PLAIN, 10), 5, 5, (Graphics2D) framework.getGraphics(), true));
        windowTileWidth = (int)Math.ceil((double)windowWidth/tileHeight);
        windowTileHeight = (int)Math.ceil((double)windowHeight/tileHeight);
        xOffset = 0;
        yOffset = 0;
        maxXOffset = mapTileWidth*tileWidth - windowWidth;
        maxYOffset = mapTileHeight*tileHeight - windowHeight;
        maxXTileOffset = Math.floorDiv(maxXOffset,tileWidth);
        maxYTileOffset = Math.floorDiv(maxYOffset,tileHeight);
        playerMoveDistance = tileWidth*6; // Once the player hits 6 Tiles from the edge the map will start to move instead of the character
        if (connection != null) { // Checks if we came to this map by a connection or just a spawn/respawn
            for (MapConnection temp: mapConnections) {
                if (temp.getMapNew().equals(connection.getMapOld())) {
                    if (temp.getName().equals(connection.getName())) {
                        // This code may not work (for example, spawning part of the character offscreen)
                        // I'm hoping any issues will be dealt with by the end-of-map collision detection
                        mainSpawnPoint[0] = (int) (temp.getRect().getX() + temp.getRect().getWidth()/2);
                        mainSpawnPoint[1] = (int) (temp.getRect().getY() + temp.getRect().getHeight()/2);
                        // This initializes the map offset if  the character spawns on a part of the map that would be offscreen
                        if (mainSpawnPoint[0] + MainCharacter.characterWidth + playerMoveDistance > windowWidth) {
                            xOffset = MainCharacter.characterWidth + mainSpawnPoint[0] + playerMoveDistance - windowWidth;
                            if (xOffset > maxXOffset) {
                                xOffset = maxXOffset;
                            }
                            mainSpawnPoint[0] -= xOffset;
                        }
                        if (mainSpawnPoint[1] + MainCharacter.characterHeight + playerMoveDistance > windowHeight) {
                            yOffset = MainCharacter.characterHeight + mainSpawnPoint[1] + playerMoveDistance - windowHeight;
                            if (yOffset > maxYOffset) {
                                yOffset = maxYOffset;
                            }
                            mainSpawnPoint[1] -= yOffset;
                        }
                        System.out.println("SpawnX: " + mainSpawnPoint[0] + ", SpawnY: " + mainSpawnPoint[1]);
                    }
                }
            }
        }
        if (MapManager.getMapData(this.name) != null) {
            MapManager.getMapData(this.name).restoreMapData(this);
        }
    }

    private void readtmxFile(String filePath) {
        try {
            File tmxFile = new FileUtility().GetFile(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(tmxFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            //doc.getDocumentElement().normalize();

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
                    firstGid.add(temp, Integer.parseInt(eElement.getAttribute("firstgid")));
                    // Gets the image source element of the tilemap
                    Node imageNode = eElement.getElementsByTagName("image").item(0);
                    Element imageElement = (Element) imageNode;
                    String stringPath = imageElement.getAttribute("source");
                    stringPath = stringPath.substring(2, stringPath.length());
                    stringPath = "/resources" + stringPath;

                    // Gets each of the attributes of the tileset and creates a new TileSet with them
                    tilesets[temp] = new TileSet(eElement.getAttribute("name"), Integer.parseInt(eElement.getAttribute("firstgid")),
                            Integer.parseInt(eElement.getAttribute("tilewidth")), Integer.parseInt(eElement.getAttribute("tileheight")),
                            Integer.parseInt(eElement.getAttribute("tilecount")), stringPath);

                }
            }

            // Gets us a list of the layer elements used for the map
            nList = doc.getElementsByTagName("layer");
            // Here we translate the layer data into each layer's gid and tile rotation data
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
                            backgroundLayerMap[j] = (int)gid;
                            backgroundRotations.add(rotations);
                        }
                        break;
                    case ("Foreground"):
                        foregroundLayer = new HashMap<>();
                        foregroundRotations = new HashMap<>();
                        for (int j = 0; j < (tileDataLength); j++) {
                            tileElement = (Element) tileData.item(j);
                            gid = Long.parseLong(tileElement.getAttribute("gid"));
                            if (gid != 0) {
                                boolean[] rotations = new boolean[3];
                                if ((gid & (1 << 31)) != 0) { // Checks Horizontal Flipping
                                    rotations[0] = true;
                                    gid = gid - (long) Math.pow(2, 31);
                                }
                                if ((gid & (1 << 30)) != 0) { // Checks Vertical Flipping
                                    rotations[1] = true;
                                    gid = gid - (long) Math.pow(2, 30);
                                }
                                if ((gid & (1 << 29)) != 0) { // Checks Diagonal Flipping
                                    rotations[2] = true;
                                    gid = gid - (long) Math.pow(2, 29);
                                }
                                foregroundLayer.put(j, (int)gid);
                                foregroundRotations.put(j, rotations);
                            }
                        }
                        break;
                    case ("Top"):
                        topLayer = new HashMap<>();
                        topRotations = new HashMap<>();
                        for (int j = 0; j < (tileDataLength); j++) {
                            tileElement = (Element) tileData.item(j);
                            gid = Long.parseLong(tileElement.getAttribute("gid"));
                            if (gid != 0) {
                                boolean[] rotations = new boolean[3];
                                if ((gid & (1 << 31)) != 0) { // Checks Horizontal Flipping
                                    rotations[0] = true;
                                    gid = gid - (long) Math.pow(2, 31);
                                }
                                if ((gid & (1 << 30)) != 0) { // Checks Vertical Flipping
                                    rotations[1] = true;
                                    gid = gid - (long) Math.pow(2, 30);
                                }
                                if ((gid & (1 << 29)) != 0) { // Checks Diagonal Flipping
                                    rotations[2] = true;
                                    gid = gid - (long) Math.pow(2, 29);
                                }
                                topLayer.put(j, (int)gid);
                                topRotations.put(j, rotations);
                            }
                        }
                        break;
                    case ("Foreground2"):
                        foregroundLayer2 = new HashMap<>();
                        foregroundRotations2 = new HashMap<>();
                        for (int j = 0; j < (tileDataLength); j++) {
                            tileElement = (Element) tileData.item(j);
                            gid = Long.parseLong(tileElement.getAttribute("gid"));
                            if (gid != 0) {
                                boolean[] rotations = new boolean[3];
                                if ((gid & (1 << 31)) != 0) { // Checks Horizontal Flipping
                                    rotations[0] = true;
                                    gid = gid - (long) Math.pow(2, 31);
                                }
                                if ((gid & (1 << 30)) != 0) { // Checks Vertical Flipping
                                    rotations[1] = true;
                                    gid = gid - (long) Math.pow(2, 30);
                                }
                                if ((gid & (1 << 29)) != 0) { // Checks Diagonal Flipping
                                    rotations[2] = true;
                                    gid = gid - (long) Math.pow(2, 29);
                                }
                                foregroundLayer2.put(j, (int)gid);
                                foregroundRotations2.put(j, rotations);
                            }
                        }
                        break;
                    case ("Top2"):
                        topLayer2 = new HashMap<>();
                        topRotations2 = new HashMap<>();
                        for (int j = 0; j < (tileDataLength); j++) {
                            tileElement = (Element) tileData.item(j);
                            gid = Long.parseLong(tileElement.getAttribute("gid"));
                            if (gid != 0) {
                                boolean[] rotations = new boolean[3];
                                if ((gid & (1 << 31)) != 0) { // Checks Horizontal Flipping
                                    rotations[0] = true;
                                    gid = gid - (long) Math.pow(2, 31);
                                }
                                if ((gid & (1 << 30)) != 0) { // Checks Vertical Flipping
                                    rotations[1] = true;
                                    gid = gid - (long) Math.pow(2, 30);
                                }
                                if ((gid & (1 << 29)) != 0) { // Checks Diagonal Flipping
                                    rotations[2] = true;
                                    gid = gid - (long) Math.pow(2, 29);
                                }
                                topLayer2.put(j, (int)gid);
                                topRotations2.put(j, rotations);
                            }
                        }
                        break;
                }
            }
            // Here we get all the different object/event locations that were stored in the Tiled map (Collisions, spawn points, ect.)
            nList = doc.getElementsByTagName("objectgroup");
            for (int i = 0; i < nList.getLength(); i++) {
                // Grabs the current layer node
                Element eElement = (Element) nList.item(i);
                NodeList objectData = eElement.getElementsByTagName("object");
                int objectDataLength = objectData.getLength();
                Element objectElement = null;
                String parsedName = eElement.getAttribute("name");
                String connection = "";
                if (parsedName.contains("_")) {
                    connection = parsedName.substring(parsedName.indexOf("_") + 1);
                    parsedName = parsedName.substring(0, parsedName.indexOf("_"));
                }
                switch (parsedName) {
                    case ("Collision"):
                        // Gets all the collision boxes on the map
                        collisionBoxes = new ArrayList<>();
                        int rectX = 0;
                        int rectY = 0;
                        int rectWidth = 0;
                        int rectHeight = 0;
                        for (int j = 0; j < objectDataLength; j++) {
                            objectElement = (Element)objectData.item(j);
                            rectX = Integer.parseInt(objectElement.getAttribute("x"));
                            rectY = Integer.parseInt(objectElement.getAttribute("y"));
                            rectHeight = Integer.parseInt(objectElement.getAttribute("width"));
                            rectWidth = Integer.parseInt(objectElement.getAttribute("height"));
                            collisionBoxes.add(new Rectangle(rectX, rectY, rectHeight, rectWidth));
                        }
                        break;
                    // Main Spawn Point 1
                    case("MainCharacterSpawn1"):
                        objectElement = (Element)objectData.item(0);
                        mainSpawnPoint[0] = Integer.parseInt(objectElement.getAttribute("x"));
                        mainSpawnPoint[1] = Integer.parseInt(objectElement.getAttribute("y"));
                        break;
                    case("Connection"):
                        for (int j = 0; j < objectDataLength; j++) {
                            objectElement = (Element) objectData.item(j);
                            String connectionName = objectElement.getAttribute("name");
                            Rectangle rect = new Rectangle(Integer.parseInt(objectElement.getAttribute("x")), Integer.parseInt(objectElement.getAttribute("y")),
                                    Integer.parseInt(objectElement.getAttribute("width")), Integer.parseInt(objectElement.getAttribute("height")));
                            String mapPath = "/resources/tmxfiles/" + connection + ".tmx";
                            String direction = objectElement.getAttribute("type");
                            String direction2 = direction.substring(direction.indexOf("_") + 1);
                            direction = direction.substring(0, direction.indexOf("_"));
                            mapConnections.add(new MapConnection(this.name, connection, connectionName, rect, mapPath,
                                    direction, direction2));
                        }
                        break;
                    case("Removables"):
                        for (int j = 0; j < objectDataLength; j++) {
                            objectElement = (Element) objectData.item(j);
                            String temp1 = objectElement.getAttribute("name");
                            int _id = temp1.indexOf("_");
                            String removableType = temp1.substring(0, _id);
                            String removableName = temp1.substring(_id + 1);
                            String temp2 = objectElement.getAttribute("type");
                            _id = temp2.indexOf("_");
                            String actionName = temp2.substring(0, _id);
                            String actionType = temp2.substring(_id + 1);
                            Rectangle rect = new Rectangle(Integer.parseInt(objectElement.getAttribute("x")), Integer.parseInt(objectElement.getAttribute("y")),
                                    Integer.parseInt(objectElement.getAttribute("width")), Integer.parseInt(objectElement.getAttribute("height")));
                            rect.translate(0, -rect.height); // For some reason object tiles are set by their bottom left corner\
                            long gid = Long.parseLong(objectElement.getAttribute("gid"));
                            boolean[] rotations = new boolean[3];
                            if ((gid & (1 << 31)) != 0) { // Checks Horizontal Flipping
                                rotations[0] = true;
                                gid = gid - (long) Math.pow(2, 31);
                            }
                            if ((gid & (1 << 30)) != 0) { // Checks Vertical Flipping
                                rotations[1] = true;
                                gid = gid - (long) Math.pow(2, 30);
                            }
                            if ((gid & (1 << 29)) != 0) { // Checks Diagonal Flipping
                                rotations[2] = true;
                                gid = gid - (long) Math.pow(2, 29);
                            }
                            mapRemovables.add(new RemovableObject(removableType, removableName, actionName, actionType,
                                    (int)gid, rotations, rect));
                            if (actionName.equals("Interact")) {
                                if (interactionDialogBoxes.get(removableType + "_" + removableName) == null) {
                                    interactionDialogBoxes.put(removableType + "_" + removableName,
                                            new InteractionDialogBox(removableType + "_" + removableName,
                                                    new Font("Arial", Font.PLAIN, 10), 5, 5, (Graphics2D) framework.getGraphics(), true));
                                }
                            }
                        }
                        break;
                    case ("TerrainChange"):
                        for (int j = 0; j < objectDataLength; j++) {
                            objectElement = (Element) objectData.item(j);
                            String temp1 = objectElement.getAttribute("name");
                            int _id = temp1.indexOf("_");
                            String method1 = temp1.substring(0, _id);
                            String method2 = temp1.substring(_id + 1);
                            String temp2 = objectElement.getAttribute("type");
                            _id = temp2.indexOf("_");
                            String side1 = temp2.substring(0, _id);
                            String side2 = temp2.substring(_id + 1);
                            Rectangle rect = new Rectangle(Integer.parseInt(objectElement.getAttribute("x")), Integer.parseInt(objectElement.getAttribute("y")),
                                    Integer.parseInt(objectElement.getAttribute("width")), Integer.parseInt(objectElement.getAttribute("height")));
                            mapTerrainChanges.add(new TerrainChange(rect, method1, method2, side1, side2));
                        }
                        break;
                    case ("CharacterSpawn"):
                        for (int j = 0; j < objectDataLength; j++) {
                            objectElement = (Element) objectData.item(j);
                            String name = objectElement.getAttribute("name");
                            String type = objectElement.getAttribute("type");
                            mapCharacters.put(name, new Character(name, Integer.parseInt(objectElement.getAttribute("x")),
                                    Integer.parseInt(objectElement.getAttribute("y")), framework));
                        }
                        break;
                    case ("CharacterPaths"):
                        for (int j = 0; j < objectDataLength; j++) {
                            objectElement = (Element) objectData.item(j);
                            String name = objectElement.getAttribute("name");
                            String type = objectElement.getAttribute("type");
                            Rectangle rect = new Rectangle(Integer.parseInt(objectElement.getAttribute("x")), Integer.parseInt(objectElement.getAttribute("y")),
                                    Integer.parseInt(objectElement.getAttribute("width")), Integer.parseInt(objectElement.getAttribute("height")));
                            mapCharacters.get(name).setWalkPath(type, rect);
                        }
                        break;
                    case ("CreatureSpawnBoxes"):
                        for (int j = 0; j < objectDataLength; j++) {
                            objectElement = (Element) objectData.item(j);
                            String name = objectElement.getAttribute("name");
                            String type = objectElement.getAttribute("type");
                            String[] creatures = type.split(",");
                            Rectangle rect = new Rectangle(Integer.parseInt(objectElement.getAttribute("x")), Integer.parseInt(objectElement.getAttribute("y")),
                                    Integer.parseInt(objectElement.getAttribute("width")), Integer.parseInt(objectElement.getAttribute("height")));
                            LinkedHashMap<String, Double> temp = new LinkedHashMap<>();
                            for (String creature: creatures) {
                                String[] values = creature.split("_");
                                if (!mapCreatures.containsKey(values[0])) {
                                    mapCreatures.put(values[0], new Creature(values[0], 0, 0, framework));
                                }
                                temp.put(values[0], Double.valueOf(values[1]));
                            }
                            mapCreatureSpawnBoxes.put(name, new CreatureSpawnBox(name, rect, temp));
                        }
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finished Loading Tile Data");
    }

    public void update(float elapsedTime, MainCharacter character, boolean[][] keyboardstate) {
        // Reset the dialog boxes as inactive
        for (InteractionDialogBox dialogBox : interactionDialogBoxes.values()) {
            dialogBox.setActive(false);
        }
        // update each of the map characters and
        // Reset the characters isDrawn and isStop
        for (Character mapCharacter : mapCharacters.values()) {
            mapCharacter.update(elapsedTime, keyboardstate);
            mapCharacter.setDrawn(false);
            if (mapCharacter.isStop()) {
                // Don't unStop the map character if they are speaking
                if (mapCharacter.getDialogBox().isActive()) {
                    mapCharacter.setStop(true);
                } // Don't unStop the map character unless the main character moves out of the way
                else if (mapCharacter.direction == 0 && mapCharacter.collisionBox.y + mapCharacter.collisionBox.height - yOffset >= character.collisionBox.y
                        && mapCharacter.collisionBox.x - xOffset <= character.collisionBox.x + character.collisionBox.width &&
                        mapCharacter.collisionBox.x + mapCharacter.collisionBox.width - xOffset >= character.collisionBox.x) {
                    mapCharacter.setStop(true);
                } else if (mapCharacter.direction == 1 && mapCharacter.collisionBox.x - xOffset <= character.collisionBox.x + character.collisionBox.width
                        &&  mapCharacter.collisionBox.y + mapCharacter.collisionBox.height - yOffset >= character.collisionBox.y &&
                        mapCharacter.collisionBox.y - yOffset <= character.collisionBox.y + character.collisionBox.height) {
                    mapCharacter.setStop(true);
                } else if (mapCharacter.direction == 2 && mapCharacter.collisionBox.x + mapCharacter.collisionBox.width - xOffset >= character.collisionBox.x
                        &&  mapCharacter.collisionBox.y + mapCharacter.collisionBox.height - yOffset >= character.collisionBox.y &&
                        mapCharacter.collisionBox.y - yOffset <= character.collisionBox.y + character.collisionBox.height) {
                    mapCharacter.setStop(true);
                } else if (mapCharacter.direction == 3 && mapCharacter.collisionBox.y - yOffset <= character.collisionBox.y + character.collisionBox.height
                        && mapCharacter.collisionBox.x - xOffset <= character.collisionBox.x + character.collisionBox.width &&
                        mapCharacter.collisionBox.x + mapCharacter.collisionBox.width - xOffset >= character.collisionBox.x) {
                    mapCharacter.setStop(true);
                } else {
                    mapCharacter.setStop(false);
                }
            }
        }
    }

    public void moveMap(MainCharacter character) {
        // Move the map if necessary
        int distance = 0;
        int correction = 0;
        if (character.x < playerMoveDistance && xOffset > 0) {
            distance = playerMoveDistance - character.x;
            xOffset = xOffset - distance;
            if (xOffset < 0) {
                correction = -xOffset;
                xOffset = 0;
            }
            character.x = playerMoveDistance - correction;
        } else if (character.x > windowWidth - character.characterWidth - playerMoveDistance && xOffset < maxXOffset) {
            distance = character.x - (windowWidth - character.characterWidth - playerMoveDistance);
            xOffset = xOffset + distance;
            if (xOffset > maxXOffset) {
                correction = maxXOffset-xOffset;
                xOffset = maxXOffset;
            }
            character.x = (windowWidth - character.characterWidth - playerMoveDistance) + correction;
        }
        correction = 0;
        if (character.y < playerMoveDistance && yOffset > 0) {
            distance = playerMoveDistance - character.y;
            yOffset = yOffset - distance;
            if (yOffset < 0) {
                correction = -yOffset;
                yOffset = 0;
            }
            character.y = playerMoveDistance - correction;
        } else if (character.y > windowHeight - character.characterHeight - playerMoveDistance && yOffset < maxYOffset) {
            distance = character.y - (windowHeight - character.characterHeight - playerMoveDistance);
            yOffset = yOffset + distance;
            if (yOffset > maxYOffset) {
                correction = maxYOffset-yOffset;
                yOffset = maxYOffset;
            }
            character.y = (windowHeight - character.characterHeight - playerMoveDistance) + correction;
        }
        character.collisionBox.setLocation(character.x + character.characterWidth/6, character.y + character.characterHeight/2);
    };

    public void resolveInteraction(MainCharacter character, boolean[][] keyboardstate) {
        // Deals with interactions between character and removable objects
        for (RemovableObject object : mapRemovables) {
            Rectangle actionRect = object.getActionRect();
            actionRect.translate(-xOffset, -yOffset);
            if (actionRect.intersects(character.collisionBox)) {
                if (object.getActionName().equals("Interact")) {
                    if (keyboardstate[KeyEvent.VK_SPACE][1]) { // If the space bar is pressed
                        if (object.getActionType().equals("Facing")) {
                            Rectangle rect = object.getRect();
                            rect.translate(-xOffset, -yOffset);
                            // Now check if it is interactable
                            if (character.direction == 0 && character.collisionBox.y + character.collisionBox.height <= rect.y) {
                                // Facing Down
                                object.remove(character);
                            } else if (character.direction == 1 && character.collisionBox.x >= rect.x + rect.width) {
                                // Facing Left
                                object.remove(character);
                            } else if (character.direction == 2 && character.collisionBox.x + character.collisionBox.width <= rect.x) {
                                // Facing Right
                                object.remove(character);
                            } else if (character.direction == 3 && character.collisionBox.y >= rect.y + rect.height) {
                                // Facing Up
                                object.remove(character);
                            }
                            rect.translate(xOffset, yOffset);
                        } else if (object.getActionType().equals("On")) {
                            object.remove(character);
                        }
                    } else if (!object.isRemoved()) {
                        interactionDialogBoxes.get(object.getRemovableType() + "_" + object.getRemovableName()).setActive(true);
                    }
                }
            }
            actionRect.translate(xOffset, yOffset);
        }

        // Resolve talking interactions with mapCharacters
        if (!character.isTalking()) {
            for (Character mapCharacter : mapCharacters.values()) {
                Rectangle actionRect = mapCharacter.getInteractionRect();
                actionRect.translate(-xOffset, -yOffset);
                if (actionRect.intersects(character.collisionBox)) {
                    if (keyboardstate[KeyEvent.VK_SPACE][1]) { // If the space bar is pressed
                        Rectangle rect = mapCharacter.collisionBox;
                        rect.translate(-xOffset, -yOffset);
                        if (character.direction == 0 && character.collisionBox.y + character.collisionBox.height <= rect.y) {
                            // Facing Down
                            mapCharacter.startDiaolog(3);
                            character.setStop(true);
                            character.setTalking(true);
                            rect.translate(xOffset, yOffset);
                            actionRect.translate(xOffset, yOffset);
                            break;
                        } else if (character.direction == 1 && character.collisionBox.x >= rect.x + rect.width) {
                            // Facing Left
                            mapCharacter.startDiaolog(2);
                            character.setStop(true);
                            character.setTalking(true);
                            rect.translate(xOffset, yOffset);
                            actionRect.translate(xOffset, yOffset);
                            break;
                        } else if (character.direction == 2 && character.collisionBox.x + character.collisionBox.width <= rect.x) {
                            // Facing Right
                            mapCharacter.startDiaolog(1);
                            character.setStop(true);
                            character.setTalking(true);
                            rect.translate(xOffset, yOffset);
                            actionRect.translate(xOffset, yOffset);
                            break;
                        } else if (character.direction == 3 && character.collisionBox.y >= rect.y + rect.height) {
                            // Facing Up
                            mapCharacter.startDiaolog(0);
                            character.setStop(true);
                            character.setTalking(true);
                            rect.translate(xOffset, yOffset);
                            actionRect.translate(xOffset, yOffset);
                            break;
                        }
                        rect.translate(xOffset, yOffset);
                    } else if (!mapCharacter.getDialogBox().isActive()) {
                        interactionDialogBoxes.get("Speak_Character_Instructions").setActive(true);
                    }
                }
                actionRect.translate(xOffset, yOffset);
            }
        }

        // Resolve interactions with CreatureSpawnBoxes
        for (CreatureSpawnBox box: mapCreatureSpawnBoxes.values()) {
            Rectangle rect = box.getRect();
            rect.translate(-xOffset, -yOffset);
            if ((character.vX != 0 || character.vY != 0) && rect.intersects(character.collisionBox)) {
                String creature = box.spawnCreature();
                if (creature != null) {
                    Creature c = mapCreatures.get(creature);
                    c.health = c.maxHealth;
                    c.mana = c.maxMana;
                    StartGameState.startBattleState(c);
                }
            }
            rect.translate(xOffset, yOffset);
        }

        // Resolve interactions with mapConnections
        for (MapConnection connection : mapConnections) {
            Rectangle rect = connection.getRect();
            rect.translate(-xOffset, -yOffset);
            if (rect.intersects(character.collisionBox)) {
                String mapOld = connection.getMapOld();
                String mapNew = connection.getMapNew();
                String connectionName = connection.getName();
                String direction = connection.getExitDirection();
                String mapPath = connection.getMapPath();
                boolean worked = false;
                int xCorrection = 0;
                int yCorrection = 0;
                if (direction.equals("Any")) {
                    // The connection type is Any, so you move to the map
                    StartGameState.changeMap(connection);
                    xCorrection = -character.characterWidth/2;
                    yCorrection = -character.characterHeight/2;
                    worked = true;
                } else if (direction.equals("Down") && character.direction == 0) {
                    // The connection type is down and player is moving down, so you move to the map
                    StartGameState.changeMap(connection);
                    worked = true;
                } else if (direction.equals("Left") && character.direction == 1) {
                    // The connection type is left and player is moving left, so you move to the map
                    StartGameState.changeMap(connection);
                    worked = true;
                } else if (direction.equals("Right") && character.direction == 2) {
                    // The connection type is right and player is moving right, so you move to the map
                    StartGameState.changeMap(connection);
                    worked = true;
                } else if (direction.equals("Up") && character.direction == 3) {
                    // The connection type is up and player is moving up, so you move to the map
                    StartGameState.changeMap(connection);
                    worked = true;
                }
                // The corrections are used to center the character in the spawn box
                // And also to account for which direction they entered the spawn box in
                // (Their back should be towards the edge of the spawn box)
                // This sets the map offset if necessary and corrects the character position with that offset
                // NOTE: The character's position now corresponds to the next map. THE CHARACTER SHOULD NOT BE MOVED UNTIL THE NEXT UPDATE!
                if (worked) {
                    System.out.println(mapOld + " to " + mapNew + " using " + connectionName + " by traveling " + direction);
                    switch (connection.getEntranceDirection()) {
                        case("Down"):
                            character.direction = 0;
                            xCorrection = -character.characterWidth/2;
                            yCorrection = -character.characterHeight/2;
                            break;
                        case("Left"):
                            character.direction = 1;
                            xCorrection = -character.characterWidth;
                            yCorrection = -character.characterHeight + character.collisionBox.height/2;
                            break;
                        case("Right"):
                            character.direction = 2;
                            xCorrection = 0;
                            yCorrection = -character.characterHeight + character.collisionBox.height/2;
                            break;
                        case("Up"):
                            character.direction = 3;
                            xCorrection = -character.characterWidth/2;
                            yCorrection = -character.characterHeight;
                            break;
                    }
                    character.translate(xCorrection, yCorrection);
                }
            }
            rect.translate(xOffset, yOffset);
        }
    }

    public void resolveCollisions(MainCharacter character) {
        // Deals with collisions against collision boxes (impassable)
        for (Rectangle rect : collisionBoxes) {
            rect.translate(-xOffset, -yOffset);
            int distance = 0;
            if (rect.intersects(character.collisionBox)) {
                // Collided traveling to the right
                if (character.collisionBox.x + character.collisionBox.width > rect.x &&
                        character.collisionBox.x + character.collisionBox.width - character.vX <= rect.x) {
                    distance = character.collisionBox.x + character.collisionBox.width - rect.x;
                    character.translate(-distance, 0);
                }
                // Collided traveling to the left
                else if (character.collisionBox.x < rect.x + rect.width &&
                        character.collisionBox.x - character.vX >= rect.x + rect.width) {
                    distance = (rect.x + rect.width) - character.collisionBox.x;
                    character.translate(distance, 0);
                }
                // Collided traveling down
                if (character.collisionBox.y + character.collisionBox.height > rect.y &&
                        character.collisionBox.y + character.collisionBox.height - character.vY <= rect.y) {
                    distance = character.collisionBox.y + character.collisionBox.height - rect.y;
                    character.translate(0, -distance);
                }
                // Collided traveling up
                else if (character.collisionBox.y < rect.y + rect.height &&
                        character.collisionBox.y - character.vY >= rect.y + rect.height) {
                    distance = (rect.y + rect.height) - character.collisionBox.y;
                    character.translate(0, distance);
                }
            }
            rect.translate(xOffset, yOffset);
        }
        // Resolve collisions with collidable removable objects ("Objects with the actionType facing")
        for (RemovableObject object : mapRemovables) {
            if (!object.isRemoved() && object.getActionType().equals("Facing")) {
                Rectangle rect = object.getRect();
                rect.translate(-xOffset, -yOffset);
                int distance = 0;
                if (rect.intersects(character.collisionBox)) {
                    // Collided traveling to the right
                    if (character.collisionBox.x + character.collisionBox.width > rect.x &&
                            character.collisionBox.x + character.collisionBox.width - character.vX <= rect.x) {
                        distance = character.collisionBox.x + character.collisionBox.width - rect.x;
                        character.translate(-distance, 0);
                    }
                    // Collided traveling to the left
                    else if (character.collisionBox.x < rect.x + rect.width &&
                            character.collisionBox.x - character.vX >= rect.x + rect.width) {
                        distance = (rect.x + rect.width) - character.collisionBox.x;
                        character.translate(distance, 0);
                    }
                    // Collided traveling down
                    if (character.collisionBox.y + character.collisionBox.height > rect.y &&
                            character.collisionBox.y + character.collisionBox.height - character.vY <= rect.y) {
                        distance = character.collisionBox.y + character.collisionBox.height - rect.y;
                        character.translate(0, -distance);
                    }
                    // Collided traveling up
                    else if (character.collisionBox.y < rect.y + rect.height &&
                            character.collisionBox.y - character.vY >= rect.y + rect.height) {
                        distance = (rect.y + rect.height) - character.collisionBox.y;
                        character.translate(0, distance);
                    }
                }
                rect.translate(xOffset, yOffset);
            }
        }

        // Resolve collisions between different map characters
        for (int i = 0; i < mapCharacters.size(); i++) {
            for (int j = i + 1; j < mapCharacters.size(); j++) {
                Character character1 = mapCharacters.getIndexed(i);
                Character character2 = mapCharacters.getIndexed(j);
                Rectangle rect1 = mapCharacters.getIndexed(i).collisionBox;
                Rectangle rect2 = mapCharacters.getIndexed(j).collisionBox;
                int distance = 0;
                if (rect1.intersects(rect2)) {
                    // Note, these collisions are from the perspective of character2
                    // Collided traveling to the right
                    if (rect2.x + rect2.width > rect1.x &&
                            rect2.x + rect2.width - character2.vX <= rect1.x - character1.vX) {
                        distance = rect2.x + rect2.width - rect1.x;
                        if (character2.vX > 0 && character1.vX < 0) {
                            if (distance % 2 == 1) {
                                character1.translate(distance / 2 + 1, 0);
                                character2.translate(-distance / 2, 0);
                            } else {
                                character1.translate(distance / 2, 0);
                                character2.translate(-distance / 2, 0);
                            }
                            character1.setStop(true);
                            character2.setStop(true);
                        } else if (character1.vX < 0) {
                            character.translate(distance, 0);
                            character1.setStop(true);
                        } else if (character2.vX > 0) {
                            character2.translate(-distance, 0);
                            character2.setStop(true);
                        }
                    }
                    // Collided traveling to the left
                    else if (rect2.x < rect1.x + rect1.width &&
                            rect2.x - character2.vX >= rect1.x + rect1.width - character1.vX) {
                        distance = (rect1.x + rect1.width) - rect2.x;
                        if (character2.vX < 0 && character1.vX > 0) {
                            if (distance % 2 == 1) {
                                character1.translate(-distance / 2 - 1, 0);
                                character2.translate(distance / 2, 0);
                            } else {
                                character1.translate(-distance / 2, 0);
                                character2.translate(distance / 2, 0);
                            }
                            character1.setStop(true);
                            character2.setStop(true);
                        } else if (character1.vX > 0) {
                            character1.translate(-distance, 0);
                            character1.setStop(true);
                        } else if (character2.vX < 0) {
                            character2.translate(distance, 0);
                            character2.setStop(true);
                        }
                    }
                    // Collided traveling down
                    if (rect2.y + rect2.height > rect1.y &&
                            rect2.y + rect2.height - character2.vY <= rect1.y - character1.vY) {
                        distance = rect2.y + rect2.height - rect1.y;
                        if (character2.vY > 0 && character1.vY < 0) {
                            if (distance % 2 == 1) {
                                character1.translate(0, distance / 2 + 1);
                                character2.translate(0, -distance / 2);
                            } else {
                                character1.translate(0, distance / 2);
                                character2.translate(0, -distance / 2);
                            }
                            character1.setStop(true);
                            character2.setStop(true);
                        } else if (character1.vY < 0) {
                            character1.translate(0, distance);
                            character1.setStop(true);
                        } else if (character2.vY > 0) {
                            character2.translate(0, -distance);
                            character2.setStop(true);
                        }
                    }
                    // Collided traveling up
                    else if (rect2.y < rect1.y + rect1.height &&
                            rect2.y - character2.vY >= rect1.y + rect1.height - character1.vY) {
                        distance = (rect1.y + rect1.height) - rect2.y;
                        if (character2.vY < 0 && character1.vY > 0) {
                            if (distance % 2 == 1) {
                                character1.translate(0, -distance / 2 - 1);
                                character2.translate(0, distance / 2);
                            } else {
                                character1.translate(0, -distance / 2);
                                character2.translate(0, distance / 2);
                            }
                            character1.setStop(true);
                            character2.setStop(true);
                        } else if (character1.vY > 0) {
                            character1.translate(0, -distance);
                            character1.setStop(true);
                        } else if (character2.vY < 0) {
                            character2.translate(0, distance);
                            character2.setStop(true);
                        }
                    }
                }
            }
        }

        // Resolve collisions between map characters and the main character
        for (Character mapCharacter : mapCharacters.values()) {
            Rectangle rect = mapCharacter.collisionBox;
            rect.translate(-xOffset, -yOffset);
            int distance = 0;
            if (rect.intersects(character.collisionBox)) {
                // Collided traveling to the right
                if (character.collisionBox.x + character.collisionBox.width > rect.x &&
                        character.collisionBox.x + character.collisionBox.width - character.vX <= rect.x - mapCharacter.vX) {
                    distance = character.collisionBox.x + character.collisionBox.width - rect.x;
                    character.translate(-distance, 0);
                    if (mapCharacter.vX < 0) {
                        mapCharacter.setStop(true);
                    }
                }
                // Collided traveling to the left
                else if (character.collisionBox.x < rect.x + rect.width &&
                        character.collisionBox.x - character.vX >= rect.x + rect.width - mapCharacter.vX) {
                    distance = (rect.x + rect.width) - character.collisionBox.x;
                    character.translate(distance, 0);
                    if (mapCharacter.vX > 0) {
                        mapCharacter.setStop(true);
                    }
                }
                // Collided traveling down
                if (character.collisionBox.y + character.collisionBox.height > rect.y &&
                        character.collisionBox.y + character.collisionBox.height - character.vY <= rect.y - mapCharacter.vY) {
                    distance = character.collisionBox.y + character.collisionBox.height - rect.y;
                    character.translate(0, -distance);
                    if (mapCharacter.vY < 0) {
                        mapCharacter.setStop(true);
                    }
                }
                // Collided traveling up
                else if (character.collisionBox.y < rect.y + rect.height &&
                        character.collisionBox.y - character.vY >= rect.y + rect.height - mapCharacter.vY) {
                    distance = (rect.y + rect.height) - character.collisionBox.y;
                    character.translate(0, distance);
                    if (mapCharacter.vY > 0) {
                        mapCharacter.setStop(true);
                    }
                }
            }
            rect.translate(xOffset, yOffset);
        }

        // Resolve collisions with terrain changes
        for (TerrainChange change : mapTerrainChanges) {
            Rectangle rect = change.getRect();
            rect.translate(-xOffset, -yOffset);
            if (rect.intersects(character.collisionBox)) {
                if (change.getMethod2().equals("Swim")) {
                    if (character.canSwim()) {
                        change.setEntered(true);
                    } else {
                        int distance = 0;
                        if (rect.intersects(character.collisionBox)) {
                            // Collided traveling to the right
                            if (character.collisionBox.x + character.collisionBox.width > rect.x &&
                                    character.collisionBox.x + character.collisionBox.width - character.vX <= rect.x) {
                                distance = character.collisionBox.x + character.collisionBox.width - rect.x;
                                character.translate(-distance, 0);
                            }
                            // Collided traveling to the left
                            else if (character.collisionBox.x < rect.x + rect.width &&
                                    character.collisionBox.x - character.vX >= rect.x + rect.width) {
                                distance = (rect.x + rect.width) - character.collisionBox.x;
                                character.translate(distance, 0);
                            }
                            // Collided traveling down
                            if (character.collisionBox.y + character.collisionBox.height > rect.y &&
                                    character.collisionBox.y + character.collisionBox.height - character.vY <= rect.y) {
                                distance = character.collisionBox.y + character.collisionBox.height - rect.y;
                                character.translate(0, -distance);
                            }
                            // Collided traveling up
                            else if (character.collisionBox.y < rect.y + rect.height &&
                                    character.collisionBox.y - character.vY >= rect.y + rect.height) {
                                distance = (rect.y + rect.height) - character.collisionBox.y;
                                character.translate(0, distance);
                            }
                        }
                    }
                }
            }
            if (change.isEntered()) {
                if (change.getSide1() == "Top") {
                    if (character.collisionBox.y + character.collisionBox.height <= rect.y) {
                        character.changeTravelMethod(change.getMethod1());
                        change.setEntered(false);
                    } else if(character.collisionBox.y >= rect.y + rect.height) {
                        character.changeTravelMethod(change.getMethod2());
                        change.setEntered(false);
                    }
                } else if (change.getSide1() == "Bottom") {
                    if (character.collisionBox.y + character.collisionBox.height <= rect.y) {
                        character.changeTravelMethod(change.getMethod2());
                        change.setEntered(false);
                    } else if(character.collisionBox.y >= rect.y + rect.height) {
                        character.changeTravelMethod(change.getMethod1());
                        change.setEntered(false);
                    }
                } else if (change.getSide1() == "Left") {
                    if (character.collisionBox.x + character.collisionBox.width <= rect.x) {
                        character.changeTravelMethod(change.getMethod1());
                        change.setEntered(false);
                    } else if(character.collisionBox.x >= rect.x + rect.width) {
                        character.changeTravelMethod(change.getMethod2());
                        change.setEntered(false);
                    }
                } else if (change.getSide1() == "Right") {
                    if (character.collisionBox.x + character.collisionBox.width <= rect.x) {
                        character.changeTravelMethod(change.getMethod2());
                        change.setEntered(false);
                    } else if(character.collisionBox.x >= rect.x + rect.width) {
                        character.changeTravelMethod(change.getMethod1());
                        change.setEntered(false);
                    }
                }
            }
            rect.translate(xOffset, yOffset);
        }

        // Resolve collisions with the edge of the map
        if (character.x < 0) {
            character.setPosition(0, character.y);
        } else if (character.x + character.characterWidth > windowWidth) {
            character.setPosition(windowWidth - character.characterWidth, character.y);
        }
        if (character.y < 0) {
            character.setPosition(character.x, 0);
        } else if (character.y + character.characterHeight > windowHeight) {
            character.setPosition(character.x, windowHeight - character.characterHeight);
        }
    }

    public void drawBottomLayer(Graphics2D g2d, int mainCharacterY) {
        // Determine which tiles need to be drawn based on the offsets
        int xTileOffset = Math.floorDiv(xOffset, tileWidth);
        int yTileOffset = Math.floorDiv(yOffset, tileHeight);
        int rightExtraTile = 1;
        int leftExtraTile = 1;
        int topExtraTile = 1;
        int botExtraTile = 1;
        if (xTileOffset == 0) {leftExtraTile = 0;}
        else if (xTileOffset == maxXTileOffset) {rightExtraTile = 0;}
        if (yTileOffset == 0) {topExtraTile = 0;}
        else if (yTileOffset == maxYTileOffset) {botExtraTile = 0;}

        int tileCounter = 0;
        int gid = 0;
        for (int i = yTileOffset - topExtraTile; i < yTileOffset + windowTileHeight + botExtraTile; i++) {
            for (int j = xTileOffset - leftExtraTile; j < xTileOffset + windowTileWidth + rightExtraTile; j++) {
                tileCounter = j + (mapTileWidth)*(i);

                gid = backgroundLayerMap[tileCounter]; // Get the stored gid
                // ERROR WITH CODE: Works with square tiles, but will not work with rectangular tiles.
                // This is because when a tile is rotated 90 or 270 degrees the width and height switch.
                // This error will need fixing before implementation with non-square tiles.
                // The tile height and width are used from the class-wide variable in the getTransform method.
                AffineTransform afx = getTransform(backgroundRotations.get(tileCounter), j*tileWidth - xOffset,
                        i*tileHeight - yOffset);
                getTileSet(gid).drawTile(g2d, gid, afx);
                if (foregroundLayer.containsKey(tileCounter)) {
                    gid = foregroundLayer.get(tileCounter);
                    afx = getTransform(foregroundRotations.get(tileCounter), j*tileWidth - xOffset,
                            i*tileHeight - yOffset);
                    getTileSet(gid).drawTile(g2d, gid, afx);
                }

                if (foregroundLayer2 != null) {
                    if (foregroundLayer2.containsKey(tileCounter)) {
                        gid = foregroundLayer2.get(tileCounter);
                        afx = getTransform(foregroundRotations2.get(tileCounter), j*tileWidth - xOffset,
                                i*tileHeight - yOffset);
                        getTileSet(gid).drawTile(g2d, gid, afx);
                    }
                }
                // Rotate/Flip the drawing if necessary
                // This was the original code but it created a new image for every drawing so it no longer is used
                //BufferedImage temp = transformTile(getTileSet(gid).getTile(gid - firstGid.get(getTileSetIndex(gid))),
                //        backgroundRotations.get(tileCounter)); // Get the Tile
                //g2d.drawImage(temp, j*tileWidth, i*tileHeight, tileWidth, tileHeight, null);// draw the Tile
                //tileCounter += 1;
            }
        }
        // draw Removable objects if they are not yet removed
        for (RemovableObject object: mapRemovables) {
            if (!object.isRemoved()) {
                object.draw(g2d, this);
            }
        }
        // draw characters that are above the main character
        // Also, due to the sorting done in initialization, characters will be drawn from top to bottom
        for (Character character: mapCharacters.values()) {
            if (character.y - yOffset <= mainCharacterY) {
                character.draw(g2d, xOffset, yOffset);
                character.setDrawn(true);
            }
        }
    };

    public void drawTopLayer(Graphics2D g2d, MainCharacter character) {
        int xTileOffset = Math.floorDiv(xOffset, mapTileWidth);
        int xExtraTile = 0;
        if (xTileOffset*mapTileWidth - xOffset != 0) {
            xExtraTile = 1;
        }
        int yTileOffset = Math.floorDiv(yOffset, mapTileHeight);
        int yExtraTile = 0;
        if (yTileOffset*mapTileHeight - yOffset != 0) {
            yExtraTile = 1;
        }
        // Draws all the Top layer objects
        for (Integer key : topLayer.keySet()) {
            int row = Math.floorDiv(key, mapTileWidth);
            int col = key - row*mapTileWidth;
            int gid = topLayer.get(key);
            AffineTransform afx = getTransform(topRotations.get(key), col*tileWidth - xOffset,
                    row*tileHeight - yOffset);
            getTileSet(gid).drawTile(g2d, gid, afx);
        }

        if (topLayer2 != null) {
            for (Integer key : topLayer2.keySet()) {
                int row = Math.floorDiv(key, mapTileWidth);
                int col = key - row*mapTileWidth;
                int gid = topLayer2.get(key);
                AffineTransform afx = getTransform(topRotations2.get(key), col*tileWidth - xOffset,
                        row*tileHeight - yOffset);
                getTileSet(gid).drawTile(g2d, gid, afx);
            }
        }

        // draw the characters that are below the MainCharacter;
        for (Character charact: mapCharacters.values()) {
            if (!charact.isDrawn()) {
                charact.draw(g2d, xOffset, yOffset);
                charact.setDrawn(true);
            }
        }

        // draw the interactionDialogBoxes
        for (InteractionDialogBox dialogBox : interactionDialogBoxes.values()) {
            if (dialogBox.isActive()) {
                dialogBox.draw(g2d);
            }
        }

        // This code draws the map collisionBoxes, it is for testing purposes only
        g2d.setColor(Color.BLACK);
        for (Rectangle rect : collisionBoxes) {
            rect.translate(-xOffset, -yOffset);
            g2d.draw(rect);
            rect.translate(xOffset, yOffset);
        }

        // This code draws map connection boxes, it is for testing purposes only
        g2d.setColor(Color.YELLOW);
        for (MapConnection mapConnection: mapConnections) {
            Rectangle rect = mapConnection.getRect();
            rect.translate(-xOffset, -yOffset);
            g2d.draw(rect);
            rect.translate(xOffset, yOffset);
        }

        // This code draws map removable boxes blue, it is for testing purposes only
        g2d.setColor(Color.BLUE);
        for(RemovableObject removable: mapRemovables) {
            if (!removable.isRemoved()) {
                if (removable.getActionName().equals("Interact")) {
                    Rectangle rect = removable.getActionRect();
                    rect.translate(-xOffset, -yOffset);
                    g2d.draw(rect);
                    rect.translate(xOffset, yOffset);
                }
            }
        }

        // This code draws map terrain change boxes green, it is for testing purposes only
        g2d.setColor(Color.GREEN);
        for (TerrainChange terrainChange: mapTerrainChanges) {
            Rectangle rect = terrainChange.getRect();
            rect.translate(-xOffset, -yOffset);
            g2d.draw(rect);
            rect.translate(xOffset, yOffset);
        }

        // This code draws map creature spawn boxes dark gray, it is for testing purposes only
        g2d.setColor(Color.darkGray);
        for (CreatureSpawnBox box: mapCreatureSpawnBoxes.values()) {
            Rectangle rect = box.getRect();
            rect.translate(-xOffset, -yOffset);
            g2d.draw(rect);
            rect.translate(xOffset, yOffset);
        }
    }

    public TileSet getTileSet(int gid) {
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

    public AffineTransform getTransform(boolean[] tmxRotations, int posX, int posY) {
        AffineTransform tx = null;

        if (tmxRotations[0]) {
            if (tmxRotations[1]) {
                if (tmxRotations[2]) {
                    // Rotated 270 degrees clockwise,
                    tx = AffineTransform.getRotateInstance(Math.toRadians(90));
                    // Then flipped horizontally
                    tx.scale(-1, 1);
                    tx.translate(-tileWidth - posY, -tileHeight - posX);
                } else {
                    // Rotated 180 degrees
                    tx = AffineTransform.getRotateInstance(Math.toRadians(180));
                    tx.translate(-tileWidth - posX, -tileHeight - posY);
                }
            } else {
                if (tmxRotations[2]) {
                    // Rotated 90 degrees clockwise
                    tx = AffineTransform.getRotateInstance(Math.toRadians(90));
                    tx.translate(posY, -tileHeight - posX);
                } else {
                    // Flipped Horizontally (x direction)
                    tx = AffineTransform.getScaleInstance(-1, 1);
                    tx.translate(-tileWidth - posX, posY);
                }
            }
        } else {
            if (tmxRotations[1]) {
                if (tmxRotations[2]) {
                    // Rotated 270 degrees clockwise
                    tx = AffineTransform.getRotateInstance(Math.toRadians(270));
                    tx.translate(-tileWidth - posY, posX);
                } else {
                    // Flipped Vertically
                    tx = AffineTransform.getScaleInstance(1, -1);
                    tx.translate(posX, -tileHeight -posY);
                }
            } else {
                if (tmxRotations[2]) {
                    // Rotated 90 degress clockwise
                    tx = AffineTransform.getRotateInstance(Math.toRadians(270));
                    // Then flipped horizontally
                    tx.scale(-1, 1);
                    tx.translate(posY, posX);
                } else {
                    //Do Nothing
                    tx = AffineTransform.getTranslateInstance(posX, posY);
                }
            }
        }
        return tx;
    }

    public int getMainSpawnX() {
        return mainSpawnPoint[0];
    }

    public int getMainSpawnY() {
        return mainSpawnPoint[1];
    }

    public String getName() {
        return name;
    }
}

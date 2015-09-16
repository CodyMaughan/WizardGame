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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Cody on 9/12/2015.
 */
public class TileMap {

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
    protected ArrayList<Rectangle>  collisionBoxes; // A list of all the collisionBoxes for the map
    protected int playerMoveDistance; // A set distance from the edges of the window where the map begins to move with the player.
    protected int[] mainSpawnPoint; // A list of the main spawn points |||| NOTE: (This object is subject to being replaced by mapConnections)
    protected ArrayList<MapConnection> mapConnections; // A map of the object boxes that indicate map connections to the corresponding map file path


    public TileMap(int windowWidth, int windowHeight, String tmxFilePath, MapConnection connection) {
        int charID1 = tmxFilePath.indexOf("/resources/tmxfiles/");
        int charID2 = tmxFilePath.indexOf(".tmx");
        this.name = tmxFilePath.substring(charID1 + 20, charID2);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        mapConnections = new ArrayList<>();
        mainSpawnPoint = new int[2];
        readtmxFile(tmxFilePath);
        windowTileWidth = (int)Math.ceil((double)windowWidth/tileHeight);
        windowTileHeight = (int)Math.ceil((double)windowHeight/tileHeight);
        xOffset = 0;
        yOffset = 0;
        maxXOffset = mapTileWidth*tileWidth - windowWidth;
        maxYOffset = mapTileHeight*tileHeight - windowHeight;
        maxXTileOffset = Math.floorDiv(maxXOffset,tileWidth);
        maxYTileOffset = Math.floorDiv(maxYOffset,tileHeight);
        playerMoveDistance = tileWidth*6; // Once the player hits 6 Tiles from the edge the map will start to move instead of the character
        if (connection != null) {
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
                            if (gid < 0 || gid > 140) {
                                System.out.println(gid);
                                System.out.println("Index: " + j);
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
                                if (gid < 0 || gid > 140) {
                                    System.out.println(gid);
                                    System.out.println("Index: " + j);
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
                                if (gid < 0 || gid > 140) {
                                    System.out.println(gid);
                                    System.out.println("Index: " + j);
                                }
                                topLayer.put(j, (int)gid);
                                topRotations.put(j, rotations);
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
                            String mapPath = this.getClass().getResource("/resources/tmxfiles/" + connection + ".tmx").getPath();
                            String direction = objectElement.getAttribute("type");
                            mapConnections.add(new MapConnection(this.name, connection, connectionName, rect, mapPath, direction));
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
        character.collisionBox.setLocation(character.x + character.characterWidth/4, character.y + character.characterHeight/2);
    };

    public void resolveCollisions(MainCharacter character) {
        // WARNING, WARNING, WARNING!!!!!!!!
        // The if statements here may be inefficient. Consider recoding them.
        for (Rectangle rect : collisionBoxes) {
            rect.translate(-xOffset, -yOffset);
            int distance = 0;
            if (rect.intersects(character.collisionBox)) {
                // Collided traveling to the right
                if (character.collisionBox.x + character.collisionBox.width > rect.x &&
                        character.collisionBox.x + character.collisionBox.width - character.vX <= rect.x) {
                    distance = character.collisionBox.x + character.collisionBox.width - rect.x;
                    character.translate(-distance, 0);
                    //character.setPosition(rect.x - character.collisionBox.width, 0);
                }
                // Collided traveling to the left
                else if (character.collisionBox.x < rect.x + rect.width &&
                        character.collisionBox.x - character.vX >= rect.x + rect.width) {
                    //character.setPosition(rect.x + rect.width, 0);
                    distance = (rect.x + rect.width) - character.collisionBox.x;
                    character.translate(distance, 0);
                }
                // Collided traveling down
                if (character.collisionBox.y + character.collisionBox.height > rect.y &&
                        character.collisionBox.y + character.collisionBox.height - character.vY <= rect.y) {
                    //character.setPosition(0, rect.y - character.collisionBox.height);
                    distance = character.collisionBox.y + character.collisionBox.height - rect.y;
                    character.translate(0, -distance);
                }
                // Collided traveling up
                else if (character.collisionBox.y < rect.y + rect.height &&
                        character.collisionBox.y - character.vY >= rect.y + rect.height) {
                    //character.setPosition(0, rect.y + rect.height);
                    distance = (rect.y + rect.height) - character.collisionBox.y;
                    character.translate(0, distance);
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
        // Resolve collisions with mapConnections
        for (MapConnection connection : mapConnections) {
            Rectangle rect = connection.getRect();
            rect.translate(-xOffset, -yOffset);
            if (rect.intersects(character.collisionBox)) {
                String mapOld = connection.getMapOld();
                String mapNew = connection.getMapNew();
                String connectionName = connection.getName();
                String direction = connection.getDirection();
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
                    xCorrection = -character.characterWidth/2;
                    yCorrection = -character.characterHeight/2;
                    worked = true;
                } else if (direction.equals("Left") && character.direction == 1) {
                    // The connection type is left and player is moving left, so you move to the map
                    StartGameState.changeMap(connection);
                    xCorrection = 0;
                    yCorrection = -character.characterHeight/2;
                    worked = true;
                } else if (direction.equals("Right") && character.direction == 2) {
                    // The connection type is right and player is moving right, so you move to the map
                    StartGameState.changeMap(connection);
                    xCorrection = 0;
                    yCorrection = -character.characterHeight/2;
                    worked = true;
                } else if (direction.equals("Up") && character.direction == 3) {
                    // The connection type is up and player is moving up, so you move to the map
                    StartGameState.changeMap(connection);
                    xCorrection = -character.characterWidth/2;
                    yCorrection = -character.characterHeight;
                    worked = true;
                }
                // The corrections are used to center the character in the spawn box
                // And also to account for which direction they entered the spawn box in
                // (Their back should be towards the edge of the spawn box)
                // This sets the map offset if necessary and corrects the character position with that offset
                // NOTE: The character's position now corresponds to the next map. THE CHARACTER SHOULD NOT BE MOVED UNTIL THE NEXT UPDATE!
                if (worked) {
                    System.out.println(mapOld + " to " + mapNew + " using " + connectionName + " by traveling " + direction);
                    System.out.println("CharacterX: " + character.x + ", CharacterY: " + character.y);
                    character.translate(xCorrection, yCorrection);
                }
            }
            rect.translate(xOffset, yOffset);
        }
    }

    public void drawBottomLayer(Graphics2D g2d) {
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
                // Rotate/Flip the drawing if necessary
                // This was the original code but it created a new image for every drawing so it no longer is used
                //BufferedImage temp = transformTile(getTileSet(gid).getTile(gid - firstGid.get(getTileSetIndex(gid))),
                //        backgroundRotations.get(tileCounter)); // Get the Tile
                //g2d.drawImage(temp, j*tileWidth, i*tileHeight, tileWidth, tileHeight, null);// Draw the Tile
                //tileCounter += 1;
            }
        }

    };

    public void drawTopLayer(Graphics2D g2d) {
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
        // Currently Draws all the Top layer objects regardless of offset. Add if statement to check that
        // row and col fit within the possible tiles on the map?
        for (Integer key : topLayer.keySet()) {
            int row = Math.floorDiv(key, mapTileWidth);
            int col = key - row*mapTileWidth;
            int gid = topLayer.get(key);
            AffineTransform afx = getTransform(topRotations.get(key), col*tileWidth - xOffset,
                    row*tileHeight - yOffset);
            getTileSet(gid).drawTile(g2d, gid, afx);
        }
        // This code draws the map collisionBoxes, it is for testing purposes only
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
    }

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

    private AffineTransform getTransform(boolean[] tmxRotations, int posX, int posY) {
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
}

import java.awt.*;

/**
 * Created by Cody on 9/14/2015.
 */
public class MapConnection {

    private String mapOld;
    private String mapNew;
    private String connectionName;
    private Rectangle rect;
    private String mapPath;
    private String exitDirection;
    private String entranceDirection;

    public MapConnection(String mapOld, String mapNew, String connectionName, Rectangle rect, String mapPath,
                         String exitDir, String entranceDir) {
        this.mapOld = mapOld;
        this.mapNew = mapNew;
        this.connectionName = connectionName;
        this.rect = rect;
        this.mapPath = mapPath;
        this.exitDirection = exitDir;
        this.entranceDirection = entranceDir;
    }

    public String getMapOld() {
        return mapOld;
    }

    public String getMapNew() {
        return mapNew;
    }

    public String getName() {
        return connectionName;
    }

    public Rectangle getRect() {
        return rect;
    }

    public String getMapPath() {
        return mapPath;
    }

    public String getExitDirection() {
        return exitDirection;
    }

    public String getEntranceDirection() {return entranceDirection; }

}

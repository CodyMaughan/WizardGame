import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Cody on 9/17/2015.
 */
public class MapData {

    // This class is used to store some information about the map in RAM
    // For Example: Keep track of which wizards you have already fought and what items you have already removed

    private ArrayList<RemovableObject> mapRemovables;

    public MapData(TileMap tilemap) {
        this.mapRemovables = tilemap.mapRemovables;
    }

    public void restoreMapData(TileMap tilemap) {
        tilemap.mapRemovables = this.mapRemovables;
    }

}

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/17/2015.
 */
public class MapManager {

    private static MapManager manager;
    private static Map<String, MapData> mapDataList;

    private MapManager() {
        mapDataList = new HashMap<>();
    }

    public static MapManager getInstance() {
        if (manager == null) {
            manager = new MapManager();
        }
        return manager;
    }

    public static void addMapData(TileMap map) {
        mapDataList.put(map.getName(), new MapData(map));
    }

    public static void addMapData(String name, MapData data) {
        mapDataList.put(name, data);
    }

    public static void removeMapData(String name) {
        mapDataList.remove(name);
    }

    public static MapData getMapData(String name){
        return mapDataList.get(name);
    }

}

import java.util.*;

/**
 * Created by Cody on 9/20/2015.
 */
public class MapUtility {

    public static LinkedHashMap<String, Character> sortCharactersByY(Map<String, Character> characters) {
        // I don't know if this is the most efficient way of doing this,
        // so if you come up with a better way please feel free to improve it
        LinkedHashMap<String, Character> newMap = new LinkedHashMap<>();
        TreeMap<Integer, Character> temp = new TreeMap<>(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.compare((int)o1, (int)o2);
            }
        });
        for (Character character : characters.values()) {
            temp.put(character.y, character);
        }
        for (Character character : temp.values()) {
            newMap.put(character.characterName, character);
        }
        return newMap;
    }

}

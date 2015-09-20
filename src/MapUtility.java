import java.util.*;

/**
 * Created by Cody on 9/20/2015.
 */
public class MapUtility {

    public static IndexedLinkedHashMap<String, Character> sortCharactersByY(Map<String, Character> characters) {
        // I don't know if this is the most efficient way of doing this,
        // so if you come up with a better way please feel free to improve it
        IndexedLinkedHashMap<String, Character> newMap = new IndexedLinkedHashMap<>();
        TreeMap<Integer, Character> temp = new TreeMap<>(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                int i = Integer.compare((int)o1, (int)o2);
                if (i == 0) {
                   i = -1;
                }
                return i;
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

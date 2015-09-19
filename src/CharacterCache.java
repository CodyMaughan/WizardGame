import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/19/2015.
 */
public class CharacterCache {

    private static Map<String, CharacterInfo> characters = new HashMap<String, CharacterInfo>() {
        {
            put("Character1", new CharacterInfo("Character1", "/resources/images/Character1.png", "", 64, 64));
            put("Character2", new CharacterInfo("Character2", "/resources/images/Character2.png", "", 64, 64));
            put("Character3", new CharacterInfo("Character3", "/resources/images/Character3.png", "", 64, 64));
        }
    };

    public static CharacterInfo getCharacterInfo(String name) {
        return characters.get(name);
    }
}

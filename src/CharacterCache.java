import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/19/2015.
 */
public class CharacterCache {

    private static Map<String, CharacterInfo> characters = new HashMap<String, CharacterInfo>() {
        {
            put("Character1", new CharacterInfo("Character1", "/resources/images/Characters/Character1.png", "", 64, 64));
            put("Character2", new CharacterInfo("Character2", "/resources/images/Characters/Character2.png", "", 64, 64));
            put("Character3", new CharacterInfo("Character3", "/resources/images/Characters/Character3.png", "", 64, 64));
            put("Character4", new CharacterInfo("Character4", "/resources/images/Characters/Character4.png", "", 64, 64));
            put("AlchemistVendor_MageCity", new CharacterInfo("AlchemistVendor_MageCity",
                    "/resources/images/Characters/male_blue_chemist_merchant_by_leon_murayami-d96zlqr.png", "", 64, 64));
            put("Mage1_MageCity", new CharacterInfo("Mage1_MageCity", "/resources/images/Characters/mage__male__by_darklack-d90t7qq.png",
                    "", 64, 64));
            put("Mage2_MageCity", new CharacterInfo("Mage2_MageCity", "/resources/images/Characters/mage__female__by_darklack-d90t8i1.png",
                    "", 64, 64));
            put("Mage3_MageCity", new CharacterInfo("Mage3_MageCity", "/resources/images/Characters/mage__female__by_darklack-d90t8i1.png",
                    "", 64, 64));
            put("Pervert1_MageCity", new CharacterInfo("Pervert1_MageCity",
                    "/resources/images/Characters/rmvx___sprite_reqest_autolycus_by_leon_murayami-d7eze2n.png", "", 64, 64));

            put("Necromancer", new CharacterInfo("Necromancer", "/resources/images/Characters/necromancer__male__by_darklack-d92uqtq.png", "", 64, 64));
        }
    };

    public static CharacterInfo getCharacterInfo(String name) {
        return characters.get(name);
    }
}

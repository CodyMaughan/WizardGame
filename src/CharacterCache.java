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

            // Mage City Characters
            put("AlchemistVendor_MageCity", new CharacterInfo("AlchemistVendor_MageCity",
                    "/resources/images/Characters/male_blue_chemist_merchant_by_leon_murayami-d96zlqr.png", "", 64, 64));
            put("Innkeeper_MageCity", new CharacterInfo("Innkeeper_MageCity",
                    "/resources/images/Characters/free_radical_game_sprites__attempt__by_demonhuntrpg-d5tbcs4-0-0.png", "increaseInteractionBoxHeight", 64, 64));
            put("InnkeeperDaughter_MageCity", new CharacterInfo("InnkeeperDaughter_MageCity",
                    "/resources/images/Characters/rmvx___mystic_quest___kaeli_by_leon_murayami-d8knd49.png", "", 64, 64));
            put("Mage1_MageCity", new CharacterInfo("Mage1_MageCity",
                    "/resources/images/Characters/mage__male__by_darklack-d90t7qq.png", "", 64, 64));
            put("Mage2_MageCity", new CharacterInfo("Mage2_MageCity",
                    "/resources/images/Characters/mage__female__by_darklack-d90t8i1.png", "", 64, 64));
            put("Mage3_MageCity", new CharacterInfo("Mage3_MageCity",
                    "/resources/images/Characters/mage__female__by_darklack-d90t8i1.png", "", 64, 64));
            put("Mage4_MageCity", new CharacterInfo("Mage4_MageCity",
                    "/resources/images/Characters/mage__female__by_darklack-d90t8i1.png", "", 64, 64));
            put("Cleric1_MageCity", new CharacterInfo("Cleric1_MageCity",
                    "/resources/images/Characters/cleric__male__by_darklack-d95lbvu.png", "", 64, 64));
            put("Annabelle_MageCity", new CharacterInfo("Annabelle_MageCity",
                    "/resources/images/Characters/Character2.png", "", 64, 64));
            put("Pervert1_MageCity", new CharacterInfo("Pervert1_MageCity",
                    "/resources/images/Characters/rmvx___sprite_reqest_autolycus_by_leon_murayami-d7eze2n.png", "", 64, 64));
            put("PalaceGuard1_MageCity", new CharacterInfo("PalaceGuard1_MageCity",
                    "/resources/images/Characters/pack_knights_1_by_darklack-d90exuq-1-0.png", "", 64, 64));
            put("PalaceGuard2_MageCity", new CharacterInfo("PalaceGuard2_MageCity",
                    "/resources/images/Characters/pack_knights_1_by_darklack-d90exuq-1-0.png", "", 64, 64));
            put("CityGuard1_MageCity", new CharacterInfo("CityGuard1_MageCity",
                    "/resources/images/Characters/pack_knights_1_by_darklack-d90exuq-1-0.png", "", 64, 64));
            put("CityGuard2_MageCity", new CharacterInfo("CityGuard2_MageCity",
                    "/resources/images/Characters/pack_knights_1_by_darklack-d90exuq-1-0.png", "", 64, 64));
            put("CityGuard3_MageCity", new CharacterInfo("CityGuard3_MageCity",
                    "/resources/images/Characters/pack_knights_1_by_darklack-d90exuq-1-0.png", "", 64, 64));
            put("CityGuard4_MageCity", new CharacterInfo("CityGuard4_MageCity",
                    "/resources/images/Characters/pack_knights_1_by_darklack-d90exuq-1-1.png", "", 64, 64));
            put("CityGuard5_MageCity", new CharacterInfo("CityGuard5_MageCity",
                    "/resources/images/Characters/pack_knights_1_by_darklack-d90exuq-1-1.png", "", 64, 64));
            put("CityGuard6_MageCity", new CharacterInfo("CityGuard6_MageCity",
                    "/resources/images/Characters/pack_knights_1_by_darklack-d90exuq-1-1.png", "", 64, 64));
            put("CityGuard7_MageCity", new CharacterInfo("CityGuard7_MageCity",
                    "/resources/images/Characters/pack_knights_1_by_darklack-d90exuq-1-1.png", "", 64, 64));
            put("CityGuard8_MageCity", new CharacterInfo("CityGuard8_MageCity",
                    "/resources/images/Characters/pack_knights_1_by_darklack-d90exuq-1-0.png", "", 64, 64));

            put("Necromancer", new CharacterInfo("Necromancer", "/resources/images/Characters/necromancer__male__by_darklack-d92uqtq.png", "", 64, 64));
        }
    };

    public static CharacterInfo getCharacterInfo(String name) {
        return characters.get(name);
    }
}

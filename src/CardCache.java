import java.util.HashMap;

/**
 * Created by Cody on 9/27/2015.
 */
public class CardCache {


    private static final HashMap<String, String> cardExplanations = new HashMap<String, String>() {
        {
            put("Singe", "Using the power of fire, singe your opponent's eyebrows!");
            put("Quake", "Using the power of earth, quake the ground beneath your opponent!");
            put("Frost", "Using the power of ice, conjure an early morning frost on your opponent's face!");
        }
    };

    private static final HashMap<String, Card> cards = new HashMap<String, Card>() {
        {
            put("Singe", new Card("Singe", "Singe", 10, 5, new CardEffect(), "/resources/images/Cards/40X56 Card Frames Revised/fire_test.png"));
            put("Quake", new Card("Quake", "Quake", 10, 5, new CardEffect(), "/resources/images/Cards/40X56 Card Frames Revised/earth_test.png"));
            put("Frost", new Card("Frost", "Frost", 10, 5, new CardEffect(), "/resources/images/Cards/40X56 Card Frames Revised/ice_test.png"));
            put("Time Travel", new Card("Time Travel", "Time Travel", 0, 10, new CardEffect(), "/resources/images/Cards/40X56 Card Frames Revised/test.png"));
        }
    };

    public static Card getCard(String name) {
        return cards.get(name);
    }

    public static String getCardExplanation(String name) {
        return cardExplanations.get(name);
    }
}

import java.util.HashMap;

/**
 * Created by Cody on 9/27/2015.
 */
public class CardCache {

    private static final HashMap<String, Card> cards = new HashMap<String, Card>() {
        {
            put("Singe", new Card("Singe", "Singe", 10, 5, new CardEffect(), "/resources/images/Cards/40X56 Card Frames Revised/Color_Mirror.png"));
            put("Quake", new Card("Quake", "Quake", 10, 5, new CardEffect(), "/resources/images/Cards/40X56 Card Frames Revised/Color_Mirror.png"));
            put("Frost", new Card("Frost", "Frost", 10, 5, new CardEffect(), "/resources/images/Cards/40X56 Card Frames Revised/Color_Mirror.png"));
            put("Time Travel", new Card("Time Travel", "Time Travel", 0, 10, new CardEffect(), "/resources/images/Cards/40X56 Card Frames Revised/Color_Mirror.png"));
        }
    };

    public static Card getCard(String name) {
        return cards.get(name);
    }

}

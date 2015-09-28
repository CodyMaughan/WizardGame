import java.util.HashMap;

/**
 * Created by Cody on 9/27/2015.
 */
public class CardCache {

    private static final HashMap<String, Card> cards = new HashMap<String, Card>() {
        {
            put("Singe", new Card("Singe", "Singe", 10, 5));
            put("Quake", new Card("Quake", "Quake", 10, 5));
            put("Frost", new Card("Frost", "Frost", 10, 5));
        }
    };

}

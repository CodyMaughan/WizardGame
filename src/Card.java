/**
 * Created by Cody on 9/27/2015.
 */
public class Card {

    private String id;
    private String cardName;
    private int damage;
    private int magicCost;

    public Card(String id, String cardName, int damage, int magicCost) {
        this.id = id;
        this.cardName = cardName;
        this.damage = damage;
        this.magicCost = magicCost;
    }

}

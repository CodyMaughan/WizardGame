/**
 * Created by Cody on 9/27/2015.
 */
public class CardEffect {

    private String type;
    private int damage;

    public CardEffect() {

    }

    public void activate() {
        if (type.contains("Damage")) {
            // Apply damage
        }
        if (type.contains("Paralyze")) {
            // Random chance of paralyzation
            // MainCharacter.battleStatus = paralyze

        }
    }

}

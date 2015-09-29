/**
 * Created by Cody on 9/28/2015.
 */
public class ItemEffect {

    private String type;
    private int amount;

    public ItemEffect(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public String activate() {
        String string = "";
        if (type.contains("Heal,")) {
            if (MainCharacter.health != MainCharacter.maxHealth) {
                MainCharacter.health += amount;
                if (MainCharacter.health > MainCharacter.maxHealth) {
                    MainCharacter.health = MainCharacter.maxHealth;
                }
            } else {
                string = string + "Heal,";
            }
        } else if (type.contains("Restore Mana,")) {
            if (MainCharacter.mana != MainCharacter.maxMana) {
                MainCharacter.mana += amount;
                if (MainCharacter.mana > MainCharacter.maxMana) {
                    MainCharacter.mana = MainCharacter.maxMana;
                }
            } else {
                string = string + "Restore Mana,";
            }
        }
        return string;
    }

    public String getErrorMessage(String status) {
        if (status.contains("Heal,")) {
            return "You are already at max health!";
        } else if (status.contains("Restore Mana,")) {
            return "You are already at max mana!";
        }
        return null;
    }
}

/**
 * Created by Cody on 9/28/2015.
 */
public class ItemEffect {

    private String[] types;
    private int[] values;

    public ItemEffect(String[] types, int[] values) {
        this.types = types;
        this.values = values;
    }

    public String activate() {
        String errorString = "";
        String status = "";
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals("Heal")) {
                if (StartGameState.character.health != StartGameState.character.maxHealth) {
                    StartGameState.character.health += values[i];
                    if (StartGameState.character.health > StartGameState.character.maxHealth) {
                        StartGameState.character.health = StartGameState.character.maxHealth;
                    }
                    status += "You have been healed for " + String.valueOf(values[i]) + " health! ";
                } else {
                    errorString = errorString + "Heal,";
                }
            } else if (types[i].equals("Restore Mana")) {
                if (StartGameState.character.mana != StartGameState.character.maxMana) {
                    StartGameState.character.mana += values[i];
                    if (StartGameState.character.mana > StartGameState.character.maxMana) {
                        StartGameState.character.mana = StartGameState.character.maxMana;
                    }
                    status += "You have been restored " + String.valueOf(values[i]) + " mana! ";
                } else {
                    errorString = errorString + "Restore Mana,";
                }
            }
        }
        if (status.equals("")) {
            errorString += "<>";
            return errorString;
        } else {
            return status;
        }
    }

    public String getErrorMessage(String status) {
        if (status.contains("Heal,")) {
            return "You are already at max health!<>";
        } else if (status.contains("Restore Mana,")) {
            return "You are already at max mana!<>";
        }
        return null;
    }

}

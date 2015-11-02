import java.util.Random;

/**
 * Created by Cody on 10/30/2015.
 */
public class BattleMoveEffect {

    String[] types;
    int[] values;
    int[] chances;
    int effectCount;

    public BattleMoveEffect(String[] types, int[] values, int[] chances) {
        this.types = types;
        this.values = values;
        this.chances = chances;
        effectCount = types.length;
    }

    public String applyEffect(BattleEnemy attacker, BattleEnemy defender, BattleMove move) {
        String effectMessage = "";
        for (int i = 0; i < effectCount; i++) {
            if (i > 0) {
                effectMessage += " ";
            }
            String[] effectTypes = types[i].split("_");
            Random rand = new Random();
            int r = rand.nextInt(100);
            if (effectTypes[0].equals("Damage") && r < chances[i]) {
                int damage = 0;
                if (effectTypes[1].equals("Physical")) {
                    double difference = attacker.stats.get("Attack") -  defender.stats.get("Defense");
                    double multiplier = 1 + 0.09*difference/(1 + .09*Math.abs(difference));
                    damage = (int) (values[i]*multiplier);
                    defender.health -= damage;
                } else {
                    Integer attackerSkill = attacker.skills.get(effectTypes[1]);
                    if (attackerSkill == null) {
                        attackerSkill = 0;
                    }
                    Integer defenderSkill = defender.skills.get(effectTypes[1]);
                    if (defenderSkill == null) {
                        defenderSkill = 0;
                    }
                    double skillDifference = attackerSkill - defenderSkill;
                    double statDifference = attacker.stats.get("Wisdom") - defender.stats.get("Willpower");
                    double multiplierStat = 1 + 0.04*statDifference/(1 + .04*Math.abs(statDifference));
                    double multiplierSkill = 1 + 0.08*skillDifference/(1 + .08*Math.abs(skillDifference));
                    damage = (int) (values[i]*multiplierStat*multiplierSkill);
                    defender.health -= damage;
                    if (defender.health < 0) {
                        defender.health = 0;
                    }
                }
                effectMessage += defender.getName() + " has taken " + String.valueOf(damage) + " damage!";
            } else if (effectTypes[0].equals("Paralyze")) {
                Integer skill = defender.skills.get(effectTypes[1]);
                double multiplier;
                if (skill != null) {
                    multiplier = 1 - 0.05*skill/(1 + .05*Math.abs(skill));
                } else {
                    multiplier = 1;
                }
                r = (int)(r*multiplier);
                if (r < chances[i]) {
                    defender.paralyzeCount = values[i];
                    effectMessage += defender.getName() + " has been paralyzed for " + String.valueOf(values[i]);
                    if (values[i] > 1) {
                        effectMessage += " turns!";
                    } else {
                        effectMessage += " turn!";
                    }
                } else {
                    effectMessage += move.getName() + " has missed " + defender.getName() + "!";
                }
            } else {
                return null;
            }
        }
        return effectMessage;
    }


    public String[] getEffectStrings() {
        String[] strings = new String[effectCount];
        for (int i = 0; i < effectCount; i++) {
            String[] split = types[i].split("_");
            strings[i] = split[0] + " (" + split[1] + "): " + String.valueOf(values[i]);
            if (split[0].equals("Paralyze")) {
                if (values[i] > 1) {
                    strings[i] += " turns";
                } else {
                    strings[i] += " turn";
                }
            }
        }
        return strings;
    }
}

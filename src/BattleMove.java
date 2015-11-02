/**
 * Created by Cody on 10/30/2015.
 */
public class BattleMove {

    protected String name;
    protected BattleMoveEffect effect;

    public BattleMove(String name, BattleMoveEffect effect) {
        this.name = name;
        this.effect = effect;
    }

    public String applyEffect(BattleEnemy attacker, BattleEnemy defender) {
        return effect.applyEffect(attacker, defender, this);
    }

    public String getName() {
        return name;
    }
}

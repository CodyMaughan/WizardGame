/**
 * Created by Cody on 9/17/2015.
 */
public class Equipment extends Vendable {

    public Equipment(String name, int price, String vendableType) {
        super(name, price, vendableType);
        System.out.println("The Equipment " + name + " was added to your inventory.");
    }

    public void equip(MainCharacter character) {

    }

    public void unequip(MainCharacter character) {

    }

}

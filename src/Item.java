/**
 * Created by Cody on 9/17/2015.
 */
public class Item extends Vendable {

    private ItemEffect effect;

    public Item(String name, int price, String vendableType, ItemEffect effect){
        super(name, price, vendableType);
        this.effect = effect;
    }

    public boolean use(){
        String status = effect.activate();
        if (!status.equals("")) {
            System.out.println(effect.getErrorMessage(status));
            return false;
        }
        return true;
    }

}

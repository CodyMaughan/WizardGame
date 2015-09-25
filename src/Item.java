/**
 * Created by Cody on 9/17/2015.
 */
public class Item extends Vendable {

    public Item(String name, int price){
        super(name, price);
        System.out.println("The Item " + name + " was added to your inventory.");
    }

    public void use(){

    }

}

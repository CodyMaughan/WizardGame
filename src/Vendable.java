/**
 * Created by Cody on 9/24/2015.
 */
public abstract class Vendable {

    protected String name;
    protected int price;
    protected String type;

    public Vendable(String name, int price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getVendableType() {
        return type;
    }

}

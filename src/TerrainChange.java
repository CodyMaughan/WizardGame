import java.awt.*;

/**
 * Created by Cody on 9/17/2015.
 */
public class TerrainChange {

    private int x;
    private int y;
    private int width;
    private int height;
    private Rectangle rect;
    private String method1;
    private String method2;
    private String side1;
    private String side2;
    private boolean entered;

    public TerrainChange(Rectangle rect, String method1, String method2, String side1, String side2){
        this.rect = rect;
        this.x = rect.x;
        this.y = rect.y;
        this.width = rect.width;
        this.height = rect.height;
        this.method1 = method1;
        this.method2 = method2;
        this.side1 = side1;
        this.side2 = side2;
        this.entered = false;
    }

    public Rectangle getRect() {
        return rect;
    }

    public String getMethod1() {
        return method1;
    }

    public String getMethod2() {
        return method2;
    }

    public String getSide1() {
        return side1;
    }

    public String getSide2() {
        return side2;
    }

    public void setEntered(boolean bool) {
        entered = bool;
    }

    public boolean isEntered() {
        return entered;
    }

}

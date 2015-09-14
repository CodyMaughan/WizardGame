import java.awt.image.BufferedImage;

/**
 * Created by Cody on 9/13/2015.
 */
public class MainCharacter {

    public static int x;
    public static int y;
    //public static CharacterAnimation character;
    public static BufferedImage image;

    public MainCharacter(BufferedImage image, int posX, int posY) {
        this.image = image;
        this.x = posX;
        this.y = posY;
    }


}

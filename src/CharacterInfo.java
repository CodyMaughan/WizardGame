/**
 * Created by Cody on 9/19/2015.
 */
public class CharacterInfo {

    private String name;
    private String path;
    private String dialog;
    private int width;
    private int height;

    public CharacterInfo(String name, String path, String dialog, int width, int height) {
        this.name = name;
        this.path = path;
        this.dialog = dialog;
        this.width = width;
        this.height = height;
    }

    public String getPath() {
        return path;
    }

    public String getDialog() {
        return dialog;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}

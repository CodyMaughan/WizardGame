/**
 * Created by Cody on 9/19/2015.
 */
public class CharacterInfo {

    private String name;
    private String path;
    private String script;
    private int width;
    private int height;

    public CharacterInfo(String name, String path, String script, int width, int height) {
        this.name = name;
        this.path = path;
        this.script = script;
        this.width = width;
        this.height = height;
    }

    public String getPath() {
        return path;
    }

    public String getScript() {
        return script;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}

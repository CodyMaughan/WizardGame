import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Cody on 9/17/2015.
 */
public class RemovableObject {

    private int x;
    private int y;
    private int width;
    private int height;
    private Rectangle rect;
    private String removableType;
    private String removableName;
    private String actionName;
    private String actionType;
    private Rectangle actionRect;
    private int gid;
    private boolean[] rotation;
    private boolean isRemoved;

    public RemovableObject(String removableType, String removableName, String actionName, String actionType, int gid,
                           boolean[] rotation, Rectangle rect) {
        this.rect = rect;
        this.x = rect.x;
        this.y = rect.y;
        this.width = rect.width;
        this.height = rect.height;
        this.removableType = removableType;
        this.removableName = removableName;
        this.actionName = actionName;
        this.actionType = actionType;
        this.gid = gid;
        this.rotation = rotation;
        actionRect = new Rectangle(rect);
        if (actionName.equals("Interact")) {
            if (actionType.equals("Facing")){
                actionRect.width += 16;
                actionRect.height += 16;
                actionRect.translate(-8, -8);
            }
        }
        isRemoved = false;
    }

    public void draw(Graphics2D g2d, TileMap tilemap) {
        AffineTransform afx = tilemap.getTransform(rotation, x - tilemap.xOffset, y - tilemap.yOffset);
        tilemap.getTileSet(gid).drawTile(g2d, gid, afx);
    }

    public void update(float elapsedTime, boolean[][] keyboardstate) {

    }

    public void remove(MainCharacter character){
        isRemoved = true;
        if (removableType.equals("Item")) {
            character.addItem(removableName, ItemCache.getItem(removableName));
        } else if (removableType.equals("Equipment")) {
            character.addEquipment(removableName, ItemCache.getEquipment(removableName));
        }
    }

    public String getActionName(){
        return actionName;
    }

    public String getActionType(){
        return actionType;
    }

    public String getRemovableType() { return removableType; }

    public String getRemovableName() { return removableName; }

    public Rectangle getRect(){
        return rect;
    }

    public Rectangle getActionRect(){
        return actionRect;
    }

    public boolean isRemoved(){
        return isRemoved;
    }

    public void setRemoved(boolean bool){
        isRemoved = bool;
    }

}

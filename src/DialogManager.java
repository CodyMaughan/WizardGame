import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/18/2015.
 */
public class DialogManager {

    private static DialogManager manager;
    private static Map<String, InteractionDialogBox> interactionBoxes;
    private static Framework framework;

    private DialogManager(Framework framework) {
        interactionBoxes = new HashMap<>();
        this.framework = framework;
    }

    public static DialogManager getInstance(Framework framework) {
        if (manager == null) {
            manager = new DialogManager(framework);
        }
        return manager;
    }

    public static void addInteractionDialogBox(String name) {
        addInteractionDialogBox(name, new InteractionDialogBox(name, new Font("Arial", Font.PLAIN, 10),
                5, 5, (Graphics2D)framework.getGraphics()));
    }

    public static void addInteractionDialogBox(String name, InteractionDialogBox dialogBox) {
        interactionBoxes.put(name, dialogBox);
    }

    public static InteractionDialogBox getInteractionDialogBox(String name) {
        return interactionBoxes.get(name);
    }

    public static void includeInteractionDialogBox(String name) {
        if (interactionBoxes.get(name) == null) {
            addInteractionDialogBox(name);
        }
    }
}

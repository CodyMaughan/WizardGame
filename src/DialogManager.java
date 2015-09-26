import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/18/2015.
 */
public class DialogManager {

    private static DialogManager manager;
    private static Framework framework;
    private static Map<String, DialogSequenceBox> dialogSequences;
    private static DialogSequenceBox currentDialog;
    private static int branchCount;
    private static int branchSize;

    private DialogManager(Framework framework) {
        dialogSequences = new HashMap<>();
        this.framework = framework;
        branchCount = 0;
        branchSize = 1;
    }

    public static DialogManager getInstance(Framework framework) {
        if (manager == null) {
            manager = new DialogManager(framework);
        }
        return manager;
    }

    public static void update(float elapsedTime, boolean[][] keyboardstate) {

    }

    public static void draw(Graphics2D g2d) {

    }

    public static void draw(Graphics g2d, Character character) {

    }

    public static void addDialogBox(String name) {
        addDialogBox(name, new DialogSequenceBox(name, new Font("Arial", Font.PLAIN, 10),
                5, 5, (Graphics2D) framework.getGraphics()));
    }

    public static void addDialogBox(String name, DialogSequenceBox dialogBox) {
        dialogSequences.put(name, dialogBox);
    }

    public static DialogSequenceBox getDialogBox(String name) {
        return dialogSequences.get(name);
    }

    public static void includeDialogBox(String name) {
        if (dialogSequences.get(name) == null) {
            addDialogBox(name);
        }
    }

    public static void startDialog(String name) {
        currentDialog = dialogSequences.get(name);
        currentDialog.startDialog();
    }

    public static void progressDialog() {
        currentDialog.progressDialog();
    }

    public static void endDialog() {
        branchCount = 0;
        branchSize = 1;
        currentDialog = null;
    }

    public static void setBranch(int branch, int numBranches) {
        branchCount = branch;
        branchSize = numBranches;
    }

    public static int getBranchSize() {
        return branchSize;
    }

    public static int getBranchCount() {
        return branchCount;
    }
}

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cody on 9/30/2015.
 */
public class TreeUtility {

    private static List<Node> breadthQueue = new ArrayList<>();

    public static void addToQueue(Node node) {
        breadthQueue.add(node);
    }

    public static Node getNextBreadthNode() {
        return breadthQueue.get(0);
    }

    public static void removeFirstBreadthNode() {
        breadthQueue.remove(0);
    }

}

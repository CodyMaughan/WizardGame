import java.util.HashMap;

/**
 * Created by Cody on 10/1/2015.
 */
public class QuestManager {

    private static Framework framework;
    private static QuestManager manager;
    private static HashMap<String, Quest> activeQuests;
    private static HashMap<String, Quest> completeQuests;

    private QuestManager(Framework framework) {
        this.framework = framework;
        activeQuests = new HashMap<>();
        completeQuests = new HashMap<>();
    }

    public static QuestManager getInstance(Framework framework) {
        if (manager == null) {
            manager = new QuestManager(framework);
        }
        return manager;
    }

    public static boolean questIsActive(String name) {
        if (activeQuests.get(name) == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void addActiveQuest(Quest quest) {
        activeQuests.put(quest.getName(), quest);
    }

    public static void removeActiveQuest(String name) {
        activeQuests.remove(name);
    }

    public static boolean questIsComplete(String name) {
        if (completeQuests.get(name) == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void addCompletedQuest(Quest quest) {
        completeQuests.put(quest.getName(), quest);
    }
}

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by Cody on 9/26/2015.
 */
public class IndexedTreeMap<V, K> extends TreeMap<V, K> {
    private HashMap<Integer,V> index;

    public IndexedTreeMap() {
        super();
        index = new HashMap<>();
    }


    @Override
    public K put(V key, K value) {
        K val = super.put(key, value);
        int i = 0;
        index = new HashMap<>();
        for (V vKey : keySet()) {
            index.put(i, vKey);
            i++;
        }
        return val;
    }

    public K getIndexed(int ind) {
        return super.get(index.get(ind));
    }

    @Override
    public K remove(Object key) {
        K val = super.remove(key);
        int i = 0;
        index = new HashMap<>();
        for (V vKey : keySet()) {
            index.put(i, vKey);
            i++;
        }
        return val;
    }
}

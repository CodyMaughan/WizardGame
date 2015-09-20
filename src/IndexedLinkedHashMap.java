import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Cody on 9/20/2015.
 */
public class IndexedLinkedHashMap<V, K> extends LinkedHashMap<V, K> {

    private HashMap<Integer,V> index;
    private int curr;

    public IndexedLinkedHashMap() {
        super();
        index = new HashMap<>();
        curr = 0;
    }


    @Override
    public K put(V key, K value) {
        index.put(curr, key);
        curr++;
        return super.put(key, value);
    }

    public K putWithIndex(int ind, V key, K value) {
        index.put(ind, key);
        return super.put(key, value);
    }

    public K getIndexed(int ind) {
        return super.get(index.get(ind));
    }
}

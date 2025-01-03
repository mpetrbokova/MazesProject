package disjointsets;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    HashMap<T, Integer> nodes;
    int size;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        nodes = new HashMap<>();
        size = 0;

    }

    @Override
    public void makeSet(T item) {
        if (nodes.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        pointers.add(-1);
        nodes.put(item, size);
        size++;


    }

    @Override
    public int findSet(T item) {
        if (!nodes.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int index = nodes.get(item);
        int rootID = findRoot(index);
        int value = pointers.get(index);
        while (value >= 0) {
            pointers.set(index, rootID);
            index = value;
            value = pointers.get(index);
        }
        return rootID;


    }

    public int findRoot(int index) {
        int value = pointers.get(index);
        if (value < 0) {
            return index;
        } else {
            return findRoot(value);
        }
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!nodes.containsKey(item1) || !nodes.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int root1 = findSet(item1);
        int root2 = findSet(item2);
        if (root1 != root2) {
            int weight1 = pointers.get(root1) * -1;
            int weight2 = pointers.get(root2) * -1;
            int newRoot = -1 * (weight1 + weight2);
            if (weight1 < weight2) {
                pointers.set(root2, newRoot);
                pointers.set(root1, root2);
            } else {
                pointers.set(root1, newRoot);
                pointers.set(root2, root1);
            }
            return true;
        }
        return false;
    }


}

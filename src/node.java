import java.util.ArrayList;

public class node implements Comparable<node> {
    int key;
    ArrayList<String> values;

    public node(int key) {
        this.key = key;
        values = new ArrayList<String>();
    }

    public int getKey() {
        return key;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void add(String value) {
        values.add(value);
    }

    public int compareTo(node o) {
        node n = o;
        if (key > n.key) {
            return 1;
        } else if (key < n.key) {
            return -1;
        } else {
            return 0;
        }
    }
}

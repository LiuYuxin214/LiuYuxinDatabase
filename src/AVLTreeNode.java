import java.util.ArrayList;

public class AVLTreeNode implements Comparable<AVLTreeNode> {
    protected int key;
    protected ArrayList<String> values = new ArrayList<>();
    protected AVLTreeNode left;
    protected AVLTreeNode right;
    protected int height = 0;

    public AVLTreeNode(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public void add(String value) {
        values.add(value);
    }

    public int compareTo(AVLTreeNode o) {
        if (key > o.key) {
            return 1;
        } else if (key < o.key) {
            return -1;
        } else {
            return 0;
        }
    }
}
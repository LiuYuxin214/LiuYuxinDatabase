import java.util.ArrayList;

public class TreeNode implements Comparable<TreeNode> {
    protected int key;
    protected ArrayList<String> values = new ArrayList<>();
    protected TreeNode left;
    protected TreeNode right;

    public TreeNode(int key) {
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

    public int compareTo(TreeNode o) {
        if (key > o.key) {
            return 1;
        } else if (key < o.key) {
            return -1;
        } else {
            return 0;
        }
    }
}
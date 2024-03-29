import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Table {
    AVLTree table;
    int columns;
    int rows;
    String name;

    Set<String> headers;

    public Table(String name) {
        this.name = name;
        table = new AVLTree();
    }

    public Table(String name, String[] headers) {
        this.name = name;
        this.columns = headers.length;
        this.headers = new LinkedHashSet<>(List.of(headers));
        table = new AVLTree();
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public String getHeaders() {
        return headers.toString();
    }

    public void insert(int key, String[] values) {
        AVLTreeNode n = new AVLTreeNode(key);
        for (int i = 0; i < columns; i++) {
            n.add(values[i]);
        }
        table.insert(n);
        rows++;
    }

    public void delete(int key) {
        AVLTreeNode n = new AVLTreeNode(key);
        table.delete(n);
        rows--;
    }

    public void deleteAll() {
        table.clear();
        rows = 0;
    }

    public void update(int key, String[] values) {
        AVLTreeNode n = new AVLTreeNode(key);
        for (int i = 0; i < columns; i++) {
            n.add(values[i]);
        }
        table.delete(n);
        table.insert(n);
    }

    public AVLTreeNode select(int key) {
        AVLTreeNode n = new AVLTreeNode(key);
        return table.search(n);
    }

    public ArrayList<AVLTreeNode> selectAll() {
        return table.inorder();
    }

    public void saveToFile() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("Tables/" + name + ".txt");
        writer.println(columns);
        writer.println(headers);
        ArrayList<AVLTreeNode> nodes = selectAll();
        for (AVLTreeNode node : nodes) {
            writer.println(node.getKey() + " " + node.getValues());
        }
        writer.println("EOF");
        writer.close();
    }

    public void getFromFile() throws FileNotFoundException {
        try {
            File file = new File("Tables/" + name + ".txt");
            Scanner reader = new Scanner(file);
            columns = reader.nextInt();
            reader.nextLine();
            String headerString = reader.nextLine();
            headers = new LinkedHashSet<>(List.of(headerString.substring(1, headerString.length() - 1).split(", ")));
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                if (data.equals("EOF")) {
                    break;
                }
                String[] entry = data.split(" ");
                int key = Integer.parseInt(entry[0]);
                String[] values = new String[columns];
                for (int i = 1; i < entry.length; i++) {
                    if (i == 1) {
                        values[i - 1] = entry[i].substring(1, entry[i].length() - 1);
                    } else if (i == entry.length - 1) {
                        values[i - 1] = entry[i].substring(0, entry[i].length() - 1);
                    } else {
                        values[i - 1] = entry[i].substring(0, entry[i].length() - 1);
                    }
                }
                insert(key, values);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not accessible");
        }
    }
}

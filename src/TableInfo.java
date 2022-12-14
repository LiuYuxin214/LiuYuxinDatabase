import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TableInfo {
    Map<String, Integer> tableInfo;

    public TableInfo() {
        tableInfo = new HashMap<>();
    }

    public void addTable(String name, int size) {
        tableInfo.put(name, size);
    }

    public void removeTable(String name) {
        tableInfo.remove(name);
    }

    public int getSize(String name) {
        return tableInfo.get(name);
    }

    public boolean contains(String name) {
        return tableInfo.containsKey(name);
    }

    public void saveToFile() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("tableInfo.txt");
        for (String name : tableInfo.keySet()) {
            writer.println(name + " " + tableInfo.get(name));
        }
        writer.close();
    }

    public void getFromFile() throws FileNotFoundException {
        try {
            File file = new File("tableInfo.txt");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] parts = data.split(" ");
                tableInfo.put(parts[0], Integer.parseInt(parts[1]));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not accessible");
        }
    }
}

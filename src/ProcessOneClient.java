import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ProcessOneClient implements Runnable {
    ServerLog serverLog;
    String serverName, serverStatus;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private int clientID;
    private String userName;

    public ProcessOneClient(Socket socket, ServerLog Log, String databaseName, String databaseStatus) throws IOException {
        try {
            this.socket = socket;
            this.serverLog = Log;
            this.serverName = databaseName;
            this.serverStatus = databaseStatus;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            serverLog.add("The connection to the client failed");
            serverLog.add("Error: " + e.getMessage());
        }
    }

    public void run() {
        try {
            clientID = in.readInt();
            out.writeUTF(serverName);
            out.writeUTF("1.0");
            out.writeUTF(serverStatus);
            userName = in.readUTF();
            if (userName.equals("disconnect")) {
                serverLog.addClient(clientID, "Client actively disconnects");
                in.close();
                out.close();
                socket.close();
                Server.numOfUsers--;
                return;
            }
            serverLog.addClientUser(clientID, userName, "User Name: " + userName);
            String password = in.readUTF();
            serverLog.addClientUser(clientID, userName, "Password: " + password);
            serverLog.addClientUser(clientID, userName, "Checking User ID and Password...");
            if (new File("Users/" + userName + ".txt").exists()) {
                User user = new User(userName);
                user.getFromFile();
                serverLog.addClientUser(clientID, userName, "User " + userName + " exists");
                if (user.getPassword().equals(password)) {
                    serverLog.addClientUser(clientID, userName, "User " + userName + " Password Correct");
                    out.writeUTF("Password Correct");
                    out.writeUTF(new Date().toString());
                    TableInfo tableInfo = new TableInfo();
                    tableInfo.getFromFile();
                    while (true) {
                        String option = in.readUTF();
                        switch (option) {
                            case "createtable" -> {
                                String name = in.readUTF();
                                String[] headers = in.readUTF().split(",");
                                tableInfo.addTable(name, headers.length);
                                tableInfo.saveToFile();
                                Table table = new Table(name, headers);
                                table.saveToFile();
                                serverLog.addClientUser(clientID, userName, "User " + userName + " created a table named " + name);
                                out.writeUTF("Table Created");
                            }
                            case "deletetable" -> {
                                String name = in.readUTF();
                                tableInfo.removeTable(name);
                                tableInfo.saveToFile();
                                new File("Tables/" + name + ".txt").delete();
                                serverLog.addClientUser(clientID, userName, "User " + userName + " deleted a table named " + name);
                                out.writeUTF("Table Deleted");
                            }
                            case "insert" -> {
                                String name = in.readUTF();
                                int key = in.readInt();
                                String[] values = in.readUTF().split(",");
                                Table table = new Table(name);
                                table.getFromFile();
                                table.insert(key, values);
                                table.saveToFile();
                                serverLog.addClientUser(clientID, userName, "User " + userName + " inserted a row into table named " + name);
                                out.writeUTF("Row Inserted");
                            }
                            case "delete" -> {
                                String name = in.readUTF();
                                String arguments = in.readUTF();
                                Table table = new Table(name);
                                table.getFromFile();
                                if (arguments.equals("*")) {
                                    table.deleteAll();
                                    table.saveToFile();
                                    serverLog.addClientUser(clientID, userName, "User " + userName + " deleted all rows from table named " + name);
                                    out.writeUTF("All Rows Deleted");
                                } else {
                                    int key = Integer.parseInt(arguments);
                                    table.delete(key);
                                    table.saveToFile();
                                    serverLog.addClientUser(clientID, userName, "User " + userName + " deleted a row from table named " + name);
                                    out.writeUTF("Row Deleted");
                                }

                            }
                            case "update" -> {
                                String name = in.readUTF();
                                int key = in.readInt();
                                String[] values = in.readUTF().split(",");
                                Table table = new Table(name);
                                table.getFromFile();
                                table.update(key, values);
                                table.saveToFile();
                                serverLog.addClientUser(clientID, userName, "User " + userName + " updated a row in table named " + name);
                                out.writeUTF("Row Updated");
                            }
                            case "select" -> {
                                String name = in.readUTF();
                                String arguments = in.readUTF();
                                tableInfo.getFromFile();
                                if (!tableInfo.contains(name)) {
                                    out.writeUTF("No such table");
                                    break;
                                }
                                Table table = new Table(name);
                                table.getFromFile();
                                out.writeUTF(table.getHeaders());
                                if (arguments.equals("*")) {
                                    out.writeInt(table.getRows());
                                    ArrayList<TreeNode> rows = table.selectAll();
                                    for (TreeNode row : rows) {
                                        out.writeUTF(row.getKey() + " " + row.getValues().toString());
                                    }
                                    serverLog.addClientUser(clientID, userName, "User " + userName + " selected all rows from table named " + name);
                                } else {
                                    int key = Integer.parseInt(arguments);
                                    out.writeUTF(table.select(key).getKey() + " " + table.select(key).getValues().toString());
                                    serverLog.addClientUser(clientID, userName, "User " + userName + " selected a row from table named " + name);
                                }
                                serverLog.addClientUser(clientID, userName, "User " + userName + " selected a row from table named " + name);
                            }
                            case "date" -> out.writeUTF(new Date().toString());
                            case "?" -> {
                                File helpFile = new File("help.txt");
                                Scanner helpScanner = new Scanner(helpFile);
                                while (helpScanner.hasNextLine()) {
                                    out.writeUTF(helpScanner.nextLine());
                                }
                                out.writeUTF("end");
                            }
                            case "version" -> out.writeUTF("Liu Yuxin Datebase 1.0");
                            case "quit" -> {
                                user.saveToFile();
                                serverLog.addClientUser(clientID, userName, "Client logs out");
                            }
                        }
                        if (option.equals("quit")) break;
                    }
                } else {
                    serverLog.addClientUser(clientID, userName, "Wrong Password");
                    out.writeUTF("Wrong Password");
                }
            } else {
                serverLog.addClientUser(clientID, userName, "User not found");
                out.writeUTF("User not found");
            }
            in.close();
            out.close();
            socket.close();
            Server.numOfUsers--;
            serverLog.addClientUser(clientID, userName, "The client is disconnected normally");
        } catch (IOException e) {
            try {
                in.close();
                out.close();
                socket.close();
                Server.numOfUsers--;
                serverLog.addClientUser(clientID, userName, "The client was disconnected unexpectedly");
                serverLog.addClientUser(clientID, userName, "Error: " + e.getMessage());
            } catch (IOException e1) {
                Server.numOfUsers--;
                serverLog.addClientUser(clientID, userName, "Failed to close socket");
                serverLog.addClientUser(clientID, userName, "Error: " + e1.getMessage());
            }
        }
    }
}

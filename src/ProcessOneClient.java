import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
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

    public ProcessOneClient(Socket socket, ServerLog Log, String serverName, String serverStatus) throws IOException {
        try {
            this.socket = socket;
            this.serverLog = Log;
            this.serverName = serverName;
            this.serverStatus = serverStatus;
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
                    while (true) {
                        String option = in.readUTF();
                        switch (option) {
                            case "test" -> {
                                int num = in.readInt();
                                out.writeInt(num + 1);
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

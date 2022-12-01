import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Server {
    public static int numOfUsers = 0;

    public static void main(String[] args) throws IOException {
        //Prepare server
        ServerLog serverLog = new ServerLog();
        serverLog.add("Liu Yuxin Database Server Version 1.0");
        serverLog.add("Server booting up...");
        File configfile = new File("config.txt");
        if (!configfile.exists()) {
            configfile.createNewFile();
            PrintWriter writer = new PrintWriter(configfile);
            writer.println("20000");
            writer.println("MyDatabase");
            writer.println("\033[32mOK\033[0m");
            writer.close();
        }
        Scanner configSc = new Scanner(configfile);
        int port = configSc.nextInt();
        String serverName = configSc.next();
        String serverStatus = configSc.next();
        ServerSocket server = new ServerSocket(port);
        serverLog.add("Server started at " + new Date());
        serverLog.add("Server Name: " + serverName);
        serverLog.add("IP: " + InetAddress.getLocalHost().getHostAddress());
        serverLog.add("Port: " + port);
        serverLog.add("Waiting for client...");
        //Listen for client
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverLog.add("A new Client Connected");
                    numOfUsers++;
                    serverLog.add("The number of clients is " + numOfUsers);
                    new Thread(new ProcessOneClient(socket, serverLog, serverName, serverStatus)).start();
                } catch (IOException e) {
                    socket.close();
                    serverLog.add("Server error occurred, a user's process stopped running");
                    serverLog.add("Error: " + e.getMessage());
                    numOfUsers--;
                }
            }
        } catch (IOException e) {
            serverLog.add("Fatal error occurred, server stopped running");
            serverLog.add("Error: " + e.getMessage());
            server.close();
            System.exit(1);
        }
    }
}

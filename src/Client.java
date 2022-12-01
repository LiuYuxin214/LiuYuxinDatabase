import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Client {

    public static DataInputStream in;
    public static DataOutputStream out;

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        String ip = "localhost";
        int port = 20000;
        int clientID = 0;
        PrintWriter settingPrintWriter;
        try {
            File file = new File("ClientSetting.txt");
            Scanner scanner = new Scanner(file);
            ip = scanner.next();
            port = scanner.nextInt();
            clientID = scanner.nextInt();
        } catch (FileNotFoundException e) {
            System.out.println("\033[31mClient Setting lost!\033[0m");
            System.out.println("Restored to default settings");
            try (PrintWriter resetPrintWriter = new PrintWriter("ClientSetting.txt")) {
                resetPrintWriter.println("localhost");
                resetPrintWriter.println("20000");
                resetPrintWriter.print("0");
            } catch (FileNotFoundException e1) {
                System.out.println("\033[31mCannot create ClientSetting.txt\033[0m");
                System.out.println("Please check the file permission or wait a few seconds and try again.");
                System.out.println("This program will exit now.");
                sleep(5000);
                System.exit(0);
            }
        }
        while (true) {
            System.out.println("\033[31m+==================\033[1mNot connected\033[0m\033[31m==================+\033[0m");
            System.out.println(" Client Version: 1.0");
            System.out.println(" IP: " + ip + "\n Port: " + port + "\n Client ID: " + clientID);
            System.out.println("\033[31m+=================================================+\033[0m");
            System.out.println("\033[5mPress enter to connect\033[0m");
            System.out.println("Type \033[31me\033[0m then press enter to exit.");
            System.out.println("Type \033[36ms\033[0m then press enter to setting.");
            Scanner waiter = new Scanner(System.in);
            String function = waiter.nextLine();
            if (function.equals("e")) {
                System.out.println("\033[31mExiting...\033[0m");
                sleep(500);
                System.exit(0);
            }
            if (function.equals("s")) {
                while (true) {
                    System.out.println("*=====================\033[1mSetting\033[0m=====================*");
                    System.out.println("|--> 1. Change the server's IP address            |");
                    System.out.println("|--> 2. Change the server's port                  |");
                    System.out.println("|--> 3. Change the client's ID                    |");
                    System.out.println("|--> 4. Show the current setting                  |");
                    System.out.println("|--> 5. Back                                      |");
                    System.out.println("*=================================================*");
                    System.out.print("\033[1mEnter a choice: \033[0m");
                    Scanner setting = new Scanner(System.in);
                    int option = setting.nextInt();
                    switch (option) {
                        case 1:
                            System.out.print("Please enter the server's IP address: ");
                            ip = setting.next();
                            settingPrintWriter = new PrintWriter("ClientSetting.txt");
                            settingPrintWriter.println(ip);
                            settingPrintWriter.println(port);
                            settingPrintWriter.print(clientID);
                            settingPrintWriter.close();
                            System.out.println("\033[32mIP address changed!\033[0m");
                            sleep(1000);
                            break;
                        case 2:
                            System.out.print("Please enter the server's port: ");
                            port = setting.nextInt();
                            settingPrintWriter = new PrintWriter("ClientSetting.txt");
                            settingPrintWriter.println(ip);
                            settingPrintWriter.println(port);
                            settingPrintWriter.print(clientID);
                            settingPrintWriter.close();
                            System.out.println("\033[32mPort changed!\033[0m");
                            sleep(1000);
                            break;
                        case 3:
                            System.out.print("Please enter the client's ID: ");
                            clientID = setting.nextInt();
                            settingPrintWriter = new PrintWriter("ClientSetting.txt");
                            settingPrintWriter.println(ip);
                            settingPrintWriter.println(port);
                            settingPrintWriter.print(clientID);
                            settingPrintWriter.close();
                            System.out.println("\033[32mClient ID changed!\033[0m");
                            sleep(1000);
                            break;
                        case 4:
                            System.out.println("++++++++++++++++++\033[1mCurrent Setting\033[0m++++++++++++++++++");
                            System.out.println("-Server's IP address: " + ip);
                            System.out.println("-Server's port: " + port);
                            System.out.println("-Client's ID: " + clientID);
                            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
                            waiter();
                            break;
                        case 5:
                            break;
                    }
                    if (option == 5) {
                        break;
                    }
                }
                continue;
            }
            System.out.print("\033[32mConnecting to server\033[0m");
            for (int i = 0; i < 3; i++) {
                sleep(500);
                System.out.print("\033[32m.\033[0m");
            }
            System.out.println();
            try (Socket socket = new Socket(ip, port)) {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                out.writeInt(clientID);
                System.out.println("\033[32mConnect to server successfully!\033[0m");
                Scanner loginSc = new Scanner(System.in);
                System.out.println("\033[33m+==================\033[1mNot logged in\033[0m\033[33m==================+\033[0m");
                System.out.println(" Server Name: " + in.readUTF());
                System.out.println(" Server Version: " + in.readUTF());
                System.out.println(" IP: " + ip + "\n Port: " + port + "\n Client ID: " + clientID);
                System.out.println(" Server State: " + in.readUTF());
                System.out.println("\033[33m+=================================================+\033[0m");
                System.out.println("Type \033[31md\033[0m to disconnect from server.");
                System.out.print("\033[1mLogin as: \033[0m");
                String enter = loginSc.next();
                String password;
                if (enter.equals("d")) {
                    System.out.print("\033[31mDisconnecting\033[0m");
                    for (int i = 0; i < 3; i++) {
                        sleep(500);
                        System.out.print("\033[31m.\033[0m");
                    }
                    System.out.println();
                    out.writeUTF("disconnect");
                    in.close();
                    out.close();
                    socket.close();
                    System.out.println("\033[31mDisconnected from server\033[0m");
                    sleep(1000);
                    continue;
                }
                String userName = enter;
                System.out.print("\033[1mPassword: \033[0m");
                password = loginSc.next();
                out.writeUTF(userName);
                out.writeUTF(password);
                String result = in.readUTF();
                switch (result) {
                    case "Password Correct" -> {
                        System.out.println("\033[32mPassword Correct\033[0m");
                        String dateTime = in.readUTF();
                        System.out.println("\033[32m+====================\033[1mLogged in\033[0m\033[32m====================+\033[0m");
                        System.out.println(" \033[1mUser Name: \033[33m" + userName + "\033[0m");
                        System.out.println(" \033[1mLogin Date&Time: \033[36m" + dateTime + "\033[0m");
                        System.out.println("\033[32m+=================================================+\033[0m");
                        System.out.println("To log out, type \033[31mquit\033[0m.");
                        System.out.println("To get help, type \033[34m?\033[0m.");
                        while (true) {
                            System.out.print(userName + "> ");
                            Scanner command = new Scanner(System.in);
                            String instruction = command.next();
                            switch (instruction) {
                                case "test" -> {
                                    out.writeUTF("test");
                                    int n = command.nextInt();
                                    out.writeInt(n);
                                    int result1 = in.readInt();
                                    System.out.println("Result: " + result1);
                                }
                                case "date" -> {
                                    out.writeUTF("date");
                                    String date = in.readUTF();
                                    System.out.println(date);
                                }
                                case "?" -> {
                                    out.writeUTF("?");
                                    while (true) {
                                        String help = in.readUTF();
                                        if (help.equals("end")) {
                                            break;
                                        }
                                        System.out.println(help);
                                    }
                                }
                                case "version" -> {
                                    out.writeUTF("version");
                                    String version = in.readUTF();
                                    System.out.println(version);
                                }
                                case "quit" -> {
                                    out.writeUTF("quit");
                                    System.out.println("\033[1mThank you for using Liu Yuxin Database!\033[0m");
                                    sleep(1000);
                                }
                                default -> {
                                    System.out.println("\033[31mInvalid instruction!\033[0m Please Check \"" + instruction + "\" Again!");
                                }
                            }
                            if (instruction.equals("quit")) break;
                        }
                    }
                    case "Wrong Password" -> {
                        System.out.println("\033[31mWrong Password\033[0m");
                        sleep(2000);
                    }
                    case "User not found" -> {
                        System.out.println("\033[31mUser not found\033[0m");
                        sleep(2000);
                    }
                }
                in.close();
                out.close();
            } catch (IOException e) {
                System.out.println("\033[31mCannot connect to server!\033[0m");
                System.out.println("Please check the Internet connection or wait a few seconds and try again.");
                waiter();
            }
        }
    }

    public static void waiter() {
        Scanner waiter = new Scanner(System.in);
        System.out.println("\033[5mPress enter to continue\033[0m");
        waiter.nextLine();
    }
}

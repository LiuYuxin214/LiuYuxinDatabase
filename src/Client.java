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
            System.out.println("\033[31m|\033[0m             \033[1mWelcome to Liu Yuxin Database!\033[0m             \033[31m|\033[0m");
            System.out.println("\033[31m|\033[0m           Liu Yuxin Database Client Version 1.0        \033[31m|\033[0m");
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
                String dateTime = in.readUTF();
                String announcement = in.readUTF();
                System.out.println("\033[32mConnect to server successfully!\033[0m");
                sleep(1000);
                Scanner scanner = new Scanner(System.in);
                System.out.println("\033[33m+==================\033[1mNot logged in\033[0m\033[33m==================+\033[0m");
                System.out.println("\033[33m|\033[0m             \033[1mWelcome to Liu Yuxin Database!\033[0m             \033[33m|\033[0m");
                System.out.printf("\033[33m|\033[0m\033[1mAnnouncement: \033[35m%-35s\033[0m\033[33m|\033[0m\n", announcement);
                System.out.println("\033[33m|\033[0m\033[1mServer Time&Date: \033[36m" + dateTime + "\033[0m" + "   \033[33m|\033[0m");
                System.out.println("\033[33m+=================================================+\033[0m");
                System.out.println("If you want to disconnect from server, type \033[31md\033[0m.");
                System.out.print("\033[1mLogin as: \033[0m");
                String enter = scanner.next();
                String password;
                if (enter.equals("d")) {
                    System.out.print("\033[31mDisconnecting\033[0m");
                    for (int i = 0; i < 3; i++) {
                        sleep(500);
                        System.out.print("\033[31m.\033[0m");
                    }
                    System.out.println();
                    out.writeInt(-1);
                    in.close();
                    out.close();
                    socket.close();
                    System.out.println("\033[31mDisconnected from server\033[0m");
                    sleep(1000);
                    continue;
                }
                String id = enter;
                System.out.print("\033[1mPassword: \033[0m");
                Console console = System.console();
                if (!(console == null)) {
                    char[] passwordArray = console.readPassword();
                    password = new String(passwordArray);
                } else {
                    password = scanner.next();
                }
                System.out.println("\033[36mSending ID and password to server...\033[0m");
                out.writeUTF(id);
                out.writeUTF(password);
                sleep(500);
                String result = in.readUTF();
                switch (result) {
                    case "Password Correct" -> {
                        System.out.println("\033[32mPassword Correct\033[0m");
                        System.out.println("\033[1mLoading...\033[0m");
                        System.out.print("\033[1m(|\033[0m");
                        for (int i = 0; i < 47; i++) {
                            sleep(30);
                            System.out.print("\033[32m*\033[0m");
                        }
                        System.out.println("\033[1m|)\033[0m");
                        dateTime = in.readUTF();
                        System.out.println("\033[32m+====================\033[1mLogged in\033[0m\033[32m====================+\033[0m");
                        System.out.println("\033[32m|\033[0m             \033[1mWelcome to Liu Yuxin Database!\033[0m             \033[32m|\033[0m");
                        System.out.println("\033[32m|\033[0m\033[1mUser ID: \033[33m" + id + "\033[0m" + "                                       \033[32m|\033[0m");
                        System.out.printf("\033[32m|\033[0m\033[1mAnnouncement: \033[35m%-35s\033[0m\033[32m|\033[0m\n", announcement);
                        System.out.println("\033[32m|\033[0m\033[1mLogin Date&Time: \033[36m" + dateTime + "\033[0m" + "    \033[32m|\033[0m");
                        System.out.println("\033[32m+=================================================+\033[0m");
                        while (true) {
                            System.out.print(id + ">");
                            Scanner input = new Scanner(System.in);
                            double amount;
                            String option = input.next();
                            switch (option) {
                                case "test" -> {
                                    out.writeInt(1);
                                    double balance = in.readDouble();
                                    System.out.println("\033[1mThe balance is \033[32m$" + balance + "\033[0m");
                                    waiter();
                                }
                                case "quit" -> {
                                    out.writeInt(8);
                                    System.out.println("\033[1mThank you for using ATM Machine!\033[0m");
                                    sleep(2000);
                                }
                                default -> {
                                    System.out.println("\033[31mInvalid choice\033[0m");
                                    sleep(2000);
                                }
                            }
                            if (option.equals("quit")) break;
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
                    case "User is frozen" -> {
                        System.out.println("\033[31mUser is frozen, please contact the staff\033[0m");
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

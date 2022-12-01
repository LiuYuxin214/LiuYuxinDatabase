import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class User {
    private String userName;
    private String password;

    public User() {
    }


    public User(String userName) {
        this.userName = userName;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void saveToFile() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("Users/" + userName + ".txt");
        writer.println(userName);
        writer.println(password);
        writer.close();
    }

    public void getFromFile() throws FileNotFoundException {
        try {
            File file = new File("Users/" + userName + ".txt");
            Scanner reader = new Scanner(file);
            userName = reader.next();
            password = reader.next();
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not accessible");
        }
    }

}

package main.database;

public class UsersDTO {

    String id;
    String username;
    String password;
    double balance;

    public UsersDTO (String id, String username, String password, double balance) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public String getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public double getBalance() { return this.balance; }

}

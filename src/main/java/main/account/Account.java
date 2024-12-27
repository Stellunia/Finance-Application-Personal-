package main.account;

import main.ApplicationManager;
import main.database.DatabaseManager;
import main.database.UsersDTO;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;

public class Account {
    public File userFolder;
    private Scanner scanner = new Scanner(System.in);
    static String currentUser = null;
    public static HashMap<String, AccountDetails> userInfo = new HashMap<>();
    private DatabaseManager databaseManager;

    public Account() {
        databaseManager = new DatabaseManager();
        //userInfo.put("Gardevoir", new AccountDetails("PokemonType", "PokemonName", 5000));
        //userInfo.put("Maushold", new AccountDetails("Maushold", "The Council", 4000));
    }

    // DONE: Change to Database-variant
    // Handles the login procedure for the user
    public void authenticate() {
        System.out.println("Please enter username: ");
        String nameCredentials = scanner.nextLine();

        if (DatabaseManager.authenticateUser(nameCredentials)) {
            System.out.println("Please enter password: ");
            String passCredentials = scanner.nextLine();

            String userId = (DatabaseManager.authenticate(nameCredentials, passCredentials));
            if (userId != null) {
                System.out.println("Login successful.");
                System.out.println("Welcome, " + nameCredentials + ".");
                System.out.println("Write 'help' to receive a list of commands.");
                currentUser = userId;
                getBalance();
                ApplicationManager.loginCheck = false;
            } else {
                throw new IllegalArgumentException(
                        "'" + passCredentials + "' is incorrect."
                );
            }
        } else {
            throw new IllegalArgumentException(
                    "'" + nameCredentials + "' is not a valid username.");
        }
    }

    // Get current user's balance
    public double getBalance() {
        UsersDTO currentAccount = databaseManager.getUserByID(currentUser);
        //TODO: Get the user information for the relevant user and establish them as the current user
        // Currently gets the AccountDetails and establishes that instead of the database entry
        if (currentAccount != null) {
            return currentAccount.getBalance();
        } else {
            throw new IllegalStateException("User not found.");
        }
    }

    // DONE: Change to Database-variant
    // Need to move this into "AccountWriter" eventually
    // Handles addition of new accounts
    public void accountAddition() {
        System.out.println("Please enter username: ");
        String nameCredentials = scanner.nextLine();

        if (!userInfo.containsKey(nameCredentials)) {
            System.out.println("Please enter password: ");
            String passCredentials = scanner.nextLine();

            try (PreparedStatement createUser = DatabaseManager.connection.prepareStatement("INSERT INTO users (username, password, balance) VALUES (?, ?, ?)")) {
                createUser.setString(1, nameCredentials);
                createUser.setString(2, passCredentials);
                createUser.setDouble(3, 1000);
                if (createUser.executeUpdate() == 0) {
                    System.out.println("Nothing was inserted into the database.");
                    return;
                }
            } catch (SQLException e) {
                System.out.println("Failed to save user" + nameCredentials + "to database.");
                System.out.println("Error: " + e.getMessage());
                return;
            }

            System.out.println("Registered user " + nameCredentials + ", with an initial balance of: '1000'.");
    }
}

    // TODO: Change to Database-variant
    // Handles reading out the user's account details when called upon
    public void readAccountDetails() {
        UsersDTO accountInfo = databaseManager.getUserByID(Account.getCurrentUser());
        String username = accountInfo.getUsername();
        String password = accountInfo.getPassword();
        String userid = accountInfo.getId();
        double balance = accountInfo.getBalance();

        System.out.println("User ID: " + userid);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Balance: " + balance);
    }


    // DONE: This needs to be changed to not read each individual line, but instead opt for something more streamlined:
    // "Title: Stinky | Message: You are stinky | Amount: 500 | Type: ONE_TIME | Date: 25-09-2024"
    // "Title: Smelly | Message: You are smelly | Amount: 900 | Type: RECURRING_WEEKLY | Date: 27-09-2024"
    // DONE: Needs to build a history-reading system that would format the transactions based on current date, see Transactions.java?

    // Getters for the user, not all in use
    public static String getCurrentUser() { return currentUser; }
}


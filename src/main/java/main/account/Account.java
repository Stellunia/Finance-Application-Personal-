package main.account;

import main.ApplicationManager;
import main.database.DatabaseManager;
import main.database.UsersDTO;

import javax.xml.crypto.Data;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;

public class Account {
    public File userFolder;
    private Scanner scanner = new Scanner(System.in);
    static String currentUser = null;
    private String currentPass = null;
    private double currentBalance = 0;
    public static HashMap<String, AccountDetails> userInfo = new HashMap<>();
    private DatabaseManager databaseManager;

    public Account() {
        databaseManager = new DatabaseManager();
        //DatabaseManager.initializeDatabaseLoader();
        //userInfo.put("Gardevoir", new AccountDetails("PokemonType", "PokemonName", 5000));
        //userInfo.put("Maushold", new AccountDetails("Maushold", "The Council", 4000));
    }

    // TODO: Change to Database-variant
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
        return;
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
                    System.out.println("Nothing was inserted, perhaps there was a conflict.");
                    return;
                }
            } catch (SQLException e) {
                System.out.println("Failed to save user" + nameCredentials + "to database.");
                System.out.println("Error: " + e.getMessage());
                return;
            }

            System.out.println("Registered user " + nameCredentials + ", with an initial balance of: '1000'.");

            //AccountManager.userInfo.put(nameCredentials, new AccountDetails(nameCredentials, passCredentials, 1000));
/*            AccountDetails newAccount = new AccountDetails(nameCredentials, passCredentials, 1000.0);
            userInfo.put(nameCredentials, newAccount);*/
    }
}

    // TODO: Change to Database-variant: By method of getting the userid and then removing the row from the database.
    // Handles removal of user files, need to move this to "AccountRemover" for proper organisation
    public void removeAccount() {
        userInfo.remove(getCurrentUser());
        File userFile = new File(userFolder, "user_" + getCurrentUser() + ".txt");
        File histFile = new File(userFolder, "history_" + getCurrentUser() + ".txt");
        userFile.delete();
        histFile.delete();
        ApplicationManager.loginCheck = true;
        ApplicationManager.accountCheck = false;
    }

    // TODO: Change to Database-variant
    // Handles reading out the user's account details when called upon
    public void readAccountDetails() {
        File accountInfo = new File(userFolder,"user_" + getCurrentUser() + ".txt");

        if (!accountInfo.exists()) {
            System.out.println("There was no file found attributed to " + getCurrentUser() + ".");
            return;
        }
        try (Scanner fileScanner = new Scanner(accountInfo)) {
            if (fileScanner.hasNextLine()) {
                String accountUsername = fileScanner.nextLine();
                String accountPassword = fileScanner.hasNextLine() ? fileScanner.nextLine(): "N/A";
                String accountBalance = fileScanner.hasNextLine() ? fileScanner.nextLine(): "0";

                System.out.println("Username: " + accountUsername);
                System.out.println("Password: " + accountPassword);
                System.out.println("Balance: " + accountBalance);
            } else {
                System.out.println("This user's account information is empty.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("File was not found.");
            e.printStackTrace();
        }
    }


    // DONE: This needs to be changed to not read each individual line, but instead opt for something more streamlined:
    // "Title: Stinky | Message: You are stinky | Amount: 500 | Type: ONE_TIME | Date: 25-09-2024"
    // "Title: Smelly | Message: You are smelly | Amount: 900 | Type: RECURRING_WEEKLY | Date: 27-09-2024"
    // DONE: Needs to build a history-reading system that would format the transactions based on current date, see Transactions.java?

    // Getters for the user, not all in use
    public static String getCurrentUser() { return currentUser; }
    public String getCurrentPass() { return currentPass; }
    public double getCurrentBalance() { return currentBalance; }
}


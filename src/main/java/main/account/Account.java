package main.account;

import main.ApplicationManager;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Account {
    public File userFolder;
    Scanner scanner = new Scanner(System.in);
    static String currentUser = null;
    private String currentPass = null;
    private double currentBalance = 0;
    public static HashMap<String, AccountDetails> userInfo = new HashMap<>();
    private FileReader fileReader;

    public Account() {
        initializeSaveManager();
        loadUsers();
        //userInfo.put("Gardevoir", new AccountDetails("PokemonType", "PokemonName", 5000));
        //userInfo.put("Maushold", new AccountDetails("Maushold", "The Council", 4000));
    }

    public void authenticate() {
        System.out.println("Please enter username: ");
        String nameCredentials = scanner.nextLine();

        if (userInfo.containsKey(nameCredentials)) {
            System.out.println("Please enter password: ");
            String passCredentials = scanner.nextLine();

            if (userInfo.containsKey(nameCredentials) && userInfo.get
                    (nameCredentials).getPassword().equals(passCredentials)) {
                System.out.println("Login successful.");
                System.out.println("Welcome, " + nameCredentials + ".");
                System.out.println("Write 'help' to receive a list of commands.");
                currentUser = nameCredentials;
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
        AccountDetails currentAccount = userInfo.get(currentUser);
        if (currentAccount != null) {
            return currentAccount.getBalance();
        } else {
            throw new IllegalStateException("User not found.");
        }
    }

    // Handles addition of new accounts
    public void accountAddition() {
        System.out.println("Please enter username: ");
        String nameCredentials = scanner.nextLine();
        if (!userInfo.containsKey(nameCredentials)) {
            System.out.println("Please enter password: ");
            String passCredentials = scanner.nextLine();
            //AccountManager.userInfo.put(nameCredentials, new AccountDetails(nameCredentials, passCredentials, 1000));
            AccountDetails newAccount = new AccountDetails(nameCredentials, passCredentials, 1000.0);
            userInfo.put(nameCredentials, newAccount);

            if (saveAccount(newAccount)) {
                System.out.println("Account was created with an initial balance of: '1000'");
            } else {
                System.out.println("Account was created, but there was an error saving the information.");
            }
        } else {
            System.out.println("Username already exists.");
        }
    }

    // Initialise user folder to store user and history files within
    private void initializeSaveManager() {
        userFolder = new File("./users/");
        //historyFolder = new File("./users/history/");
        if (!userFolder.exists()) {
            if (userFolder.mkdirs()) {
                System.out.println("User folder was created.");
            } else {
                System.out.println("Failed to create user folder.");
            }
        }
    }

    // Handles saving user accounts into the user folder
    private boolean saveAccount(AccountDetails account) {
        if (userFolder == null) {
            System.out.println("User folder does not exist.");
            return false;
        }

        File userFile = new File(userFolder/* + "/" + account.getUsername()*/, "user_" + account.getUsername() + ".txt");
        if (userFile.exists()) {
            System.out.println("User account already exists.");
            return false;
        }
        return true;
    }

    public void removeAccount() {
        userInfo.remove(getCurrentUser());
        File userFile = new File(userFolder, "user_" + getCurrentUser() + ".txt");
        File histFile = new File(userFolder, "history_" + getCurrentUser() + ".txt");
        userFile.delete();
        histFile.delete();
        ApplicationManager.loginCheck = true;
        ApplicationManager.accountCheck = false;
    }

    // Gets all "user_(username).txt" files and inserts them into a list to then assign them to the hashmap "userInfo"
    // in order for the application to be able to utilize the account functions properly
    public void loadUsers() {
        // Use the function that scans the user information and prints it
        // Instead of it printing it for the current user, return it and enter it into the hashmap
        // Use a for function to go through each file and return every "username", "password" and "balance" into the hashmap
        // Bridge the gap between Hashmap and File to make it easier to use accounts
        // Scan the files upon each initialisation of the application and add all available "user_(name).txt" into the hashmap
        // File accountInfo = new File(userFolder,"user_" + getCurrentUser() + ".txt");
        // DONE.

        File[] userFiles = userFolder.listFiles();
        if (userFiles == null) {
            System.out.println("Unable to access the users folder.");
            return;
        }

        for (File accountInfo : userFiles){
            String username = null;
            String password = null;
            double balance = 0;

            if (accountInfo.getName().startsWith("user_") &&  accountInfo.getName().endsWith(".txt")){
                try (Scanner fileScanner = new Scanner(accountInfo)){

                    if (fileScanner.hasNextLine()) {
                        username = fileScanner.nextLine().trim();
                    }
                    if (fileScanner.hasNextLine()) {
                        password = fileScanner.nextLine().trim();
                    }
                    if (fileScanner.hasNextLine()) {
                        try {
                            balance = Double.parseDouble(fileScanner.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("The balance amount in " + accountInfo.getName() + " could not be retrieved.");
                        }
                    }

                    if (username != null && password != null) {
                        userInfo.put(username, new AccountDetails(username, password, balance));
                    } else {
                        System.out.println("Could not read the data in " + accountInfo.getName());
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error reading " + accountInfo.getName());
                }
            }
        }
    }

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


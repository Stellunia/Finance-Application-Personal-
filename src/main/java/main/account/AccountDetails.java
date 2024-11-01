package main.account;

import main.TransType;
import main.transaction.TransactionType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Set up AccountDetails which manages the object for the hashmap that stores accounts
public class AccountDetails {
    private Account account;
    private List<TransactionType> transactionType = new ArrayList<>();
    private HashMap<String, AccountDetails> userInfo;

    // Hashmap that sets up the account functions
    public AccountDetails(HashMap<String, AccountDetails> userInfo, Account account) {
        this.userInfo = userInfo;
        this.account = account;
    }

    String username;
    String password;
    double balance;

    // Return getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }

    // Full hashmap setter
    public AccountDetails(String username, String password, Double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        //this.transactionType = new ArrayList<>(); // Unused transaction list, instead opted for history_(user).txt to be read into the application
    }

    // Singular values setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setBalance(double balance) { this.balance = balance; }

    public void addTransactionType(TransactionType transaction){
        transactionType.add(transaction);
    }

    public List<TransactionType> getTransactionType() {
        return transactionType;
    }

    // Function that allows "balance" value in user_(name).txt to be overwritten, foundation for other functions.
    public void overwriteBalance(int indexNumber, double newBalance) throws IOException {
        Path path = Path.of("./users/", "user_" + account.getCurrentUser() + ".txt");
        //File userFile = new File(account.userFolder, "user_" + account.getCurrentUser() + ".txt");
        String newStrBalance = String.valueOf(newBalance);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        //Files.write(path, lines, StandardCharsets.UTF_8);
        lines.set(indexNumber, newStrBalance);
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    // Function to add balance to an account.
    public double addBalanceInt(String addTitle, String addMessage, double addAmount, TransType addTransType, LocalDate addTransDate) {
        String username = account.getCurrentUser();
        AccountDetails currentAccount = account.userInfo.get(username);
        double newBalance = currentAccount.getBalance() + addAmount;
        currentAccount.setBalance(newBalance);
        System.out.println("You've received " + addAmount + " to your account.");
        try { overwriteBalance(2, newBalance);
            writeHistory(addTitle, addMessage, addAmount, addTransType, addTransDate, true);
            System.out.println("The user file & transaction history has been updated.");
        } catch (IOException e) {
            System.out.println("Error updating the user and history files: " + e.getMessage());
        }
        return newBalance;
    }

    // Function to remove balance from an account.
    public double removeBalanceInt(String removeTitle, String removeMessage, double removeAmount, TransType removeTransType, LocalDate removeTransDate) {
        String username = account.getCurrentUser();
        AccountDetails currentAccount = account.userInfo.get(username);
        if (balanceAmount(removeAmount)){
            double newBalance = currentAccount.getBalance() - removeAmount;
            currentAccount.setBalance(newBalance);
            System.out.println("You've transferred " + removeAmount + " to " + "'Pokémon Trainer'" + " from your account.");
            try { overwriteBalance(2, newBalance);
                writeHistory(removeTitle, removeMessage, removeAmount, removeTransType, removeTransDate, false);
                System.out.println("The user file has been updated with the new amount.");
            } catch (IOException e) {
                System.out.println("Error updating the balance amount in user file: " + e.getMessage());
            }
        } else {
            System.out.println(username + " does not have enough balance to cover this transaction.");
        }
        return balance;
    }


    // DONE: This needs to be changed to not read each individual line, but instead opt for something more streamlined:
    // "Title: Stinky, Message: You are stinky, Amount: 500, Type: RECURRING_WEEKLY, Date: 25-09-2024"
    // "Title: Stinky | Message: You are stinky | Amount: 500 | Type: RECURRING_WEEKLY | Date: 25-09-2024"

    // Handles writing to history_(user).txt file in order to append new transactions into the file itself.
    public void writeHistory(String title, String message, double amount, TransType transType, LocalDate currentDate, boolean isDeposit) throws IOException {
        String username = account.getCurrentUser();
        Path historyPath = Path.of("./users/", "history_" + username + ".txt");

        String formattedAmount;
        if (isDeposit) { formattedAmount = "+" + amount; }
        else { formattedAmount = "-" + amount; }
        String transactionEntry = String.format("Title: %s | Message: %s | Amount: %s | Type: %s | Date: %s%n%n",
                title, message, formattedAmount, transType, currentDate);
        Files.write(historyPath, transactionEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    // Checks if the current user has enough balance to cover said transaction.
    boolean balanceAmount(double removeAmount) {
        return balance >= removeAmount;
    }
}

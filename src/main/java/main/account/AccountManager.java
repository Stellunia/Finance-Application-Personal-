package main.account;

import main.database.DatabaseManager;
import main.database.UsersDTO;
import main.transaction.TransType;
import main.transaction.TransactionManager;

import java.io.IOException;
import java.time.LocalDate;

public class AccountManager {
    private AccountWriter accountWriter;
    private DatabaseManager databaseManager;
    private TransactionManager transactionManager;

    public AccountManager() {
        accountWriter = new AccountWriter();
        databaseManager = new DatabaseManager();
        transactionManager = new TransactionManager();
    }

    // TODO: Change to be database variant
    // Function to add balance to an account.
    public double addBalanceInt(String addTitle, String addMessage, double addAmount, TransType addTransType, LocalDate addTransDate) {
        UsersDTO currentAccount = databaseManager.getUserByID(Account.getCurrentUser());
        String username = currentAccount.getUsername();
        String userid = currentAccount.getId();

        double newBalance = currentAccount.getBalance() + addAmount;
        currentAccount.setBalance(newBalance);
        System.out.println("You've received " + addAmount + " to your account.");
        try { accountWriter.overwriteBalance(Account.getCurrentUser(), newBalance);
            accountWriter.writeHistory(userid, addTitle, addMessage, addAmount, addTransType, addTransDate, true);
            System.out.println("The user & transaction history has been updated.");
        } catch (IOException e) {
            System.out.println("Error updating the database: " + e.getMessage());
        }
        return newBalance;
    }

    // TODO: Change to be database variant
    // Function to remove balance from an account.
    public double removeBalanceInt(String removeTitle, String removeMessage, double removeAmount, TransType removeTransType, LocalDate removeTransDate) {
        UsersDTO currentAccount = databaseManager.getUserByID(Account.getCurrentUser());
        String username = currentAccount.getUsername();
        String userid = currentAccount.getId();
        double balance = currentAccount.getBalance();

        if (transactionManager.balanceAmount(balance, removeAmount)){
            double newBalance = currentAccount.getBalance() - removeAmount;
            currentAccount.setBalance(newBalance);
            System.out.println("You've transferred " + removeAmount + " to " + "'Pokémon Trainer'" + " from your account.");
            try { accountWriter.overwriteBalance(userid, newBalance);
                accountWriter.writeHistory(userid, removeTitle, removeMessage, removeAmount, removeTransType, removeTransDate, false);
                System.out.println(username + " has been updated with the new amount.");
            } catch (IOException e) {
                System.out.println("Error updating the balance amount in user database: " + e.getMessage());
            }
        } else {
            System.out.println(username + " does not have enough balance to cover this transaction.");
        }
        return currentAccount.getBalance();
    }
}

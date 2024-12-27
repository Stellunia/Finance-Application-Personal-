package main.account;

import main.transaction.TransactionType;

import java.util.ArrayList;
import java.util.List;

// Set up AccountDetails which manages the object for the hashmap that stores accounts
public class AccountDetails {
    private List<TransactionType> transactionType = new ArrayList<>();

    private String username;
    private String password;
    private double balance;

    // Full hashmap setter
/*    public AccountDetails(String username, String password, double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        //this.transactionType = new ArrayList<>(); // Unused transaction list, instead opted for history_(user).txt to be read into the application
    }*/

    // Checks if the current user has enough balance to cover said transaction.
/*    public boolean balanceAmount(double removeAmount) {
        return balance >= removeAmount;
    }*/
}

package main.transaction;

import main.account.Account;
import java.time.LocalDate;

public class Transaction {
    private Account account;

    private String transTitle;
    private String transMessage;
    private double transAmount;
    private String transType;
    private LocalDate transDate;

    // Setter for Transactions
    public Transaction(String transTitle, String transMessage, double transAmount, String transType, LocalDate transDate) {
        this.transTitle = transTitle;
        this.transMessage = transMessage;
        this.transAmount = transAmount;
        this.transType = transType;
        this.transDate = transDate;
    }

    public Transaction(String transTitle, String transMessage, double transAmount, String transType, String transDate) {
        this.transTitle = transTitle;
        this.transMessage = transMessage;
        this.transAmount = transAmount;
        this.transType = transType;
        this.transDate = LocalDate.parse(transDate);
    }

    public String getTransTitle() { return transTitle; }
    public String getTransMessage() { return transMessage; }
    public double getTransAmount() { return transAmount; }
    public String getTransType() { return transType; }
    public LocalDate getTransDate() { return transDate; }

    // Format for each transaction within history_(user).txt
    @Override
    public String toString() {
        return "Title: " + transTitle + " | Message: " + transMessage +
                " | Amount: " + transAmount + " | Type: " + transType +
                " | Date: " + transDate;
    }
}

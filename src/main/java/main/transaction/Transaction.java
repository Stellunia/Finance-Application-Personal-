package main.transaction;

import main.ApplicationManager;
import main.TransType;
import main.account.Account;
import main.account.AccountDetails;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Transaction {

    private Account account;
    //private AccountDetails accountDetails;

/*    boolean addingBalance = false;
    boolean removingBalance = false;*/

    private String transTitle;
    private String transMessage;
    private double transAmount;
    private String transType;
    private LocalDate transDate;

    // Setter for Transactions
    Transaction(String transTitle, String transMessage, double transAmount, String transType, LocalDate transDate) {
        this.transTitle = transTitle;
        this.transMessage = transMessage;
        this.transAmount = transAmount;
        this.transType = transType;
        this.transDate = transDate;
    }

    public String getTransTitle() { return transTitle; }
    public String getTransMessage() { return transMessage; }
    public double getTransAmount() { return transAmount; }
    public String getTransType() { return transType; }
    public LocalDate getTransDate() { return transDate; }

    // Read existing transactions into lists to use for sorting
    public static List<Transaction> readTransactions(String currentUser) {
        List<Transaction> transactions = new ArrayList<>();
        File historyInfo = new File("users", "history_" + currentUser + ".txt");

        if (!historyInfo.exists()) {
            System.out.println("There was no history file found attributed to " + currentUser + ".");
            return transactions;
        }

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (Scanner fileScanner = new Scanner(historyInfo)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.startsWith("Title: ")) {
                    String[] parts = line.split("\\|");
                    String transTitle = parts[0].substring(7).trim();
                    String transMessage = parts[1].substring(9).trim();
                    double transAmount = Double.parseDouble(parts[2].substring(8).trim());
                    String transType = parts[3].substring(6).trim();
                    LocalDate transDate = LocalDate.parse(parts[4].substring(6).trim(), dateFormat);

                    transactions.add(new Transaction(transTitle, transMessage, transAmount, transType, transDate));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found.");
            e.printStackTrace();
        }
        return transactions;
    }

    // TODO: Call upon the balance of the user, allow to transfer between accounts.
    // DONE: List transaction history upon user asking for access.
    // DONE (Half done, sort of - removes transaction from history rather than one that hasn't occured yet
    // as there is no current way to handle transactions-to-be-made): Remove transactions.
    // DONE: Add balance to existing account's balance amount.

    // Handles the creation of transactions
    public void createTransaction() {
        while (true) {
            AccountDetails currentAccount = account.userInfo.get(account.getCurrentUser());
            System.out.println(account.getCurrentUser() + " has currently got: " + currentAccount.getBalance() + " to spend.");
            System.out.println("Type 'create' to send a transaction to someone else. Does not work yet. DO NOT USE.");
            System.out.println("Type 'deposit' to add balance to your account.");
            System.out.println("Type 'send' to remove a current transaction.");
            System.out.println("Type 'return' to return to the previous menu.");

            String transCommand = ApplicationManager.commandScanner.nextLine();

            switch (transCommand) {
                case "create":
                    System.out.println("Enter the following details in succession:");
                    System.out.println("""
                            Title: ...
                            Message: ...
                            Amount: ...""");
                    System.out.println("Enter the transaction title: ");
                    String createTransTitle = ApplicationManager.commandScanner.nextLine();
                    System.out.println("Enter the transaction message: ");
                    String createTransMessage = ApplicationManager.commandScanner.nextLine();
                    System.out.println("Enter the amount you'd want to transfer: ");
                    double createTransAmount = ApplicationManager.commandScanner.nextDouble();
/*                    System.out.println("Select the type of transaction this is.");
                    System.out.println("""
                            one-off,
                            weekly,
                            monthly,
                            yearly
                            """);
                    String createTransDate = ApplicationManager.commandScanner.nextLine();
                    switch (createTransDate) {
                        case "one-off":
                        case "weekly":
                        case "monthly":
                        case "yearly":
                    }*/

/*                    transTitle = createTransTitle;
                    transMessage = createTransMessage;
                    transAmount = createTransAmount;*/
                    //transDate = createTransDate;


                    return;

                case "deposit":
                    System.out.println("Write a title for the addition: ");
                    String addBalanceTitle = ApplicationManager.commandScanner.nextLine();
                    System.out.println("Write a description for the addition: ");
                    String addBalanceMessage = ApplicationManager.commandScanner.nextLine();
                    System.out.println("Enter the amount you'd like to deposit: ");
                    double addBalanceAmount = ApplicationManager.commandScanner.nextDouble();
                    ApplicationManager.commandScanner.nextLine();
                    System.out.println("Select transaction type (using numerals): \n" +
                            "1. One-time\n" +
                            "2. Daily\n" +
                            "3. Weekly\n" +
                            "4. Monthly\n" +
                            "5. Yearly");
                    int addTypeChoice = ApplicationManager.commandScanner.nextInt();
                    ApplicationManager.commandScanner.nextLine();

                    TransType addTransType;
                    switch (addTypeChoice) { // This doesn't serve a purpose, but is just there for vanity
                        case 1: addTransType = TransType.ONE_TIME; break;
                        case 2: addTransType = TransType.RECURRING_DAILY; break;
                        case 3: addTransType = TransType.RECURRING_WEEKLY; break;
                        case 4: addTransType = TransType.RECURRING_MONTHLY; break;
                        case 5: addTransType = TransType.RECURRING_YEARLY; break;
                        default:
                            System.out.println("Invalid choice, the transaction will default to one-time.");
                            addTransType = TransType.ONE_TIME;
                    }
                    LocalDate addStartDate = LocalDate.now();
                    TransactionType newTransaction = new TransactionType(addBalanceTitle, addBalanceMessage, addBalanceAmount, addTransType, addStartDate);
                    currentAccount.addTransactionType(newTransaction);
                    System.out.println("Successfully deposited balance into your account.");

                    currentAccount.addBalanceInt(addBalanceTitle, addBalanceMessage, addBalanceAmount, addTransType, addStartDate);
                    break;

                case "send":
                    System.out.println("Write a title for the transaction: ");
                    String removeBalanceTitle = ApplicationManager.commandScanner.nextLine();
                    System.out.println("Write a description for the transaction: ");
                    String removeBalanceMessage = ApplicationManager.commandScanner.nextLine();
                    System.out.println("Enter the amount you'd like to send: ");
                    int removeBalanceAmount = ApplicationManager.commandScanner.nextInt();
                    ApplicationManager.commandScanner.nextLine();
                    System.out.println("Select transaction type (using numerals): \n" +
                            "1. One-time\n" +
                            "2. Daily\n" +
                            "3. Weekly\n" +
                            "4. Monthly\n" +
                            "5. Yearly");
                    int removeTypeChoice = ApplicationManager.commandScanner.nextInt();
                    ApplicationManager.commandScanner.nextLine();

                    TransType removeTransType;
                    switch (removeTypeChoice) { // This doesn't serve a purpose, but is just there for vanity
                        case 1: removeTransType = TransType.ONE_TIME; break;
                        case 2: removeTransType = TransType.RECURRING_DAILY; break;
                        case 3: removeTransType = TransType.RECURRING_WEEKLY; break;
                        case 4: removeTransType = TransType.RECURRING_MONTHLY; break;
                        case 5: removeTransType = TransType.RECURRING_YEARLY; break;
                        default:
                            System.out.println("Invalid choice, the transaction will default to one-time.");
                            removeTransType = TransType.ONE_TIME;
                    }
                    LocalDate removeStartDate = LocalDate.now();
                    TransactionType newRemoveTransaction = new TransactionType(removeBalanceTitle, removeBalanceMessage, removeBalanceAmount, removeTransType, removeStartDate);
                    currentAccount.addTransactionType(newRemoveTransaction);
                    System.out.println("Successfully sent a transaction.");

                    currentAccount.removeBalanceInt(removeBalanceTitle, removeBalanceMessage, removeBalanceAmount, removeTransType, removeStartDate);
                    break;

                case "return":
                    return;
                default:
                    System.out.println("'" + transCommand + "' is not a valid command. Write 'help' to get a list of commands.");
            }
        }
    }

    // DONE: Needs to handle the removal of transactions from the history_(user).txt.
    public static void removeTransaction(String currentUser) {
        List<String> transactions = new ArrayList<>();
        File historyInfo = new File("users", "history_" + currentUser + ".txt");

        if (!historyInfo.exists()) {
            System.out.println("There was no history file found attributed to " + currentUser + ".");
            return;
        }

        try (Scanner fileScanner = new Scanner(historyInfo)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    transactions.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not read file.");
            e.printStackTrace();
            return;
        }

        for (int i = 0; i < transactions.size(); i++) {
            System.out.println((i + 1) + ": " + transactions.get(i));
        }

        Scanner removalScanner = new Scanner(System.in);
        int removeTransaction = 0;
        while (true) {
            System.out.println("Type the numeral of the transaction you'd wish to remove, otherwise type '0' to cancel.");
            try {
                removeTransaction = Integer.parseInt(removalScanner.nextLine());
                if (removeTransaction >= 0 && removeTransaction <= transactions.size()) {
                    break;
                } else {
                    System.out.println(removalScanner + " is not a valid input, please enter a numeral.");
                }
            } catch (NumberFormatException e) {
                System.out.println(removalScanner + " is not a valid input, please enter a numeral.");
            }
        }

            if (removeTransaction != 0) {
                String deletedTransaction = transactions.remove(removeTransaction - 1);
                System.out.println("Removed transaction: " + deletedTransaction);

                try (PrintWriter writer = new PrintWriter(new FileWriter(historyInfo))) {
                    for (String trans : transactions) {
                        writer.println(trans + "\n");
                    }
                    System.out.println("Updated history_" + currentUser + ".txt.");
                } catch (IOException e) {
                    System.out.println("Could not write to history_" + currentUser + ".txt.");
                }
            } else {
                System.out.println("Returned to previous menu.");
            }
    }

    // Format for each transaction within history_(user).txt
    @Override
    public String toString() {
        return "Title: " + transTitle + " | Message: " + transMessage +
                " | Amount: " + transAmount + " | Type: " + transType +
                " | Date: " + transDate;
    }
}

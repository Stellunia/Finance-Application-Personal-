package main.transaction;

import main.account.Account;
import main.database.DatabaseManager;
import main.database.UsersDTO;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static main.database.DatabaseManager.connection;

public class HistoryReader {
    private Account account;
    private DatabaseManager databaseManager;

    // TODO: Call upon the balance of the user, allow to transfer between accounts.
    // DONE: List transaction history upon user asking for access.
    // DONE (Half done, sort of - removes transaction from history rather than one that hasn't occured yet
    // as there is no current way to handle transactions-to-be-made): Remove transactions.
    // DONE: Add balance to existing account's balance amount.
    // DONE: Convert existing fundamentals to use database instead of history_(username).txt
    // TODO: Use a format to

    public HistoryReader () {
        databaseManager = new DatabaseManager();
    }

    // Handles organising the transaction's dates in order to display them cleanly when the user sorts the history
    private String categoriseTransaction(LocalDate currentDate, LocalDate transactionDate) {
        long daysBetween = ChronoUnit.DAYS.between(transactionDate, currentDate);

        if (daysBetween == 0) {
            return "Today";
        } else if (daysBetween == 1) {
            return "1 day ago";
        } else if (daysBetween <= 7) {
            return daysBetween + " days ago";
        } else if (daysBetween <= 30) {
            long weeksBetween = ChronoUnit.WEEKS.between(transactionDate, currentDate);
            return weeksBetween == 1 ? "1 week ago" : weeksBetween + " weeks ago";
        } else if (daysBetween <= 365) {
            long monthsBetween = ChronoUnit.MONTHS.between(transactionDate, currentDate);
            return monthsBetween == 1 ? "1 month ago" : monthsBetween + " months ago";
        } else {
            long yearsBetween = ChronoUnit.YEARS.between(transactionDate, currentDate);
            return yearsBetween == 1 ? "1 year ago" : yearsBetween + " years ago";
        }
    }

    // TODO: Change to Database-variant
    // TODO: Add a method to check for sender and receiver or a separate table for whether it's a deposit or sending transaction
    // Handles reading out the history details for the user when called upon
    public void readHistoryDetails(LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactionList = readTransactions(account.getCurrentUser());
        //TODO: Add username instead of ID readout
        LocalDate currentDate = LocalDate.now();

        double transactionCount = 0;
        double totalIncome = 0;
        double totalSpending = 0;

        for (Transaction t : transactionList) {
            if ((startDate == null || !t.getTransDate().isBefore(startDate)) &&
                    (endDate == null || !t.getTransDate().isAfter(endDate))) {


                transactionCount++;
                System.out.println("Transaction #" + transactionCount);
                System.out.println(categoriseTransaction(currentDate, t.getTransDate()));
                System.out.println(t.toString());
                System.out.println();

                double amount = t.getTransAmount();
                if (amount > 0) {
                    totalIncome += amount;
                } else {
                    totalSpending += Math.abs(amount);
                }
            }
        }

        if (transactionCount == 0) {
            System.out.println("There is no valid history information for this user.");
        } else {
            System.out.println("Income: " + totalIncome);
            System.out.println("Spendings: " + totalSpending);
        }
    }

    // TODO: Change to Database-variant
    // Read existing transactions into lists to use for sorting
    public List<Transaction> readTransactions(String userid) {
        var transactions = new ArrayList<Transaction>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactionHistory WHERE userid = '" + userid + "';")) {
            ResultSet transactionHistory = statement.executeQuery();
            try {
                while (transactionHistory.next()) {
                    transactions.add(new Transaction(transactionHistory.getString("title"), transactionHistory.getString("message"),
                            transactionHistory.getDouble("amount"), transactionHistory.getString("type"),
                            transactionHistory.getString("date")));
                }
            } catch (SQLException e) {
                System.out.println("Error within readTransactions: " + e.getMessage());
            }
            System.out.println(transactions.size());

        } catch (SQLException e) {
            System.out.println("Error at readTransactions: " + e.getMessage());
        }
        return transactions;
    }

/*      //File historyInfo = new File("users", "history_" + currentUser + ".txt");
        if (!historyInfo.exists()) {
            System.out.println("There was no history information found attributed to " + username + ".");
            return transactions;
        }

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // This splits the line for each line it finds something in within the file itself and distributes it to different strings
        try (Scanner fileScanner = new Scanner(historyInfo)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.startsWith("Title: ")) {
                    String[] parts = line.split("\\|");
                    String transactionTitle = parts[0].substring(7).trim();
                    String transactionMessage = parts[1].substring(9).trim();
                    double transactionAmount = Double.parseDouble(parts[2].substring(8).trim());
                    String transactionType = parts[3].substring(6).trim();
                    LocalDate transactionDate = LocalDate.parse(parts[4].substring(6).trim(), dateFormat);

                    transactions.add(new Transaction(transactionTitle, transactionMessage,
                            transactionAmount, transactionType, transactionDate));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found.");
            e.printStackTrace();
        }
        return transactions;*/
}

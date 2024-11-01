package main.transaction;

import main.account.Account;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HistoryReader {
    private Account account;

    // TODO: Call upon the balance of the user, allow to transfer between accounts.
    // DONE: List transaction history upon user asking for access.
    // DONE (Half done, sort of - removes transaction from history rather than one that hasn't occured yet
    // as there is no current way to handle transactions-to-be-made): Remove transactions.
    // DONE: Add balance to existing account's balance amount.

    // Handles organising the transaction's dates in order to display them cleanly when the user sorts the history
    private String categoriseTransaction(LocalDate currentDate, LocalDate transDate) {
        long daysBetween = ChronoUnit.DAYS.between(transDate, currentDate);

        if (daysBetween == 0) {
            return "Today";
        } else if (daysBetween == 1) {
            return "1 day ago";
        } else if (daysBetween <= 7) {
            return daysBetween + " days ago";
        } else if (daysBetween <= 30) {
            long weeksBetween = ChronoUnit.WEEKS.between(transDate, currentDate);
            return weeksBetween == 1 ? "1 week ago" : weeksBetween + " weeks ago";
        } else if (daysBetween <= 365) {
            long monthsBetween = ChronoUnit.MONTHS.between(transDate, currentDate);
            return monthsBetween == 1 ? "1 month ago" : monthsBetween + " months ago";
        } else {
            long yearsBetween = ChronoUnit.YEARS.between(transDate, currentDate);
            return yearsBetween == 1 ? "1 year ago" : yearsBetween + " years ago";
        }
    }

    // Handles reading out the history details for the user when called upon
    public void readHistoryDetails(LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactionList = readTransactions(account.getCurrentUser());
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

    // Read existing transactions into lists to use for sorting
    public List<Transaction> readTransactions(String currentUser) {
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
}

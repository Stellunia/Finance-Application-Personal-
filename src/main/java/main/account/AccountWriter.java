package main.account;

import main.transaction.TransType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;

public class AccountWriter {
    private Account account;


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
}

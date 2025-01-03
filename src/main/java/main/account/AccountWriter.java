package main.account;

import main.database.DatabaseManager;
import main.database.UsersDTO;
import main.transaction.TransType;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static main.database.DatabaseManager.connection;

public class AccountWriter {
    private Account account;
    private DatabaseManager databaseManager;

    public AccountWriter() {
        databaseManager = new DatabaseManager();
    }

    // Insert account saving into this file whenever I figure it out, I guess.


    // Function that allows "balance" value in user_(name).txt to be overwritten, foundation for other functions.
    public void overwriteBalance(String userid, double newBalance) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE users SET balance = ? WHERE id = '" + userid + "';")) {
            statement.setDouble(1, newBalance);

            if (statement.executeUpdate() == 0) {
                System.out.println("Nothing was inserted to the database.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error at overwriteBalance: " + e.getMessage());
            return;
        }
    }

    /*
    public void overwriteBalance(int indexNumber, double newBalance) throws IOException {
        Path path = Path.of("./users/", "user_" + account.getCurrentUser() + ".txt");
        //File userFile = new File(account.userFolder, "user_" + account.getCurrentUser() + ".txt");
        String newStrBalance = String.valueOf(newBalance);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        //Files.write(path, lines, StandardCharsets.UTF_8);
        lines.set(indexNumber, newStrBalance);
        Files.write(path, lines, StandardCharsets.UTF_8);
    }*/

    // DONE: This needs to be changed to not read each individual line, but instead opt for something more streamlined:
    // "(Not visible) ID: 1 | (Not visible) UUID: xyz123-abc456 | Title: Stinky, Message: You are stinky, Amount: 500, Type: RECURRING_WEEKLY, Date: 25-09-2024"
    // "Title: Stinky | Message: You are stinky | Amount: 500 | Type: RECURRING_WEEKLY | Date: 25-09-2024"

    // TODO: Change to Database-variant
    // Handles writing to history_(user).txt file in order to append new transactions into the file itself.
    public void writeHistory(String userid, String title, String message, double amount, TransType transType, LocalDate currentDate, boolean isDeposit) throws IOException {
        java.sql.Date date = java.sql.Date.valueOf(currentDate);


        try (PreparedStatement statement = DatabaseManager.connection.prepareStatement(
                "INSERT INTO transactionHistory (userid, title, message, amount, type, date) VALUES ('" + userid + "', ?, ?, ?, ?, ?);")) {
            statement.setString(1, title);
            statement.setString(2, message);
            statement.setDouble(3, amount);
            statement.setString(4, transType.toString());
            statement.setDate(5, date);
            // TODO: add back isDeposit to table and thus checks for - or +

            if (statement.executeUpdate() == 0) {
                System.out.println("Nothing was inserted.");
                return;
            }

        } catch (SQLException e) {
            System.out.println("Error at overwriteBalance: " + e.getMessage());
            return;
        }
        System.out.println("Successfully saved to database.");
    }
}

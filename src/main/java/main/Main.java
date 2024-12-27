package main;

import main.database.DatabaseManager;

import java.sql.SQLException;

public class Main {
    public boolean running = true;
    public ApplicationManager applicationManager = new ApplicationManager(this);

    public static void main(String[] args) {
        Main main = new Main();
        System.out.println("Welcome to Your Personal Finance Application.");
        try {
            DatabaseManager.connectToDatabase();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        while (main.running) {
            try {
                main.applicationManager.readCommand();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

package main;

import eu.hansolo.tilesfx.Command;
import main.account.Account;
import main.command.CommandManager;
import main.command.StopCommand;
import main.database.DatabaseManager;
import main.transaction.TransactionCreator;

import java.util.Scanner;

public class ApplicationManager {
        public static Scanner commandScanner;
        private CommandManager commandManager;
        private StopCommand stopCommand;
        private Account account;
        private TransactionCreator transactionCreator;
        private DatabaseManager databaseManager;

        static public boolean loginCheck = true;
        static public boolean accountCheck = false;

        public ApplicationManager(Main main) {
            stopCommand = new StopCommand(main);
            databaseManager = new DatabaseManager();
            commandManager = new CommandManager(main);
            transactionCreator = new TransactionCreator(main);
            account = new Account();
            commandScanner = new Scanner(System.in);
        }

        // Handles the general usage of the application with a scanner that reads user inputs,
        // with initial lock-out to prevent user's without accounts from entering
        public void readCommand(){
            while (loginCheck) {
                commandManager.displayLoginHelp();
                try {
                    String command = commandScanner.nextLine();
                    if (command.equalsIgnoreCase("login")) {
                        account.authenticate();
                    } else if (command.equalsIgnoreCase("register")) {
                        account.accountAddition();
                    } else if (command.equalsIgnoreCase("stop")) {
                        stopCommand.run();
                        return;
                    } else {
                        throw new IllegalArgumentException("'" + command + "' is not a valid command.");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (!loginCheck) {
                String input = commandScanner.nextLine();
                if (input.isBlank()) {
                    throw new IllegalArgumentException("Command cannot be empty.");
                }

                String[] args = input.split(" ");
                if (args.length == 0) {
                    throw new IllegalArgumentException("Command cannot be empty.");
                }

                String command = args[0];
                switch (command) {
                    case "stop":
                        stopCommand.run();
                        databaseManager.closeConnection();
                        return;
                    case "transaction":
                        commandManager.handleTransaction(account, transactionCreator);
                        break;
                    case "history":
                        commandManager.handleHistory(account);
                        break;
                    case "account":
                        commandManager.handleAccount(account);
                        break;
                    case "help":
                        commandManager.displayHelp();
                        break;
                    case "logout":
                        loginCheck = true;
                        System.out.println("Logged out successfully.");
                        return;
                    default:
                        System.out.println("'" + command + "' is not a valid command. Write 'help' to get a list of commands.");

                }
            }
    }
}

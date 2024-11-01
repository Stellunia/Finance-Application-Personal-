package main;

import main.account.Account;
import main.command.CommandManagerImpI;
import main.command.StopCommand;
import main.transaction.Transaction;

import java.time.LocalDate;
import java.util.Scanner;

public class ApplicationManager {
        public static Scanner commandScanner;
        //private CommandManager helpCommand;
        private CommandManagerImpI commandManager;
        private StopCommand stopCommand;
        private Account account;
        private Transaction transaction;

        static public boolean loginCheck = true;
        static public boolean accountCheck = false;
        //static public boolean transCheck = false;
        //static public boolean historyCheck = false;

        public ApplicationManager(Main main) {
            stopCommand = new StopCommand(main);
            //helpCommand = new CommandManager();
            transaction = new Transaction("", "", 0.0, "ONE_TIME", LocalDate.now());
            commandManager = new CommandManagerImpI();
            account = new Account();
            commandScanner = new Scanner(System.in);
        }

        // Handles the general usage of the application with a scanner that reads user inputs,
        // with initial lock-out to prevent user's without accounts from entering
        public void readCommand(){
            while (loginCheck) {
                System.out.println("Enter 'register' to create an account.");
                System.out.println("Enter 'login' to log onto an account.");
                try {
                    String command = commandScanner.nextLine();
                    if (command.equalsIgnoreCase("login")) {
                        account.authenticate();
                    }
                    else if (command.equalsIgnoreCase("register")) {
                        account.accountAddition();
                    } else {
                        throw new IllegalArgumentException(
                                "'" + command + "' is not a valid command. Write 'help' to get a list of commands.");
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

                //System.out.println("Welcome, " + this.accountManager);
                String command = args[0];
                switch (command) {
                    case "stop":
                        stopCommand.run(args);
                        return;
                    case "transaction":
                        commandManager.handleTransaction(account, transaction);
                        //commandManager.handleTransactionCommand(account, transaction);
                        break;
                    case "history":
                        // DONE: Set up removal of transactions
                        commandManager.handleHistory(account);
                        //commandManager.handleHistoryCommand(account);
                        break;
                    case "account":
                        commandManager.handleAccount(account);
                        //commandManager.handleAccountCommand(account);
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
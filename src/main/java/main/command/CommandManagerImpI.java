package main.command;

import main.ApplicationInterface;
import main.ApplicationManager;
import main.account.Account;
import main.transaction.Transaction;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CommandManagerImpI implements ApplicationInterface {
    @Override
    public void displayHelp() {
        System.out.println("Commands:");
        System.out.println(" transaction - Create a new transaction.");
        System.out.println(" history - View the history of transactions.");
        System.out.println(" account - View the current account.");
        System.out.println(" help - display a list of available commands.");
        System.out.println(" logout - log out of your account and return to login.");
        System.out.println(" stop - quit the application.");
    }

    @Override
    public void displayHistoryHelp() {
        System.out.println("Type 'today' to sort by today's transactions.");
        System.out.println("Type 'yesterday' to sort by transactions from the last day.");
        System.out.println("Type 'week' to sort by transactions from the last week.");
        System.out.println("Type 'month' to sort by transactions from the last month.");
        System.out.println("Type 'year' to sort by transactions from the last year.");
        System.out.println("Type 'all' to show all transactions.");
        System.out.println("Type 'remove' to remove a transaction from your history.");
        System.out.println("Type 'return' to return to the previous menu.");
    }

    @Override
    public void handleTransaction(Account account, Transaction transaction) {
        System.out.println("Create a recurring or one-time transaction.");
        account.loadUsers();
        transaction.createTransaction();
    }

    @Override
    public void handleAccount(Account account) {
        System.out.println("These are your account details.");
        account.readAccountDetails();

        while (true) {
            System.out.println("Type 'remove' to delete your account.");
            System.out.println("Type 'return' to return to the previous menu.");

            String accountCommand = ApplicationManager.commandScanner.nextLine();

            switch(accountCommand) {
                case "remove":
                    handleAccountRemoval(account);
                    return;
                case "return":
                    return;
                default:
                    System.out.println("'" + accountCommand + "' is not a valid command. Write 'help' to get a list of commands.");
            }
        }
    }

    @Override
    public void handleHistory(Account account) {
        String currentUser = account.getCurrentUser();
        System.out.println("This is your transaction history.");
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = null;

        while (true) {
            displayHistoryHelp();
            String historyCommand = ApplicationManager.commandScanner.nextLine();

            switch (historyCommand) {
                case "today":
                    startDate = endDate;
                    break;
                case "yesterday":
                    startDate = endDate.minus(1, ChronoUnit.DAYS);
                    break;
                case "week":
                    startDate = endDate.minus(1, ChronoUnit.WEEKS);
                    break;
                case "month":
                    startDate = endDate.minus(1, ChronoUnit.MONTHS);
                    break;
                case "year":
                    startDate = endDate.minus(1, ChronoUnit.YEARS);
                    break;
                case "all":
                    startDate = null;
                    endDate = null;
                    break;
                case "remove":
                    handleTransactionRemoval(currentUser);
                    return;
                case "return":
                    return;
                default:
                    System.out.println("'" + historyCommand + "' is not a valid command.");
                    continue;
            }

            account.readHistoryDetails(startDate, endDate);
            endDate = LocalDate.now();
        }
    }

    private void handleAccountRemoval(Account account) {
        System.out.println("Are you sure you'd like to delete your account?");
        System.out.println("Enter 'yes' to continue, otherwise 'return' to go back.");
        String confirmation = ApplicationManager.commandScanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            account.removeAccount();
        }
    }

    private void handleTransactionRemoval(String currentUser) {
        while (true) {
            System.out.println("Type 'remove' to delete a transaction.");
            System.out.println("Type 'return' to return to the previous menu.");

            String command = ApplicationManager.commandScanner.nextLine();
            switch(command) {
                case "remove":
                    System.out.println("Are you sure you'd like to delete a transaction?");
                    System.out.println("Enter 'yes' to continue, otherwise 'return' to go back.");
                    String confirmation = ApplicationManager.commandScanner.nextLine();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        Transaction.removeTransaction(currentUser);
                        return;
                    } else if (confirmation.equalsIgnoreCase("return")){
                        return;
                    } else {
                        System.out.println(confirmation + " is not a valid input.");
                    }
                    break;
                case "return":
                    return;
                default:
                    System.out.println("'" + command + "' is not a valid command. Write 'help' to get a list of commands.");
            }
        }
    }
}

package main.command;

import main.ApplicationInterface;
import main.ApplicationManager;
import main.Main;
import main.account.AccountRemover;
import main.menu.AccountMenu;
import main.menu.HistoryMenu;
import main.transaction.HistoryReader;
import main.account.Account;
import main.transaction.TransactionCreator;
import main.transaction.TransactionRemover;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CommandManager implements ApplicationInterface {
    private HistoryReader historyReader;
    private TransactionRemover transactionRemover;
    private AccountRemover accountRemover;
    private HistoryMenu historyMenu;
    private AccountMenu accountMenu;

    public CommandManager (Main main) {
        historyReader = new HistoryReader();
        transactionRemover = new TransactionRemover();
        accountRemover = new AccountRemover();
        historyMenu = new HistoryMenu(main);
        accountMenu = new AccountMenu(main);
    }

    @Override
    public void displayLoginHelp() {
        System.out.println("Enter 'register' to create an account.");
        System.out.println("Enter 'login' to log onto an account.");
        System.out.println("Enter 'stop' to quit the application.");
    }

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
    public void handleTransaction(Account account, TransactionCreator transaction) {
        System.out.println("Create a recurring or one-time transaction.");
        //account.loadUsers();
        transaction.createTransaction(account);
    }

    @Override
    public void handleAccount(Account account) {
        System.out.println("These are your account details.");
        account.readAccountDetails();

        accountMenu.displayHelp();

        while (true) {
            String accountCommand = ApplicationManager.commandScanner.nextLine();

            switch(accountCommand) {
                case "remove":
                    accountRemover.handleAccountRemoval(account);
                    return;
                case "return":
                    System.out.println("Returned to the previous menu. Write 'help' to get a list of commands.");
                    return;
                case "help":
                    accountMenu.displayHelp();
                    break;
                case "stop":
                    accountMenu.stopHandler();
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
            historyMenu.displayHelp();
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
                    transactionRemover.handleTransactionRemoval(currentUser);
                    return;
                case "return":
                    System.out.println("Returned to the previous menu. Write 'help' to get a list of commands.");
                    return;
                case "stop":
                    historyMenu.stopHandler();
                    return;
                default:
                    System.out.println("'" + historyCommand + "' is not a valid command.");
                    continue;
            }

            historyReader.readHistoryDetails(startDate, endDate);
            endDate = LocalDate.now();
        }
    }
}

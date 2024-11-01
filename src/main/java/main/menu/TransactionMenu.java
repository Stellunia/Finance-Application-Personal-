package main.menu;

import main.Main;
import main.account.Account;
import main.account.AccountDetails;
import main.command.StopCommand;

public class TransactionMenu implements MenuInterface {
    private StopCommand stopCommand;
    private Account account;

    public TransactionMenu(Main main) {
        stopCommand = new StopCommand(main);
    }

    @Override
    public void displayHelp() {
        AccountDetails currentAccount = account.userInfo.get(account.getCurrentUser());
        System.out.println(account.getCurrentUser() + " has currently got: " + currentAccount.getBalance() + " to spend.");
        System.out.println("Type 'create' to send a transaction to someone else. Does not work yet. DO NOT USE.");
        System.out.println("Type 'deposit' to add balance to your account.");
        System.out.println("Type 'send' to remove a current transaction.");
        System.out.println("Type 'return' to return to the previous menu.");
        System.out.println("stop - quit the application.");
    }

    @Override
    public void stopHandler() {
        stopCommand.run();
    }
}

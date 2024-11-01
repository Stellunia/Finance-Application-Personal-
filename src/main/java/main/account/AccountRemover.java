package main.account;

import main.ApplicationManager;

public class AccountRemover {

    public void handleAccountRemoval(Account account) {
        System.out.println("Are you sure you'd like to delete your account?");
        System.out.println("Enter 'yes' to continue, otherwise 'return' to go back.");
        String confirmation = ApplicationManager.commandScanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            account.removeAccount();
        }
    }
}

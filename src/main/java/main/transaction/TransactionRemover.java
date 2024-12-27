package main.transaction;

import main.ApplicationManager;

public class TransactionRemover {
    private TransactionCreator transactionCreator;
    private ApplicationManager applicationManager;

    // TODO: Change to Database-variant, removing column attributed to the username and serial ID
    public void handleTransactionRemoval(String currentUser) {
        while (true) {
            System.out.println("Type 'remove' to delete a transaction.");
            System.out.println("Type 'return' to return to the previous menu.");

            String command = applicationManager.commandScanner.nextLine();
            switch(command) {
                case "remove":
                    System.out.println("Are you sure you'd like to delete a transaction?");
                    System.out.println("Enter 'yes' to continue, otherwise 'return' to go back.");
                    String confirmation = ApplicationManager.commandScanner.nextLine();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        transactionCreator.removeTransaction(currentUser);
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

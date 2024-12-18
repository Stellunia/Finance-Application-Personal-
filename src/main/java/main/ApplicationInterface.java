package main;

import main.account.Account;
import main.transaction.TransactionCreator;

public interface ApplicationInterface {
    void displayLoginHelp();
    void displayHelp();
    void handleTransaction(Account account, TransactionCreator transaction);

    void handleAccount(Account account);
    void handleHistory(Account account);
}
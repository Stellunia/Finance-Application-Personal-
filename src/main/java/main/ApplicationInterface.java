package main;

import main.account.Account;
import main.transaction.Transaction;

public interface ApplicationInterface {
    void displayHelp();
    void displayHistoryHelp();
    void handleTransaction(Account account, Transaction transaction);

    void handleAccount(Account account);
    void handleHistory(Account account);
}
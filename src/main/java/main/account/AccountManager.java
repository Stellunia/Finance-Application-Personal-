package main.account;

import main.TransType;

import java.io.IOException;
import java.time.LocalDate;

public class AccountManager {
    private AccountWriter accountWriter;

    public AccountManager() {
        accountWriter = new AccountWriter();
    }

    // Function to add balance to an account.
    public double addBalanceInt(String addTitle, String addMessage, double addAmount, TransType addTransType, LocalDate addTransDate) {
        AccountDetails currentAccount = Account.userInfo.get(Account.getCurrentUser());
        //AccountDetails currentAccount = account.userInfo.get(username);

        double newBalance = currentAccount.getBalance() + addAmount;
        currentAccount.setBalance(newBalance);
        System.out.println("You've received " + addAmount + " to your account.");
        try { accountWriter.overwriteBalance(2, newBalance);
            accountWriter.writeHistory(addTitle, addMessage, addAmount, addTransType, addTransDate, true);
            System.out.println("The user file & transaction history has been updated.");
        } catch (IOException e) {
            System.out.println("Error updating the user and history files: " + e.getMessage());
        }
        return newBalance;
    }

    // Function to remove balance from an account.
    public double removeBalanceInt(String removeTitle, String removeMessage, double removeAmount, TransType removeTransType, LocalDate removeTransDate) {
        AccountDetails currentAccount = Account.userInfo.get(Account.getCurrentUser());
        String username = Account.getCurrentUser();
        //AccountDetails currentAccount = account.userInfo.get(username);

        if (currentAccount.balanceAmount(removeAmount)){
            double newBalance = currentAccount.getBalance() - removeAmount;
            currentAccount.setBalance(newBalance);
            System.out.println("You've transferred " + removeAmount + " to " + "'Pokémon Trainer'" + " from your account.");
            try { accountWriter.overwriteBalance(2, newBalance);
                accountWriter.writeHistory(removeTitle, removeMessage, removeAmount, removeTransType, removeTransDate, false);
                System.out.println("The user file has been updated with the new amount.");
            } catch (IOException e) {
                System.out.println("Error updating the balance amount in user file: " + e.getMessage());
            }
        } else {
            System.out.println(username + " does not have enough balance to cover this transaction.");
        }
        return currentAccount.getBalance();
    }
}

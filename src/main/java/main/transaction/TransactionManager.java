package main.transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private List<TransactionType> transactionType = new ArrayList<>();

    public void addTransactionType(TransactionType transaction){
        transactionType.add(transaction);
    }

    public List<TransactionType> getTransactionType() {
        return transactionType;
    }

    public boolean balanceAmount(double balance, double removeAmount) {
        return balance >= removeAmount;
    }
}

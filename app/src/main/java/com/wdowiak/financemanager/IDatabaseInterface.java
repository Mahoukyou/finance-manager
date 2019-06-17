package com.wdowiak.financemanager;

import java.util.ArrayList;

public interface IDatabaseInterface
{
    /* Transactions */
    ArrayList<Transaction> getTransactions();
    boolean newTransaction(Transaction transaction);

    
}

package com.wdowiak.financemanager;

import com.wdowiak.financemanager.data.Transaction;

import java.util.ArrayList;

public interface IDatabaseInterface
{
    /* Transactions */
    ArrayList<Transaction> getTransactions();
    boolean newTransaction(Transaction transaction);

    
}

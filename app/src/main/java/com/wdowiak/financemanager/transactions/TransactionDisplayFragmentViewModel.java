package com.wdowiak.financemanager.transactions;

import androidx.lifecycle.ViewModel;
import com.wdowiak.financemanager.data.Transaction;
import java.util.ArrayList;

public class TransactionDisplayFragmentViewModel extends ViewModel
{
    public ArrayList<Transaction> transactionsData;
    public TransactionsAdapter transactionsAdapter;
}

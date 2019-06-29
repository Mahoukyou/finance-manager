package com.wdowiak.financemanager.transactions;

import com.wdowiak.financemanager.commons.DisplayFragmentViewModel;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.transaction_sorting.TransactionSortSettings;
import com.wdowiak.financemanager.transactions_filter.TransactionFilter;

import java.security.PrivateKey;

// since we cannot get .class of templated type CommonViewModel<TYPE, AdAPTER>
// we have to make empty class derived from the templated one
public class TransactionDisplayFragmentViewModel
        extends DisplayFragmentViewModel<Transaction, TransactionsAdapter>
{
    private TransactionFilter transactionFilter;

    public TransactionDisplayFragmentViewModel()
    {
        sortSettings = new TransactionSortSettings();
    }

    public void setTransactionFilter(TransactionFilter filter)
    {
        this.transactionFilter = filter;
    }

    public TransactionFilter getTransactionFilter()
    {
        return transactionFilter;
    }

    @Override
    public TransactionSortSettings getSortSettings()
    {
        return (TransactionSortSettings) sortSettings;
    }
}
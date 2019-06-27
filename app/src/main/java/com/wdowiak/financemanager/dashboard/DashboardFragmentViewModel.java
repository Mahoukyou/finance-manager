package com.wdowiak.financemanager.dashboard;

import androidx.lifecycle.ViewModel;

import com.wdowiak.financemanager.transactions_filter.TransactionFilter;

public class DashboardFragmentViewModel extends ViewModel
{
    private TransactionFilter transactionFilter;

    void setTransactionFilter(TransactionFilter filter)
    {
        this.transactionFilter = filter;
    }

    TransactionFilter getTransactionFilter()
    {
        return transactionFilter;
    }

}

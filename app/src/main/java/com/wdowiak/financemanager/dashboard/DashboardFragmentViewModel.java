package com.wdowiak.financemanager.dashboard;

import androidx.lifecycle.ViewModel;

import com.wdowiak.financemanager.transactions_filter.TransactionFilter;

public class DashboardFragmentViewModel extends ViewModel
{
    private TransactionFilter transactionFilter;

    DataSpanSettings.EType spanType = DataSpanSettings.EType.Monthly;

    void setTransactionFilter(TransactionFilter filter)
    {
        this.transactionFilter = filter;
    }

    TransactionFilter getTransactionFilter()
    {
        return transactionFilter;
    }

    public DataSpanSettings.EType getSpanType()
    {
        return spanType;
    }

    public void setSpanType(DataSpanSettings.EType spanType)
    {
        this.spanType = spanType;
    }
}

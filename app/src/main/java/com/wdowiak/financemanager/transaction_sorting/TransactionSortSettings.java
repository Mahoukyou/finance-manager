package com.wdowiak.financemanager.transaction_sorting;

import com.wdowiak.financemanager.commons.CommonSortSettings;

// for now, might be expanded later
public class TransactionSortSettings extends CommonSortSettings
{
    public enum ESortBy
    {
        Date,
        SourceAccount,
        TargetAccount,
        Category,
        SourceCurrency,
        TargetCurrency,
        SourceGroup,
        TargetGroup,
        Description,
        Amount
    }

    ESortBy sortBy = ESortBy.Date;

    public ESortBy getSortBy()
    {
        return sortBy;
    }

    public void setSortBy(final ESortBy sortBy)
    {
        if(getSortBy() == sortBy)
        {
            toggleSortType();
            return;
        }

        this.sortBy = sortBy;
        setSortType(defaultSortType);
    }
}
package com.wdowiak.financemanager.transaction_sorting;

// for now, might be expanded later
public class TransactionSortSettings {
    public enum ESortType {
        Ascending,
        Descending
    }

    public enum ESortBy {
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

    public static ESortType defaultSortType = ESortType.Descending;

    ESortType sortType = defaultSortType;
    ESortBy sortBy = ESortBy.Date;

    public void setSortType(ESortType sortType) {
        this.sortType = sortType;
    }

    public void toggleSortType()
    {
        setSortType(getSortType() == ESortType.Ascending ? ESortType.Descending : ESortType.Ascending);
    }

    public ESortType getSortType()
    {
        return sortType;
    }

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
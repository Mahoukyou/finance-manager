package com.wdowiak.financemanager.transaction_sorting;

// for now, might be expanded later
public class TransactionSortSettings
{
    public enum ESortType
    {
        ASCENDING,
        DESCENDING
    }

    ESortType sortType = ESortType.DESCENDING;

    public void setSortType(ESortType sortType)
    {
        this.sortType = sortType;
    }

    public ESortType getSortType()
    {
        return sortType;
    }

    public String getSortingString()
    {
        String sortValue = getSortType() == ESortType.ASCENDING ? "true" : "false";
        return "SortAscending=" + sortValue;
    }
}
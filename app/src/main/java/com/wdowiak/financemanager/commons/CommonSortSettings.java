package com.wdowiak.financemanager.commons;

public class CommonSortSettings
{
    public enum ESortType
    {
        Ascending,
        Descending
    }

    public static ESortType defaultSortType = ESortType.Descending;
    ESortType sortType = defaultSortType;

    public void setSortType(ESortType sortType)
    {
        this.sortType = sortType;
    }

    public ESortType getSortType()
    {
        return sortType;
    }

    public void toggleSortType()
    {
        setSortType(getSortType() == ESortType.Ascending ? ESortType.Descending : ESortType.Ascending);
    }
}

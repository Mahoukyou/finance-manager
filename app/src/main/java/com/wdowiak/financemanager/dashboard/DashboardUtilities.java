package com.wdowiak.financemanager.dashboard;

import com.wdowiak.financemanager.dashboard.category.CategoriesData;
import com.wdowiak.financemanager.dashboard.category.CategoryData;
import com.wdowiak.financemanager.data.Transaction;

import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DashboardUtilities
{
    public static CategoriesData createCategoriesData(@NotNull ArrayList<Transaction> transactions)
    {
        CategoriesData categoriesData = new CategoriesData();
        categoriesData.categoriesHashMap = new HashMap<>();

        for(Transaction transaction : transactions)
        {
            if(categoriesData.categoriesHashMap.containsKey(transaction.getCategory().getId()))
            {
                CategoryData categoryData = categoriesData.categoriesHashMap.get(transaction.getCategory().getId());
                categoryData.amount += transaction.getAmount();
                ++categoryData.count;
            }
            else
            {
                CategoryData categoryData = new CategoryData();
                categoryData.amount = transaction.getAmount();
                categoryData.count = 1;
                categoryData.categoryName = transaction.getCategory().getName();

                categoriesData.categoriesHashMap.put(transaction.getCategory().getId(), categoryData);
            }

            categoriesData.totalAmount += transaction.getAmount();
            ++categoriesData.totalCount;
        }

        return categoriesData;
    }

    public static CategoriesData joinLowValueDataOverAmount(@NotNull CategoriesData categoriesData, double thresholdPercentage)
    {
        CategoriesData newCategoriesData = new CategoriesData();
        newCategoriesData.categoriesHashMap = new HashMap<>();

        final double thresholdAmount = categoriesData.totalAmount * (thresholdPercentage/100.0);
        final double thresholdCount = categoriesData.totalCount * (thresholdPercentage/100.0);

        Iterator it = categoriesData.categoriesHashMap.entrySet().iterator();

        CategoryData others = new CategoryData();
        others.categoryName = "Others";
        while (it.hasNext())
        {
            final Map.Entry pair = (Map.Entry)it.next();
            final CategoryData categoryData = (CategoryData) pair.getValue();

            if(categoryData.amount <= thresholdAmount)
            {
                others.amount += categoryData.amount;
                others.count += categoryData.count;
            }
            else
            {
                newCategoriesData.categoriesHashMap.put((Long) pair.getKey(), categoryData);
            }
        }

        if(others.count != 0)
        {
            newCategoriesData.categoriesHashMap.put(-1L, others);
        }

        return newCategoriesData;
    }

    public static TimelyData createTimelyData(
            @NotNull final ArrayList<Transaction> transactions,
            @NotNull final DataSpanSettings.EType span)
    {
        TimelyData timelyData = new TimelyData();
        timelyData.timelyDataHashMap = new HashMap<>();

        for(Transaction transaction : transactions)
        {
            final CustomDate date = new CustomDate(transaction.getDate(), span);
            if(timelyData.timelyDataHashMap.containsKey(date))
            {
                final SingleTimelyData singleTimelyData = timelyData.timelyDataHashMap.get(date);
                singleTimelyData.amount += transaction.getAmount();
                ++singleTimelyData.transactionCount;
            }
            else
            {
                final SingleTimelyData singleTimelyData = new SingleTimelyData();
                singleTimelyData.amount = transaction.getAmount();
                singleTimelyData.transactionCount = 1;
                singleTimelyData.date = date;

                timelyData.timelyDataHashMap.put(date, singleTimelyData);
            }

            timelyData.totalAmount += transaction.getAmount();
            ++timelyData.totalTransactions;
        }

        timelyData.sortedTimelyData = new ArrayList<>();
        // No time for efficiency atm
        timelyData.sortedTimelyData.addAll(timelyData.timelyDataHashMap.values());
        timelyData.sortedTimelyData.sort((lhs, rhs) ->
                ObjectUtils.compare(lhs.date, rhs.date));

        return timelyData;
    }

    private void sortHashMap()
    {

    }
}

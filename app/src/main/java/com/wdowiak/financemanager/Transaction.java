package com.wdowiak.financemanager;

import org.json.JSONException;
import org.json.JSONObject;

public final class Transaction
{
    public static final Transaction createFromJSONObject(final JSONObject jsonObject) throws JSONException
    {
        final Transaction transaction = new Transaction();

        // todo redo obviously, just for test if it works
        transaction.id = jsonObject.getLong("TransactionId");
        if(!jsonObject.isNull("SourceAccount"))
        {
            transaction.sourceAccountName = jsonObject.getJSONObject("SourceAccount").getString("Name");
        }
        if(!jsonObject.isNull("TargetAccount"))
        {
            transaction.targetAccountName = jsonObject.getJSONObject("TargetAccount").getString("Name");
        }

        if(!jsonObject.isNull("Category"))
        {
            transaction.categoryName = jsonObject.getJSONObject("Category").getString("Name");
        }

        transaction.amount = jsonObject.getDouble("Amount");
        transaction.description = jsonObject.getString("Description");

        if(!jsonObject.isNull("TransactionStatus"))
        {
            transaction.statusName = jsonObject.getJSONObject("TransactionStatus").getString("Name");
        }

        return transaction;
    }

    public final long getId()
    {
        return id;
    }

    public final String getSourceAccountName()
    {
        return sourceAccountName;
    }

    public final String getTargetAccountName()
    {
        return targetAccountName;
    }

    public final String getCategoryName()
    {
        return categoryName;
    }

    public final double getAmount()
    {
        return amount;
    }

    public final String getDescription()
    {
        return description;
    }

    public final String getStatusName()
    {
        return statusName;
    }

    long id;
    String sourceAccountName;// todo
    String targetAccountName; // todo
    String categoryName;
    double amount;
    String description;
    String statusName;
    // Date todo
    // Status
    // Category
}
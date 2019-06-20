package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

public final class Transaction
{
    public static final Transaction createFromJSONObject(final JSONObject jsonObject) throws JSONException
    {
        final Transaction transaction = new Transaction();

        // todo redo obviously, just for test if it works
        transaction.id = jsonObject.getLong("TransactionId");
        transaction.amount = jsonObject.getDouble("Amount");
        transaction.description = jsonObject.getString("Description");

        if(!jsonObject.isNull("SourceAccount"))
        {
            transaction.sourceAccount = Account.createFromJSONObject(jsonObject.getJSONObject("SourceAccount"));
        }

        if(!jsonObject.isNull("TargetAccount"))
        {
            transaction.targetAccount = Account.createFromJSONObject(jsonObject.getJSONObject("TargetAccount"));
        }

        if(!jsonObject.isNull("Category"))
        {
            transaction.category = Category.createFromJSONObject(jsonObject.getJSONObject("Category"));
        }

        if(!jsonObject.isNull("TransactionStatus"))
        {
            transaction.transactionStatus = TransactionStatus.createFromJSONObject(jsonObject.getJSONObject("TransactionStatus"));
        }

        return transaction;
    }

    @Contract(pure = true)
    public final long getId()
    {
        return id;
    }

    @Contract(pure = true)
    public final double getAmount()
    {
        return amount;
    }

    @Contract(pure = true)
    public final String getDescription()
    {
        return description;
    }

    @Contract(pure = true)
    public final Account getSourceAccount()
    {
        return sourceAccount;
    }

    @Contract(pure = true)
    public final Account getTargetAccount()
    {
        return targetAccount;
    }

    @Contract(pure = true)
    public final Category getCategory()
    {
        return category;
    }

    @Contract(pure = true)
    public final TransactionStatus getStatus()
    {
        return transactionStatus;
    }

    long id;
    double amount;
    String description;

    Account sourceAccount;
    Account targetAccount;
    Category category;
    TransactionStatus transactionStatus;
}
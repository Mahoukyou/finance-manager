package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

public class TransactionStatus
{
    public static final TransactionStatus createFromJSONObject(final JSONObject jsonObject) throws JSONException
    {
        final TransactionStatus transactionStatus = new TransactionStatus();

        transactionStatus.id = jsonObject.getLong("TransactionStatusId");
        transactionStatus.name = jsonObject.getString("Name");

        return transactionStatus;
    }

    @Contract(pure = true)
    public final long getId()
    {
        return id;
    }

    @Contract(pure = true)
    public final String getName()
    {
        return name;
    }

    long id;
    String name;
}

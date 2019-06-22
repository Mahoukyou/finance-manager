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

    // todo, at least for now for easier array search
    @Contract(value = "null -> false", pure = true)
    public final boolean equals(Object object)
    {
        if(!(object instanceof TransactionStatus))
        {
            return false;
        }

        TransactionStatus transactionStatus = (TransactionStatus)object;
        return getId() == transactionStatus.getId();
    }

    public final int hashCode()
    {
        Long lId = id;
        return lId.hashCode();
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

package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TransactionStatus implements IItem, Comparable<TransactionStatus>
{
    private TransactionStatus() {}

    public TransactionStatus(@NotNull final String name)
    {
        this.name = name;
    }

    public TransactionStatus(long id, @NotNull final String name)
    {
        this(name);
        this.id = id;
    }

    public static final TransactionStatus createFromJSONObject(@NotNull final JSONObject jsonObject) throws JSONException
    {
        final TransactionStatus transactionStatus = new TransactionStatus();

        transactionStatus.id = jsonObject.getLong("TransactionStatusId");
        transactionStatus.name = jsonObject.getString("Name");

        return transactionStatus;
    }

    @NotNull
    @Contract(" -> new")
    @Override
    public final JSONObject createJSONObject()
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", getName());
        params.put("Ordinal", "4");
        params.put("Class", "tralala");
        params.put("Color", "aaa");
        // todo, do we need the color && ordinal since its not required in api??

        return new JSONObject(params);
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

    @Override
    public int compareTo(TransactionStatus transactionStatus)
    {
        return getName().compareTo(transactionStatus.getName());
    }

    @Contract(pure = true)
    @Override
    public final long getId()
    {
        return id;
    }

    @Contract(pure = true)
    @Override
    public final String getName()
    {
        return name;
    }

    long id;
    String name;
}

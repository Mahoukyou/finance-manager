package com.wdowiak.financemanager.data;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Account implements IItem, Comparable<Account>
{
    private Account() { }

    public Account(
            @NotNull final String name,
            @NotNull final Group group,
            @NotNull final Currency currency,
            @NotNull final double startingAmount)
    {
        this.name = name;
        this.group = group;
        this.currency = currency;
        this.startingAmount = startingAmount;
    }

    public Account(
            final long id,
            @NotNull final String name,
            @NotNull final Group group,
            @NotNull final Currency currency,
            @NotNull final double startingAmount)
    {
        this(name, group, currency, startingAmount);
        this.id = id;
    }

    public static final Account createFromJSONObject(@NotNull final JSONObject jsonObject) throws JSONException
    {
        final Account account = new Account();

        account.id = jsonObject.getLong("AccountId");
        account.name = jsonObject.getString("Name");
        account.startingAmount = jsonObject.getDouble("StartAmount");

        if (!jsonObject.isNull("Group"))
        {
            account.group = Group.createFromJSONObject(jsonObject.getJSONObject("Group"));
        }

        if (!jsonObject.isNull("Currency"))
        {
            account.currency = Currency.createFromJSONObject(jsonObject.getJSONObject("Currency"));
        }

        return account;
    }

    @NotNull
    @Contract(" -> new")
    @Override
    public final JSONObject createJSONObject()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("name", getName());

            final JSONObject groupJSONObject = new JSONObject();
            groupJSONObject.put("groupId", getGroup().getId());
            jsonObject.put("group", groupJSONObject);

            final JSONObject currencyJSONObject = new JSONObject();
            currencyJSONObject.put("currencyId", getCurrency().getId());
            jsonObject.put("currency", currencyJSONObject);

            jsonObject.put("startAmount", "0"); // unused atm

        }
        catch (JSONException ex)
        {
            /// .... gotta catch the json or it wont compile...
            throw new RuntimeException(ex);
        }

        return jsonObject;
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

    @Contract(pure = true)
    public final Group getGroup()
    {
        return group;
    }

    @Contract(pure = true)
    public final Currency getCurrency()
    {
        return currency;
    }

    // Is it even useful? Api doesnt use past storing the starting amount
    @Contract(pure = true)
    public final double getStartingAmount()
    {
        return startingAmount;
    }

    // todo, at least for now for easier array search
    @Contract(value = "null -> false", pure = true)
    public final boolean equals(Object object)
    {
        if (!(object instanceof Account))
        {
            return false;
        }

        Account account = (Account) object;
        return getId() == account.getId();
    }

    @Override
    public int compareTo(final Account account)
    {
        return getName().compareTo(account.getName());
    }

    public final int hashCode()
    {
        Long lId = id;
        return lId.hashCode();
    }

    long id = -1;
    String name = null; // todo not null
    Group group = null; // todo not null
    Currency currency = null; // todo ,not nulls
    double startingAmount = 0.0;
}

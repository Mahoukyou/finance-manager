package com.wdowiak.financemanager.data;


import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

public class Account
{
    public static final Account createFromJSONObject(final JSONObject jsonObject) throws JSONException
    {
        final Account account = new Account();

        account.id = jsonObject.getLong("AccountId");
        account.name = jsonObject.getString("Name");
        account.startingAmount = jsonObject.getDouble("StartAmount");

        if(!jsonObject.isNull("Group"))
        {
            account.group = Group.createFromJSONObject(jsonObject.getJSONObject("Group"));
        }

        if(!jsonObject.isNull("Currency"))
        {
            account.currency = Currency.createFromJSONObject(jsonObject.getJSONObject("Currency"));
        }

        return account;
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

    @Contract(pure = true)
    public final double getStartingAmount()
    {
        return startingAmount;
    }

    // todo, at least for now for easier array search
    @Contract(value = "null -> false", pure = true)
    public final boolean equals(Object object)
    {
        if(!(object instanceof Account))
        {
            return false;
        }

        Account account = (Account)object;
        return getId() == account.getId();
    }

    public final int hashCode()
    {
        Long lId = id;
        return lId.hashCode();
    }

    long id = -1;
    String name = null;
    Group group = null;
    Currency currency = null;
    double startingAmount = 0.0;

}

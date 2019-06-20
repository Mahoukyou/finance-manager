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
            account.groupName = jsonObject.getJSONObject("Group").getString("Name");
        }

        if(!jsonObject.isNull("Currency"))
        {
            account.currencyName = jsonObject.getJSONObject("Currency").getString("Name");
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
    public final String getGroupName()
    {
        return groupName;
    }

    @Contract(pure = true)
    public final String getCurrencyName()
    {
        return currencyName;
    }

    @Contract(pure = true)
    public final double getStartingAmount()
    {
        return startingAmount;
    }

    long id;
    String name;
    String groupName;
    String currencyName;
    double startingAmount;

}

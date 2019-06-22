package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

public class Currency
{
    public static final Currency createFromJSONObject(final JSONObject jsonObject) throws JSONException
    {
        final Currency currency = new Currency();

        currency.id = jsonObject.getLong("CurrencyId");
        currency.name = jsonObject.getString("Name");
        currency.symbol = jsonObject.getString("Symbol");
        currency.acronym = jsonObject.getString("Acronym");

        return currency;
    }

    // todo, at least for now for easier array search
    @Contract(value = "null -> false", pure = true)
    public final boolean equals(Object object)
    {
        if(!(object instanceof Currency))
        {
            return false;
        }

        Currency currency = (Currency)object;
        return getId() == currency.getId();
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

    @Contract(pure = true)
    public final String getSymbol()
    {
        return symbol;
    }

    @Contract(pure = true)
    public final String getAcronym()
    {
        return acronym;
    }

    long id;
    String name;
    String symbol;
    String acronym;
    //todo, wtf was prefix boolean?
}

package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Currency
{
    private Currency() {}

    public Currency(final String name, final String symbol, final String acronym, final boolean prefix)
    {
        this.name = name;
        this.symbol = symbol;
        this.acronym = acronym;
        this.prefix = prefix;
    }

    public Currency(long id, final String name, final String symbol, final String acronym, final boolean prefix)
    {
        this(name, symbol, acronym, prefix);
        this.id = id;
    }

    public static final Currency createFromJSONObject(@NotNull final JSONObject jsonObject) throws JSONException
    {
        final Currency currency = new Currency();

        currency.id = jsonObject.getLong("CurrencyId");
        currency.name = jsonObject.getString("Name");
        currency.symbol = jsonObject.getString("Symbol");
        currency.acronym = jsonObject.getString("Acronym");
        currency.prefix = jsonObject.getBoolean("Prefix"); // todo, might need fix -- test it

        return currency;
    }

    @NotNull
    @Contract(" -> new")
    public final JSONObject createJSONObject()
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", getName());
        params.put("symbol", getSymbol());
        params.put("acronym", getAcronym());
        params.put("prefix", getPrefix() ? "1" : "0");

        return new JSONObject(params);
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

    @Contract(pure = true)
    public final boolean getPrefix()
    {
        return prefix;
    }

    long id;
    String name;
    String symbol;
    String acronym;
    boolean prefix;
}
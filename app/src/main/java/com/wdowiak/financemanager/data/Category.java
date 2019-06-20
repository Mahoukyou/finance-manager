package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

public class Category
{
    public static final Category createFromJSONObject(final JSONObject jsonObject) throws JSONException
    {
        final Category category = new Category();

        category.id = jsonObject.getLong("CategoryId");
        category.name = jsonObject.getString("Name");

        return category;
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

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

    // todo, at least for now for easier array search
    @Contract(value = "null -> false", pure = true)
    public final boolean equals(Object object)
    {
        if(!(object instanceof Category))
        {
            return false;
        }

        Category category = (Category) object;
        return getId() == category.getId();
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

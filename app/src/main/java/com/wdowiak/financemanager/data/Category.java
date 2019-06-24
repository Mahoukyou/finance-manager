package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Category
{
    private Category() {}

    public Category(@NotNull String name)
    {
        this.name = name;
    }

    public Category(long id, @NotNull String name)
    {
        this.id = id;
        this.name = name;
    }

    public static final Category createFromJSONObject(@NotNull final JSONObject jsonObject) throws JSONException
    {
        final Category category = new Category();

        category.id = jsonObject.getLong("CategoryId");
        category.name = jsonObject.getString("Name");

        return category;
    }


    @NotNull
    @Contract(" -> new")
    public final JSONObject createJSONObject()
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", getName());
        params.put("rootid", "0");

        return new JSONObject(params);
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

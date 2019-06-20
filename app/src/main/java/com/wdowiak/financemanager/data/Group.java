package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

public class Group
{
    public static final Group createFromJSONObject(final JSONObject jsonObject) throws JSONException
    {
        final Group group = new Group();

        group.id = jsonObject.getLong("GroupId");
        group.name = jsonObject.getString("Name");
        group.info = jsonObject.getString("Info");

        return group;
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
    public final String getInfo()
    {
        return info;
    }

    long id;
    String name;
    String info;
}

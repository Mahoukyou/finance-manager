package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class Group
{

    public static final Group createFromJSONObject(@NotNull final JSONObject jsonObject) throws JSONException
    {
        final Group group = new Group();

        group.id = jsonObject.getLong("GroupId");
        group.name = jsonObject.getString("Name");
        group.info = jsonObject.getString("Info");

        return group;
    }

    // todo, at least for now for easier array search
    @Contract(value = "null -> false", pure = true)
    public final boolean equals(Object object)
    {
        if(!(object instanceof Group))
        {
            return false;
        }

        Group group = (Group) object;
        return getId() == group.getId();
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
    public final String getInfo()
    {
        return info;
    }

    long id;
    String name;
    String info;
}

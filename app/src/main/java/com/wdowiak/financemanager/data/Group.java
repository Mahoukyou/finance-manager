package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Group
{
    private Group() {}

    public Group(@NotNull String name, @Nullable String description)
    {
        this.name = name;
        this.info = description;
    }

    public Group(long id, @NotNull String name, @Nullable String description)
    {
        this.id = id;
        this.name = name;
        this.info = description;
    }

    public static final Group createFromJSONObject(@NotNull final JSONObject jsonObject) throws JSONException
    {
        final Group group = new Group();

        group.id = jsonObject.getLong("GroupId");
        group.name = jsonObject.getString("Name");
        group.info = jsonObject.getString("Info");

        return group;
    }

    public final JSONObject createJSONObject()
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", getName());
        params.put("info", getInfo());
        params.put("ordinal", "0");

        return new JSONObject(params);
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

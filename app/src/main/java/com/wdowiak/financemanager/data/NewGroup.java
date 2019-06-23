package com.wdowiak.financemanager.data;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NewGroup
{
    public NewGroup(@NotNull String name, @Nullable String description)
    {
        this.name = name;
        this.info = description;
    }

    public final JSONObject createJSONObject()
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", getName());
        params.put("info", getInfo());
        params.put("ordinal", "0");


        return new JSONObject(params);
    }

    @Contract(pure = true)
    public final boolean isValid()
    {
        return !name.isEmpty();
    }

    @Contract(pure = true)
    @NotNull
    public final String getName()
    {
        return name;
    }

    @Contract(pure = true)
    @Nullable
    public final String getInfo()
    {
        return info;
    }

    @NonNull
    String name;

    @Nullable
    String info;
}

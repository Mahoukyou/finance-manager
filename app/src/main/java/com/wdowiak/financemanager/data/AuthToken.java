package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AuthToken
{
    public AuthToken(final String authToken)
    {
        this.authToken = authToken;
    }

    @Contract(pure = true)
    public final String getAuthToken()
    {
        return authToken;
    }

    @NotNull
    @Contract(pure = true)
    public final String getFullAuthToken()
    {
        return prefix + " " + authToken;
    }

    @Contract(pure = true)
    public final boolean isTokenValid()
    {
        return prefix != null && !prefix.isEmpty();
    }


    final static String prefix = "Bearer";

    @Nullable
    String authToken;
}

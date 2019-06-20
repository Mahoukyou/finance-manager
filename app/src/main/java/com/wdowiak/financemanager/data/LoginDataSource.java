package com.wdowiak.financemanager.data;

import android.util.Log;

import com.wdowiak.financemanager.api.AuthApi;
import com.wdowiak.financemanager.api.NetworkManager;

import org.jetbrains.annotations.Contract;

import java.io.IOException;

public class LoginDataSource
{
    public final Result<LoggedInUser> login(final String username, final String password)
    {
        final Result<AuthToken> result = AuthApi.authUser(username, password);
        if (result instanceof Result.Success)
        {
            authToken = ((Result.Success<AuthToken>) result).getData();
            if(authToken == null || !authToken.isTokenValid())
            {
                authToken = null;
                return new Result.Error(new Exception("Cannot log in, invalid auth token"));
            }
        }

        final Result<LoggedInUser> userResult = AuthApi.getUser(username);
        if (userResult instanceof Result.Success)
        {
            return userResult;
        }

        // Couldn't query the user
        logout();

        return userResult;
    }

    public final void logout()
    {
        authToken = null;
    }

    @Contract(pure = true)
    public final AuthToken getAuthToken()
    {
        return authToken;
    }

    private AuthToken authToken;
}

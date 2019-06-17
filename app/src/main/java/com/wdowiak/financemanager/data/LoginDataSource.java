package com.wdowiak.financemanager.data;

import android.util.Log;

import com.wdowiak.financemanager.api.AuthApi;
import com.wdowiak.financemanager.api.NetworkManager;

import java.io.IOException;

public class LoginDataSource
{
    public final Result<LoggedInUser> login(String username, String password)
    {
        AuthApi authApi = new AuthApi(NetworkManager.getInstance().getContext(), null);

        Result<AuthToken> result = authApi.authUser(username, password);
        if (result instanceof Result.Success)
        {
            AuthToken data = ((Result.Success<AuthToken>) result).getData();
            authToken = data.getFullAuthToken();
            LoggedInUser newUSer = new LoggedInUser(0, data.getFullAuthToken(), "", "", "");
            return new Result.Success(newUSer);
        }

        return null;

    }

    public final void logout()
    {
        authToken = null;
    }

    public final String getAuthToken()
    {
        return authToken;
    }

    private String authToken;
}

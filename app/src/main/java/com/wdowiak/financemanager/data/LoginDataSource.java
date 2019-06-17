package com.wdowiak.financemanager.data;

import com.wdowiak.financemanager.api.AuthApi;

import java.io.IOException;

public class LoginDataSource
{
    public final Result<LoggedInUser> login(String username, String password)
    {

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

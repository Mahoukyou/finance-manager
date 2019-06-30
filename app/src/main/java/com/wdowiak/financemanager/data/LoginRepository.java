package com.wdowiak.financemanager.data;

import android.util.Log;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository
{
    private static volatile LoginRepository instance;

    private LoginRepository(final LoginDataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public final static LoginRepository getInstance(final LoginDataSource dataSource)
    {
        if (instance == null)
        {
            instance = new LoginRepository(dataSource);
        }

        return instance;
    }

    public final static LoginRepository getInstance()
    {
        if(instance == null)
        {
            throw new RuntimeException("Login repository instance is not valid");
        }

        return instance;
    }


    public final boolean isLoggedIn()
    {
        return user != null && dataSource.getAuthToken() != null && dataSource.getAuthToken().isTokenValid();
    }

    public final AuthToken getAuthToken()
    {
        return dataSource.getAuthToken();
    }

    public final void logout()
    {
        user = null;
        dataSource.logout();
    }

    public final LoggedInUser getLoggedInUser()
    {
        return user;
    }

    private final void setLoggedInUser(final LoggedInUser user)
    {
        this.user = user;
    }

    public Result<LoggedInUser> login(final String username, final String password)
    {
        logout();
        Result<LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success)
        {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }

        return result;
    }

    private LoginDataSource dataSource;
    private LoggedInUser user = null;
}

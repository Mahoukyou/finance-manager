package com.wdowiak.financemanager.ui.login;

import androidx.annotation.Nullable;

import com.wdowiak.financemanager.data.LoggedInUser;

class LoginResult
{
    @Nullable
    private LoggedInUser success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error)
    {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUser success)
    {
        this.success = success;
    }

    @Nullable
    LoggedInUser getSuccess()
    {
        return success;
    }

    @Nullable
    Integer getError()
    {
        return error;
    }
}

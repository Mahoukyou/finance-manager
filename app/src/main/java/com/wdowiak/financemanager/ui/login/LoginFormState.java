package com.wdowiak.financemanager.ui.login;

import androidx.annotation.Nullable;

class LoginFormState
{
    @Nullable
    private Integer loginError;
    @Nullable
    private Integer passwordError;

    private boolean isDataValid;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError)
    {
        this.loginError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid)
    {
        this.loginError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError()
    {
        return loginError;
    }

    @Nullable
    Integer getPasswordError()
    {
        return passwordError;
    }

    boolean isDataValid()
    {
        return isDataValid;
    }
}

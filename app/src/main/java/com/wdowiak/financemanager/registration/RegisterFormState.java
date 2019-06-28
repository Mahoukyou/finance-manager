package com.wdowiak.financemanager.registration;

import androidx.annotation.Nullable;

public class RegisterFormState
{
    @Nullable
    private Integer loginError;

    @Nullable
    private Integer firstNameError;

    @Nullable
    private Integer lastNameError;

    @Nullable
    private Integer emailError;

    @Nullable
    private Integer passwordError;

    @Nullable
    private Integer repeatPasswordError;

    public RegisterFormState(
            @Nullable Integer loginError,
            @Nullable Integer firstNameError,
            @Nullable Integer lastNameError,
            @Nullable Integer emailError,
            @Nullable Integer passwordError,
            @Nullable Integer repeatPasswordError)
    {
        this.loginError = loginError;
        this.firstNameError = firstNameError;
        this.lastNameError = lastNameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.repeatPasswordError = repeatPasswordError;
    }

    @Nullable
    public Integer getLoginError()
    {
        return loginError;
    }

    @Nullable
    public Integer getFirstNameError()
    {
        return firstNameError;
    }

    @Nullable
    public Integer getLastNameError()
    {
        return lastNameError;
    }

    @Nullable
    public Integer getEmailError()
    {
        return emailError;
    }

    @Nullable
    public Integer getPasswordError()
    {
        return passwordError;
    }

    @Nullable
    public Integer getRepeatPasswordError()
    {
        return repeatPasswordError;
    }

    boolean isDataValid()
    {
        return loginError == null &&
                firstNameError == null &&
                lastNameError == null &&
                emailError == null &&
                passwordError == null &&
                repeatPasswordError == null;
    }
}

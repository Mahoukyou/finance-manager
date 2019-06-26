package com.wdowiak.financemanager.accounts;

import androidx.annotation.Nullable;

import com.wdowiak.financemanager.commons.CommonAddEditFormState;

public class AccountAddEditFormState
        extends CommonAddEditFormState
{
    @Nullable
    private Integer nameError;

    @Nullable
    private Integer groupError;

    @Nullable
    private Integer currencyError;

    AccountAddEditFormState(@Nullable Integer nameError, @Nullable Integer groupError, @Nullable Integer currencyError)
    {
        this.nameError = nameError;
        this.groupError = groupError;
        this.currencyError = currencyError;
    }

    public Integer getNameError()
    {
        return nameError;
    }

    public Integer getGroupError()
    {
        return groupError;
    }

    public Integer getCurrencyError()
    {
        return currencyError;
    }

    @Override
    public boolean isDataValid()
    {
        return getNameError() == null && getGroupError() == null && getCurrencyError() == null;
    }
}

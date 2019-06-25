package com.wdowiak.financemanager.transaction_statuses;

import androidx.annotation.Nullable;

import com.wdowiak.financemanager.commons.CommonAddEditFormState;

public class TransactionStatusAddEditFormState
        extends CommonAddEditFormState
{
    @Nullable
    private Integer nameError;

    TransactionStatusAddEditFormState(@Nullable Integer nameError)
    {
        this.nameError = nameError;
    }

    public Integer getNameError()
    {
        return nameError;
    }


    @Override
    public boolean isDataValid()
    {
        return getNameError() == null;
    }
}

package com.wdowiak.financemanager.transactions;

import androidx.annotation.Nullable;

import com.wdowiak.financemanager.commons.CommonAddEditFormState;

public class TransactionAddEditFormState
        extends CommonAddEditFormState
{
    @Nullable
    private Integer amountError;

    @Nullable
    private Integer descriptionError;

    @Nullable
    private Integer sourceAccountError;

    @Nullable
    private Integer targetAccountError;

    @Nullable
    private Integer categoryError;

    @Nullable
    private Integer statusError;

    @Nullable Integer dateError;

    TransactionAddEditFormState(
            @Nullable Integer amountError,
            @Nullable Integer descriptionError,
            @Nullable Integer sourceAccountError,
            @Nullable Integer targetAccountError,
            @Nullable Integer categoryError,
            @Nullable Integer statusError,
            @Nullable Integer dateError)
    {
        this.amountError = amountError;
        this.descriptionError = descriptionError;
        this.sourceAccountError = sourceAccountError;
        this.targetAccountError = targetAccountError;
        this.categoryError = categoryError;
        this.statusError = statusError;
        this.dateError = dateError;
    }

    @Nullable
    public Integer getAmountError()
    {
        return amountError;
    }

    @Nullable
    public Integer getDescriptionError()
    {
        return descriptionError;
    }

    @Nullable
    public Integer getSourceAccountError()
    {
        return sourceAccountError;
    }

    @Nullable
    public Integer getTargetAccountError()
    {
        return targetAccountError;
    }

    @Nullable
    public Integer getCategoryError()
    {
        return categoryError;
    }

    @Nullable
    public Integer getStatusError()
    {
        return statusError;
    }

    @Nullable
    public Integer getDateError()
    {
        return dateError;
    }

    @Override
    public boolean isDataValid()
    {
        return  getAmountError() == null &&
                getDescriptionError() == null &&
                getSourceAccountError() == null &&
                getTargetAccountError() == null &&
                getCategoryError() == null &&
                getStatusError() == null &&
                getDateError() == null;
    }
}

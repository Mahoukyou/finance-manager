package com.wdowiak.financemanager.currencies;

import androidx.annotation.Nullable;

import com.wdowiak.financemanager.commons.CommonAddEditFormState;

public class CurrencyAddEditFormState
        extends CommonAddEditFormState
{
    @Nullable
    private Integer nameError;

    @Nullable
    private Integer acronymError;

    @Nullable
    private Integer symbolError;

    CurrencyAddEditFormState(@Nullable Integer nameError, @Nullable Integer acronymError, @Nullable Integer symbolError)
    {
        this.nameError = nameError;
        this.acronymError = acronymError;
        this.symbolError = symbolError;
    }

    public Integer getNameError()
    {
        return nameError;
    }

    @Nullable
    public Integer getAcronymError()
    {
        return acronymError;
    }

    @Nullable
    public Integer getSymbolError()
    {
        return symbolError;
    }

    @Override
    public boolean isDataValid()
    {
        return getNameError() == null && getAcronymError() == null && getSymbolError() == null;
    }
}

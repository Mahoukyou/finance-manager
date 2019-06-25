package com.wdowiak.financemanager.categories;

import androidx.annotation.Nullable;

import com.wdowiak.financemanager.commons.CommonAddEditFormState;

public class CategoryAddEditFormState
        extends CommonAddEditFormState
{
    @Nullable
    private Integer nameError;

    CategoryAddEditFormState(@Nullable Integer nameError)
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

package com.wdowiak.financemanager.groups;

import androidx.annotation.Nullable;

import com.wdowiak.financemanager.commons.CommonAddEditFormState;

public class GroupAddEditFormState
        extends CommonAddEditFormState
{
    @Nullable
    private Integer nameError;

    @Nullable
    private Integer descriptionError;

    GroupAddEditFormState(@Nullable Integer nameError, @Nullable Integer descriptionError)
    {
        this.nameError = nameError;
        this.descriptionError = descriptionError;
    }

    public Integer getNameError()
    {
        return nameError;
    }

    public Integer getDescriptionError()
    {
        return descriptionError;
    }

    @Override
    public boolean isDataValid()
    {
        return getNameError() == null && getDescriptionError() == null;
    }
}

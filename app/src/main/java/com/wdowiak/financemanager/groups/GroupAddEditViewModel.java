package com.wdowiak.financemanager.groups;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.commons.CommonAddEditViewModel;
import com.wdowiak.financemanager.data.Group;

public class GroupAddEditViewModel
    extends CommonAddEditViewModel<Group, GroupAddEditFormState>
{
    public boolean hasDataChanged()
    {
        // todo
        if(getQueriedItem() == null)
        {
            // check if something chagned?
        }
        return false;
    }

    public void dataChanged(String name, String description)
    {
        Integer nameError = null;
        Integer descriptionError = null;

        if(name == null || name.isEmpty())
        {
            nameError = R.string.cannot_be_empty;
        }

        if(description == null || description.isEmpty())
        {
            descriptionError = R.string.cannot_be_empty;
        }

        setFormState(new GroupAddEditFormState(nameError, descriptionError));
    }
}

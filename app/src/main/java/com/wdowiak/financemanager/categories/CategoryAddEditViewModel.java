package com.wdowiak.financemanager.categories;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.commons.CommonAddEditViewModel;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.Group;

public class CategoryAddEditViewModel
    extends CommonAddEditViewModel<Category, CategoryAddEditFormState>
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

    public void dataChanged(String name)
    {
        Integer nameError = null;

        if(name == null || name.isEmpty())
        {
            nameError = R.string.cannot_be_empty;
        }

        setFormState(new CategoryAddEditFormState(nameError));
    }
}

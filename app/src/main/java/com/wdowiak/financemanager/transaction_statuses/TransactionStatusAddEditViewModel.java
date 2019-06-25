package com.wdowiak.financemanager.transaction_statuses;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.commons.CommonAddEditViewModel;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.TransactionStatus;

public class TransactionStatusAddEditViewModel
    extends CommonAddEditViewModel<TransactionStatus, TransactionStatusAddEditFormState>
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
            nameError = R.string.app_name;
        }

        setFormState(new TransactionStatusAddEditFormState(nameError));
    }
}

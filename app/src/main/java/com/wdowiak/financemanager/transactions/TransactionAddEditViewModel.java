package com.wdowiak.financemanager.transactions;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.categories.AdapterDataModel;
import com.wdowiak.financemanager.commons.CommonAddEditViewModel;
import com.wdowiak.financemanager.commons.NameAdapter;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.Currency;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.data.TransactionStatus;

public class TransactionAddEditViewModel
    extends CommonAddEditViewModel<Transaction, TransactionAddEditFormState>
{
    // We could use same adapter for accounts, but we would need another data, its easier this way, than deriving yet another class
    AdapterDataModel<Account, NameAdapter<Account>> sourceAccountsDataModel = new AdapterDataModel<>();
    AdapterDataModel<Account, NameAdapter<Account>> targetAccountsDataModel = new AdapterDataModel<>();
    AdapterDataModel<Category, NameAdapter<Category>> categoriesAdapterDataModel = new AdapterDataModel<>();
    AdapterDataModel<TransactionStatus, NameAdapter<TransactionStatus>> statusesAdapterDataModel = new AdapterDataModel<>();

    public AdapterDataModel<Account, NameAdapter<Account>> getSourceAccountsDataModel()
    {
        return sourceAccountsDataModel;
    }

    public AdapterDataModel<Account, NameAdapter<Account>> getTargetAccountsDataModel()
    {
        return targetAccountsDataModel;
    }

    public AdapterDataModel<Category, NameAdapter<Category>> getCategoriesAdapterDataModel()
    {
        return categoriesAdapterDataModel;
    }

    public AdapterDataModel<TransactionStatus, NameAdapter<TransactionStatus>> getStatusesAdapterDataModel()
    {
        return statusesAdapterDataModel;
    }

    public boolean hasDataChanged()
    {
        // todo
        if(getQueriedItem() == null)
        {
            // check if something chagned?
        }
        return false;
    }

    public void dataChanged(
            double amount,
            String description,
            Account sourceAccount,
            Account targetAccount,
            Category category,
            TransactionStatus status)
    {
        Integer amountError = null;
        Integer descriptionError = null;
        Integer categoryError = null;
        Integer statusError = null;

        if(amount <= 0)
        {
            amountError = R.string.app_name;
        }

        if(description == null || description.isEmpty())
        {
            descriptionError = R.string.app_name;
        }

        if(category == null)
        {
            categoryError = R.string.app_name;
        }

        if(status == null)
        {
            statusError = R.string.app_name;
        }

        setFormState(new TransactionAddEditFormState(
                amountError,
                descriptionError,
                null,
                null,
                categoryError,
                statusError));
    }
}

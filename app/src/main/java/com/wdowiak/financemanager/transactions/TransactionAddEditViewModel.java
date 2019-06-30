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

import java.util.Date;

public class TransactionAddEditViewModel
    extends CommonAddEditViewModel<Transaction, TransactionAddEditFormState>
{
    // We could use same adapter for accounts, but we would need another data, its easier this way, than deriving yet another class
    private AdapterDataModel<Account, NameAdapter<Account>> sourceAccountsDataModel = new AdapterDataModel<>();
    private AdapterDataModel<Account, NameAdapter<Account>> targetAccountsDataModel = new AdapterDataModel<>();
    private AdapterDataModel<Category, NameAdapter<Category>> categoriesAdapterDataModel = new AdapterDataModel<>();
    private AdapterDataModel<TransactionStatus, NameAdapter<TransactionStatus>> statusesAdapterDataModel = new AdapterDataModel<>();

    private Date date;

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
            TransactionStatus status,
            Date date)
    {
        Integer amountError = null;
        Integer descriptionError = null;
        Integer categoryError = null;
        Integer statusError = null;
        Integer dateError = null;

        if(amount <= 0)
        {
            amountError = R.string.amount_less_than_zero;
        }

        if(description == null || description.isEmpty())
        {
            descriptionError = R.string.cannot_be_empty;
        }

        if(category == null)
        {
            categoryError = R.string.cannot_be_empty;
        }

        if(status == null)
        {
            statusError = R.string.cannot_be_empty;
        }

        if(date == null)
        {
            dateError = R.string.cannot_be_empty;
        }

        setFormState(new TransactionAddEditFormState(
                amountError,
                descriptionError,
                null,
                null,
                categoryError,
                statusError,
                dateError));
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

}

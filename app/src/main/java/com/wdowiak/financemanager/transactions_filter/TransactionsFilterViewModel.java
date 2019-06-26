package com.wdowiak.financemanager.transactions_filter;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.categories.AdapterDataModel;
import com.wdowiak.financemanager.commons.CommonAddEditViewModel;
import com.wdowiak.financemanager.commons.NameAdapter;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.data.TransactionStatus;

public class TransactionsFilterViewModel extends ViewModel
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
}

package com.wdowiak.financemanager.transactions_filter;

import androidx.annotation.Nullable;
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

import java.util.Calendar;
import java.util.Date;

public class TransactionsFilterViewModel extends ViewModel
{
    // To make our live easier when setting via parameter reference
    // since I think date = new date is local (sets new pointer, not object)
    class DateContainer
    {
        @Nullable
        Date date;

        void set(@Nullable Date date)
        {
            this.date = date;
        }

        @Nullable
        Date get()
        {
            return date;
        }
    }

    // We could use same adapter for accounts, but we would need another data, its easier this way, than deriving yet another class
    AdapterDataModel<Account, NameAdapter<Account>> sourceAccountsDataModel = new AdapterDataModel<>();
    AdapterDataModel<Account, NameAdapter<Account>> targetAccountsDataModel = new AdapterDataModel<>();
    AdapterDataModel<Category, NameAdapter<Category>> categoriesAdapterDataModel = new AdapterDataModel<>();
    AdapterDataModel<TransactionStatus, NameAdapter<TransactionStatus>> statusesAdapterDataModel = new AdapterDataModel<>();

    DateContainer beginDate = new DateContainer();
    DateContainer endDate = new DateContainer();

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

    public DateContainer getBeginDate()
    {
        return beginDate;
    }

    public DateContainer getEndDate()
    {
        return endDate;
    }
}

package com.wdowiak.financemanager.accounts;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.categories.AdapterDataModel;
import com.wdowiak.financemanager.commons.CommonAddEditViewModel;
import com.wdowiak.financemanager.commons.NameAdapter;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Currency;
import com.wdowiak.financemanager.data.Group;

public class AccountAddEditViewModel
    extends CommonAddEditViewModel<Account, AccountAddEditFormState>
{
    AdapterDataModel<Currency, NameAdapter<Currency>> currenciesAdapterDataModel = new AdapterDataModel<>();
    AdapterDataModel<Group, NameAdapter<Group>> groupsAdapterDataModel = new AdapterDataModel<>();

    public AdapterDataModel<Currency, NameAdapter<Currency>> getCurrenciesAdapterDataModel()
    {
        return currenciesAdapterDataModel;
    }

    public AdapterDataModel<Group, NameAdapter<Group>> getGroupsAdapterDataModel()
    {
        return groupsAdapterDataModel;
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

    public void dataChanged(String name, Group group, Currency currency)
    {
        Integer nameError = null;
        Integer groupError = null;
        Integer currencyError = null;

        if(name == null || name.isEmpty())
        {
            nameError = R.string.app_name;
        }

        if(group == null)
        {
            groupError = R.string.app_name;
        }

        if(currency == null)
        {
            currencyError = R.string.app_name;
        }

        setFormState(new AccountAddEditFormState(nameError, groupError, currencyError));
    }
}

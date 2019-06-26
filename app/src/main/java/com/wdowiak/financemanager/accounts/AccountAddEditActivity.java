package com.wdowiak.financemanager.accounts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.commons.CommonAddEditActivity;
import com.wdowiak.financemanager.commons.NameAdapter;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Currency;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.IItem;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountAddEditActivity extends CommonAddEditActivity<Account, AccountAddEditFormState>
{
    EditText accountName;
    Spinner groupSpinner, currencySpinner;
    AtomicInteger queryCounter = new AtomicInteger(-1); // Could be done better, but time is pressing

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_add_edit);

        itemType = IItem.Type.Account;

        accountName = findViewById(R.id.account_name);
        groupSpinner = findViewById(R.id.account_group);
        currencySpinner = findViewById(R.id.account_currency);

        accountName.addTextChangedListener(afterTextChangedListener);
        groupSpinner.setOnItemSelectedListener(afterSelectionChangedListener);
        currencySpinner.setOnItemSelectedListener(afterSelectionChangedListener);

        afterCreate();
    }

    TextWatcher afterTextChangedListener = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            // ignore
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            // ignore
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            getViewModel().dataChanged(
                    accountName.getText().toString(),
                    getSelectedGroup(),
                    getSelectedCurrency());
        }
    };

    AdapterView.OnItemSelectedListener afterSelectionChangedListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
        {
            getViewModel().dataChanged(
                    accountName.getText().toString(),
                    getSelectedGroup(),
                    getSelectedCurrency());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView)
        {
            getViewModel().dataChanged(
                    accountName.getText().toString(),
                    getSelectedGroup(),
                    getSelectedCurrency());
        }
    };


    protected AccountAddEditViewModel createViewModel()
    {
        return ViewModelProviders.of(this).get(AccountAddEditViewModel.class);
    }

    @Override
    protected void afterSuccessfulItemQuery()
    {
        queryLocalItems();
    }

    @Override
    protected void queryLocalItems()
    {
        showProgressBar(true);

        final int queriesToFinish = 2; // Group && Currency
        queryCounter.set(queriesToFinish);

        queryGroups();
        queryCurrencies();
    }

    protected void updateAddEditView()
    {
        final Account account = getViewModel().getQueriedItem();
        if(account == null)
        {
            throw new RuntimeException("Account should not be null");
        }

        final EditText editText = findViewById(R.id.account_name);
        editText.setText(account.getName());

        final int currencyToSelect = getViewModel().getCurrenciesAdapterDataModel().getData().indexOf(account.getCurrency());
        currencySpinner.setSelection(currencyToSelect);

        final int groupToSelect = getViewModel().getGroupsAdapterDataModel().getData().indexOf(account.getGroup());
        groupSpinner.setSelection(groupToSelect);
    }

    protected void populateSpinners()
    {
        createOrRefreshCurrenciesAdapter();
        createOrRefreshGroupsAdapter();
    }

    protected Account createItemFromInput()
    {
        if(!getViewModel().getFormState().getValue().isDataValid())
        {
            return null;
        }

        // todo, redo somehow else?
        Account newAccount = null;
        if(isEdit())
        {
            newAccount = new Account(
                    getViewModel().getItemId(),
                    accountName.getText().toString(),
                    getSelectedGroup(),
                    getSelectedCurrency(),
                    0.0); // unused atm
        }
        else
        {
            newAccount = new Account(
                    accountName.getText().toString(),
                    getSelectedGroup(),
                    getSelectedCurrency(),
                    0.0); // unused atm
        }

        return newAccount;
    }

    protected void onFormStateChanged(AccountAddEditFormState formState)
    {
        accountName.setError(formState.getNameError() != null ? getString(formState.getNameError()) : null);
    }

    protected AccountAddEditViewModel getViewModel()
    {
        return (AccountAddEditViewModel) super.getViewModel();
    }

    @Nullable
    private Group getSelectedGroup()
    {
        // todo, use custom adapter?
        final int selectedIndex = groupSpinner.getSelectedItemPosition();
        if(selectedIndex < 0)
        {
            return null;
        }

        final ArrayList<Group> groups = getViewModel().getGroupsAdapterDataModel().getData();
        if(groups == null || groups.size() <= selectedIndex)
        {
            return null;
        }

        return groups.get(selectedIndex);
    }

    @Nullable
    private Currency getSelectedCurrency()
    {
        // todo, use custom adapter?
        final int selectedIndex = currencySpinner.getSelectedItemPosition();
        if(selectedIndex < 0)
        {
            return null;
        }

        final ArrayList<Currency> currencies = getViewModel().getCurrenciesAdapterDataModel().getData();
        if(currencies == null || currencies.size() <= selectedIndex)
        {
            return null;
        }

        return currencies.get(selectedIndex);
    }

    private void queryGroups()
    {
        QueryApi.getItems(IItem.Type.Group, new Api.IQueryCallback<ArrayList<Group>>()
        {
            @Override
            public void onSuccess(ArrayList<Group> result)
            {

                getViewModel().getGroupsAdapterDataModel().setData(result);
                queryCounter.decrementAndGet();

                onLocalQueryFinished();
            }

            @Override
            public void onError(Exception error)
            {
                // todo
            }
        });
    }

    private void queryCurrencies()
    {
        QueryApi.getItems(IItem.Type.Currency, new Api.IQueryCallback<ArrayList<Currency>>()
        {
            @Override
            public void onSuccess(ArrayList<Currency> result)
            {
                getViewModel().getCurrenciesAdapterDataModel().setData(result);
                queryCounter.decrementAndGet();

                onLocalQueryFinished();
            }

            @Override
            public void onError(Exception error)
            {
                // todo
            }
        });
    }

    private void onLocalQueryFinished()
    {
        if(queryCounter.get() != 0)
        {
            return;
        }

        populateSpinners();
        if(isEdit())
        {
            updateAddEditView();
        }

        showProgressBar(false);
    }

    private void createOrRefreshGroupsAdapter()
    {
        NameAdapter<Group> adapter = getViewModel().getGroupsAdapterDataModel().getAdapter();
        if(adapter == null)
        {
            adapter = new NameAdapter<>(getViewModel().getGroupsAdapterDataModel().getData(), getApplicationContext());
            getViewModel().getGroupsAdapterDataModel().setAdapter(adapter);
            groupSpinner.setAdapter(adapter);
        }
        else
        {
            getViewModel().getGroupsAdapterDataModel().replaceAdaptersDataAndNotify();
        }
    }

    private void createOrRefreshCurrenciesAdapter()
    {
        NameAdapter<Currency> adapter = getViewModel().getCurrenciesAdapterDataModel().getAdapter();
        if(adapter == null)
        {
            adapter = new NameAdapter<>(getViewModel().getCurrenciesAdapterDataModel().getData(), getApplicationContext());
            getViewModel().getCurrenciesAdapterDataModel().setAdapter(adapter);
            currencySpinner.setAdapter(adapter);
        }
        else
        {
            getViewModel().getCurrenciesAdapterDataModel().replaceAdaptersDataAndNotify();
        }
    }
}

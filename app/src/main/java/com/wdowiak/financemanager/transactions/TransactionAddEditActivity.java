package com.wdowiak.financemanager.transactions;

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
import com.wdowiak.financemanager.categories.AdapterDataModel;
import com.wdowiak.financemanager.commons.CommonAddEditActivity;
import com.wdowiak.financemanager.commons.NameAdapter;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.Currency;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.data.TransactionStatus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionAddEditActivity extends CommonAddEditActivity<Transaction, TransactionAddEditFormState>
{
    interface ILocalQuerySuccess<T extends IItem>
    {
        void onSuccess(ArrayList<T> result);
    }

    EditText transactionDescription, transactionAmount;
    Spinner sourceAccountSpinner, targetAccountSpinner, categorySpinner, statusSpinner;
    AtomicInteger queryCounter = new AtomicInteger(-1); // Could be done better, but time is pressing

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_add_edit);

        itemType = IItem.Type.Transaction;

        transactionDescription = findViewById(R.id.transaction_description);
        transactionAmount = findViewById(R.id.transaction_amount);
        sourceAccountSpinner = findViewById(R.id.transaction_source_account);
        targetAccountSpinner = findViewById(R.id.transaction_target_account);
        categorySpinner = findViewById(R.id.transaction_category);
        statusSpinner = findViewById(R.id.transaction_status);

        transactionDescription.addTextChangedListener(afterTextChangedListener);
        transactionAmount.addTextChangedListener(afterTextChangedListener);
        sourceAccountSpinner.setOnItemSelectedListener(afterSelectionChangedListener);
        targetAccountSpinner.setOnItemSelectedListener(afterSelectionChangedListener);
        categorySpinner.setOnItemSelectedListener(afterSelectionChangedListener);
        statusSpinner.setOnItemSelectedListener(afterSelectionChangedListener);

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
            onDataChanged();
        }
    };

    void onDataChanged()
    {
        double amount = 0.0;
        final String amountString = transactionAmount.getText().toString();
        if(!amountString.isEmpty())
        {
            amount = Double.valueOf(amountString);
        }

        getViewModel().dataChanged(
            amount,
            transactionDescription.getText().toString(),
            getSelectedSourceAccount(),
            getSelectedTargetAccount(),
            getSelectedCategory(),
            getSelectedStatus());
    }

    AdapterView.OnItemSelectedListener afterSelectionChangedListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
        {
            onDataChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView)
        {
            onDataChanged();
        }
    };


    protected TransactionAddEditViewModel createViewModel()
    {
        return ViewModelProviders.of(this).get(TransactionAddEditViewModel.class);
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

        final int queriesToFinish = 3; // 1xAccount && Group && Currency
        queryCounter.set(queriesToFinish);

        queryLocalItem(IItem.Type.Account, getViewModel().getSourceAccountsDataModel(), new ILocalQuerySuccess()
        {
            @Override
            public void onSuccess(ArrayList result)
            {
                // add the null option
                result.add(0, null);

                // populate the target data model (since its using the same data as source)
                getViewModel().getTargetAccountsDataModel().setData(result);
            }
        });

        queryLocalItem(IItem.Type.Category, getViewModel().getCategoriesAdapterDataModel(), null);
        queryLocalItem(IItem.Type.TransactionStatus, getViewModel().getStatusesAdapterDataModel(), null);
    }

    protected void updateAddEditView()
    {
        final Transaction transaction = getViewModel().getQueriedItem();
        if(transaction == null)
        {
            throw new RuntimeException("Transaction should not be null");
        }

        EditText editText = findViewById(R.id.transaction_description);
        editText.setText(transaction.getDescription());

        editText = findViewById(R.id.transaction_amount);
        editText.setText(String.valueOf(transaction.getAmount()));

        if(transaction.getSourceAccount() != null)
        {
            final int sourceAccountToSelect = getViewModel().getSourceAccountsDataModel().getData().indexOf(transaction.getSourceAccount());
            sourceAccountSpinner.setSelection(sourceAccountToSelect);
        }

        if(transaction.getTargetAccount() != null)
        {
            final int targetAccountToSelect = getViewModel().getTargetAccountsDataModel().getData().indexOf(transaction.getTargetAccount());
            targetAccountSpinner.setSelection(targetAccountToSelect);
        }

        final int categoryToSelect = getViewModel().getCategoriesAdapterDataModel().getData().indexOf(transaction.getCategory());
        categorySpinner.setSelection(categoryToSelect);

        final int statusToSelect = getViewModel().getCategoriesAdapterDataModel().getData().indexOf(transaction.getStatus());
        statusSpinner.setSelection(statusToSelect);
    }

    protected void populateSpinners()
    {
        createOrRefreshAdapter(getViewModel().getSourceAccountsDataModel(), sourceAccountSpinner);
        createOrRefreshAdapter(getViewModel().getTargetAccountsDataModel(), targetAccountSpinner);
        createOrRefreshAdapter(getViewModel().getCategoriesAdapterDataModel(), categorySpinner);
        createOrRefreshAdapter(getViewModel().getStatusesAdapterDataModel(), statusSpinner);
    }

    protected Transaction createItemFromInput()
    {
        if(!getViewModel().getFormState().getValue().isDataValid())
        {
            return null;
        }

        // todo, redo somehow else?
        Transaction newTransaction = null;
        if(isEdit())
        {
            newTransaction = new Transaction(
                    getViewModel().getItemId(),
                    Double.valueOf(transactionAmount.getText().toString()),
                    transactionDescription.getText().toString(),
                    getSelectedSourceAccount(),
                    getSelectedTargetAccount(),
                    getSelectedCategory(),
                    getSelectedStatus());
        }
        else
        {
            newTransaction = new Transaction(
                    Double.valueOf(transactionAmount.getText().toString()),
                    transactionDescription.getText().toString(),
                    getSelectedSourceAccount(),
                    getSelectedTargetAccount(),
                    getSelectedCategory(),
                    getSelectedStatus());
        }

        return newTransaction;
    }

    protected void onFormStateChanged(TransactionAddEditFormState formState)
    {
        transactionAmount.setError(formState.getAmountError() != null ? getString(formState.getAmountError()) : null);
        transactionDescription.setError(formState.getDescriptionError() != null ? getString(formState.getDescriptionError()) : null);

        // other erros? for spinners?
    }

    protected TransactionAddEditViewModel getViewModel()
    {
        return (TransactionAddEditViewModel) super.getViewModel();
    }

    @Nullable
    private <T extends IItem> T getSelectedItem(@NotNull AdapterDataModel<T, NameAdapter<T>> dataModel, Spinner spinner)
    {
        final int selectedIndex = spinner.getSelectedItemPosition();
        if(selectedIndex < 0)
        {
            return null;
        }

        final ArrayList<T> items = dataModel.getData();
        if(items == null || items.size() <= selectedIndex)
        {
            return null;
        }

        return items.get(selectedIndex);
    }

    private Account getSelectedSourceAccount()
    {
        return getSelectedItem(getViewModel().getSourceAccountsDataModel(), sourceAccountSpinner);
    }

    private Account getSelectedTargetAccount()
    {
        return getSelectedItem(getViewModel().getTargetAccountsDataModel(), targetAccountSpinner);
    }

    private Category getSelectedCategory()
    {
        return getSelectedItem(getViewModel().getCategoriesAdapterDataModel(), categorySpinner);
    }

    private TransactionStatus getSelectedStatus()
    {
        return getSelectedItem(getViewModel().getStatusesAdapterDataModel(), statusSpinner);
    }

    private <T extends IItem> void queryLocalItem(
            IItem.Type itemType,
            @NotNull AdapterDataModel<T, NameAdapter<T>> dataModel,
            ILocalQuerySuccess localQueryCallback)
    {
        QueryApi.getItems(itemType, new Api.IQueryCallback<ArrayList<T>>()
        {
            @Override
            public void onSuccess(ArrayList<T> result)
            {
                if(localQueryCallback != null)
                {
                    localQueryCallback.onSuccess(result);
                }

                dataModel.setData(result);
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

    private <T extends IItem> void createOrRefreshAdapter(@NotNull AdapterDataModel<T, NameAdapter<T>> dataModel, Spinner spinner)
    {
        NameAdapter<T> adapter = dataModel.getAdapter();
        if(adapter == null)
        {
            adapter = new NameAdapter<>(dataModel.getData(), getApplicationContext());
            dataModel.setAdapter(adapter);
            spinner.setAdapter(adapter);
        }
        else
        {
            dataModel.replaceAdaptersDataAndNotify();
        }
    }
}

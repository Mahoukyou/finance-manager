package com.wdowiak.financemanager.transactions;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.categories.AdapterDataModel;
import com.wdowiak.financemanager.commons.AmountInputFilter;
import com.wdowiak.financemanager.commons.CommonAddEditActivity;
import com.wdowiak.financemanager.commons.Helpers;
import com.wdowiak.financemanager.commons.IntentExtras;
import com.wdowiak.financemanager.commons.NameAdapter;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.data.TransactionStatus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionAddEditActivity extends CommonAddEditActivity<Transaction, TransactionAddEditFormState>
{
    interface ILocalQuerySuccess<T extends IItem>
    {
        void onSuccess(ArrayList<T> result);
    }

    EditText transactionDescription, transactionAmount, transactionDate;
    Spinner sourceAccountSpinner, targetAccountSpinner, categorySpinner, statusSpinner;
    AtomicInteger queryCounter = new AtomicInteger(-1); // Could be done better, but time is pressing
    ImageButton setDate, setTime, resetDate;

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
        transactionDate = findViewById(R.id.transaction_date);
        setDate = findViewById(R.id.transaction_date_set_date);
        setTime = findViewById(R.id.transaction_date_set_time);
        resetDate = findViewById(R.id.transaction_date_reset);

        transactionDescription.addTextChangedListener(afterTextChangedListener);
        transactionAmount.addTextChangedListener(afterTextChangedListener);
        sourceAccountSpinner.setOnItemSelectedListener(afterSelectionChangedListener);
        targetAccountSpinner.setOnItemSelectedListener(afterSelectionChangedListener);
        categorySpinner.setOnItemSelectedListener(afterSelectionChangedListener);
        statusSpinner.setOnItemSelectedListener(afterSelectionChangedListener);
        transactionDate.addTextChangedListener(afterTextChangedListener);

        transactionAmount.setFilters(new InputFilter[] {new AmountInputFilter(25, 2)});

        setDateListener();
        setTimeListener();
        resetDate.setOnClickListener(this::onResetDate);

        afterCreate();
        if(!getIntent().hasExtra(IntentExtras.INTENT_EXTRA_ITEM_ID))
        {
            onResetDate(null);
        }
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
            getSelectedStatus(),
            getViewModel().getDate());
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

        if(transaction.getDate() != null)
        {
            setNewDate(transaction.getDate());
        }
        else
        {
            onResetDate(null);
        }
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
                    getSelectedStatus(),
                    getViewModel().getDate());
        }
        else
        {
            newTransaction = new Transaction(
                    Double.valueOf(transactionAmount.getText().toString()),
                    transactionDescription.getText().toString(),
                    getSelectedSourceAccount(),
                    getSelectedTargetAccount(),
                    getSelectedCategory(),
                    getSelectedStatus(),
                    getViewModel().getDate());
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
    private <T extends IItem> T getSelectedItem(@NotNull AdapterDataModel<T, NameAdapter<T>> dataModel, @NotNull Spinner spinner)
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

    private void setDateListener()
    {
        setDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Calendar c = Calendar.getInstance();
                if(getViewModel().getDate() != null)
                {
                    c.setTime(getViewModel().getDate());
                }

                final int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                final int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(TransactionAddEditActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day)
                    {
                        Calendar calendar = Calendar.getInstance();
                        if(getViewModel().getDate() != null)
                        {
                            calendar.setTime(getViewModel().getDate());
                        }

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DATE, day);

                        setNewDate(calendar.getTime());
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
    }

    private void setTimeListener()
    {
        setTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Calendar c = Calendar.getInstance();
                if(getViewModel().getDate() != null)
                {
                    c.setTime(getViewModel().getDate());
                }

                final int hours = c.get(Calendar.HOUR);
                final int minutes = c.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(TransactionAddEditActivity.this, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute)
                    {
                        Calendar calendar = Calendar.getInstance();
                        if(getViewModel().getDate() != null)
                        {
                            calendar.setTime(getViewModel().getDate());
                        }

                        calendar.set(Calendar.HOUR, hour);
                        calendar.set(Calendar.MINUTE, minute);

                        setNewDate(calendar.getTime());
                    }
                }, hours, minutes, false);

                timePickerDialog.show();
            }
        });
    }

    private void onResetDate(final View view)
    {
        setNewDate(Calendar.getInstance().getTime());
    }

    private void setNewDate(Date date)
    {
        getViewModel().setDate(date);
        transactionDate.setText(Helpers.getSimpleDateFormatToFormat().format(getViewModel().getDate()));
    }
}

package com.wdowiak.financemanager.transactions_filter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.categories.AdapterDataModel;
import com.wdowiak.financemanager.commons.AmountInputFilter;
import com.wdowiak.financemanager.commons.CommonAddEditActivity;
import com.wdowiak.financemanager.commons.CommonAddEditViewModel;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_TRANSACTION_FILTER_PARCELABLE;

public class TransactionsFilterActivity extends AppCompatActivity
{
    interface ILocalQuerySuccess<T extends IItem>
    {
        void onSuccess(ArrayList<T> result);
    }

    private TransactionsFilterViewModel viewModel = null;


    EditText transactionDescription, transactionAmountMin, transactionAmountMax, beginDate, endDate;
    Spinner sourceAccountSpinner, targetAccountSpinner, categorySpinner, statusSpinner;
    AtomicInteger queryCounter = new AtomicInteger(-1); // Could be done better, but time is pressing

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_filter);

        transactionDescription = findViewById(R.id.transaction_description);
        transactionAmountMin = findViewById(R.id.transaction_min_amount);
        transactionAmountMax = findViewById(R.id.transaction_max_amount);
        sourceAccountSpinner = findViewById(R.id.transaction_source_account);
        targetAccountSpinner = findViewById(R.id.transaction_target_account);
        categorySpinner = findViewById(R.id.transaction_category);
        statusSpinner = findViewById(R.id.transaction_status);
        beginDate = findViewById(R.id.filter_begin_date);
        endDate = findViewById(R.id.filter_end_date);

        transactionAmountMin.setFilters(new InputFilter[] {new AmountInputFilter(25, 2)});
        transactionAmountMax.setFilters(new InputFilter[] {new AmountInputFilter(25, 2)});

        viewModel = ViewModelProviders.of(this).get(TransactionsFilterViewModel.class);

        setButtonsText();
        setDateListeners();
        setTimeListeners();
        setResetListeners();

        queryLocalItems();
    }

    protected void queryLocalItems()
    {
        showProgressBar(true);

        final int queriesToFinish = 3; // 1xAccount && Group && Currency
        queryCounter.set(queriesToFinish);

        queryLocalItem(IItem.Type.Account, viewModel.getSourceAccountsDataModel(), new ILocalQuerySuccess()
        {
            @Override
            public void onSuccess(ArrayList result)
            {
                // populate the target data model (since its using the same data as source)
                viewModel.getTargetAccountsDataModel().setData(result);
            }
        });

        queryLocalItem(IItem.Type.Category, viewModel.getCategoriesAdapterDataModel(), null);
        queryLocalItem(IItem.Type.TransactionStatus, viewModel.getStatusesAdapterDataModel(), null);
    }

    protected void updateFilterView()
    {
        // todo, use current filter?
    }

    protected void populateSpinners()
    {
        createOrRefreshAdapter(viewModel.getSourceAccountsDataModel(), sourceAccountSpinner);
        createOrRefreshAdapter(viewModel.getTargetAccountsDataModel(), targetAccountSpinner);
        createOrRefreshAdapter(viewModel.getCategoriesAdapterDataModel(), categorySpinner);
        createOrRefreshAdapter(viewModel.getStatusesAdapterDataModel(), statusSpinner);
    }

    protected TransactionFilter createFilterFromInput()
    {
        final Account sourceAccount = getSelectedSourceAccount();
        final Account targetAccount = getSelectedTargetAccount();
        final Category category = getSelectedCategory();
        final TransactionStatus status = getSelectedStatus();

        final String minAmountString = transactionAmountMin.getText().toString();
        final String maxAmountString = transactionAmountMax.getText().toString();

        final Date beginDate = viewModel.getBeginDate().get();
        final Date endDate = viewModel.getEndDate().get();


        return new TransactionFilter(
                sourceAccount != null ? sourceAccount.getId() : null,
                targetAccount != null ? targetAccount.getId() : null,
                category != null ? category.getId() : null,
                status != null ? status.getId() : null,
                transactionDescription.getText().toString(),
                !minAmountString.isEmpty() ? Double.valueOf(minAmountString) : null,
                !maxAmountString.isEmpty() ? Double.valueOf(maxAmountString) : null,
                beginDate != null ? Helpers.getSimpleDateFormatToFormat().format(beginDate) : null,
                endDate != null ? Helpers.getSimpleDateFormatToFormat().format(endDate) : null);
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
        return getSelectedItem(viewModel.getSourceAccountsDataModel(), sourceAccountSpinner);
    }

    private Account getSelectedTargetAccount()
    {
        return getSelectedItem(viewModel.getTargetAccountsDataModel(), targetAccountSpinner);
    }

    private Category getSelectedCategory()
    {
        return getSelectedItem(viewModel.getCategoriesAdapterDataModel(), categorySpinner);
    }

    private TransactionStatus getSelectedStatus()
    {
        return getSelectedItem(viewModel.getStatusesAdapterDataModel(), statusSpinner);
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
                // Add null value
                result.add(0, null);

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

    public void onAddSave(final View view)
    {
        final TransactionFilter filter = createFilterFromInput();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(INTENT_EXTRA_TRANSACTION_FILTER_PARCELABLE, filter);
        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }

    public void onCancel(final View view)
    {
        disableButtons(true);
        finish();
    }

    protected final void showProgressBar(final boolean show)
    {
        final LinearLayout detailViewLayout = findViewById(R.id.view_layout);
        detailViewLayout.setVisibility(show ? View.GONE : View.VISIBLE);

        final LinearLayout progressBarLayout = findViewById(R.id.progress_bar_layout);
        progressBarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected final void disableButtons(final boolean disable)
    {
        final Button btn_add_edit = findViewById(R.id.add_save_action);
        btn_add_edit.setEnabled(!disable);

        final Button btn_cancel = findViewById(R.id.cancel_action);
        btn_cancel.setEnabled(!disable);
    }

    protected void setButtonsText()
    {
        final Button btn_add_edit = findViewById(R.id.add_save_action);
        btn_add_edit.setText("Filter");

        final Button btn_cancel = findViewById(R.id.cancel_action);
        btn_cancel.setText("Cancel");
    }

    private void setDateListeners()
    {
        setDateListener(beginDate, findViewById(R.id.filter_begin_date_set_date), viewModel.getBeginDate());
        setDateListener(endDate, findViewById(R.id.filter_end_date_set_date), viewModel.getEndDate());
    }

    private void setTimeListeners()
    {
        setTimeListener(beginDate, findViewById(R.id.filter_begin_date_set_time), viewModel.getBeginDate());
        setTimeListener(endDate, findViewById(R.id.filter_end_date_set_time), viewModel.getEndDate());
    }

    private void setResetListeners()
    {
        setResetListener(beginDate, findViewById(R.id.filter_begin_date_reset), viewModel.getBeginDate());
        setResetListener(endDate, findViewById(R.id.filter_end_date_reset), viewModel.getEndDate());
    }

    private void setDateListener(@NotNull EditText editText, @NotNull ImageButton button, TransactionsFilterViewModel.DateContainer dateContainer)
    {
        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DatePickerDialog datePickerDialog = new DatePickerDialog(TransactionsFilterActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day)
                    {
                        Calendar calendar = Calendar.getInstance();
                        if(dateContainer.get() != null)
                        {
                            calendar.setTime(dateContainer.get());
                        }

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DATE, day);

                        dateContainer.set(calendar.getTime());

                        editText.setText(Helpers.getSimpleDateFormatToFormat().format(dateContainer.get()));
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
    }

    private void setTimeListener(@NotNull EditText editText, @NotNull ImageButton button, TransactionsFilterViewModel.DateContainer dateContainer)
    {
        final Calendar c = Calendar.getInstance();
        final int hours = c.get(Calendar.HOUR);
        final int minutes = c.get(Calendar.MINUTE);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TimePickerDialog timePickerDialog = new TimePickerDialog(TransactionsFilterActivity.this, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute)
                    {
                        Calendar calendar = Calendar.getInstance();
                        if(dateContainer.get() != null)
                        {
                            calendar.setTime(dateContainer.get());
                        }

                        calendar.set(Calendar.HOUR, hour);
                        calendar.set(Calendar.MINUTE, minute);

                        dateContainer.set(calendar.getTime());

                        editText.setText(Helpers.getSimpleDateFormatToFormat().format(dateContainer.get()));
                    }
                }, hours, minutes, false);

                timePickerDialog.show();
            }
        });
    }

    private void setResetListener(@NotNull EditText editText, @NotNull ImageButton button, TransactionsFilterViewModel.DateContainer dateContainer)
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                editText.setText("");
                dateContainer.set(null);
            }
        });
    }
}

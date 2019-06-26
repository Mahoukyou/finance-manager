package com.wdowiak.financemanager.transactions_filter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.categories.AdapterDataModel;
import com.wdowiak.financemanager.commons.CommonAddEditActivity;
import com.wdowiak.financemanager.commons.CommonAddEditViewModel;
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
import java.util.concurrent.atomic.AtomicInteger;

import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_TRANSACTION_FILTER_PARCELABLE;

public class TransactionsFilterActivity extends AppCompatActivity
{
    interface ILocalQuerySuccess<T extends IItem>
    {
        void onSuccess(ArrayList<T> result);
    }

    private TransactionsFilterViewModel viewModel = null;


    EditText transactionDescription, transactionAmountMin, transactionAmountMax;
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

        viewModel = ViewModelProviders.of(this).get(TransactionsFilterViewModel.class);

        setButtonsText();

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

        return new TransactionFilter(
                sourceAccount != null ? sourceAccount.getId() : null,
                targetAccount != null ? targetAccount.getId() : null,
                category != null ? category.getId() : null,
                status != null ? status.getId() : null,
                transactionDescription.getText().toString(),
                !minAmountString.isEmpty() ? Double.valueOf(minAmountString) : null,
                !maxAmountString.isEmpty() ? Double.valueOf(maxAmountString) : null);
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
}

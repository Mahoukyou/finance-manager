package com.wdowiak.financemanager.transactions;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.commons.CommonDisplayFragment;
import com.wdowiak.financemanager.commons.CommonSortSettings;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.transaction_sorting.TransactionSortSettings;
import com.wdowiak.financemanager.transactions_filter.TransactionFilter;
import com.wdowiak.financemanager.transactions_filter.TransactionsFilterActivity;

import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

import static com.wdowiak.financemanager.commons.IntentExtras.GET_FILTER_REQUEST;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_TRANSACTION_FILTER_PARCELABLE;

public class TransactionDisplayFragment extends CommonDisplayFragment<Transaction, TransactionsAdapter>
{
    @NotNull
    @Contract(" -> new")
    public static TransactionDisplayFragment newInstance()
    {
        return new TransactionDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        itemType = IItem.Type.Transaction;
        detailClass = TransactionDetailActivity.class;
        addEditClass = TransactionAddEditActivity.class;

        setHasOptionsMenu(true);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_FILTER_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data.hasExtra(INTENT_EXTRA_TRANSACTION_FILTER_PARCELABLE))
            {
                final TransactionFilter filter = data.getExtras().getParcelable(INTENT_EXTRA_TRANSACTION_FILTER_PARCELABLE);
                getViewModel().setTransactionFilter(filter);
                queryAndDisplayItems();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(TransactionDisplayFragmentViewModel.class);

        queryAndDisplayItems();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.transactions_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_filter:
                showFilterPopupMenu();
                break;

            case R.id.menu_sort:
                showSortingPopupMenu();
                break;
        }

        return true;
    }

    private void showFilterPopupMenu()
    {
        PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.transaction_filter_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.filter_reset:
                        if(getViewModel().getTransactionFilter() != null)
                        {
                            getViewModel().setTransactionFilter(null);
                            queryAndDisplayItems();
                        }
                        break;

                    case R.id.filter_settings:
                        Intent intent = new Intent(getActivity().getApplicationContext(), TransactionsFilterActivity.class);
                        startActivityForResult(intent, GET_FILTER_REQUEST);
                        break;
                }

                return true;
            }
        });

        popup.show();
    }

    private void showSortingPopupMenu()
    {
        PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), getActivity().findViewById(R.id.menu_sort));
        popup.getMenuInflater().inflate(R.menu.transactions_sort_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.sort_date:
                        getViewModel().getSortSettings().setSortBy(TransactionSortSettings.ESortBy.Date);
                        break;

                    case R.id.sort_source_account:
                        getViewModel().getSortSettings().setSortBy(TransactionSortSettings.ESortBy.SourceAccount);
                        break;

                    case R.id.sort_target_account:
                        getViewModel().getSortSettings().setSortBy(TransactionSortSettings.ESortBy.TargetAccount);
                        break;

                    case R.id.sort_category:
                        getViewModel().getSortSettings().setSortBy(TransactionSortSettings.ESortBy.Category);
                        break;

                    case R.id.sort_source_group:
                        getViewModel().getSortSettings().setSortBy(TransactionSortSettings.ESortBy.SourceGroup);
                        break;

                    case R.id.sort_target_group:
                        getViewModel().getSortSettings().setSortBy(TransactionSortSettings.ESortBy.TargetGroup);
                        break;

                    case R.id.sort_source_currency:
                        getViewModel().getSortSettings().setSortBy(TransactionSortSettings.ESortBy.SourceCurrency);
                        break;

                    case R.id.sort_target_currency:
                        getViewModel().getSortSettings().setSortBy(TransactionSortSettings.ESortBy.TargetAccount);
                        break;

                    case R.id.sort_description:
                        getViewModel().getSortSettings().setSortBy(TransactionSortSettings.ESortBy.Description);
                        break;

                    case R.id.sort_amount:
                        getViewModel().getSortSettings().setSortBy(TransactionSortSettings.ESortBy.Amount);
                        break;

                }

                sortItems();
                createOrUpdateAdapter();

                return true;
            }
        });

        popup.show();
    }

    protected String getQueryString()
    {
        return getViewModel().getTransactionFilter() != null ?
                getViewModel().getTransactionFilter().getFilterString() : "";
    }

    @Override
    protected void queryAndDisplayItems()
    {
        showProgressBar(true);

        QueryApi.getItemsFiltered(itemType, getQueryString(), new Api.IQueryCallback<ArrayList<Transaction>>()
        {
            @Override
            public void onSuccess(ArrayList<Transaction> items)
            {
                // async request returned when activity was destroyed
                if(getActivity() == null || getActivity().getApplicationContext() == null)
                {
                    return;
                }

                viewModel.setData(items);
                sortItems();
                createOrUpdateAdapter();

                showProgressBar(false);
            }

            @Override
            public void onError(Exception error)
            {
                if(getActivity() != null && getActivity().getApplicationContext() != null)
                {
                    Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected TransactionDisplayFragmentViewModel getViewModel()
    {
        return (TransactionDisplayFragmentViewModel) super.getViewModel();
    }

    @Override
    protected TransactionsAdapter createItemAdapter()
    {
        return new TransactionsAdapter(viewModel.getData(), getActivity().getApplicationContext());
    }

    @Override
    protected void sortItems()
    {
        switch(getViewModel().getSortSettings().getSortBy())
        {
            case Date:
                getViewModel().getData().sort((lhs, rhs) -> modifySortResultOnSortType(
                        ObjectUtils.compare(lhs.getDate(), rhs.getDate())));
                break;

            case SourceAccount:
                getViewModel().getData().sort((lhs, rhs) -> modifySortResultOnSortType(
                            ObjectUtils.compare(lhs.getSourceAccount(), rhs.getSourceAccount(), true)));
                break;

            case TargetAccount:
                getViewModel().getData().sort((lhs, rhs) -> modifySortResultOnSortType(
                        ObjectUtils.compare(lhs.getTargetAccount(), rhs.getTargetAccount(), true)));
                break;

            case Category:
                getViewModel().getData().sort((lhs, rhs) -> modifySortResultOnSortType(
                        ObjectUtils.compare(lhs.getCategory(), rhs.getCategory(), true)));
                break;

            case SourceCurrency:
                getViewModel().getData().sort((lhs, rhs) -> modifySortResultOnSortType(
                        // Oh boy, god help the reader
                        lhs.getSourceAccount() == null ? (rhs.getSourceAccount() == null ? 0 : 1) : // if lhs is null and/or rhs
                            rhs.getSourceAccount() == null ?  -1 : // if only rhs is null
                                    ObjectUtils.compare(lhs.getSourceAccount().getCurrency(), rhs.getSourceAccount().getCurrency(), true)));
            break;

            case TargetCurrency:
                getViewModel().getData().sort((lhs, rhs) -> modifySortResultOnSortType(
                        // Oh boy, god help the reader
                        lhs.getTargetAccount() == null ? (rhs.getTargetAccount() == null ? 0 : 1) :
                                rhs.getTargetAccount() == null ?  -1 :
                                        ObjectUtils.compare(lhs.getTargetAccount().getCurrency(), rhs.getTargetAccount().getCurrency(), true)));
                break;

            case SourceGroup:
                getViewModel().getData().sort((lhs, rhs) -> modifySortResultOnSortType(
                        // Oh boy, god help the reader
                        lhs.getSourceAccount() == null ? (rhs.getSourceAccount() == null ? 0 : 1) :
                                rhs.getSourceAccount() == null ?  -1 :
                                        ObjectUtils.compare(lhs.getSourceAccount().getGroup(), rhs.getSourceAccount().getGroup(), true)));
                break;

            case TargetGroup:
                getViewModel().getData().sort((lhs, rhs) -> modifySortResultOnSortType(
                        // Oh boy, god help the reader
                        lhs.getTargetAccount() == null ? (rhs.getTargetAccount() == null ? 0 : 1) :
                                rhs.getTargetAccount() == null ?  -1 :
                                        ObjectUtils.compare(lhs.getTargetAccount().getGroup(), rhs.getTargetAccount().getGroup(), true)));
                break;
            case Description:
                getViewModel().getData().sort((lhs, rhs) -> modifySortResultOnSortType(
                        ObjectUtils.compare(lhs.getDescription(), rhs.getDescription(), true)));
                break;

            case Amount:
                getViewModel().getData().sort((lhs, rhs) -> modifySortResultOnSortType(
                        ObjectUtils.compare(lhs.getAmount(), rhs.getAmount())));
                break;

            default:
                throw new RuntimeException("Sort by not implemented");
        }
    }
}
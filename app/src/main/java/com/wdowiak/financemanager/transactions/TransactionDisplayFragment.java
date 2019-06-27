package com.wdowiak.financemanager.transactions;

import androidx.lifecycle.ViewModelProvider;
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
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.transactions_filter.TransactionFilter;
import com.wdowiak.financemanager.transactions_filter.TransactionsFilterActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
                queryWithFilterAndDisplay();
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
        popup.getMenuInflater().inflate(R.menu.default_sort_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.sort_ascending:
                        break;

                    case R.id.sort_descending:
                        break;
                }

                return true;
            }
        });

        popup.show();
    }

    protected void queryWithFilterAndDisplay()
    {
        showProgressBar(true);

        String filterString = "";
        if(getViewModel().getTransactionFilter() != null)
        {
            filterString = getViewModel().getTransactionFilter().getFilterString();
        }

        QueryApi.getItemsFiltered(itemType, filterString, new Api.IQueryCallback<ArrayList<Transaction>>()
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
                if(viewModel.getAdapter() == null)
                {
                    viewModel.setAdapter(createItemAdapter());
                    getItemsListView().setAdapter(viewModel.getAdapter());
                }
                else
                {
                    viewModel.populateAdapterWithDataAndNotify();
                }

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
}
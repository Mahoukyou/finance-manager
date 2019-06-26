package com.wdowiak.financemanager.transactions;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.commons.CommonDisplayFragment;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
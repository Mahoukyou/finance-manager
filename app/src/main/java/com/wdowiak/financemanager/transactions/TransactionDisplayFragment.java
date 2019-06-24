package com.wdowiak.financemanager.transactions;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wdowiak.financemanager.CommonDisplayFragment;
import com.wdowiak.financemanager.IDisplayFragmentViewModel;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
    protected IDisplayFragmentViewModel<Transaction, TransactionsAdapter> getViewModel()
    {
        return super.getViewModel();
    }

    @Override
    protected TransactionsAdapter createItemAdapter()
    {
        return new TransactionsAdapter(viewModel.getData(), getActivity().getApplicationContext());
    }
}
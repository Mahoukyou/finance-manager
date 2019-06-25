package com.wdowiak.financemanager.transaction_statuses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.commons.CommonDisplayFragment;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.TransactionStatus;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TransactionStatusesDisplayFragment extends CommonDisplayFragment<TransactionStatus, TransactionStatusesAdapter>
{
    @NotNull
    @Contract(" -> new")
    public static TransactionStatusesDisplayFragment newInstance()
    {
        return new TransactionStatusesDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        itemType = IItem.Type.TransactionStatus;
        detailClass = TransactionStatusesDetailActivity.class;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(TransactionStatusesDisplayFragmentViewModel.class);

        queryAndDisplayItems();
    }

    @Override
    protected TransactionStatusesDisplayFragmentViewModel getViewModel()
    {
        return (TransactionStatusesDisplayFragmentViewModel) super.getViewModel();
    }

    @Override
    protected TransactionStatusesAdapter createItemAdapter()
    {
        return new TransactionStatusesAdapter(viewModel.getData(), getActivity().getApplicationContext());
    }
}

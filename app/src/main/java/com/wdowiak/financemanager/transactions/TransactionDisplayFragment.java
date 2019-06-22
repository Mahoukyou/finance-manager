package com.wdowiak.financemanager.transactions;

import androidx.lifecycle.ViewModelProviders;

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

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.TransactionsApi;
import com.wdowiak.financemanager.data.Transaction;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TransactionDisplayFragment extends Fragment
{
    private TransactionDisplayFragmentViewModel mViewModel;

    ListView transactionsListView;

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
        final View view = inflater.inflate(R.layout.listview_display_fragment, container, false);

        transactionsListView = view.findViewById(R.id.display_listview);
        transactionsListView.setOnItemClickListener(this::OnTransactionClicked);

        view.findViewById(R.id.fab_add).setOnClickListener(this::onAddTransaction);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TransactionDisplayFragmentViewModel.class);

        queryAndDisplayTransactions();
    }

    private void OnTransactionClicked(AdapterView<?> adapterView, View view, int i, long l)
    {
        Intent intent = new Intent(getActivity().getApplicationContext(), TransactionDetailActivity.class);
        intent.putExtra(TransactionDetailActivity.INTENT_EXTRA_TRANSACTION_ID, mViewModel.transactionsData.get(i).getId());
        startActivity(intent);
    }

    void queryAndDisplayTransactions()
    {
        TransactionsApi.getTransactions(new TransactionsApi.ITransactionCallback<ArrayList<Transaction>>()
        {
            @Override
            public void OnSuccess(ArrayList<Transaction> transactions)
            {
                mViewModel.transactionsData = transactions;
                if(mViewModel.transactionsAdapter == null)
                {
                    mViewModel.transactionsAdapter = new TransactionsAdapter(mViewModel.transactionsData, getActivity().getApplicationContext());
                    transactionsListView.setAdapter(mViewModel.transactionsAdapter);
                }
                else
                {
                    mViewModel.transactionsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void OnError(Exception error)
            {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final void onAddTransaction(final View view)
    {
        Intent intent = new Intent(getActivity().getApplicationContext(), TransactionAddEditActivity.class);
        startActivity(intent);
    }
}

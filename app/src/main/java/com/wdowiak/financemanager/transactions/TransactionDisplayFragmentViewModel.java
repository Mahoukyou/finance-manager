package com.wdowiak.financemanager.transactions;

import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModel;

import com.wdowiak.financemanager.IDisplayFragmentViewModel;
import com.wdowiak.financemanager.data.Transaction;
import java.util.ArrayList;

public class TransactionDisplayFragmentViewModel
        extends ViewModel
        implements IDisplayFragmentViewModel<Transaction, TransactionsAdapter>
{
    private ArrayList<Transaction> transactionsData;
    private TransactionsAdapter transactionsAdapter;


    @Override
    public ArrayList<Transaction> getData()
    {
        return transactionsData;
    }

    @Override
    public void setData(ArrayList<Transaction> data)
    {
        transactionsData = data;
        if(transactionsAdapter != null)
        {
            transactionsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public TransactionsAdapter getAdapter()
    {
        return transactionsAdapter;
    }

    @Override
    public void setAdapter(TransactionsAdapter adapter)
    {
        transactionsAdapter = adapter;
    }
}

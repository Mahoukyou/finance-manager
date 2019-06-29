package com.wdowiak.financemanager.commons;

import android.content.ClipData;
import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModel;

import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.transaction_sorting.TransactionSortSettings;

import java.util.ArrayList;

// Since each and every one of our display view models will have at least data and adapter, we can implement it here
// instead of doing 100 copy paste again for the same implementation, just with different types
public class DisplayFragmentViewModel<ItemType extends IItem, AdapterType extends ArrayAdapter>
    extends ViewModel
{
    private ArrayList<ItemType> data;
    private AdapterType adapter;
    protected CommonSortSettings sortSettings = new CommonSortSettings();

    public ArrayList<ItemType> getData()
    {
        return data;
    }

    public AdapterType getAdapter()
    {
        return adapter;
    }

    public void setData(ArrayList<ItemType> data)
    {
        this.data = data;
    }

    public void setAdapter(AdapterType adapter)
    {
        this.adapter = adapter;
    }

    public void populateAdapterWithDataAndNotify()
    {
        final ArrayList<Transaction> clonedData = (ArrayList<Transaction>) getData().clone();
        getAdapter().clear(); // todo, clears all the data(getdata) before setting ti
        getAdapter().addAll(clonedData);
        getAdapter().notifyDataSetChanged();
    }

    public CommonSortSettings getSortSettings()
    {
        return sortSettings;
    }

}

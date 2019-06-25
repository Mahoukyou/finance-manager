package com.wdowiak.financemanager;

import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModel;

import com.wdowiak.financemanager.data.IItem;

import java.util.ArrayList;

// Since each and every one of our display view models will have at least data and adapter, we can implement it here
// instead of doing 100 copy paste again for the same implementation, just with different types
public class DisplayFragmentViewModel<ItemType extends IItem, AdapterType extends ArrayAdapter>
    extends ViewModel
{
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
        getAdapter().clear();
        getAdapter().addAll(getData());
        getAdapter().notifyDataSetChanged();
    }

    private ArrayList<ItemType> data;
    private AdapterType adapter;
}

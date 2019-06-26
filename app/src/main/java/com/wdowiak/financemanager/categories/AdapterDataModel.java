package com.wdowiak.financemanager.categories;

import android.widget.ArrayAdapter;

import com.wdowiak.financemanager.data.IItem;

import java.util.ArrayList;

public class AdapterDataModel<T extends IItem, Adapter extends ArrayAdapter<T>>
{
    private ArrayList<T> data;
    private Adapter adapter;

    public ArrayList<T> getData()
    {
        return data;
    }

    public void setData(ArrayList<T> data)
    {
        this.data = data;
    }

    public Adapter getAdapter()
    {
        return adapter;
    }

    public void setAdapter(Adapter adapter)
    {
        this.adapter = adapter;
    }

    public void replaceAdaptersDataAndNotify()
    {
        getAdapter().clear();
        getAdapter().addAll(getData());
        getAdapter().notifyDataSetChanged();
    }
}

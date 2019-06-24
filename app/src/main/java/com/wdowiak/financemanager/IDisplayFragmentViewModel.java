package com.wdowiak.financemanager;

import android.widget.ArrayAdapter;

import com.wdowiak.financemanager.data.IItem;

import java.util.ArrayList;

public interface IDisplayFragmentViewModel<ItemType extends IItem, AdapterType extends ArrayAdapter>
{
    ArrayList<ItemType> getData();
    AdapterType getAdapter();

    void setData(ArrayList<ItemType> data);
    void setAdapter(AdapterType adapter);
}

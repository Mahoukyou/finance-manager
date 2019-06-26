package com.wdowiak.financemanager.commons;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.IItem;

import java.util.ArrayList;

public class NameAdapter<T extends IItem> extends ArrayAdapter<T>
{
    private ArrayList<T> data;
    private Context context;

    private static class ViewHolder
    {
        TextView name;
    }

    public NameAdapter(ArrayList<T> data, Context context)
    {
        super(context, R.layout.spinner_item, data);
        this.data = data;
        this.context = context;

        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public T getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        ViewHolder viewHolder;
        View result;

        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
            viewHolder.name = convertView.findViewById(R.id.spinner_text);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        T item = getItem(position);
        viewHolder.name.setText(item != null ? item.getName() : "");

        return result;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        View result;

        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
            viewHolder.name = convertView.findViewById(R.id.spinner_text);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        T item = getItem(position);
        viewHolder.name.setText(item != null ? item.getName() : "");

        return result;
    }
}

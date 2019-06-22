package com.wdowiak.financemanager.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.Category;

import java.util.ArrayList;

public class CategoriesAdapter extends ArrayAdapter
{
    private ArrayList<Category> data;
    private Context context;

    private static class ViewHolder
    {
        TextView name;
    }

    public CategoriesAdapter(ArrayList<Category> data, Context context)
    {
        super(context, R.layout.account_listview_item, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Category getItem(int position)
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_listview_item, parent, false);
            viewHolder.name = convertView.findViewById(R.id.category_item_name);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        final Category category = getItem(position);
        viewHolder.name.setText(category.getName());

        return result;
    }
}

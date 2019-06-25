package com.wdowiak.financemanager.transaction_statuses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.TransactionStatus;

import java.util.ArrayList;

public class TransactionStatusesAdapter extends ArrayAdapter
{
    private ArrayList<TransactionStatus> data;
    private Context context;

    private static class ViewHolder
    {
        TextView name;
    }

    public TransactionStatusesAdapter(ArrayList<TransactionStatus> data, Context context)
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
    public TransactionStatus getItem(int position)
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_text_listview_item, parent, false);
            viewHolder.name = convertView.findViewById(R.id.single_item_text);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        final TransactionStatus transactionStatus = getItem(position);
        viewHolder.name.setText(transactionStatus.getName());

        return result;
    }
}

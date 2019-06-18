package com.wdowiak.financemanager.transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.Transaction;

import java.util.ArrayList;

public class TransactionsAdapter extends ArrayAdapter
{
    private ArrayList<Transaction> data;
    private Context context;

    private static class ViewHolder
    {
        TextView sourceAccount;
        TextView targetAccount;
        TextView description;
        TextView amount;
    }

    public TransactionsAdapter(ArrayList<Transaction> data, Context context)
    {
        super(context, R.layout.transaction_listview_item, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Transaction getItem(int position)
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_listview_item, parent, false);
            viewHolder.sourceAccount = convertView.findViewById(R.id.transaction_source_account);
            viewHolder.targetAccount = convertView.findViewById(R.id.transaction_target_account);
            viewHolder.description = convertView.findViewById(R.id.transaction_description);
            viewHolder.amount = convertView.findViewById(R.id.transaction_amount);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Transaction transaction = getItem(position);
        viewHolder.sourceAccount.setText(transaction.getSourceAccountName());
        viewHolder.targetAccount.setText(transaction.getTargetAccountName());
        viewHolder.description.setText(transaction.getDescription());
        viewHolder.amount.setText(String.valueOf(transaction.getAmount()));

        // todo
        //viewHolder..setTag(transaction.getId());
        return result;
    }
}

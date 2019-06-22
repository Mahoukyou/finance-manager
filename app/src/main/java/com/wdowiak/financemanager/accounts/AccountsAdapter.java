package com.wdowiak.financemanager.accounts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Currency;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.Transaction;

import java.util.ArrayList;

public class AccountsAdapter extends ArrayAdapter
{
    private ArrayList<Account> data;
    private Context context;

    private static class ViewHolder
    {
        TextView name;
        TextView group;
        TextView currency;
    }

    public AccountsAdapter(ArrayList<Account> data, Context context)
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
    public Account getItem(int position)
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_listview_item, parent, false);
            viewHolder.name = convertView.findViewById(R.id.account_item_name);
            viewHolder.group = convertView.findViewById(R.id.account_item_group);
            viewHolder.currency = convertView.findViewById(R.id.account_item_currency);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Account account = getItem(position);

        viewHolder.name.setText(account.getName());

        final Group group = account.getGroup();
        viewHolder.group.setText(group != null ? group.getName() : null);

        final Currency currency = account.getCurrency();
        viewHolder.currency.setText(currency != null ? currency.getName() : null);

        return result;
    }
}

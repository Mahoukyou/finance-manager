package com.wdowiak.financemanager.transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.commons.Helpers;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Currency;
import com.wdowiak.financemanager.data.Transaction;

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
        TextView category;
        TextView date;
        TextView amount;
        TextView status;
    }

    public TransactionsAdapter(ArrayList<Transaction> data, Context context)
    {
        super(context, R.layout.transaction_listview_new_item, data);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_listview_new_item, parent, false);
            viewHolder.sourceAccount = convertView.findViewById(R.id.transaction_item_source_account);
            viewHolder.targetAccount = convertView.findViewById(R.id.transaction_item_target_account);
            viewHolder.description = convertView.findViewById(R.id.transaction_item_description);
            viewHolder.category = convertView.findViewById(R.id.transaction_item_category);
            viewHolder.date  = convertView.findViewById(R.id.transaction_item_date);
            viewHolder.amount = convertView.findViewById(R.id.transaction_item_amount);
            viewHolder.status = convertView.findViewById(R.id.transaction_item_status);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Transaction transaction = getItem(position);

        final Account sourceAccount = transaction.getSourceAccount();
        viewHolder.sourceAccount.setText(sourceAccount != null ? sourceAccount.getName() : null);

        final Account targetAccount = transaction.getTargetAccount();
        viewHolder.targetAccount.setText(targetAccount != null ? targetAccount.getName() : null);

        viewHolder.description.setText(transaction.getDescription());

        viewHolder.category.setText(transaction.getCategory().getName());
        viewHolder.date.setText(Helpers.getSimpleDateFormatToFormat().format(transaction.getDate()));

        Currency currency = transaction.getLeadingCurrency();
        if(currency != null)
        {
            final boolean isPrefix = currency.getPrefix();
            final String currencySymbol = currency.getSymbol();

            viewHolder.amount.setText(isPrefix ?
                    currencySymbol + " " + transaction.getAmount() :
                    transaction.getAmount() + " " + currencySymbol );
        }
        else
        {
            viewHolder.amount.setText(String.valueOf(transaction.getAmount()));
        }

        viewHolder.status.setText(transaction.getStatus().getName());

        return result;
    }
}

package com.wdowiak.financemanager.currencies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.Currency;

import java.util.ArrayList;

public class CurrenciesAdapter extends ArrayAdapter
{
    private ArrayList<Currency> data;
    private Context context;

    private static class ViewHolder
    {
        TextView name;
        TextView acronym;
    }

    public CurrenciesAdapter(ArrayList<Currency> data, Context context)
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
    public Currency getItem(int position)
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.currencies_listview_item, parent, false);
            viewHolder.name = convertView.findViewById(R.id.currency_item_name);
            viewHolder.acronym = convertView.findViewById(R.id.currency_item_acronym);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Currency currency = getItem(position);

        viewHolder.name.setText(currency.getName());
        viewHolder.acronym.setText(currency.getAcronym());

        return result;
    }
}

package com.wdowiak.financemanager.currencies;

import android.os.Bundle;
import android.widget.TextView;

import com.wdowiak.financemanager.commons.CommonDetailViewActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.Currency;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.groups.GroupAddEditActivity;

public class CurrencyDetailActivity extends CommonDetailViewActivity<Currency>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencies_detail);

        addEditClass = CurrencyAddEditActivity.class;
        itemType = IItem.Type.Currency;

        afterCreate();
    }

    @Override
    protected final void updateDetailViewInfo(final Currency currency)
    {
        if(currency == null)
        {
            throw new NullPointerException("Currency is not valid");
        }

        TextView textView = findViewById(R.id.currency_name);
        textView.setText(currency.getName());

        textView = findViewById(R.id.currency_acronym);
        textView.setText(currency.getAcronym());

        textView = findViewById(R.id.currency_symbol);
        textView.setText(currency.getSymbol());

        textView = findViewById(R.id.currency_prefix);
        textView.setText(currency.getPrefix() ? "Yes" : "No");

        // todo, transactions info
    }
}

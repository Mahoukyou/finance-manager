package com.wdowiak.financemanager.categories;

import android.os.Bundle;
import android.widget.TextView;

import com.wdowiak.financemanager.commons.CommonDetailViewActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.groups.GroupAddEditActivity;
import com.wdowiak.financemanager.transactions_filter.TransactionFilter;

import java.util.ArrayList;

public class CategoryDetailActivity extends CommonDetailViewActivity<Category>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        addEditClass = CategoryAddEditActivity.class;
        itemType = IItem.Type.Category;

        afterCreate();
    }

    @Override
    protected final void updateDetailViewInfo(final Category category)
    {
        if(category == null)
        {
            throw new NullPointerException("Category is not valid");
        }

        TextView textView = findViewById(R.id.category_name);
        textView.setText(category.getName());

        // todo, transactions info
    }

    @Override
    protected void filteredTransactionsResult(final ArrayList<Transaction> transactions)
    {
        TextView textView = findViewById(R.id.transaction_count);
        textView.setText(String.valueOf(transactions.size()));
    }

    @Override
    protected TransactionFilter getTransactionFilter()
    {
        return new TransactionFilter(
                null,
                null,
                getItemId(),
                null,
                null,
                null,
                null,
                null,
                null);
    }

}

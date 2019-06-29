package com.wdowiak.financemanager.transaction_statuses;

import android.os.Bundle;
import android.widget.TextView;

import com.wdowiak.financemanager.commons.CommonDetailViewActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.data.TransactionStatus;
import com.wdowiak.financemanager.groups.GroupAddEditActivity;
import com.wdowiak.financemanager.transactions_filter.TransactionFilter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.TransferQueue;

public class TransactionStatusesDetailActivity extends CommonDetailViewActivity<TransactionStatus>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactionstatus_detail);

        addEditClass = TransactionStatusAddEditActivity.class;
        itemType = IItem.Type.TransactionStatus;

        afterCreate();
    }

    @Override
    protected final void updateDetailViewInfo(final TransactionStatus transactionStatus)
    {
        if(transactionStatus == null)
        {
            throw new NullPointerException("TransactionStatus is not valid");
        }

        TextView textView = findViewById(R.id.transaction_status_name);
        textView.setText(transactionStatus.getName());
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
            null,
            getItemId(),
            null,
            null,
            null,
            null,
            null);
    }
}

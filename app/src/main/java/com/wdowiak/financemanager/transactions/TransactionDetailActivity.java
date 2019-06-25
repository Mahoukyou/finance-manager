package com.wdowiak.financemanager.transactions;

import android.os.Bundle;
import android.widget.TextView;

import com.wdowiak.financemanager.commons.CommonDetailViewActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;

import org.jetbrains.annotations.Contract;

public class TransactionDetailActivity extends CommonDetailViewActivity<Transaction>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        addEditClass = TransactionAddEditActivity.class;
        itemType = IItem.Type.Transaction;

        afterCreate();
    }

    @Contract("null -> fail")
    protected final void updateDetailViewInfo(final Transaction transaction)
    {
        if(transaction == null)
        {
            throw new NullPointerException("Transaction is not valid");
        }

        TextView textView = findViewById(R.id.transaction_source_account);
        textView.setText(transaction.getSourceAccount() != null ? transaction.getSourceAccount().getName() : null);

        textView = findViewById(R.id.transaction_target_account);
        textView.setText(transaction.getTargetAccount() != null ? transaction.getTargetAccount().getName() : null);

        textView = findViewById(R.id.transaction_description);
        textView.setText(transaction.getDescription());

        textView = findViewById(R.id.transaction_amount);
        textView.setText(String.valueOf(transaction.getAmount()));

        textView = findViewById(R.id.transaction_status);
        textView.setText(transaction.getStatus() != null ? transaction.getStatus().getName() : null);

        textView = findViewById(R.id.transaction_category);
        textView.setText(transaction.getCategory() != null ? transaction.getCategory().getName() : null);

    }
}

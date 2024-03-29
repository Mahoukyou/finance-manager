package com.wdowiak.financemanager.transactions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wdowiak.financemanager.attachments.AttachmentsAdapter;
import com.wdowiak.financemanager.attachments.AttachmentsViewActivity;
import com.wdowiak.financemanager.commons.CommonDetailViewActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.commons.Helpers;
import com.wdowiak.financemanager.commons.IntentExtras;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;

import org.jetbrains.annotations.Contract;

import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_ITEM_ID;

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

        Account account = transaction.getSourceAccount();
        account = account == null ? transaction.getTargetAccount() : account;
        final String amount = String.valueOf(transaction.getAmount());
        if(account != null)
        {
            final boolean isPrefix = account.getCurrency().getPrefix();
            final String currencySymbol = account.getCurrency().getSymbol();

            textView.setText(isPrefix ? currencySymbol + " " + amount : amount + " " + currencySymbol );
        }
        else
        {
            textView.setText(amount);
        }

        textView = findViewById(R.id.transaction_status);
        textView.setText(transaction.getStatus() != null ? transaction.getStatus().getName() : null);

        textView = findViewById(R.id.transaction_category);
        textView.setText(transaction.getCategory() != null ? transaction.getCategory().getName() : null);

        textView = findViewById(R.id.transaction_date);
        textView.setText(Helpers.getSimpleDateFormatToFormat().format(transaction.getDate()));
    }

    public void displayAttachments(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AttachmentsViewActivity.class);
        intent.putExtra(INTENT_EXTRA_ITEM_ID, getItemId());
        startActivity(intent);
    }
}

package com.wdowiak.financemanager.transactions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdowiak.financemanager.MainActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.Transaction;
import com.wdowiak.financemanager.api.TransactionsApi;

import java.util.ArrayList;

public class TransactionDetailActivity extends AppCompatActivity
{
    public final static String INTENT_EXTRA_TRANSACTION_ID = "INTENT_EXTRA_TRANSACTION_ID";

    Long transactionId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        transactionId = getIntent().getExtras().getLong(INTENT_EXTRA_TRANSACTION_ID);
        queryTransaction();
    }

    final private void queryTransaction()
    {
        TransactionsApi.getTransactionById(transactionId, new TransactionsApi.ITransactionCallback<Transaction>()
        {
            @Override
            public void OnSuccess(final Transaction transaction)
            {
                if(transaction == null)
                {
                    Toast.makeText(getApplicationContext(), "Transaction[id= " + transactionId + "] does not exist", Toast.LENGTH_SHORT);
                    finish();

                    return;
                }

                updateDetailViewInfo(transaction);
                showDetailViewLayout();
            }

            @Override
            public void OnError(final Exception error)
            {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    final void showDetailViewLayout()
    {
        final LinearLayout detailViewLayout = findViewById(R.id.transaction_detail_view_layout);
        detailViewLayout.setVisibility(View.VISIBLE);

        final LinearLayout progressBarLayout = findViewById(R.id.transaction_progress_bar_layout);
        progressBarLayout.setVisibility(View.GONE);
    }

    final void updateDetailViewInfo(final Transaction transaction)
    {
        if(transaction == null)
        {
            throw new NullPointerException("Transaction is not valid");
        }

        TextView textView = findViewById(R.id.transaction_source_account);
        textView.setText(transaction.getSourceAccountName());

        textView = findViewById(R.id.transaction_target_account);
        textView.setText(transaction.getTargetAccountName());

        textView = findViewById(R.id.transaction_description);
        textView.setText(transaction.getDescription());

        textView = findViewById(R.id.transaction_amount);
        textView.setText(String.valueOf(transaction.getAmount()));

        textView = findViewById(R.id.transaction_status);
        textView.setText(transaction.getStatusName());

        textView = findViewById(R.id.transaction_category);
        textView.setText(transaction.getCategoryName());
    }

    public final void beginEditTransaction(final View view)
    {
        Intent intent = new Intent(getApplicationContext(), TransactionAddEditActivity.class);
        intent.putExtra(TransactionAddEditActivity.INTENT_EXTRA_TRANSACTION_ID, transactionId);
        startActivity(intent);
    }

}

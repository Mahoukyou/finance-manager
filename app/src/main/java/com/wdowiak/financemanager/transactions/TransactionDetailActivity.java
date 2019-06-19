package com.wdowiak.financemanager.transactions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        final long transactionId = getIntent().getExtras().getLong(INTENT_EXTRA_TRANSACTION_ID);
        queryTransaction(transactionId);
    }

    private void queryTransaction(final long transactionId)
    {
        TransactionsApi.getTransactionById(transactionId, new TransactionsApi.ITransactionCallback<Transaction>()
        {
            @Override
            public void OnSuccess(Transaction transaction)
            {
                if(transaction == null)
                {
                    Toast.makeText(getApplicationContext(), "Transaction doesnt seem to exist", Toast.LENGTH_SHORT);
                    finish();

                    return;
                }

                TextView textView = findViewById(R.id.transaction_source_account);
                textView.setText(transaction.getSourceAccountName());

                textView = findViewById(R.id.transaction_target_account);
                textView.setText(transaction.getSourceAccountName());

                textView = findViewById(R.id.transaction_description);
                textView.setText(transaction.getDescription());

                textView = findViewById(R.id.transaction_amount);
                textView.setText(String.valueOf(transaction.getAmount()));

                textView = findViewById(R.id.transaction_status);
                textView.setText(transaction.getStatusName());

                textView = findViewById(R.id.transaction_category);
                textView.setText(transaction.getCategoryName());
            }

            @Override
            public void OnError(Exception error)
            {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}

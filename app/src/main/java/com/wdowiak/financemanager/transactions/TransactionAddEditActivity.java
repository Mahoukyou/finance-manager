package com.wdowiak.financemanager.transactions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wdowiak.financemanager.R;

public class TransactionAddEditActivity extends AppCompatActivity {

    public final static String INTENT_EXTRA_TRANSACTION_ID = "INTENT_EXTRA_TRANSACTION_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_add_edit);
    }
}

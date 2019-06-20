package com.wdowiak.financemanager.transactions;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.AccountsApi;
import com.wdowiak.financemanager.data.Account;

import java.util.ArrayList;

public class TransactionAddEditActivity extends AppCompatActivity {

    public final static String INTENT_EXTRA_TRANSACTION_ID = "INTENT_EXTRA_TRANSACTION_ID";

    Long transactionId = null;

    ArrayList<Account> accounts;
    // categories
    // statuses

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_add_edit);

        transactionId = getIntent().getExtras().getLong(INTENT_EXTRA_TRANSACTION_ID);

        getAccounts();
    }

    private final void getAccounts()
    {
        AccountsApi.getAccounts(new AccountsApi.IAccountsCallback<ArrayList<Account>>()
        {
            @Override
            public void OnSuccess(ArrayList<Account> result)
            {
                accounts = result;
                if(accounts == null || accounts.isEmpty())
                {
                    return;
                }

                final Spinner sourceAccountSpinner = findViewById(R.id.transaction_source_account);
                final Spinner targetAccountSpinner = findViewById(R.id.transaction_target_account);
                final Spinner targetAccountSpinner1 = findViewById(R.id.transaction_category);
                final Spinner targetAccountSpinner2 = findViewById(R.id.transaction_status);

                final ArrayList<String> accountsSpinnerData = accountNamesToArrayList(accounts);
                sourceAccountSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, accountsSpinnerData));
                targetAccountSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, accountsSpinnerData));
                targetAccountSpinner1.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, accountsSpinnerData));
                targetAccountSpinner2.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, accountsSpinnerData));
            }

            @Override
            public void OnError(Exception error)
            {
                error.printStackTrace();
                Toast.makeText(TransactionAddEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    ArrayList<String> accountNamesToArrayList(ArrayList<Account> accounts)
    {
        ArrayList<String> retVal = new ArrayList<>();

        for(Account account : accounts)
        {
            retVal.add(account.getName());
        }

        return retVal;
    }
}

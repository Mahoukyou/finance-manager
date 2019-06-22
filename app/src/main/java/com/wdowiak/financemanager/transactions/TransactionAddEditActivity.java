package com.wdowiak.financemanager.transactions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.api.AccountsApi;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.CategoriesApi;
import com.wdowiak.financemanager.api.TransactionStatusesApi;
import com.wdowiak.financemanager.api.TransactionsApi;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.TransactionStatus;

import java.util.ArrayList;

public class TransactionAddEditActivity extends AppCompatActivity {

    public final static String INTENT_EXTRA_TRANSACTION_ID = "INTENT_EXTRA_TRANSACTION_ID";

    Long transactionId = null;

    Transaction transaction;

    ArrayList<Account> accounts;
    ArrayList<Category> categories;
    ArrayList<TransactionStatus> transactionStatuses;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_add_edit);

        if(getIntent().hasExtra(INTENT_EXTRA_TRANSACTION_ID))
        {
            transactionId = getIntent().getExtras().getLong(INTENT_EXTRA_TRANSACTION_ID);
            getTransaction();
        }
        else
        {
            // query
        }
    }

    private final void getAccounts()
    {
        AccountsApi.getAccounts(new AccountsApi.IAccountsCallback<ArrayList<Account>>()
        {
            @Override
            public void OnSuccess(ArrayList<Account> result)
            {
                accounts = result;
                if (accounts == null || accounts.isEmpty())
                {
                    return;
                }

                final Spinner sourceAccountSpinner = findViewById(R.id.transaction_source_account);
                final Spinner targetAccountSpinner = findViewById(R.id.transaction_target_account);

                final ArrayList<String> accountsSpinnerData = accountNamesToArrayList(accounts);
                sourceAccountSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, accountsSpinnerData));
                targetAccountSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, accountsSpinnerData));

                if (transaction.getSourceAccount() != null)
                {
                    final int sourceAccountToSelect = accounts.indexOf(transaction.getSourceAccount());
                    sourceAccountSpinner.setSelection(sourceAccountToSelect);
                }

                if (transaction.getTargetAccount() != null)
                {
                    final int targetAccountToSelect = accounts.indexOf(transaction.getTargetAccount());
                    targetAccountSpinner.setSelection(targetAccountToSelect);
                }
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

    private final void getCategories()
    {
        CategoriesApi.getCategories(new Api.IQueryCallback<ArrayList<Category>>()
        {
            @Override
            public void onSuccess(ArrayList<Category> result)
            {
                categories = result;
                if(categories == null || categories.isEmpty())
                {
                    return;
                }

                final Spinner categorySpinner = findViewById(R.id.transaction_category);

                final ArrayList<String> categorySpinnerData = categoryNamesToArrayList(categories);
                categorySpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, categorySpinnerData));
            }

            @Override
            public void onError(Exception error)
            {
                error.printStackTrace();
                Toast.makeText(TransactionAddEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private final void getTransactionStatuses()
    {
        TransactionStatusesApi.getTransactionStatuses(new Api.IQueryCallback<ArrayList<TransactionStatus>>()
        {
            @Override
            public void onSuccess(ArrayList<TransactionStatus> result)
            {
                transactionStatuses = result;
                if(transactionStatuses == null || transactionStatuses.isEmpty())
                {
                    return;
                }

                final Spinner transactionStatusSpinner = findViewById(R.id.transaction_status);

                final ArrayList<String> statusesSpinnerData = transactionStatusNamesToArrayList(transactionStatuses);
                transactionStatusSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, statusesSpinnerData));
            }

            @Override
            public void onError(Exception error)
            {
                error.printStackTrace();
                Toast.makeText(TransactionAddEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    final void getTransaction()
    {
        TransactionsApi.getTransactionById(transactionId, new TransactionsApi.ITransactionCallback<Transaction>()
        {
            @Override
            public void OnSuccess(Transaction result)
            {
                transaction = result;
                if(transaction == null)
                {
                    Toast.makeText(getApplicationContext(), "Transaction[id= " + transactionId + "] does not exist", Toast.LENGTH_SHORT);
                    finish();
                }

                getAccounts();
                getCategories();
                getTransactionStatuses();

                EditText editText = findViewById(R.id.transaction_description);
                editText.setText(transaction.getDescription());

                editText = findViewById(R.id.transaction_amount);
                editText.setText(String.valueOf(transaction.getAmount()));
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

    // Todo, there is got to be a better way (other tahn implementic a common interfacewith getname)
    ArrayList<String> accountNamesToArrayList(ArrayList<Account> accounts)
    {
        ArrayList<String> retVal = new ArrayList<>();

        for(Account account : accounts)
        {
            retVal.add(account.getName());
        }

        return retVal;
    }

    ArrayList<String> categoryNamesToArrayList(ArrayList<Category> categories)
    {
        ArrayList<String> retVal = new ArrayList<>();

        for(Category category : categories)
        {
            retVal.add(category.getName());
        }

        return retVal;
    }

    ArrayList<String> transactionStatusNamesToArrayList(ArrayList<TransactionStatus> transactionStatuses)
    {
        ArrayList<String> retVal = new ArrayList<>();

        for(TransactionStatus transactionStatus : transactionStatuses)
        {
            retVal.add(transactionStatus.getName());
        }

        return retVal;
    }
}

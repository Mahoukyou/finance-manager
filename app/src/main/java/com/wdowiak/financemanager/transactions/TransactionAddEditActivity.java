package com.wdowiak.financemanager.transactions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.TransactionStatus;

import java.util.ArrayList;

import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_ITEM_ID;

public class TransactionAddEditActivity extends AppCompatActivity {

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

        if(getIntent().hasExtra(INTENT_EXTRA_ITEM_ID))
        {
            transactionId = getIntent().getExtras().getLong(INTENT_EXTRA_ITEM_ID);
            getTransaction();
        }
        else
        {
            getAccounts();
            getCategories();
            getTransactionStatuses();
        }
    }

    private final void getAccounts()
    {
        QueryApi.getItems(IItem.Type.Account,new Api.IQueryCallback<ArrayList<Account>>()
        {
            @Override
            public void onSuccess(ArrayList<Account> result)
            {
                accounts = result;
                if (accounts == null || accounts.isEmpty())
                {
                    return;
                }

                final Spinner sourceAccountSpinner = findViewById(R.id.transaction_source_account);
                final Spinner targetAccountSpinner = findViewById(R.id.transaction_target_account);

                final ArrayList<String> accountsSpinnerData = accountNamesToArrayList(accounts);
                accountsSpinnerData.add(0, "[None]"); // todo, use account spinner data
                sourceAccountSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, accountsSpinnerData));
                targetAccountSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, accountsSpinnerData));

                if(transaction != null)
                {
                    if (transaction.getSourceAccount() != null)
                    {
                        final int sourceAccountToSelect = accounts.indexOf(transaction.getSourceAccount()) + 1;
                        sourceAccountSpinner.setSelection(sourceAccountToSelect);
                    }

                    if (transaction.getTargetAccount() != null)
                    {
                        final int targetAccountToSelect = accounts.indexOf(transaction.getTargetAccount()) + 1;
                        targetAccountSpinner.setSelection(targetAccountToSelect);
                    }
                }
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

    private final void getCategories()
    {
        QueryApi.getItems(IItem.Type.Category, new Api.IQueryCallback<ArrayList<Category>>()
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
                categorySpinnerData.add(0, "[None]"); // add cat spin adapter
                categorySpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, categorySpinnerData));

                if(transaction != null && transaction.getCategory() != null)
                {
                    final int categoryToSelect = categories.indexOf(transaction.getCategory()) + 1;
                    categorySpinner.setSelection(categoryToSelect);
                }
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
        QueryApi.getItems(IItem.Type.TransactionStatus, new Api.IQueryCallback<ArrayList<TransactionStatus>>()
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
                statusesSpinnerData.add(0, "[None]");
                transactionStatusSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, statusesSpinnerData));

                if(transaction != null)
                {
                    final int statusToSelect = transactionStatuses.indexOf(transaction.getStatus()) + 1;
                    transactionStatusSpinner.setSelection(statusToSelect);
                }
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
        QueryApi.getItemById(transactionId, IItem.Type.Transaction, new Api.IQueryCallback<Transaction>()
        {
            @Override
            public void onSuccess(Transaction result)
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
            public void onError(Exception error)
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

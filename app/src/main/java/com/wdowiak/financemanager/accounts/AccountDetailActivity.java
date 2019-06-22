package com.wdowiak.financemanager.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.AccountsApi;
import com.wdowiak.financemanager.api.TransactionsApi;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.transactions.TransactionAddEditActivity;

public class AccountDetailActivity extends AppCompatActivity
{
    public final static String INTENT_EXTRA_ACCOUNT_ID = "INTENT_EXTRA_ACCOUNT_ID";

    Long accountId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        accountId = getIntent().getExtras().getLong(INTENT_EXTRA_ACCOUNT_ID);
        queryAccount();
    }

    final private void queryAccount()
    {
        AccountsApi.getAccountById(accountId, new AccountsApi.IAccountsCallback<Account>()
        {
            @Override
            public void OnSuccess(final Account account)
            {
                if(account == null)
                {
                    Toast.makeText(getApplicationContext(), "Account[id= " + accountId + "] does not exist", Toast.LENGTH_SHORT);
                    finish();

                    return;
                }

                updateDetailViewInfo(account);
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
        final LinearLayout detailViewLayout = findViewById(R.id.account_detail_view_layout);
        detailViewLayout.setVisibility(View.VISIBLE);

        final LinearLayout progressBarLayout = findViewById(R.id.account_progress_bar_layout);
        progressBarLayout.setVisibility(View.GONE);
    }

    final void updateDetailViewInfo(final Account account)
    {
        if(account == null)
        {
            throw new NullPointerException("Account is not valid");
        }

        TextView textView = findViewById(R.id.account_name);
        textView.setText(account.getName());

        textView = findViewById(R.id.account_group);
        textView.setText(account.getGroup() != null ? account.getGroup().getName() : null);

        textView = findViewById(R.id.account_currency);
        textView.setText(account.getCurrency() != null ? account.getCurrency().getName() : null);

        // todo, transactions info
    }

    public final void beginEditAccount(final View view)
    {
       // Intent intent = new Intent(getApplicationContext(), TransactionAddEditActivity.class);
       // intent.putExtra(TransactionAddEditActivity.INTENT_EXTRA_TRANSACTION_ID, transactionId);
       // startActivity(intent);
    }
}

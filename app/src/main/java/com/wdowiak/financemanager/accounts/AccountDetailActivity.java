package com.wdowiak.financemanager.accounts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wdowiak.financemanager.CommonDetailViewActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.AccountsApi;
import com.wdowiak.financemanager.api.TransactionsApi;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.groups.GroupAddEditActivity;
import com.wdowiak.financemanager.transactions.TransactionAddEditActivity;

public class AccountDetailActivity extends CommonDetailViewActivity<Account>
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        // todo, no aaddedit for account just yet
        addEditClass = GroupAddEditActivity.class;

        afterCreate();
    }

    @Override
    final protected void queryItem()
    {
        showProgressBar(true);

        AccountsApi.getAccountById(getItemId(), new AccountsApi.IAccountsCallback<Account>()
        {
            @Override
            public void OnSuccess(final Account account)
            {
                if(account == null)
                {
                    Toast.makeText(getApplicationContext(), "Account[id= " + getItemId() + "] does not exist", Toast.LENGTH_SHORT);
                    finish();

                    return;
                }

                updateDetailViewInfo(account);
                showProgressBar(false);
            }

            @Override
            public void OnError(final Exception error)
            {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected final void updateDetailViewInfo(final Account account)
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

    @Override
    protected final void deleteItem()
    {
        throw new RuntimeException("STUB");
    }
}

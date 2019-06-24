package com.wdowiak.financemanager.accounts;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.wdowiak.financemanager.CommonDetailViewActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.groups.GroupAddEditActivity;

public class AccountDetailActivity extends CommonDetailViewActivity<Account>
{
    IItem.Type type = IItem.Type.Account;

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

        QueryApi.getItemById(getItemId(), type, new Api.IQueryCallback<Account>()
        {
            @Override
            public void onSuccess(final Account account)
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
            public void onError(final Exception error)
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

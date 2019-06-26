package com.wdowiak.financemanager.accounts;

import android.os.Bundle;
import android.widget.TextView;

import com.wdowiak.financemanager.commons.CommonDetailViewActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.groups.GroupAddEditActivity;

public class AccountDetailActivity extends CommonDetailViewActivity<Account>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        addEditClass = AccountAddEditActivity.class;
        itemType = IItem.Type.Account;

        afterCreate();
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
}

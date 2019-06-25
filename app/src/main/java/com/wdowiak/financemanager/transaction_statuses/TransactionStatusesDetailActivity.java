package com.wdowiak.financemanager.transaction_statuses;

import android.os.Bundle;
import android.widget.TextView;

import com.wdowiak.financemanager.commons.CommonDetailViewActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.TransactionStatus;
import com.wdowiak.financemanager.groups.GroupAddEditActivity;

public class TransactionStatusesDetailActivity extends CommonDetailViewActivity<TransactionStatus>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactionstatus_detail);

        // todo, no aaddedit for account just yet
        addEditClass = GroupAddEditActivity.class;
        itemType = IItem.Type.TransactionStatus;

        afterCreate();
    }

    @Override
    protected final void updateDetailViewInfo(final TransactionStatus transactionStatus)
    {
        if(transactionStatus == null)
        {
            throw new NullPointerException("TransactionStatus is not valid");
        }

        TextView textView = findViewById(R.id.transaction_status_name);
        textView.setText(transactionStatus.getName());

        // todo, transactions info
    }

}

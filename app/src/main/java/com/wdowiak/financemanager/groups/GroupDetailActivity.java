package com.wdowiak.financemanager.groups;

import android.os.Bundle;
import android.widget.TextView;

import com.wdowiak.financemanager.CommonDetailViewActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.IItem;

import org.jetbrains.annotations.Contract;

public class GroupDetailActivity extends CommonDetailViewActivity<Group>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        addEditClass = GroupAddEditActivity.class;
        itemType = IItem.Type.Group;

        afterCreate();
    }

    @Contract("null -> fail")
    @Override
    protected final void updateDetailViewInfo(final Group group)
    {
        if(group == null)
        {
            throw new NullPointerException("Group is not valid");
        }

        TextView textView = findViewById(R.id.group_name);
        textView.setText(group.getName());

        textView = findViewById(R.id.group_description);
        textView.setText(group.getInfo());

        // todo, transactions info
    }
}

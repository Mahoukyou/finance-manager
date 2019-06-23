package com.wdowiak.financemanager.groups;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.CategoriesApi;
import com.wdowiak.financemanager.api.GroupsApi;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.Group;

public class GroupDetailActivity extends AppCompatActivity
{
    public final static String INTENT_EXTRA_GROUP_ID = "INTENT_EXTRA_GROUP_ID";

    Long groupId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        groupId = getIntent().getExtras().getLong(INTENT_EXTRA_GROUP_ID);
        queryCategory();
    }

    final private void queryCategory()
    {
        GroupsApi.getGroupyById(groupId, new Api.IQueryCallback<Group>() {
            @Override
            public void onSuccess(Group group)
            {
                if(group == null)
                {
                    Toast.makeText(getApplicationContext(), "Group[id= " + groupId + "] does not exist", Toast.LENGTH_SHORT);
                    finish();

                    return;
                }

                updateDetailViewInfo(group);
                showDetailViewLayout();
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    final void showDetailViewLayout()
    {
        final LinearLayout detailViewLayout = findViewById(R.id.detail_view_layout);
        detailViewLayout.setVisibility(View.VISIBLE);

        final LinearLayout progressBarLayout = findViewById(R.id.progress_bar_layout);
        progressBarLayout.setVisibility(View.GONE);
    }

    final void updateDetailViewInfo(final Group group)
    {
        if(group == null)
        {
            throw new NullPointerException("Group is not valid");
        }

        TextView textView = findViewById(R.id.group_name);
        textView.setText(group.getName());

        // todo, transactions info
    }

    public final void beginEditAccount(final View view)
    {
       // Intent intent = new Intent(getApplicationContext(), TransactionAddEditActivity.class);
       // intent.putExtra(TransactionAddEditActivity.INTENT_EXTRA_TRANSACTION_ID, transactionId);
       // startActivity(intent);
    }

    public final void deleteGroup(final View view)
    {
        // todo confirmation

        GroupsApi.deleteGroupById(groupId, new Api.IQueryCallback<String>()
        {
            @Override
            public void onSuccess(String  result)
            {
                // todo
            }

            @Override
            public void onError(Exception error)
            {
                // todo
            }
        });
    }
}

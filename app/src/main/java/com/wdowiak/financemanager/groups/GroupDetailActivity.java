package com.wdowiak.financemanager.groups;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.GroupsApi;
import com.wdowiak.financemanager.data.Group;

public class GroupDetailActivity extends AppCompatActivity
{
    public final static String INTENT_EXTRA_GROUP_ID = "INTENT_EXTRA_GROUP_ID";
    public final static String INTENT_EXTRA_RESULT_GROUP_WAS_DELETED = "INTENT_EXTRA_RESULT_GROUP_WAS_DELETED";
    static final int EDIT_GROUP_REQUEST = 2;


    Long groupId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        groupId = getIntent().getExtras().getLong(INTENT_EXTRA_GROUP_ID);
        queryGroup();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_GROUP_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data.hasExtra(GroupDetailActivity.INTENT_EXTRA_RESULT_GROUP_WAS_DELETED))
            {
                queryGroup();
            }
        }
    }

    final private void queryGroup()
    {
        GroupsApi.getGroupById(groupId, new Api.IQueryCallback<Group>() {
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

        textView = findViewById(R.id.group_description);
        textView.setText(group.getInfo());

        // todo, transactions info
    }

    public final void onEdit(final View view)
    {
        Intent intent = new Intent(getApplicationContext(), GroupAddEditActivity.class);
        intent.putExtra(GroupAddEditActivity.INTENT_EXTRA_GROUP_ID, groupId);
        startActivityForResult(intent, EDIT_GROUP_REQUEST);
    }

    public final void onDelete(final View view)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        // todo, show progress bar
                        // todo, disable buttons
                        deleteGroup(dialog);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this group?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private final void deleteGroup(DialogInterface dialog)
    {
        GroupsApi.deleteGroupById(groupId, new Api.IQueryCallback<String>()
        {
            @Override
            public void onSuccess(String  result)
            {
                Toast.makeText(getApplicationContext(), "Group was deleted successfully", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra(INTENT_EXTRA_RESULT_GROUP_WAS_DELETED, INTENT_EXTRA_RESULT_GROUP_WAS_DELETED);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getApplicationContext(), "Group could not be deleted. Is there any account using it?", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

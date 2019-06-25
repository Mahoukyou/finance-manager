package com.wdowiak.financemanager.groups;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.IItem;

import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_ITEM_ID;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_CREATED;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED;

public class GroupAddEditActivity extends AppCompatActivity
{
    Long groupId = null;
    Group group = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add_edit);

        final Button btn_add_save = findViewById(R.id.add_save_action);
        btn_add_save.setText(isEdit() ? "Save" : "Create");

        if(getIntent().hasExtra(INTENT_EXTRA_ITEM_ID))
        {
            groupId = getIntent().getExtras().getLong(INTENT_EXTRA_ITEM_ID);
            queryGroup();
        }
    }

    final void queryGroup()
    {
        showProgressBar(true);

        QueryApi.getItemById(groupId, IItem.Type.Group,new Api.IQueryCallback<Group>()
        {
            @Override
            public void onSuccess(Group result)
            {
                group = result;
                if(group == null)
                {
                    Toast.makeText(getApplicationContext(), "Group[id= " + groupId + "] does not exist", Toast.LENGTH_SHORT);
                    finish();
                }

                EditText editText = findViewById(R.id.group_name);
                editText.setText(group.getName());

                editText = findViewById(R.id.group_description);
                editText.setText(group.getInfo());

                showProgressBar(false);
            }

            @Override
            public void onError(Exception error)
            {
                error.printStackTrace();
                Toast.makeText(GroupAddEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private final boolean isEdit()
    {
        return getIntent().hasExtra(INTENT_EXTRA_ITEM_ID);
    }

    public void onAddSave(final View view)
    {
        disableButtons(true);

        Group newGroup = createGroupFromInput();
        /* if(!newGroup.isValid())
        {
        // todo
            Toast.makeText(this, "Input is not valid", Toast.LENGTH_SHORT).show();
            return;
        } */

        if(isEdit())
        {
            // todo, check if there was any change
            updateGroup(newGroup);
        }
        else
        {
            createGroup(newGroup);
        }

        // todo, notify about result
    }

    public void onCancel(final View view)
    {
        disableButtons(true);
        final boolean changed = true; // todo

        if(changed)
        {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    switch (which)
                    {
                        case DialogInterface.BUTTON_POSITIVE:
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            disableButtons(false);
                            dialog.dismiss();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to discard the changes?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .setCancelable(false)
                    .show();
        }
        else
        {
            finish();
        }
    }

    private Group createGroupFromInput()
    {
        EditText editText = findViewById(R.id.group_name);
        final String name = editText.getText().toString();

        editText = findViewById(R.id.group_description);
        final String description = editText.getText().toString();

        if(isEdit())
        {
            return new Group(groupId, name, description);
        }

        return new Group(name, description);
    }

    private void createGroup(Group newGroup)
    {
        QueryApi.createItem(newGroup, IItem.Type.Group, new Api.IQueryCallback<Group>()
        {
            @Override
            public void onSuccess(Group result)
            {
                Toast.makeText(getApplicationContext(), "Group was added successfully", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra(INTENT_EXTRA_RESULT_ITEM_WAS_CREATED, INTENT_EXTRA_RESULT_ITEM_WAS_CREATED);
                setResult(Activity.RESULT_OK, resultIntent);

                finish();
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getApplicationContext(), "Group could not be added: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                disableButtons(false);
            }
        });
    }

    private void updateGroup(Group newGroup)
    {
        QueryApi.updateItem(newGroup, IItem.Type.Group, new Api.IQueryCallback<Group>()
        {
            @Override
            public void onSuccess(Group result)
            {
                Toast.makeText(getApplicationContext(), "Group was updated successfully", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra(INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED, INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED);
                setResult(Activity.RESULT_OK, resultIntent);

                finish();
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getApplicationContext(), "Group could not be updated: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                disableButtons(false);
            }
        });
    }

    private final void showProgressBar(final boolean show)
    {
        final LinearLayout detailViewLayout = findViewById(R.id.view_layout);
        detailViewLayout.setVisibility(show ? View.GONE : View.VISIBLE);

        final LinearLayout progressBarLayout = findViewById(R.id.progress_bar_layout);
        progressBarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private final void disableButtons(final boolean disable)
    {
        final Button btn_add_edit = findViewById(R.id.add_save_action);
        btn_add_edit.setEnabled(!disable);

        final Button btn_cancel = findViewById(R.id.cancel_action);
        btn_cancel.setEnabled(!disable);
    }
}

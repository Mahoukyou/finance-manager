package com.wdowiak.financemanager.groups;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.GroupsApi;
import com.wdowiak.financemanager.data.Group;

public class GroupAddEditActivity extends AppCompatActivity {

    public final static String INTENT_EXTRA_GROUP_ID = "INTENT_EXTRA_GROUP_ID";

    Button btn_add_save, btn_cancel;

    Long groupId = null;
    Group group = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add_edit);

        btn_add_save = findViewById(R.id.add_save_action);
        btn_cancel = findViewById(R.id.cancel_action);

        btn_add_save.setText(isEdit() ? "Save" : "Create");

        if(getIntent().hasExtra(INTENT_EXTRA_GROUP_ID))
        {
            groupId = getIntent().getExtras().getLong(INTENT_EXTRA_GROUP_ID);
            getGroup();
        }
    }

    final void getGroup()
    {
        GroupsApi.getGroupById(groupId, new Api.IQueryCallback<Group>()
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
        return getIntent().hasExtra(INTENT_EXTRA_GROUP_ID);
    }

    public void onAddSave(final View view)
    {
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
        // todo, ask for confirmation if anything changed
        finish();
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
        GroupsApi.createGroup(newGroup, new Api.IQueryCallback<Group>()
        {
            @Override
            public void onSuccess(Group result)
            {
                Toast.makeText(getApplicationContext(), "Group was added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getApplicationContext(), "Group could not be added: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateGroup(Group newGroup)
    {
        GroupsApi.updateGroup(newGroup, new Api.IQueryCallback<Group>()
        {
            @Override
            public void onSuccess(Group result)
            {
                Toast.makeText(getApplicationContext(), "Group was updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getApplicationContext(), "Group could not be updated: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

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
import com.wdowiak.financemanager.data.NewGroup;

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
        return getIntent().hasExtra(INTENT_EXTRA_GROUP_ID) && groupId != null;
    }

    public void onAddSave(final View view)
    {
        NewGroup newGroup = createGroupFromInput();
        if(!newGroup.isValid())
        {
            Toast.makeText(this, "Input is not valid", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if(isEdit())
        {
            // query update
        }
        else
        {
            createGroup(newGroup);
        }
    }

    public void onCancel(final View view)
    {
        // todo, ask for confirmation if anything changed
        finish();
    }

    private NewGroup createGroupFromInput()
    {
        EditText editText = findViewById(R.id.group_name);
        final String name = editText.getText().toString();

        editText = findViewById(R.id.group_description);
        final String description = editText.getText().toString();

        NewGroup newGroup = new NewGroup(name, description);
        return newGroup;
    }

    private void createGroup(NewGroup newGroup)
    {
        GroupsApi.createGroup(newGroup, new Api.IQueryCallback<Group>()
        {
            @Override
            public void onSuccess(Group result)
            {
                Toast.makeText(getApplicationContext(), "Group was added succcessfulty", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getApplicationContext(), "Group could not be added" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

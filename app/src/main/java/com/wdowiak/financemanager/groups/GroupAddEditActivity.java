package com.wdowiak.financemanager.groups;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.commons.CommonAddEditActivity;
import com.wdowiak.financemanager.commons.CommonAddEditFormState;
import com.wdowiak.financemanager.commons.CommonAddEditViewModel;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.IItem;

import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_ITEM_ID;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_CREATED;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED;

public class GroupAddEditActivity extends CommonAddEditActivity<Group, GroupAddEditFormState>
{
    EditText groupName, groupDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add_edit);

        itemType = IItem.Type.Group;

        groupName = findViewById(R.id.group_name);
        groupDescription = findViewById(R.id.group_description);

        groupName.addTextChangedListener(afterTextChangedListener);
        groupDescription.addTextChangedListener(afterTextChangedListener);

        afterCreate();
    }

    TextWatcher afterTextChangedListener = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            // ignore
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            // ignore
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            getViewModel().dataChanged(
                    groupName.getText().toString(),
                    groupDescription.getText().toString());
        }
    };

    protected GroupAddEditViewModel createViewModel()
    {
        return ViewModelProviders.of(this).get(GroupAddEditViewModel.class);
    }

    protected void updateAddEditView()
    {
        final Group group = getViewModel().getQueriedItem();
        if(group == null)
        {
            throw new RuntimeException("Group should not be null");
        }

        EditText editText = findViewById(R.id.group_name);
        editText.setText(group.getName());

        editText = findViewById(R.id.group_description);
        editText.setText(group.getInfo());
    }

    protected Group createItemFromInput()
    {
        if(!getViewModel().getFormState().getValue().isDataValid())
        {
            return null;
        }

        // todo, redo somehow else?
        Group newGroup = null;
        if(isEdit())
        {
            newGroup = new Group(
                    getViewModel().getItemId(),
                    groupName.getText().toString(),
                    groupDescription.getText().toString());
        }
        else
        {
            newGroup = new Group(
                    groupName.getText().toString(),
                    groupDescription.getText().toString());
        }

        return newGroup;
    }

    protected void onFormStateChanged(GroupAddEditFormState formState)
    {
        groupName.setError(formState.getNameError() != null ? getString(formState.getNameError()) : null);
        groupDescription.setError(formState.getDescriptionError() != null ? getString(formState.getDescriptionError()) : null);
    }

    protected GroupAddEditViewModel getViewModel()
    {
        return (GroupAddEditViewModel) super.getViewModel();
    }
}

package com.wdowiak.financemanager.commons;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.IItem;

import org.jetbrains.annotations.Contract;

import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_ITEM_ID;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_CREATED;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED;

abstract public class CommonAddEditActivity<ItemType extends IItem, FormState extends CommonAddEditFormState> extends AppCompatActivity
{
    private CommonAddEditViewModel<ItemType, FormState> viewModel = null;

    protected IItem.Type itemType;

    protected void afterCreate()
    {
        if(itemType == null)
        {
            throw new RuntimeException("itemType cannot be null");
        }

        viewModel = createViewModel();
        if(viewModel == null)
        {
            throw new RuntimeException("View model cannot be null");
        }

        viewModel.getFormState().observe(this, new Observer<FormState>()
        {
            @Override
            public void onChanged(@Nullable FormState loginFormState)
            {
                if (loginFormState == null)
                {
                    return;
                }

                final Button btn_add_edit = findViewById(R.id.add_save_action);
                btn_add_edit.setEnabled(loginFormState.isDataValid());

                onFormStateChanged(loginFormState);
            }
        });

        if(getIntent().hasExtra(INTENT_EXTRA_ITEM_ID))
        {
            viewModel.setItemId(getIntent().getExtras().getLong(INTENT_EXTRA_ITEM_ID));
            queryItem();
        }
    }

    protected void queryItem()
    {
        showProgressBar(true);

        QueryApi.getItemById(viewModel.getItemId(), itemType, new Api.IQueryCallback<ItemType>()
        {
            @Override
            public void onSuccess(ItemType item)
            {
                viewModel.setQueriedItem(item);
                if(viewModel.getQueriedItem() == null)
                {
                    Toast.makeText(getApplicationContext(), "Group[id= " + viewModel.getItemId()+ "] does not exist", Toast.LENGTH_SHORT);
                    finish();
                }

                afterSuccessfulItemQuery();
            }

            @Override
            public void onError(Exception error)
            {
                error.printStackTrace();
                Toast.makeText(CommonAddEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    protected void afterSuccessfulItemQuery()
    {
        updateAddEditView();
        showProgressBar(false);
    }

    protected final boolean isEdit()
    {
        return getIntent().hasExtra(INTENT_EXTRA_ITEM_ID);
    }

    public void onAddSave(final View view)
    {
        if(!viewModel.getFormState().getValue().isDataValid())
        {
            return;
        }

        disableButtons(true);

        ItemType newItem = createItemFromInput();
        if(isEdit())
        {
            // Update even if nothing has changed locally
            // the data could be changed somewhere else by the time we press the save button
            updateItem(newItem);
        }
        else
        {
            createItem(newItem);
        }
    }

    public void onCancel(final View view)
    {
        disableButtons(true);

        if(viewModel.hasDataChanged())
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

    @Contract("null -> fail")
    protected void createItem(ItemType newItem)
    {
        if(newItem == null)
        {
            throw new RuntimeException("Item cannot be null");
        }

        QueryApi.createItem(newItem, itemType, new Api.IQueryCallback<ItemType>()
        {
            @Override
            public void onSuccess(ItemType result)
            {
                Toast.makeText(getApplicationContext(), "<<<ITEM>>> was added successfully", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra(INTENT_EXTRA_RESULT_ITEM_WAS_CREATED, INTENT_EXTRA_RESULT_ITEM_WAS_CREATED);
                setResult(Activity.RESULT_OK, resultIntent);

                finish();
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getApplicationContext(), "<<ITEM>> could not be added could not be added: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                disableButtons(false);
            }
        });
    }

    @Contract("null -> fail")
    protected void updateItem(ItemType newItem)
    {
        if(newItem == null)
        {
            throw new RuntimeException("Item cannot be null");
        }

        QueryApi.updateItem(newItem, itemType, new Api.IQueryCallback<ItemType>()
        {
            @Override
            public void onSuccess(ItemType result)
            {
                Toast.makeText(getApplicationContext(), "<<<<ITEM>>>> was updated successfully", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra(INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED, INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED);
                setResult(Activity.RESULT_OK, resultIntent);

                finish();
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getApplicationContext(), "<<<IIITEM>>> could not be updated: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                disableButtons(false);
            }
        });
    }

    protected final void showProgressBar(final boolean show)
    {
        final LinearLayout detailViewLayout = findViewById(R.id.view_layout);
        detailViewLayout.setVisibility(show ? View.GONE : View.VISIBLE);

        final LinearLayout progressBarLayout = findViewById(R.id.progress_bar_layout);
        progressBarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected final void disableButtons(final boolean disable)
    {
        final Button btn_add_edit = findViewById(R.id.add_save_action);
        btn_add_edit.setEnabled(!disable);

        final Button btn_cancel = findViewById(R.id.cancel_action);
        btn_cancel.setEnabled(!disable);
    }

    abstract protected CommonAddEditViewModel<ItemType, FormState> createViewModel();
    abstract protected void updateAddEditView();
    abstract protected ItemType createItemFromInput();
    abstract protected void onFormStateChanged(FormState formState);

    protected CommonAddEditViewModel<ItemType, FormState> getViewModel()
    {
        return viewModel;
    }
}

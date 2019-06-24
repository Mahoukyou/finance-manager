package com.wdowiak.financemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.IItem;

abstract public class CommonDetailViewActivity<T extends IItem> extends AppCompatActivity
{
    public final static String INTENT_EXTRA_ITEM_ID = "INTENT_EXTRA_ITEM_ID";
    public final static String INTENT_EXTRA_RESULT_ITEM_WAS_CREATED = "INTENT_EXTRA_RESULT_ITEM_WAS_CREATED";
    public final static String INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED = "INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED";
    public final static String INTENT_EXTRA_RESULT_ITEM_WAS_DELETED = "INTENT_EXTRA_RESULT_ITEM_WAS_DELETED";
    public static final int EDIT_ITEM_REQUEST = 2;

    private Long itemId = null;
    protected Class<?> addEditClass = null;
    protected IItem.Type itemType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    protected void afterCreate()
    {
        if(addEditClass == null)
        {
            throw new RuntimeException("Add Edit class cannot be null");
        }

        itemId = getIntent().getExtras().getLong(INTENT_EXTRA_ITEM_ID);
        queryItem();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_ITEM_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data.hasExtra(INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED))
            {
                queryItem();
            }
        }
    }

    protected void queryItem()
    {
        showProgressBar(true);

        QueryApi.getItemById(getItemId(), itemType, new Api.IQueryCallback<T>()
        {
            @Override
            public void onSuccess(final T item)
            {
                if(item == null)
                {
                    Toast.makeText(getApplicationContext(), "Item[id= " + getItemId() + "] does not exist", Toast.LENGTH_SHORT);
                    finish();

                    return;
                }

                updateDetailViewInfo(item);
                showProgressBar(false);
            }

            @Override
            public void onError(final Exception error)
            {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
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

    abstract protected void updateDetailViewInfo(final T item);

    public final void onEdit(final View view)
    {
        Intent intent = new Intent(getApplicationContext(), addEditClass);
        intent.putExtra(INTENT_EXTRA_ITEM_ID, itemId);
        startActivityForResult(intent, EDIT_ITEM_REQUEST);
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
                        deleteItem();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this <<<ITEM>>>?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    protected void deleteItem()
    {
        QueryApi.deleteItemById(itemId, itemType, new Api.IQueryCallback<String>()
        {
            @Override
            public void onSuccess(String  result)
            {
                Toast.makeText(getApplicationContext(), "<<<ITEM>>> was deleted successfully", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra(INTENT_EXTRA_RESULT_ITEM_WAS_DELETED, INTENT_EXTRA_RESULT_ITEM_WAS_DELETED);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getApplicationContext(), "<<<ITEM>>> could not be deleted. Is there any account using it?", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected long getItemId()
    {
        return itemId;
    }
}

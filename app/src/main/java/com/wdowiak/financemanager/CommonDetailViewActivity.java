package com.wdowiak.financemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

abstract public class CommonDetailViewActivity<T> extends AppCompatActivity
{
    public final static String INTENT_EXTRA_ITEM_ID = "INTENT_EXTRA_ITEM_ID";
    public final static String INTENT_EXTRA_RESULT_ITEM_WAS_DELETED = "INTENT_EXTRA_RESULT_ITEM_WAS_DELETED";
    public final static String INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED = "INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED";
    static final int EDIT_ITEM_REQUEST = 2;

    private Long itemId = null;
    protected Class<?> addEditClass = null;

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

    abstract protected void queryItem();

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

    protected abstract void deleteItem();

    protected final void finishIntentOnItemDelete()
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(INTENT_EXTRA_RESULT_ITEM_WAS_DELETED, INTENT_EXTRA_RESULT_ITEM_WAS_DELETED);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    protected long getItemId()
    {
        return itemId;
    }
}

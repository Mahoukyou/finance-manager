package com.wdowiak.financemanager.transaction_statuses;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.commons.CommonAddEditActivity;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.TransactionStatus;

public class TransactionStatusAddEditActivity extends CommonAddEditActivity<TransactionStatus, TransactionStatusAddEditFormState>
{
    EditText transactionStatusName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_status_add_edit);

        itemType = IItem.Type.TransactionStatus;

        transactionStatusName = findViewById(R.id.transaction_status_name);
        transactionStatusName.addTextChangedListener(afterTextChangedListener);

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
            getViewModel().dataChanged(transactionStatusName.getText().toString());
        }
    };

    protected TransactionStatusAddEditViewModel createViewModel()
    {
        return ViewModelProviders.of(this).get(TransactionStatusAddEditViewModel.class);
    }

    protected void updateAddEditView()
    {
        final TransactionStatus transactionStatus = getViewModel().getQueriedItem();
        if(transactionStatus == null)
        {
            throw new RuntimeException("TransactionStatus should not be null");
        }

        final EditText editText = findViewById(R.id.transaction_status_name);
        editText.setText(transactionStatus.getName());
    }

    protected TransactionStatus createItemFromInput()
    {
        if(!getViewModel().getFormState().getValue().isDataValid())
        {
            return null;
        }

        // todo, redo somehow else?
        TransactionStatus newTransactionStatus = null;
        if(isEdit())
        {
            newTransactionStatus = new TransactionStatus(getViewModel().getItemId(), transactionStatusName.getText().toString());
        }
        else
        {
            newTransactionStatus = new TransactionStatus(transactionStatusName.getText().toString());
        }

        return newTransactionStatus;
    }

    protected void onFormStateChanged(TransactionStatusAddEditFormState formState)
    {
        transactionStatusName.setError(formState.getNameError() != null ? getString(formState.getNameError()) : null);
    }

    protected TransactionStatusAddEditViewModel getViewModel()
    {
        return (TransactionStatusAddEditViewModel) super.getViewModel();
    }
}

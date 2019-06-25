package com.wdowiak.financemanager.currencies;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.commons.CommonAddEditActivity;
import com.wdowiak.financemanager.data.Currency;
import com.wdowiak.financemanager.data.IItem;

public class CurrencyAddEditActivity extends CommonAddEditActivity<Currency, CurrencyAddEditFormState>
{
    EditText currencyName, currencyAcronym, currencySymbol;
    ToggleButton currencyPrefix;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_add_edit);

        itemType = IItem.Type.Currency;

        currencyName = findViewById(R.id.currency_name);
        currencyAcronym = findViewById(R.id.currency_acronym);
        currencySymbol = findViewById(R.id.currency_symbol);
        currencyPrefix = findViewById(R.id.currency_prefix);

        currencyName.addTextChangedListener(afterTextChangedListener);
        currencyAcronym.addTextChangedListener(afterTextChangedListener);
        currencySymbol.addTextChangedListener(afterTextChangedListener);

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
                    currencyName.getText().toString(),
                    currencyAcronym.getText().toString(),
                    currencySymbol.getText().toString());
        }
    };

    protected CurrencyAddEditViewModel createViewModel()
    {
        return ViewModelProviders.of(this).get(CurrencyAddEditViewModel.class);
    }

    protected void updateAddEditView()
    {
        final Currency currency = getViewModel().getQueriedItem();
        if(currency == null)
        {
            throw new RuntimeException("Currency should not be null");
        }

        currencyName.setText(currency.getName());
        currencyAcronym.setText(currency.getAcronym());
        currencySymbol.setText(currency.getSymbol());
        currencyPrefix.setChecked(currency.getPrefix());
    }

    protected Currency createItemFromInput()
    {
        if(!getViewModel().getFormState().getValue().isDataValid())
        {
            return null;
        }

        // todo, redo somehow else?
        Currency newCurrency = null;
        if(isEdit())
        {
            newCurrency = new Currency(
                    getViewModel().getItemId(),
                    currencyName.getText().toString(),
                    currencyAcronym.getText().toString(),
                    currencySymbol.getText().toString(),
                    currencyPrefix.isChecked());
        }
        else
        {
                newCurrency = new Currency(
                        currencyName.getText().toString(),
                        currencyAcronym.getText().toString(),
                        currencySymbol.getText().toString(),
                        currencyPrefix.isChecked());
        }

        return newCurrency;
    }

    protected void onFormStateChanged(CurrencyAddEditFormState formState)
    {
        currencyName.setError(formState.getNameError() != null ? getString(formState.getNameError()) : null);
        currencyAcronym.setError(formState.getAcronymError() != null ? getString(formState.getAcronymError()) : null);
        currencySymbol.setError(formState.getSymbolError() != null ? getString(formState.getSymbolError()) : null);
    }

    protected CurrencyAddEditViewModel getViewModel()
    {
        return (CurrencyAddEditViewModel) super.getViewModel();
    }
}

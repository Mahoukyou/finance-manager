package com.wdowiak.financemanager.currencies;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.commons.CommonAddEditViewModel;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.Currency;

public class CurrencyAddEditViewModel
    extends CommonAddEditViewModel<Currency, CurrencyAddEditFormState>
{
    public boolean hasDataChanged()
    {
        // todo
        if(getQueriedItem() == null)
        {
            // check if something chagned?
        }
        return false;
    }

    public void dataChanged(final String name, final String acronym, final String symbol)
    {
        Integer nameError = null;
        Integer acronymError = null;
        Integer symbolError = null;

        if(name == null || name.isEmpty())
        {
            nameError = R.string.cannot_be_empty;
        }

        if(acronym == null || acronym.isEmpty())
        {
            acronymError = R.string.cannot_be_empty;
        }

        if(symbol == null || symbol.isEmpty())
        {
            symbolError = R.string.cannot_be_empty;
        }

        setFormState(new CurrencyAddEditFormState(nameError, acronymError, symbolError));
    }
}

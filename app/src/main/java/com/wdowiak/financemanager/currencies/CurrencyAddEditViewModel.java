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
            nameError = R.string.app_name;
        }

        if(acronym == null || acronym.isEmpty())
        {
            acronymError = R.string.app_name;
        }

        if(symbol == null || symbol.isEmpty())
        {
            symbolError = R.string.app_name;
        }

        setFormState(new CurrencyAddEditFormState(nameError, acronymError, symbolError));
    }
}

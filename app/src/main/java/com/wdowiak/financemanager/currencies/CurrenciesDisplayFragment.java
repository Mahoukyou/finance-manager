package com.wdowiak.financemanager.currencies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.commons.CommonDisplayFragment;
import com.wdowiak.financemanager.data.Currency;
import com.wdowiak.financemanager.data.IItem;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CurrenciesDisplayFragment extends CommonDisplayFragment<Currency, CurrenciesAdapter>
{
    @NotNull
    @Contract(" -> new")
    public static CurrenciesDisplayFragment newInstance()
    {
        return new CurrenciesDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        itemType = IItem.Type.Currency;
        detailClass = CurrencyDetailActivity.class;
        addEditClass = CurrencyAddEditActivity.class;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CurrenciesDisplayFragmentViewModel.class);

        queryAndDisplayItems();
    }

    @Override
    protected CurrenciesDisplayFragmentViewModel getViewModel()
    {
        return (CurrenciesDisplayFragmentViewModel) super.getViewModel();
    }

    @Override
    protected CurrenciesAdapter createItemAdapter()
    {
        return new CurrenciesAdapter(viewModel.getData(), getActivity().getApplicationContext());
    }
}

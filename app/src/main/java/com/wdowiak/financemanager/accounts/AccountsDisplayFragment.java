package com.wdowiak.financemanager.accounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.commons.CommonDisplayFragment;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.IItem;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class AccountsDisplayFragment extends CommonDisplayFragment<Account, AccountsAdapter>
{
    @NotNull
    @Contract(" -> new")
    public static AccountsDisplayFragment newInstance()
    {
        return new AccountsDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        itemType = IItem.Type.Account;
        detailClass = AccountDetailActivity.class;
        addEditClass = AccountDetailActivity.class; // todo

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(AccountsDisplayFragmentViewModel.class);

        queryAndDisplayItems();
    }

    @Override
    protected AccountsDisplayFragmentViewModel getViewModel()
    {
        return (AccountsDisplayFragmentViewModel) super.getViewModel();
    }

    @Override
    protected AccountsAdapter createItemAdapter()
    {
        return new AccountsAdapter(viewModel.getData(), getActivity().getApplicationContext());
    }
}

package com.wdowiak.financemanager.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.CommonDisplayFragment;
import com.wdowiak.financemanager.DisplayFragmentViewModel;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.groups.GroupDetailActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.wdowiak.financemanager.IntentExtras.INTENT_EXTRA_ITEM_ID;

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

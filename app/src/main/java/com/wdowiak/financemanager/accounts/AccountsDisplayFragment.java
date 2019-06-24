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

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.AccountsApi;
import com.wdowiak.financemanager.api.TransactionsApi;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.transactions.TransactionAddEditActivity;
import com.wdowiak.financemanager.transactions.TransactionDetailActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AccountsDisplayFragment extends Fragment
{
    private AccountsDisplayFragmentViewModel mViewModel;

    ListView accountsListView;

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
        final View view = inflater.inflate(R.layout.listview_display_fragment, container, false);

        accountsListView = view.findViewById(R.id.display_listview);
        accountsListView.setOnItemClickListener(this::OnAccountClicked);

        view.findViewById(R.id.fab_add).setOnClickListener(this::onAddAccount);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccountsDisplayFragmentViewModel.class);

        queryAndDisplayAccounts();
    }

    private void OnAccountClicked(AdapterView<?> adapterView, View view, int i, long l)
    {
        Intent intent = new Intent(getActivity().getApplicationContext(), AccountDetailActivity.class);
        intent.putExtra(AccountDetailActivity.INTENT_EXTRA_ITEM_ID, mViewModel.accountsData.get(i).getId());
        startActivity(intent);
    }

    void queryAndDisplayAccounts()
    {
        AccountsApi.getAccounts(new AccountsApi.IAccountsCallback<ArrayList<Account>>()
        {
            @Override
            public void OnSuccess(ArrayList<Account> transactions)
            {
                mViewModel.accountsData = transactions;
                if(mViewModel.accountsAdapter == null)
                {
                    mViewModel.accountsAdapter = new AccountsAdapter(mViewModel.accountsData, getActivity().getApplicationContext());
                    accountsListView.setAdapter(mViewModel.accountsAdapter);
                }
                else
                {
                    mViewModel.accountsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void OnError(Exception error)
            {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final void onAddAccount(final View view)
    {
      //  Intent intent = new Intent(getActivity().getApplicationContext(), TransactionAddEditActivity.class);
       // startActivity(intent);
    }


}

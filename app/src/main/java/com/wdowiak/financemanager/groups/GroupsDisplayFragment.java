package com.wdowiak.financemanager.groups;

import android.app.Activity;
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

import com.wdowiak.financemanager.CommonDetailViewActivity;
import com.wdowiak.financemanager.CommonDisplayFragment;
import com.wdowiak.financemanager.DisplayFragmentViewModel;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.categories.CategoriesDisplayFragmentViewModel;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.transactions.TransactionDetailActivity;
import com.wdowiak.financemanager.transactions.TransactionsAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.wdowiak.financemanager.IntentExtras.INTENT_EXTRA_ITEM_ID;
import static com.wdowiak.financemanager.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_CREATED;
import static com.wdowiak.financemanager.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_DELETED;

public class GroupsDisplayFragment extends CommonDisplayFragment<Group, GroupsAdapter>
{
    @NotNull
    @Contract(" -> new")
    public static GroupsDisplayFragment newInstance()
    {
        return new GroupsDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        itemType = IItem.Type.Group;
        detailClass = GroupDetailActivity.class;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(GroupsDisplayFragmentViewModel.class);

        queryAndDisplayItems();
    }

    @Override
    protected GroupsDisplayFragmentViewModel getViewModel()
    {
        return (GroupsDisplayFragmentViewModel) super.getViewModel();
    }

    @Override
    protected GroupsAdapter createItemAdapter()
    {
        return new GroupsAdapter(viewModel.getData(), getActivity().getApplicationContext());
    }
}

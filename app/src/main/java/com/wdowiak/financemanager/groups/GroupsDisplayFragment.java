package com.wdowiak.financemanager.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.commons.CommonDisplayFragment;
import com.wdowiak.financemanager.commons.OnlyNameSortableDisplayFragment;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.IItem;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class GroupsDisplayFragment extends OnlyNameSortableDisplayFragment<Group, GroupsAdapter>
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
        addEditClass = GroupAddEditActivity.class;

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

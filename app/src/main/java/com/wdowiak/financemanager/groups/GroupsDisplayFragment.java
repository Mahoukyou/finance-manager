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
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.IItem;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GroupsDisplayFragment extends Fragment
{
    static final int DETAIL_VIEW_REQUEST = 1;
    static final int CREATE_GROUP_REQUEST = 2;


    private GroupsDisplayFragmentViewModel mViewModel;

    ListView groupsListView;

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
        final View view = inflater.inflate(R.layout.listview_display_fragment, container, false);

        groupsListView = view.findViewById(R.id.display_listview);
        groupsListView.setOnItemClickListener(this::onGroupClicked);

        view.findViewById(R.id.fab_add).setOnClickListener(this::onAddGroup);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GroupsDisplayFragmentViewModel.class);

        queryAndDisplayGroups();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DETAIL_VIEW_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data.hasExtra(GroupDetailActivity.INTENT_EXTRA_RESULT_ITEM_WAS_DELETED))
            {
                queryAndDisplayGroups();
            }
        }

        if(requestCode == CREATE_GROUP_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data.hasExtra(CommonDetailViewActivity.INTENT_EXTRA_RESULT_ITEM_WAS_CREATED))
            {
                queryAndDisplayGroups();
            }
        }
    }

    private void onGroupClicked(AdapterView<?> adapterView, View view, int i, long l)
    {
        Intent intent = new Intent(getActivity().getApplicationContext(), GroupDetailActivity.class);
        intent.putExtra(GroupDetailActivity.INTENT_EXTRA_ITEM_ID, mViewModel.groupsData.get(i).getId());
        startActivityForResult(intent, DETAIL_VIEW_REQUEST);
    }

    void queryAndDisplayGroups()
    {
        QueryApi.getItems(IItem.Type.Group, new Api.IQueryCallback<ArrayList<Group>>() {
            @Override
            public void onSuccess(ArrayList<Group> groups)
            {
                mViewModel.groupsData = groups;
                if(mViewModel.groupsAdapter == null)
                {
                    mViewModel.groupsAdapter = new GroupsAdapter(mViewModel.groupsData, getActivity().getApplicationContext());
                    groupsListView.setAdapter(mViewModel.groupsAdapter);
                }
                else
                {
                    mViewModel.groupsAdapter.clear();
                    mViewModel.groupsAdapter.addAll(mViewModel.groupsData);
                    mViewModel.groupsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final void onAddGroup(final View view)
    {
        Intent intent = new Intent(getActivity().getApplicationContext(), GroupAddEditActivity.class);
        startActivityForResult(intent, CREATE_GROUP_REQUEST);
    }


}

package com.wdowiak.financemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.IItem;

import java.util.ArrayList;

abstract public class CommonDisplayFragment<T extends IItem, ItemAdapter extends ArrayAdapter<T>> extends Fragment
{
    private IDisplayFragmentViewModel<T, ItemAdapter> viewModel;

    ListView accountsListView;

    IItem.Type itemType;
    protected Class<?> detailClass;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.listview_display_fragment, container, false);

        // todo, bind buttons in xml?
        accountsListView = view.findViewById(R.id.display_listview);
        accountsListView.setOnItemClickListener(this::onItemClicked);

        view.findViewById(R.id.fab_add).setOnClickListener(this::onAddItem);

        if(itemType == null)
        {
            throw new RuntimeException("itemType cannot be null");
        }

        if(detailClass == null)
        {
            throw new RuntimeException("detailClass cannot be null");
        }

        return view;
    }

    protected void onItemClicked(AdapterView<?> adapterView, View view, int i, long l)
    {
        Intent intent = new Intent(getActivity().getApplicationContext(), detailClass);
        intent.putExtra(CommonDetailViewActivity.INTENT_EXTRA_ITEM_ID, viewModel.getData().get(i).getId());
        startActivity(intent);
    }

    protected void queryAndDisplayItems()
    {
        QueryApi.getItems(IItem.Type.Account, new Api.IQueryCallback<ArrayList<T>>()
        {
            @Override
            public void onSuccess(ArrayList<T> transactions)
            {
                viewModel.setData(transactions);
                if(viewModel.getAdapter() == null)
                {
                    viewModel.setAdapter(createItemAdapter());
                    accountsListView.setAdapter(viewModel.getAdapter());
                }
                else
                {
                    viewModel.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onAddItem(final View view)
    {
        // todo
        //  Intent intent = new Intent(getActivity().getApplicationContext(), TransactionAddEditActivity.class);
        // startActivity(intent);
    }

    protected IDisplayFragmentViewModel<T, ItemAdapter> getViewModel()
    {
        return viewModel;
    }

    abstract protected ItemAdapter createItemAdapter();
}

package com.wdowiak.financemanager;

import android.app.Activity;
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

import static com.wdowiak.financemanager.IntentExtras.ADD_ITEM_REQUEST;
import static com.wdowiak.financemanager.IntentExtras.EDIT_ITEM_REQUEST;
import static com.wdowiak.financemanager.IntentExtras.INTENT_EXTRA_ITEM_ID;
import static com.wdowiak.financemanager.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_CREATED;
import static com.wdowiak.financemanager.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_DELETED;
import static com.wdowiak.financemanager.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED;

abstract public class CommonDisplayFragment<T extends IItem, ItemAdapter extends ArrayAdapter> extends Fragment
{
    protected IDisplayFragmentViewModel<T, ItemAdapter> viewModel;

    ListView itemsListView;

    protected IItem.Type itemType;
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
        itemsListView = view.findViewById(R.id.display_listview);
        itemsListView.setOnItemClickListener(this::onItemClicked);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_ITEM_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data.hasExtra(INTENT_EXTRA_RESULT_ITEM_WAS_CREATED))
            {
                queryAndDisplayItems();
            }
        }

        if(requestCode == EDIT_ITEM_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data.hasExtra(INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED) || data.hasExtra(INTENT_EXTRA_RESULT_ITEM_WAS_DELETED))
            {
                queryAndDisplayItems();
            }
        }
    }



    protected void onItemClicked(AdapterView<?> adapterView, View view, int i, long l)
    {
        Intent intent = new Intent(getActivity().getApplicationContext(), detailClass);
        intent.putExtra(INTENT_EXTRA_ITEM_ID, viewModel.getData().get(i).getId());
        startActivity(intent);
    }

    protected void queryAndDisplayItems()
    {
        QueryApi.getItems(itemType, new Api.IQueryCallback<ArrayList<T>>()
        {
            @Override
            public void onSuccess(ArrayList<T> transactions)
            {
                viewModel.setData(transactions);
                if(viewModel.getAdapter() == null)
                {
                    viewModel.setAdapter(createItemAdapter());
                    itemsListView.setAdapter(viewModel.getAdapter());
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

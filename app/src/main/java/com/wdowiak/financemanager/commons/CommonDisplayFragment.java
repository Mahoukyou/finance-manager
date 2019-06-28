package com.wdowiak.financemanager.commons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.IItem;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

import static com.wdowiak.financemanager.commons.IntentExtras.*;

abstract public class CommonDisplayFragment<T extends IItem, ItemAdapter extends ArrayAdapter> extends Fragment
{
    protected DisplayFragmentViewModel<T, ItemAdapter> viewModel;

    ListView itemsListView;
    RelativeLayout viewLayout;
    LinearLayout progressBarLayout;

    protected IItem.Type itemType;
    protected Class<?> detailClass, addEditClass;

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

        viewLayout = view.findViewById(R.id.view_layout);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);

        view.findViewById(R.id.fab_add).setOnClickListener(this::onAddItem);

        if(itemType == null)
        {
            throw new RuntimeException("itemType cannot be null");
        }

        if(detailClass == null)
        {
            throw new RuntimeException("detailClass cannot be null");
        }

        if(addEditClass == null)
        {
            throw new RuntimeException("addEditClass cannot be null");
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
        startActivityForResult(intent, EDIT_ITEM_REQUEST);
    }

    protected void queryAndDisplayItems()
    {
        showProgressBar(true);
        QueryApi.getItems(itemType, new Api.IQueryCallback<ArrayList<T>>()
        {
            @Override
            public void onSuccess(ArrayList<T> items)
            {
                // async request returned when activity was destroyed
                if(getActivity() == null || getActivity().getApplicationContext() == null)
                {
                    return;
                }

                viewModel.setData(items);
                if(viewModel.getAdapter() == null)
                {
                    viewModel.setAdapter(createItemAdapter());
                    itemsListView.setAdapter(viewModel.getAdapter());
                }
                else
                {
                    viewModel.populateAdapterWithDataAndNotify();
                }

                showProgressBar(false);
            }

            @Override
            public void onError(Exception error)
            {
                if(getActivity() != null && getActivity().getApplicationContext() != null)
                {
                    Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected final void showProgressBar(final boolean show)
    {
        viewLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void onAddItem(final View view)
    {
        Intent intent = new Intent(getActivity().getApplicationContext(), addEditClass);
        startActivityForResult(intent, ADD_ITEM_REQUEST);
    }

    @Contract(pure = true)
    protected final ListView getItemsListView()
    {
        return itemsListView;
    }

    protected DisplayFragmentViewModel<T, ItemAdapter> getViewModel()
    {
        return viewModel;
    }

    abstract protected ItemAdapter createItemAdapter();
}

package com.wdowiak.financemanager.categories;

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
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.IItem;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.wdowiak.financemanager.IntentExtras.INTENT_EXTRA_ITEM_ID;

public class CategoriesDisplayFragment extends Fragment
{
    private CategoriesDisplayFragmentViewModel mViewModel;

    ListView categoriesListView;

    @NotNull
    @Contract(" -> new")
    public static CategoriesDisplayFragment newInstance()
    {
        return new CategoriesDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.listview_display_fragment, container, false);

        categoriesListView = view.findViewById(R.id.display_listview);
        categoriesListView.setOnItemClickListener(this::onCategoryClicked);

        view.findViewById(R.id.fab_add).setOnClickListener(this::onAddCategory);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CategoriesDisplayFragmentViewModel.class);

        queryAndDisplayAccounts();
    }

    private void onCategoryClicked(AdapterView<?> adapterView, View view, int i, long l)
    {
        Intent intent = new Intent(getActivity().getApplicationContext(), CategoryDetailActivity.class);
        intent.putExtra(INTENT_EXTRA_ITEM_ID , mViewModel.categoriesData.get(i).getId());
        startActivity(intent);
    }

    void queryAndDisplayAccounts()
    {
        QueryApi.getItems(IItem.Type.Category, new Api.IQueryCallback<ArrayList<Category>>() {
            @Override
            public void onSuccess(ArrayList<Category> categories)
            {
                mViewModel.categoriesData = categories;
                if(mViewModel.categoriesAdapter == null)
                {
                    mViewModel.categoriesAdapter = new CategoriesAdapter(mViewModel.categoriesData, getActivity().getApplicationContext());
                    categoriesListView.setAdapter(mViewModel.categoriesAdapter);
                }
                else
                {
                    mViewModel.categoriesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final void onAddCategory(final View view)
    {
      //  Intent intent = new Intent(getActivity().getApplicationContext(), TransactionAddEditActivity.class);
       // startActivity(intent);
    }


}

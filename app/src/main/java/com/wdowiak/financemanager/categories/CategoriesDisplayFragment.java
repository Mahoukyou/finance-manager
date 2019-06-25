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

import com.wdowiak.financemanager.CommonDisplayFragment;
import com.wdowiak.financemanager.DisplayFragmentViewModel;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.groups.GroupDetailActivity;
import com.wdowiak.financemanager.groups.GroupsAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.wdowiak.financemanager.IntentExtras.INTENT_EXTRA_ITEM_ID;

public class CategoriesDisplayFragment extends CommonDisplayFragment<Category, CategoriesAdapter>
{
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
        itemType = IItem.Type.Category;
        detailClass = CategoryDetailActivity.class;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CategoriesDisplayFragmentViewModel.class);

        queryAndDisplayItems();
    }

    @Override
    protected DisplayFragmentViewModel<Category, CategoriesAdapter> getViewModel()
    {
        return super.getViewModel();
    }

    @Override
    protected CategoriesAdapter createItemAdapter()
    {
        return new CategoriesAdapter(viewModel.getData(), getActivity().getApplicationContext());
    }
}
